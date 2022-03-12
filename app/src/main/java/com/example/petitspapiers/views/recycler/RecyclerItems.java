package com.example.petitspapiers.views.recycler;

import android.net.Uri;

import com.example.petitspapiers.objects.Filmiz;

public class RecyclerItems {

    private String filmizTitle;
    private final int filmizType;
    private final Filmiz filmiz;
    private int tireOrder;

    public RecyclerItems(Filmiz filmiz) {
        this.filmiz = filmiz;
        this.filmizTitle = filmiz.getTitle();
        this.filmizType = filmiz.getType();
        this.tireOrder = filmiz.getTireOrder();
    }

    public String getFilmizTitle() {
        return filmizTitle;
    }

    public void setFilmizTitle(String filmizTitle) {
        this.filmizTitle = filmizTitle;
    }

    public int getFilmizType() {
        return filmizType;
    }

    public Filmiz getFilmiz() {
        return filmiz;
    }

    public int getTireOrder() {
        return tireOrder;
    }
}
