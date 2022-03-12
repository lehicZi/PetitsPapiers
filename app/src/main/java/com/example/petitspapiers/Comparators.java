package com.example.petitspapiers;

import com.example.petitspapiers.objects.Filmiz;

import java.util.Comparator;

public class Comparators {

    public static Comparator<Filmiz> filmizComparatorTitle(){
        return Comparator.comparing(Filmiz::getTitle);
    }


    public static Comparator<Filmiz> filmizComparatorType(){
        return (filmiz1, filmiz2) -> {
            if (filmiz1.getType() == filmiz2.getType()){
                return 0;
            }
            else if (filmiz1.getType() < filmiz2.getType()){
                return 1;
            }
            else {
                return -1;
            }
        };
    }

    public static Comparator<Filmiz> filmizComparatorTireOrder(){
        return (filmiz1, filmiz2) -> {
            if (filmiz1.getTireOrder() == filmiz2.getTireOrder()){
                return 0;
            }
            else if (filmiz1.getTireOrder() < filmiz2.getTireOrder()){
                return -1;
            }
            else {
                return 1;
            }
        };
    }


}
