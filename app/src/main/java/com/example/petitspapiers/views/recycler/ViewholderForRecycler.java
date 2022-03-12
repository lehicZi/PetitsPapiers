package com.example.petitspapiers.views.recycler;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petitspapiers.R;

public class ViewholderForRecycler extends RecyclerView.ViewHolder {

    TextView filmizTitle;
    TextView filmizType;
    TextView order;

    Button vu;

    RelativeLayout itemLayout;




    public ViewholderForRecycler(@NonNull View itemView) {
        super(itemView);
        filmizTitle = itemView.findViewById(R.id.filmizTitle);
        filmizType = itemView.findViewById(R.id.typeView);
        order = itemView.findViewById(R.id.orderView);

        vu = itemView.findViewById(R.id.buttonVu);

        itemLayout = itemView.findViewById(R.id.recyclerLayout);

    }


}
