package org.cryptonews.main.ui.list_utils;

import org.cryptonews.main.Utils;

public class Post implements Comparable<Post> {

    private String name, text, date;

    public Post(String name,String text,String date) {
        this.name = name;
        this.text = text;
        this.date = date;
    }
    public Post() {
        this.name = "null";
        this.text = "null";
        this.date = "null";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int compareTo(Post o) {
        return Utils.convertDate(getDate())-Utils.convertDate(o.getDate());
    }
}
