package com.example.placethequeenonchessboard;

import android.widget.ImageView;

public class BoxChess {
    private ImageView imgBox;
    private int i, j;
    private Boolean isCheck;
    private int typeBox;
    public static int QUEEN_WHITE = R.drawable.queen_white;
    public static int QUEEN_GREEN = R.drawable.queen_green;
    public static int BOX_WHITE = R.drawable.white;
    public static int BOX_GREEN = R.drawable.green;
//    private Boolean isExists = false;

    public BoxChess(int i, int j, ImageView imgBox, int typeBox, Boolean isCheck) {
        this.i = i;
        this.j = j;
        this.typeBox = typeBox;
        this.imgBox = imgBox;
        this.isCheck = isCheck;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public Boolean getCheck() {
        return isCheck;
    }

    public void setCheck(Boolean check) {
        isCheck = check;
    }

    public int getTypeBox() {
        return typeBox;
    }

    public void setTypeBox(int typeBox) {
        this.typeBox = typeBox;
    }

    public ImageView getImgBox() {
        return imgBox;
    }

    public void setImgBox(ImageView imgBox) {
        this.imgBox = imgBox;
    }
    public int getQueenBox(){
        if(this.getTypeBox() == this.BOX_GREEN){
            return this.QUEEN_GREEN;
        }else {
            return this.QUEEN_WHITE;
        }
    }
    public int getEmptyBox(){
        if(this.getTypeBox() == this.BOX_GREEN){
            return this.BOX_GREEN;
        }else {
            return this.BOX_WHITE;
        }
    }
    /*public void setExists(Boolean isExists){
        this.isExists = isExists;
    }
    public Boolean getIsExists(){
        return this.isExists;
    }*/
}
