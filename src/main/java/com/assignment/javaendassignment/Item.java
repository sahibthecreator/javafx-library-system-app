package com.assignment.javaendassignment;

import javafx.collections.ObservableList;

import java.io.Serializable;
import java.time.LocalDate;

public class Item implements Serializable {
    private int id;
    private boolean available;
    private String title;
    private String author;
    private LocalDate lendingDate;

    public Item(int id, String title, String author) {
        this.id = id;
        this.available = true;
        this.title = title;
        this.author = author;
        this.lendingDate = null;
    }
    public Item(String title, String author, ObservableList<Item> items) {
        if (items.isEmpty())
            this.id = 1;
        else
            this.id = items.get(items.size() - 1).getId() + 1;
        this.available = true;
        this.title = title;
        this.author = author;
        this.lendingDate = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAvailableBoolean() {
       return available;
    }

    public String isAvailable() {
        if(available)
            return "Yes";
        else
            return "No";
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getLendingDate() {
        return lendingDate;
    }

    public void setLendingDate(LocalDate lendingDate) {
        this.lendingDate = lendingDate;
    }
}
