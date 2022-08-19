package com.example.petitspapiers.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petitspapiers.DataShared;
import com.example.petitspapiers.Database;
import com.example.petitspapiers.R;
import com.example.petitspapiers.Utils;
import com.example.petitspapiers.constants.FilmizStatus;
import com.example.petitspapiers.constants.Filmiztype;
import com.example.petitspapiers.objects.Filmiz;
import com.example.petitspapiers.views.ViewManager;
import com.example.petitspapiers.views.recycler.AEcrireAdapter;
import com.example.petitspapiers.views.recycler.RecyclerItems;
import com.example.petitspapiers.views.recycler.VuAdapter;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrPosition;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AEcrireFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AEcrireFragment extends Fragment implements AEcrireAdapter.callback {

    ViewManager viewManager;

    String usedTitle;
    int usedType = 1;
    boolean dispo = true;


    EditText newFilmizTitle;
    Button addFilmiz;

    RadioGroup typeRG;
    RadioButton serieTypeRB;
    RadioButton filmTypeRB;
    Switch dispoSwich;

    RecyclerView recycler;
    AEcrireAdapter adapter;
    List<RecyclerItems> recyclerItemsList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AEcrireFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AEcrireFragment newInstance(String param1, String param2) {
        AEcrireFragment fragment = new AEcrireFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_a_voir, container, false);

        instantiateView(v);

        return v;
    }

    private void instantiateView(View v){

        newFilmizTitle = v.findViewById(R.id.filmizName);
        addFilmiz = v.findViewById(R.id.addButton);

        typeRG = v.findViewById(R.id.typeSelectionGroup);
        serieTypeRB = v.findViewById(R.id.serieType);
        filmTypeRB = v.findViewById(R.id.filmType);
        dispoSwich = v.findViewById(R.id.dispo);

        instantiateRecycler(v);
        setlisteners();
        getActivity().setTitle("À ajouter");


    }

    private void instantiateRecycler(View v){

        this.recyclerItemsList = new ArrayList<>();

        setRecyclerItems();
        this.adapter = new AEcrireAdapter(getActivity(), recyclerItemsList, this);

        recycler = v.findViewById(R.id.recycler);


        recycler.setLayoutManager((new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false)));
        recycler.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycler.getContext(), 1);
        recycler.addItemDecoration(dividerItemDecoration);

    }

    private void setRecyclerItems(){

        DataShared.getInstance().getStatusList(getActivity(), FilmizStatus.AECRIRE);

        viewManager = new ViewManager();
        viewManager.orderBy(DataShared.getInstance().getSortMod());

        List<Filmiz> filmizList = DataShared.getInstance().getCurrentList();

        for(Filmiz filmiz : filmizList){

            RecyclerItems filmizInfos = new RecyclerItems(filmiz);

            this.recyclerItemsList.add(filmizInfos);

        }

    }

    public void updateRecycler(){

        recyclerItemsList.clear();
        setRecyclerItems();
        adapter.setItems(recyclerItemsList);
        recycler.setAdapter(adapter);

    }

    private void setlisteners() {


        newFilmizTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                usedTitle = charSequence.toString();

                newFilmizTitle.setOnKeyListener(new View.OnKeyListener() {
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        // If the event is a key-down event on the "enter" button
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            // Perform action on key press
                            newFilmizTitle.requestFocus();

                            Filmiz filmiztoAdd = new Filmiz(usedTitle, usedType, dispo);

                            sentWarn(filmiztoAdd);

                            newFilmizTitle.getText().clear();
                            Utils.hideKeyboard(getActivity());
                            return true;
                        }
                        return false;
                    }

            });

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        addFilmiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {


                    Filmiz filmiztoAdd = new Filmiz(usedTitle, usedType, dispo);


                    sentWarn(filmiztoAdd);


                    newFilmizTitle.getText().clear();


                } catch (IllegalStateException e) {

                    Utils.showMessage("Erreur", "Il faut indiquer un type !", getActivity());

                }
            }
        });

        serieTypeRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                usedType = Filmiztype.SERIE;

            }
        });

        filmTypeRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                usedType = Filmiztype.FILM;

            }
        });

        dispoSwich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                dispo = b;
            }
        });
    }

    private boolean checkIfAlreadyInAVoir (Filmiz filmiz){

        String title = filmiz.getTitle();

        for (Filmiz existingFilmiz : Database.selectFilmizStatus(getActivity(), FilmizStatus.AVOIR)){

            if (title.equals(existingFilmiz.getTitle())){
                return true;
            }

        }

        return false;
    }

    private boolean checkIfAlreadyInVu (Filmiz filmiz){

        String title = filmiz.getTitle();

        for (Filmiz existingFilmiz : Database.selectFilmizStatus(getActivity(), FilmizStatus.VU)){

            if (title.equals(existingFilmiz.getTitle())){
                return true;
            }

        }

        return false;
    }

    private boolean checkIfAlreadyInTire (Filmiz filmiz){

        String title = filmiz.getTitle();

        for (Filmiz existingFilmiz : Database.selectFilmizStatus(getActivity(), FilmizStatus.TIRE)){

            if (title.equals(existingFilmiz.getTitle())){
                return true;
            }

        }

        return false;
    }

    private void sentWarn (Filmiz filmiz){

        if (!(checkIfAlreadyInAVoir(filmiz) || checkIfAlreadyInVu(filmiz) || checkIfAlreadyInTire(filmiz))){

            Database.addEntry(getActivity(), filmiz);
            updateRecycler();;
        }

        else if (checkIfAlreadyInAVoir(filmiz)){

            AlertDialog.Builder popUp = new AlertDialog.Builder(getActivity());
            popUp.setTitle("Attention");
            popUp.setMessage("Le film/la série que vous essayez d'ajouter est déjà dans la liste \" à voir \"");
            popUp.setPositiveButton("Ne pas ajouter", (dialog, which) -> {
            });
            popUp.setNegativeButton("Ajouter quand même", (dialog, which) -> {


                Database.addEntry(getActivity(), filmiz);
                updateRecycler();

            });
            popUp.show();


        }

        else if (checkIfAlreadyInTire(filmiz)){

            AlertDialog.Builder popUp = new AlertDialog.Builder(getActivity());
            popUp.setTitle("Attention");
            popUp.setMessage("Vous avez déjà tiré le film/la série que vous essayez d'ajouter !");
            popUp.setPositiveButton("Ne pas ajouter", (dialog, which) -> {
            });
            popUp.setNegativeButton("Ajouter quand même", (dialog, which) -> {


                Database.addEntry(getActivity(), filmiz);
                updateRecycler();

            });
            popUp.show();


        }

        else if (checkIfAlreadyInVu(filmiz)){

            AlertDialog.Builder popUp = new AlertDialog.Builder(getActivity());
            popUp.setTitle("Attention");
            popUp.setMessage("Vous avez déjà vu le film/la série que vous essayez d'ajouter !");
            popUp.setPositiveButton("Ne pas ajouter", (dialog, which) -> {
            });
            popUp.setNegativeButton("Ajouter quand même", (dialog, which) -> {


                Database.addEntry(getActivity(), filmiz);
                updateRecycler();

            });
            popUp.show();


        }


    }


    @Override
    public void onClic(View v) {
        updateRecycler();
    }
}