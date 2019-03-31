package com.example.muktadirkhan.socialnetworkingapp;

public class Post {

    String author;
    String content;
    String time;

    public Post() {

    }

    public Post(String author, String content, String time) {
        this.author = author;
        this.content = content;
        this.time = time;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
