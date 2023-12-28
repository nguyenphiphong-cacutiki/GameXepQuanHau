package com.example.placethequeenonchessboard;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PlayActivity extends AppCompatActivity {
    int numberOfBox, px, level;
    ImageView imgWayPre, imgWayNext;
    TableLayout tbBoard;
    ImageView[][] listManagerChessBox;
    BoxChess[][] arrayBox;
    int[] column, diagonalUp, diagonalDown, row;
    TextView txtTime, tvNumberOfWay, btnBackToMain, btnNewGamePlayActivity, btnSurrender;
    Integer[] pass;
    List<Integer[]> listPass;
    int numberOfWaySolution;
    LinearLayout lnlNoMain;
    CountDownTimer countDownTimeToNotificationFail, autoCheckToNotificationWin;



    int[] cheolen = new int[100];
    int[] cheoxuong = new int[100];
    int[] cot = new int[100];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        mappingAndCreateNewObject();
        Intent intent = getIntent();
        numberOfBox = intent.getIntExtra("numberOfBox", 8);
        level = intent.getIntExtra("level", 1);
        startGame();

        btnNewGamePlayActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogNewGame(PlayActivity.this);
            }
        });
        btnSurrender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                surrenderAndShowSolution();
            }
        });
        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlayActivity.this, MainActivity.class));
            }
        });
    }
    private void mappingAndCreateNewObject(){
        // mapping
        tbBoard = findViewById(R.id.tbBoard);
        txtTime = findViewById(R.id.txtTime);
        imgWayNext = findViewById(R.id.wayNext);
        imgWayPre = findViewById(R.id.wayPre);
        tvNumberOfWay = findViewById(R.id.txtNumberOfWay);
        lnlNoMain = findViewById(R.id.lnlNoMain);
        btnBackToMain = findViewById(R.id.btnBackToMain);
        btnNewGamePlayActivity = findViewById(R.id.btnNewGamePlayActivity);
        btnSurrender = findViewById(R.id.btnSurrender);
        // new object
        listManagerChessBox = new ImageView[20][20];
        arrayBox = new BoxChess[20][20];
        column = new int[100];
        diagonalDown = new int[100];
        diagonalUp = new int[100];
        row = new int[100];
        pass = new Integer[100];
        listPass = new ArrayList<>();
    }
    private void createBoard(){
        for(int i = 1; i<= numberOfBox; i++){
            for(int j = 1; j<= numberOfBox; j++){
                ImageView imageView = new ImageView(this);
                int typeBox;
                if(i%2==0 & j%2==0 || i%2==1 && j%2 ==1){
                    typeBox = BoxChess.BOX_GREEN;
                }else{
                    typeBox = BoxChess.BOX_WHITE;
                }
                arrayBox[i][j] = new BoxChess(i, j, imageView, typeBox, false);
            }
        }

    }
    private void paintBoard(){
        tbBoard.removeAllViews();
        for(int i = 1; i<=numberOfBox; i++){
            TableRow tableRow = new TableRow(this);
            for(int j = 1; j<=numberOfBox; j++){
                TableRow.LayoutParams params = new TableRow.LayoutParams(px, px);
                arrayBox[i][j].getImgBox().setLayoutParams(params);
                arrayBox[i][j].getImgBox().setImageResource(arrayBox[i][j].getTypeBox());
                int finalI = i;
                int finalJ = j;
                arrayBox[i][j].getImgBox().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Boolean isCheck = arrayBox[finalI][finalJ].getCheck();

                            if(isCheck){
                                // if check is true then set check = false, set image empty and array mark = 0
                                arrayBox[finalI][finalJ].setCheck(false);
                                arrayBox[finalI][finalJ].getImgBox().setImageResource(arrayBox[finalI][finalJ].getEmptyBox());
                                column[finalJ] = diagonalDown[finalJ - finalI+numberOfBox] = diagonalUp[finalI+finalJ] = row[finalI] = 0;
                                //

                            }else{
                                // if check is false
                                if(column[finalJ] == 1 || diagonalDown[finalJ - finalI+numberOfBox] == 1 ||  diagonalUp[finalI+finalJ] ==1 || row[finalI] == 1){
                                    Toast.makeText(PlayActivity.this, "Không thể đi nước cờ này vì sẽ bị quân hậu khác chiếu tới", Toast.LENGTH_SHORT).show();
                                }else{
                                    arrayBox[finalI][finalJ].getImgBox().setImageResource(arrayBox[finalI][finalJ].getQueenBox());
                                    arrayBox[finalI][finalJ].setCheck(true);
                                    column[finalJ] = diagonalDown[finalJ - finalI+numberOfBox] = diagonalUp[finalI+finalJ] = row[finalI] = 1;
                                }
//                            arrayBox[finalI][finalJ].getImgBox().setImageResource(android.R.color.transparent); // remove background
                        }
                    }
                });

                tableRow.addView(arrayBox[i][j].getImgBox());

            }
            tbBoard.addView(tableRow);
        }
    }
    private void convertDpToPx(){
        // convert dp to pixel

        float dip = (float) 330/numberOfBox;
        px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip,
                getResources().getDisplayMetrics()
        );
        // convert is redundant
        px = (Resources.getSystem().getDisplayMetrics().widthPixels - 20) / numberOfBox;
        // convert completed
    }
    private void createMarkArray(){
        for(int i = 0; i<100; i++){
            column[i] = diagonalDown[i] = diagonalUp[i] = row[i]=0;
        }
    }
    private void markArray(){
        for(int i = 0; i<100; i++){
            cheolen[i] =  cheoxuong[i] = cot[i] = 0;
        }
    }
    private void startGame(){
        // set environment
        txtTime.setVisibility(View.VISIBLE);
        lnlNoMain.setVisibility(View.INVISIBLE);
        btnSurrender.setVisibility(View.VISIBLE);
        if(countDownTimeToNotificationFail != null){
            countDownTimeToNotificationFail.cancel();
        }

        createPass();

        convertDpToPx();


        createMarkArray();
        createBoard();
        paintBoard();


        if(autoCheckToNotificationWin != null){
            autoCheckToNotificationWin.cancel();
        }
        autoCheckToNotificationWin();


        countDownTimeToNotificationFalse();
    }
    private void countDownTimeToNotificationFalse(){
        int time = numberOfBox*8*level*1000;
        final int[] currentTime = {time};
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        countDownTimeToNotificationFail = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                currentTime[0] -= 1000;
                txtTime.setText(format.format(currentTime[0]));
            }

            @Override
            public void onFinish() {
                if(autoCheckToNotificationWin != null) autoCheckToNotificationWin.cancel();
                dialogFail();
            }
        }.start();
    }

   private void dialogFail(){
        Dialog dialog = new Dialog(PlayActivity.this);
        dialog.setContentView(R.layout.dialog_fail);
        TextView btnShowPass = dialog.findViewById(R.id.btnPass);
        TextView btnNewGame = dialog.findViewById(R.id.btnNewGameAgain);
        TextView btnCancel = dialog.findViewById(R.id.btnCancelFail);

        btnShowPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                surrenderAndShowSolution();
                dialog.cancel();
            }
        });
        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogNewGame(PlayActivity.this);
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
   }
   private void findPass(int i){
       for(int j = 1; j<= numberOfBox; j++){
           if(cot[j] == 0 && cheoxuong[j-i+numberOfBox] == 0 && cheolen[i+j] == 0){
               // danh dau o da dat quan co
               cot[j] =  1; cheoxuong[j-i+numberOfBox] = 1; cheolen[i+j] = 1;
               // luu o thoa man vao ket qua
               pass[i] = j;
                   if(i == numberOfBox){
                       Integer[] passTmp = new Integer[100];
                       for(int a = 1; a<= numberOfBox; a++){
                           passTmp[a] = pass[a];
                       }
                       listPass.add(passTmp);
                   }else{
                      findPass(i+1);
                   }
               cot[j] = 0; cheoxuong[j-i+numberOfBox] = 0; cheolen[i+j] = 0;
           }
       }
   }
   private void createPass(){
        listPass.clear(); // clear before create new
       // set create mark array
        markArray();
        // find
        findPass(1);
        numberOfWaySolution = listPass.size();
   }
   private void showPass(int positionOfListPass){
        // create and paint board again
        createMarkArray();
        createBoard();
        paintBoard();
        // set image for board
        Integer[] passTmp;
        passTmp = listPass.get(positionOfListPass);

       for(int i = 1; i<=numberOfBox; i++){
            arrayBox[i][passTmp[i]].getImgBox().setImageResource(arrayBox[i][passTmp[i]].getQueenBox());
        }
   }
   private void surrenderAndShowSolution(){
       if(countDownTimeToNotificationFail != null){
           countDownTimeToNotificationFail.cancel();
       }
       if(autoCheckToNotificationWin != null){
           autoCheckToNotificationWin.cancel();
       }

       btnSurrender.setVisibility(View.INVISIBLE);
       lnlNoMain.setVisibility(View.VISIBLE);
       tvNumberOfWay.setText("có "+numberOfWaySolution+" đáp án cho game này :))))");
       txtTime.setVisibility(View.INVISIBLE);
       final int[] i = {0};
       imgWayPre.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(i[0] >0) i[0]--;
               createMarkArray();
               createBoard();
               paintBoard();
               showPass(i[0]);
           }
       });
       imgWayNext.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(i[0]<numberOfWaySolution-1){
                   i[0]++;
               }
               createMarkArray();
               createBoard();
               paintBoard();
               showPass(i[0]);
           }
       });
       showPass(0);
   }

    private  void dialogNewGame(Context context){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_new_game);
        EditText edtNumberOfBox = dialog.findViewById(R.id.edtNumberOfBox);
        Button btnCancel = dialog.findViewById(R.id.btnNewGameCancel);
        Button btnOk = dialog.findViewById(R.id.btnNewGameOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numberOfBoxString = edtNumberOfBox.getText().toString().trim();
                if(numberOfBoxString.isEmpty()){
                    Toast.makeText(context, "Bạn chưa nhập số ô của bàn cờ", Toast.LENGTH_SHORT).show();
                }else{
                    int n = Integer.parseInt(numberOfBoxString);
                    if(n<4 || n>12){
                        Toast.makeText(context, "Số ô cờ không hợp lệ", Toast.LENGTH_SHORT).show();
                    }else {
                        numberOfBox = n;
                        dialog.dismiss();
                        dialogSelectLevel(context);
                    }
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private  void dialogSelectLevel(Context context){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_selec_level);
        TextView btnDifficult = dialog.findViewById(R.id.btnKho);
        TextView btnEasy = dialog.findViewById(R.id.btnDe);
        TextView btnMid = dialog.findViewById(R.id.btnTb);
        TextView btnCancel = dialog.findViewById(R.id.btnQl);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialogNewGame(context);
            }
        });
        btnEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                level = 5;
                startGame();
                dialog.dismiss();
            }
        });
        btnMid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                level = 3;
                startGame();
                dialog.dismiss();
            }
        });
        btnDifficult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                level = 1;
                startGame();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void dialogWin(){
        if(countDownTimeToNotificationFail !=null) countDownTimeToNotificationFail.cancel();

        Dialog dialog = new Dialog(PlayActivity.this);
        dialog.setContentView(R.layout.dialog_win);
        TextView btnShowPass = dialog.findViewById(R.id.btnWinShowPass);
        TextView btnNewGame = dialog.findViewById(R.id.btnWinNewGame);
        TextView btnCancel = dialog.findViewById(R.id.btnWinCancel);

        btnShowPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                surrenderAndShowSolution();
                dialog.cancel();
            }
        });
        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogNewGame(PlayActivity.this);
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
    private void autoCheckToNotificationWin(){
        autoCheckToNotificationWin = new CountDownTimer(300, 300) {
            @Override
            public void onTick(long millisUntilFinished) {
                int numCurrentBoxCompleted = 0;
                for(int i = 1; i<= numberOfBox; i++){
                    for(int j = 1; j<= numberOfBox; j++){
                        if(arrayBox[i][j].getCheck()){
                            numCurrentBoxCompleted++;
                        }
                    }
                }
                if(numCurrentBoxCompleted == numberOfBox){
                    this.cancel();
                    dialogWin();
                }
            }

            @Override
            public void onFinish() {
                this.start();
            }
        }.start();
    }
}