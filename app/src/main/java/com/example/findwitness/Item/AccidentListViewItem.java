package com.example.findwitness.Item;

public class AccidentListViewItem {
    private String year;
    private String num;
    private String died;
    private String hurt;
    public AccidentListViewItem(String year,String num,String died,String hurt) {
        this.year = year;
        this .num = num;
        this.died = died;
        this .hurt = hurt;}

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getDied() {
        return died;
    }

    public void setDied(String died) {
        this.died = died;
    }

    public String getHurt() {
        return hurt;
    }

    public void setHurt(String hurt) {
        this.hurt = hurt;
    }
}
