package com.example.petitspapiers;

import android.app.Activity;

import com.example.petitspapiers.constants.FilmizStatus;
import com.example.petitspapiers.objects.Filmiz;

import java.util.List;

public class DataShared {

    private static DataShared dataShared;

    private List<Filmiz> currentList;
    private Filmiz currentFilmiz;

    private  int currentFragment = FilmizStatus.AECRIRE;
    private int sortMod = 0;
    private String query;

    private DataShared() {

    }

    public static DataShared getInstance()
    {
        if (dataShared == null)
        {
            dataShared = new DataShared();
        }

        return dataShared;
    }


    public void getStatusList(Activity activity, int status){

        currentList = Database.selectFilmizStatus(activity, status);

    }

    public List<Filmiz> getCurrentList() {
        return currentList;
    }

    public int getSortMod() {
        return sortMod;
    }

    public void setSortMod(int sortMod) {
        this.sortMod = sortMod;
    }

    public void setCurrentList(List<Filmiz> currentList) {
        this.currentList = currentList;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Filmiz getCurrentFilmiz() {
        return currentFilmiz;
    }

    public void setCurrentFilmiz(Filmiz currentFilmiz) {
        this.currentFilmiz = currentFilmiz;
    }

    public int getCurrentFragment() {
        return currentFragment;
    }

    public void setCurrentFragment(int currentFragment) {
        this.currentFragment = currentFragment;
    }
}
