package com.example.PBL5.model;

import javax.persistence.*;

@Entity
@Table(name = "history")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String image;
    private String time;
    private int amount;
    private boolean showed;

    public History() {
    }

    public History(int id, String image, String time, int amount) {
        this.id = id;
        this.image = image;
        this.time = time;
        this.amount = amount;
    }

    public History(int id, String image, String time, int amount, boolean showed) {
        this.id = id;
        this.image = image;
        this.time = time;
        this.amount = amount;
        this.showed = showed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isShowed() {
        return showed;
    }

    public void setShowed(boolean showed) {
        this.showed = showed;
    }
}
