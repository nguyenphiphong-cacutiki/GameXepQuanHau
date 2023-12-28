package com.example.placethequeenonchessboard;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView btnNewGame, btnLaw, btnInformation;
    private static int numberBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapping();
        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogNewGame(MainActivity.this);
            }
        });
        btnInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInformation();
            }
        });
        btnLaw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLaw();
            }
        });

    }
    private void mapping(){
        btnNewGame = findViewById(R.id.btnStartGame);
        btnLaw = findViewById(R.id.btnLaw_);
        btnInformation = findViewById(R.id.btnInformation);
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
                String numberOfBox = edtNumberOfBox.getText().toString().trim();
                if(numberOfBox.isEmpty()){
                    Toast.makeText(context, "Bạn chưa nhập số ô của bàn cờ", Toast.LENGTH_SHORT).show();
                }else{
                    int n = Integer.parseInt(numberOfBox);
                    if(n<4 || n>12){
                        Toast.makeText(context, "Số ô cờ không hợp lệ", Toast.LENGTH_SHORT).show();
                    }else {
                        numberBox = n;
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
                Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                intent.putExtra("numberOfBox", numberBox);
                intent.putExtra("level", 3);
                startActivity(intent);
            }
        });
        btnMid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                intent.putExtra("numberOfBox", numberBox);
                intent.putExtra("level", 2);
                startActivity(intent);
            }
        });
        btnDifficult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                intent.putExtra("numberOfBox", numberBox);
                intent.putExtra("level", 1);
                startActivity(intent);
            }
        });
        dialog.show();
    }
    private void dialogLaw(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_law);
        dialog.setCanceledOnTouchOutside(false);
        TextView btnCancel = (TextView) dialog.findViewById(R.id.btnLawCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void dialogInformation(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_information);
        TextView btnYes = dialog.findViewById(R.id.btnWelcomeYes);
        dialog.setCanceledOnTouchOutside(false);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}