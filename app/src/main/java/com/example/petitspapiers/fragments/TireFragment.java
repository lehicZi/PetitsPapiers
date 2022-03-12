package com.example.petitspapiers.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petitspapiers.DataShared;
import com.example.petitspapiers.R;
import com.example.petitspapiers.constants.FilmizStatus;
import com.example.petitspapiers.objects.Filmiz;
import com.example.petitspapiers.views.ViewManager;
import com.example.petitspapiers.views.recycler.RecyclerItems;
import com.example.petitspapiers.views.recycler.TireAdapter;
import com.example.petitspapiers.views.recycler.VuAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TireFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TireFragment extends Fragment implements TireAdapter.callback {

    ViewManager viewManager;

    RecyclerView recycler;
    TireAdapter adapter;
    List<RecyclerItems> recyclerItemsList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TireFragment() {
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
    public static TireFragment newInstance(String param1, String param2) {
        TireFragment fragment = new TireFragment();
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

        instantiateRecycler(v);

        getActivity().setTitle("Tirés");


    }

    private void instantiateRecycler(View v){

        this.recyclerItemsList = new ArrayList<>();

        setRecyclerItems();
        this.adapter = new TireAdapter(getActivity(), recyclerItemsList, this);

        recycler = v.findViewById(R.id.recycler);


        recycler.setLayoutManager((new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false)));
        recycler.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycler.getContext(), 1);
        recycler.addItemDecoration(dividerItemDecoration);

    }

    private void setRecyclerItems(){

        DataShared.getInstance().getStatusList(getActivity(), FilmizStatus.TIRE);

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


    @Override
    public void onClic(View v) {
        updateRecycler();
    }
}