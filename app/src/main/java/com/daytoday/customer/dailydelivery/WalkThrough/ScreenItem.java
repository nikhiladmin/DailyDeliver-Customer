package com.daytoday.customer.dailydelivery.WalkThrough;

public class ScreenItem {
    int ScreenImg;
    String description;
    String title;

    public ScreenItem(int screenImg, String description,String title) {
        ScreenImg = screenImg;
        this.description = description;
        this.title = title;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
