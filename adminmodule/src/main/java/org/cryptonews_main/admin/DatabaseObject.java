package org.cryptonews_main.admin;

public class DatabaseObject {

    private String text,name,date;

    public DatabaseObject(String text, String name,String date) {
        this.text = text;
        this.name = name;
        this.date = date;
    }
    public DatabaseObject() {
        name = "null";
        text = "null";
        date = "null";
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
