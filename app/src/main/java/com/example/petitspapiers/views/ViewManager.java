package com.example.petitspapiers.views;

import com.example.petitspapiers.Comparators;
import com.example.petitspapiers.DataShared;
import com.example.petitspapiers.Database;
import com.example.petitspapiers.Utils;
import com.example.petitspapiers.constants.SortMods;
import com.example.petitspapiers.objects.Filmiz;

import java.util.ArrayList;
import java.util.List;

public class ViewManager {

    private List<Filmiz> allFilmiz;
    private List<Filmiz> listToShow;

    public ViewManager() {
        allFilmiz = DataShared.getInstance().getCurrentList();
        this.listToShow = new ArrayList<>(this.allFilmiz);

    }


    private void sortAlphabetically(){
        this.listToShow.sort(Comparators.filmizComparatorTitle());
    }

    private void sortByType(){
        this.listToShow.sort(Comparators.filmizComparatorType());
    }

    private void sortByTireOrder(){
        this.listToShow.sort(Comparators.filmizComparatorTireOrder());
    }

    public void sortBySearch(String query){

        List<Filmiz> searchAnswer = new ArrayList<>();

            for (Filmiz filmiz : allFilmiz) {

                if (Utils.stringContainsString(filmiz.getTitle(), query)) {
                    searchAnswer.add(filmiz);
                }

            }


            this.listToShow = searchAnswer;
            sortAlphabetically();
            DataShared.getInstance().setCurrentList(listToShow);
    }

    public void orderBy(int sortMod){

        switch (sortMod){
            case SortMods.ALAPHABETIC : {
                sortAlphabetically();
                DataShared.getInstance().setCurrentList(listToShow);
                break;
            }
            case SortMods.TYPE: {
                sortByType();
                DataShared.getInstance().setCurrentList(listToShow);
                break;
            }
            case SortMods.SEARCH: {
                sortBySearch(DataShared.getInstance().getQuery());
                break;
            }
            case SortMods.TIREORDER: {
                sortByTireOrder();
                DataShared.getInstance().setCurrentList(listToShow);
                break;
            }
            default: {
                throw new IllegalStateException("Ce mode de tri n'existe pas ! " + String.valueOf(sortMod));
            }

        }


    }

    public List<Filmiz> getListToShow() {
        return listToShow;
    }
}
