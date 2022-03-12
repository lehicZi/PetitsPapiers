package com.example.petitspapiers.views.recycler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petitspapiers.DataShared;
import com.example.petitspapiers.Database;
import com.example.petitspapiers.R;
import com.example.petitspapiers.Utils;
import com.example.petitspapiers.constants.Filmiztype;
import com.example.petitspapiers.constants.SortMods;
import com.example.petitspapiers.objects.Filmiz;
import com.example.petitspapiers.views.FilmizDetails;
import com.example.petitspapiers.views.MainActivity;

import java.util.List;

public class VuAdapter extends RecyclerView.Adapter<ViewholderForRecycler> {

    Context context;
    List<RecyclerItems> items;
    callback listener;



    public VuAdapter(Context context, List<RecyclerItems> items, callback listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewholderForRecycler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.items_for_recycler, parent, false);
        return new ViewholderForRecycler(LayoutInflater.from(context).inflate(R.layout.items_for_recycler, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewholderForRecycler holder, int position) {

        holder.filmizTitle.setText(items.get(position).getFilmizTitle());

        int filmizType = items.get(position).getFilmizType();

        holder.filmizType.setText(Filmiztype.getString(filmizType));
        holder.filmizType.setBackgroundResource(Filmiztype.getBackgroundColor(filmizType));

        holder.vu.setVisibility(View.INVISIBLE);

        setListeners(holder);


    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    private void setListeners(ViewholderForRecycler holder){

        int pos = holder.getAdapterPosition();
        Filmiz filmiz = items.get(pos).getFilmiz();

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataShared.getInstance().setCurrentFilmiz(filmiz);

                final Intent intent = new Intent ((Activity) context, FilmizDetails.class);
                context.startActivity(intent);

            }
        });


        holder.itemLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                askDelete(filmiz, view);

                return false;
            }
        });

    }


    public void setItems(List<RecyclerItems> items) {
        this.items = items;
    }

    private void askDelete(Filmiz filmiz, View v){

        AlertDialog.Builder popUp = new AlertDialog.Builder((Activity) context);
        popUp.setMessage("Voulez-vous supprimer cette entrée ?");
        popUp.setPositiveButton("Non", (dialog, which) -> {
        });
        popUp.setNegativeButton("Oui", (dialog, which) -> {


            Database.deleteEntry((Activity) context, filmiz);
            listener.onClic(v);

        });
        popUp.show();

    }

    public interface callback{
        public void onClic(View v);

    }
}
