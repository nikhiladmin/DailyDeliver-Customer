package com.daytoday.customer.dailydelivery.WalkThrough;

public class ScreenItem {
    int ScreenImg;
    String description;

    public ScreenItem(int screenImg, String description) {
        ScreenImg = screenImg;
        this.description = description;
    }

    public void setScreenImg(int screenImg) {
        ScreenImg = screenImg;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getScreenImg() {
        return ScreenImg;
    }
}
