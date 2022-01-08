package com.example.appdubaothoitiet;

public class WeatherNextDay {
    private String Days;
    private String trangThai;
    private String imageIcon;
    private String maxTemp;
    private String minTemp;

    public WeatherNextDay(String days, String trangThai, String imageIcon, String maxTemp, String minTemp) {
        Days = days;
        this.trangThai = trangThai;
        this.imageIcon = imageIcon;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
    }

    public String getDays() {
        return Days;
    }

    public void setDays(String days) {
        Days = days;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(String imageIcon) {
        this.imageIcon = imageIcon;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }
}
