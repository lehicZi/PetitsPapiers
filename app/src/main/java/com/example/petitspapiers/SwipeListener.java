package com.example.petitspapiers;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class SwipeListener implements View.OnTouchListener{


    GestureDetector gestureDetector;
    private SwipeResults swipeResults;

    public SwipeListener(View view, SwipeResults swipeResults) {
        //initialize threshold values
        int threshold = 50;
        int velocityThreshold = 50;

        this.swipeResults = swipeResults;


        // initialize simple gesture detector
        GestureDetector.SimpleOnGestureListener listener =
                new GestureDetector.SimpleOnGestureListener(){

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        // get x and y difference
                        float xDiff = e2.getX() - e1.getX();
                        float yDiff = e2.getY() - e1.getY();
                        try {

                            if (Math.abs(xDiff) > Math.abs(yDiff)){
                                if (Math.abs(xDiff) > threshold && Math.abs(velocityX) > velocityThreshold){
                                    if (xDiff > 0){
                                        swipeResults.onSwipedRight();
                                    }
                                    else {
                                        swipeResults.onSwipedLeft();
                                    }
                                    return true;

                                }
                            }

                            else {
                                if (Math.abs(yDiff) > threshold && Math.abs(velocityY) > velocityThreshold){
                                    if (yDiff > 0){
                                        swipeResults.onSwipedDown();
                                    }
                                    else {
                                        swipeResults.onSwipedUp();
                                    }
                                    return true;

                                }

                            }


                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        return false;

                    }
                };

        //Initialize gesture detector
        gestureDetector = new GestureDetector(listener);
        //Set listener on view
        view.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //Return gesture event
        return gestureDetector.onTouchEvent(motionEvent);
    }


    public interface SwipeResults{

        public void onSwipedRight();

        public void onSwipedLeft();

        public void onSwipedUp();

        public void onSwipedDown();
    }


}
