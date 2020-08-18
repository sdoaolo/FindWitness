package com.example.findwitness.Item;

public class GPSListViewItem {
    private String date;
    private String time;
    private String latitude;//위도
    private String longitude; //경도
    private String address;

    public GPSListViewItem(String  date, String time, String latitude,String longitude,String address){
        this.date = date;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
