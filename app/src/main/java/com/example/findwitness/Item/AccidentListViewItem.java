package com.example.findwitness.Item;

public class AccidentListViewItem {
    private String year;
    private String num;
    private String died;
    private String hurt;

    public AccidentListViewItem(String data) {
        String[] accident_data = data.split(":");
        this.year = accident_data[0];
        this .num = accident_data[1];
        this.died = accident_data[2];
        this .hurt = accident_data[3];
    }

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
