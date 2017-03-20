package com.nhpatt.rxjava.talks;

import java.util.Random;

public class Talk {

    private String id;
    private String title;
    private String time;
    private String picture;
    private String speaker;
    private boolean favorited;
    private long score;

    public Talk(String title, String speaker) {
        this.title = title;
        this.speaker = speaker;
        Random random = new Random(System.currentTimeMillis());
        this.score = random.nextInt(5);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return title + ", score: " + score;
    }
}
