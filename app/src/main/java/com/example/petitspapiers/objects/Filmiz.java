package com.example.petitspapiers.objects;

import com.example.petitspapiers.constants.Filmiztype;

public class Filmiz {

    private int id;
    private String title;
    private final int type;
    private int status;
    private int tireOrder;
    private boolean isDispo;

    public Filmiz(String title, int type, boolean dispo) {
        this.title = title;
        this.type = type;
        this.isDispo = dispo;
    }


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getType() {
        return type;
    }

    public String getStringType() {
        return Filmiztype.getString(type);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTireOrder() {
        return tireOrder;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTireOrder(int tireOrder) {
        this.tireOrder = tireOrder;
    }

    public boolean isDispo() {
        return isDispo;
    }

    public void setDispo(boolean dispo) {
        isDispo = dispo;
    }
}
