package com.exmple.funnyvideo.Model;

public class VideoPlayerState {

    private int currentTime = 0;
    private String fileName;
    private String messageText;
    private int startInt = 0;
    private int stopInt = 0;

    public String getMessageText() {
        return this.messageText;
    }

    public void setMessageText(String str) {
        this.messageText = str;
    }

    public String getFilename() {
        return this.fileName;
    }

    public void setFilename(String str) {
        this.fileName = str;
    }

    public int getStart() {
        return this.startInt;
    }

    public void setStart(int i) {
        this.startInt = i;
    }

    public int getStop() {
        return this.stopInt;
    }

    public void setStop(int i) {
        this.stopInt = i;
    }

    public void reset() {
        this.stopInt = 0;
        this.startInt = 0;
    }

    public int getDuration() {
        return this.stopInt - this.startInt;
    }

    public int getCurrentTime() {
        return this.currentTime;
    }

    public void setCurrentTime(int i) {
        this.currentTime = i;
    }

    public boolean isValid() {
        return this.stopInt > this.startInt;
    }
}
