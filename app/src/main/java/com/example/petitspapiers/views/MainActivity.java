package com.example.petitspapiers.views;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petitspapiers.Comparators;
import com.example.petitspapiers.DataShared;
import com.example.petitspapiers.Database;
import com.example.petitspapiers.R;
import com.example.petitspapiers.Utils;
import com.example.petitspapiers.constants.FilmizStatus;
import com.example.petitspapiers.constants.Filmiztype;
import com.example.petitspapiers.constants.SortMods;
import com.example.petitspapiers.fragments.AEcrireFragment;
import com.example.petitspapiers.fragments.AVoirFragment;
import com.example.petitspapiers.fragments.TireFragment;
import com.example.petitspapiers.fragments.VuFragment;
import com.example.petitspapiers.objects.Filmiz;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigationBar;

    ViewManager viewManager;

    FrameLayout mainFragment;

    VuFragment vuFragment;
    AVoirFragment aVoirFragment;
    AEcrireFragment aEcrireFragment;
    TireFragment tireFragment;

    Menu sortMenu;
    MenuItem tireOrderMenuItem;
    SearchView searchView;

    int currentFragment = DataShared.getInstance().getCurrentFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instantiateView();
        setlisteners();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.sort_menu, menu);

        sortMenu = menu;

        tireOrderMenuItem = sortMenu.findItem(R.id.tri_tireOrder);

        final MenuItem searchItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                if (!searchView.isIconified())
                {
                    searchView.setIconified(true);
                }

                searchItem.collapseActionView();

                return false;
            }

            @Override
            public boolean onQueryTextChange(final String query)
            {

                DataShared.getInstance().setQuery(query);
                DataShared.getInstance().setSortMod(SortMods.SEARCH);
                updateTheGoodOne();

                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        final int item_id = item.getItemId();

        if (item_id == R.id.info){

            Utils.showMessage("Information", goodInfo(), this);


        }

        else  if (item_id == R.id.tri_alphabetique){

            DataShared.getInstance().setSortMod(SortMods.ALAPHABETIC);
            updateTheGoodOne();
            Toast.makeText(MainActivity.this, "Liste triée par ordre alphabétique", Toast.LENGTH_SHORT).show();


        }

        else  if (item_id == R.id.importExport){

            Utils.openOtherActivity(ImportExportView.class, this);


        }

        else if (item_id == R.id.tri_type){

            DataShared.getInstance().setSortMod(SortMods.TYPE);
            updateTheGoodOne();
            Toast.makeText(MainActivity.this, "Liste triée par types", Toast.LENGTH_SHORT).show();


        }

        else if (item_id == R.id.tri_tireOrder){

            DataShared.getInstance().setSortMod(SortMods.TIREORDER);
            updateTheGoodOne();
            Toast.makeText(MainActivity.this, "Liste triée par ordre de tirage", Toast.LENGTH_SHORT).show();


        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (!closeSearchView()){

            AlertDialog.Builder popUp = new AlertDialog.Builder(MainActivity.this);
            popUp.setMessage("Voulez-vous fermer l'application ?");
            popUp.setPositiveButton("Oui", (dialog, which) -> {

                MainActivity.this.finish();

            });
            popUp.setNegativeButton("Non", (dialog, which) -> {

            });
            popUp.show();
        }

    }


    private void instantiateView(){

        DataShared.getInstance().getStatusList(this, FilmizStatus.AECRIRE);

        navigationBar = findViewById(R.id.navigationBar);

        vuFragment = new VuFragment();
        aVoirFragment = new AVoirFragment();
        aEcrireFragment = new AEcrireFragment();
        tireFragment = new TireFragment();
        mainFragment = findViewById(R.id.mainFragment);

        setCurrentFragment();

        viewManager = new ViewManager();

        notifyIfDlNeeded();


    }


    private void setlisteners(){


        navigationBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId()) {

                    case R.id.menuAecrire:

                        closeSearchView();
                        currentFragment = FilmizStatus.AECRIRE;
                        DataShared.getInstance().setCurrentFragment(currentFragment);
                        DataShared.getInstance().setSortMod(SortMods.ALAPHABETIC);
                        DataShared.getInstance().getStatusList(MainActivity.this, FilmizStatus.AECRIRE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment, aEcrireFragment).commit();
                        tireOrderMenuItem.setVisible(false);
                        break;

                    case R.id.menuVu:

                        closeSearchView();
                        currentFragment = FilmizStatus.VU;
                        DataShared.getInstance().setCurrentFragment(currentFragment);
                        DataShared.getInstance().setSortMod(SortMods.ALAPHABETIC);
                        DataShared.getInstance().getStatusList(MainActivity.this, FilmizStatus.VU);
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment, vuFragment).commit();
                        tireOrderMenuItem.setVisible(false);
                        break;

                    case R.id.menuAVoir:

                        closeSearchView();
                        currentFragment = FilmizStatus.AVOIR;
                        DataShared.getInstance().setCurrentFragment(currentFragment);
                        DataShared.getInstance().setSortMod(SortMods.ALAPHABETIC);
                        DataShared.getInstance().getStatusList(MainActivity.this, FilmizStatus.AVOIR);
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment, aVoirFragment).commit();
                        tireOrderMenuItem.setVisible(false);
                        break;

                    case R.id.menuTire:

                        closeSearchView();
                        currentFragment = FilmizStatus.TIRE;
                        DataShared.getInstance().setCurrentFragment(currentFragment);
                        DataShared.getInstance().setSortMod(SortMods.TIREORDER);
                        DataShared.getInstance().getStatusList(MainActivity.this, FilmizStatus.TIRE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment, tireFragment).commit();
                        tireOrderMenuItem.setVisible(true);
                        break;

                }

                return true;
            }


        });


    }

    private void updateTheGoodOne(){

        if (currentFragment == FilmizStatus.AVOIR){
            aVoirFragment.updateRecycler();
        }

        else if (currentFragment == FilmizStatus.VU) {
            vuFragment.updateRecycler();
        }

        else if (currentFragment == FilmizStatus.AECRIRE) {
            aEcrireFragment.updateRecycler();
        }

        else if (currentFragment == FilmizStatus.TIRE) {
            tireFragment.updateRecycler();
        }

    }

    private String goodInfo(){

        String info = "";

        int numberSeries = 0;
        int numberFilms = 0;
        int total = 0 ;

        if (currentFragment == FilmizStatus.AVOIR){

            numberSeries = Database.selectFilmizStatusAndType(this, FilmizStatus.AVOIR, Filmiztype.SERIE).size();
            numberFilms = Database.selectFilmizStatusAndType(this, FilmizStatus.AVOIR, Filmiztype.FILM).size();
            total = numberFilms + numberSeries;

            info += "Il vous reste " +
                    numberSeries + " séries et " +
                    numberFilms + " films à voir ! (soit un total de " +
                    total + " films et séries)";
        }

        else if (currentFragment == FilmizStatus.VU) {

            numberSeries = Database.selectFilmizStatusAndType(this, FilmizStatus.VU, Filmiztype.SERIE).size();
            numberFilms = Database.selectFilmizStatusAndType(this, FilmizStatus.VU, Filmiztype.FILM).size();
            total = numberFilms + numberSeries;

            info += "Vous avez vu " +
                    numberSeries + " séries et " +
                    numberFilms + " films depuis l'installation de l'application ! (soit un total de " +
                    total + " films et séries )";
        }

        else if (currentFragment == FilmizStatus.AECRIRE) {

            numberSeries = Database.selectFilmizStatusAndType(this, FilmizStatus.AECRIRE, Filmiztype.SERIE).size();
            numberFilms = Database.selectFilmizStatusAndType(this, FilmizStatus.AECRIRE, Filmiztype.FILM).size();
            total = numberFilms + numberSeries;

            info += "Il reste " +
                    numberSeries + " séries et " +
                    numberFilms + " films à ajouter aux petits papiers ! (soit un total de " +
                    total + " films et séries)";
        }

        else if (currentFragment == FilmizStatus.TIRE) {

            numberSeries = Database.selectFilmizStatusAndType(this, FilmizStatus.TIRE, Filmiztype.SERIE).size();
            numberFilms = Database.selectFilmizStatusAndType(this, FilmizStatus.TIRE, Filmiztype.FILM).size();
            total = numberFilms + numberSeries;

            info += "Vous avez tiré " +
                    numberSeries + " séries et " +
                    numberFilms + " films d'avance ! (soit un total de " +
                    total + " films et séries)";
        }

        return info;

    }

    private boolean closeSearchView(){

        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return true;
        }
        else {
            return false;
        }

    }

    private void setCurrentFragment(){

        switch (this.currentFragment){
            case FilmizStatus.AECRIRE:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment, aEcrireFragment).commit();
                navigationBar.setSelectedItemId(R.id.menuAecrire);
                break;
            case FilmizStatus.AVOIR:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment, aVoirFragment).commit();
                navigationBar.setSelectedItemId(R.id.menuAVoir);
                break;
            case FilmizStatus.TIRE:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment, tireFragment).commit();
                navigationBar.setSelectedItemId(R.id.menuTire);
                break;
            case FilmizStatus.VU:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment, vuFragment).commit();
                navigationBar.setSelectedItemId(R.id.menuVu);
                break;
            default: throw new IllegalStateException("Fragment qui n'existe pas !" + currentFragment);
        }

    }

    private void notifyIfDlNeeded(){

        List<Filmiz> tireList = Database.selectFilmizStatus(this, FilmizStatus.TIRE);
        tireList.sort(Comparators.filmizComparatorTireOrder());
        int counter = 0;
        StringBuilder filmizToDl = new StringBuilder();
        String separator = "";

        AlertDialog.Builder popUp = new AlertDialog.Builder(this);

        for (Filmiz filmiz : tireList){

            if (!filmiz.isDispo()) {
                counter += 1;
                filmizToDl.append(separator);
                filmizToDl.append(filmiz.getTitle());
                separator = "\n- ";

            }

        }

        if (counter > 1){

            popUp.setTitle("Téléchargements nécessaires !");
            popUp.setMessage("Des films ou séries qui ont été tiré ne sont pas disponibles ! Il s'agit de :\n- " + filmizToDl);
            popUp.show();

        }

        else if (counter == 1){

            popUp.setTitle("Téléchargement nécessaire !");
            popUp.setMessage("Un film ou série qui a été tiré n'est pas disponible ! Il s'agit de :\n- " + filmizToDl);
            popUp.show();

        }


    }

}