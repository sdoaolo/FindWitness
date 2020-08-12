package com.example.findwitness;

public class GPSListViewItem {
    private String date;
    private String time;
    private String latitude;//위도
    private String longitude; //경도

    public String getlatitude() {
        return latitude;
    }

    public void setRowtext1(String latitude) {
        this.latitude = latitude;
    }

    public String getlongitude() {
        return longitude;
    }

    public void setlongitude(String longitude) {
        this.longitude = longitude;
    }
}
