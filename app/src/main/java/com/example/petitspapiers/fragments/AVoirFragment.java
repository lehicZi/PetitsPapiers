package com.example.petitspapiers.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.petitspapiers.DataShared;
import com.example.petitspapiers.R;
import com.example.petitspapiers.constants.FilmizStatus;
import com.example.petitspapiers.objects.Filmiz;
import com.example.petitspapiers.views.ViewManager;
import com.example.petitspapiers.views.recycler.AVoirAdapter;
import com.example.petitspapiers.views.recycler.RecyclerItems;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AVoirFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AVoirFragment extends Fragment implements AVoirAdapter.callback {

    ViewManager viewManager;

    String usedTitle;
    int usedType = 0;

    RecyclerView recycler;
    AVoirAdapter AVoirAdapter;
    List<RecyclerItems> recyclerItemsList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AVoirFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AVoirFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AVoirFragment newInstance(String param1, String param2) {
        AVoirFragment fragment = new AVoirFragment();
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
        View v = inflater.inflate(R.layout.fragment_vu, container, false);

        instantiateView(v);

        return v;
    }

    private void instantiateView(View v){

        viewManager = new ViewManager();

        getActivity().setTitle("À voir");


        instantiateRecycler(v);

        viewManager.orderBy(DataShared.getInstance().getSortMod());


    }


    private void instantiateRecycler(View v){

        this.recyclerItemsList = new ArrayList<>();

        setRecyclerItems();
        this.AVoirAdapter = new AVoirAdapter(getActivity(), recyclerItemsList, this);

        recycler = v.findViewById(R.id.recycler);


        recycler.setLayoutManager((new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false)));
        recycler.setAdapter(AVoirAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycler.getContext(), 1);
        recycler.addItemDecoration(dividerItemDecoration);

    }

    private void setRecyclerItems(){

        DataShared.getInstance().getStatusList(getActivity(), FilmizStatus.AVOIR);

        viewManager = new ViewManager();
        viewManager.orderBy(DataShared.getInstance().getSortMod());

        List<Filmiz> filmizList = viewManager.getListToShow();

        for(Filmiz filmiz : filmizList){

            RecyclerItems filmizInfos = new RecyclerItems(filmiz);

            this.recyclerItemsList.add(filmizInfos);

        }

    }


    public void updateRecycler(){

        recyclerItemsList.clear();
        setRecyclerItems();
        AVoirAdapter.setItems(recyclerItemsList);
        recycler.setAdapter(AVoirAdapter);

    }


    @Override
    public void onClic(View v) {
        updateRecycler();
    }
}