package com.example.petitspapiers.constants;

import com.example.petitspapiers.R;

public class Filmiztype {

    public static final int SERIE = 0;
    public static final int FILM = 1;


    public static String getString (int type){
        switch (type){
            case SERIE : {
                return "Série";
            }
            case FILM : {
                return "Film";
            }
            default : {
                return "Erreur : " + String.valueOf(type);
            }
        }

    }

    public static int getBackgroundColor (int type){

        switch (type){
            case SERIE : {
                return R.drawable.purple_rounded_edges;
            }
            case FILM : {
                return R.drawable.orange_rounded_edges;
            }
            default : {
                throw new IllegalStateException("Ce type de filmiz n'existe pas !");
            }
        }

    }



}
