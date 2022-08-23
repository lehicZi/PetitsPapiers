package com.example.petitspapiers.views.details;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.petitspapiers.DataShared;
import com.example.petitspapiers.Database;
import com.example.petitspapiers.R;
import com.example.petitspapiers.Utils;
import com.example.petitspapiers.constants.FilmizStatus;
import com.example.petitspapiers.constants.Filmiztype;
import com.example.petitspapiers.constants.SortMods;
import com.example.petitspapiers.objects.Filmiz;
import com.example.petitspapiers.objects.MoreInfosModel;
import com.example.petitspapiers.objects.ResultModel;
import com.example.petitspapiers.views.MainActivity;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class FilmizDetails extends AppCompatActivity implements GetDatas.AsyncResponse, GetAdvancedDatas.AsyncResponse {

    ImageView filmizImage;
    TextView filmizTitle;
    TextView filmizDescription;
    TextView filmizVOTitle;
    TextView filmizLanguage;
    TextView filmizDate;
    TextView filmizGenres;
    TextView filmizType;
    TextView filmRuntime;

    Button suppButton;
    Button renameButton;
    Button newSeasonButton;

    Switch dispoSwitch;

    Filmiz currentFilmiz;
    String fullTitle;

    ResultModel choseFilmiz;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filmiz_details);

        currentFilmiz = DataShared.getInstance().getCurrentFilmiz();
        fullTitle = currentFilmiz.getTitle();
        GetDatas getDatas = new GetDatas(createURL(), this, this);
        getDatas.execute();

        setTitle(fullTitle);


    }

    private void instanciateView() {

        filmizImage = findViewById(R.id.detailImage);
        filmizTitle = findViewById(R.id.detailTitle);
        filmizDescription = findViewById(R.id.detailInfos);
        filmizDescription.setMovementMethod(new ScrollingMovementMethod());
        filmizLanguage = findViewById(R.id.detailLanguage);
        filmizVOTitle = findViewById(R.id.detailTitleVO);
        filmizDate = findViewById(R.id.detailDate);
        filmizGenres = findViewById(R.id.detailGenres);
        filmizType = findViewById(R.id.detailType);
        filmRuntime = findViewById(R.id.detailDuree);

        suppButton = findViewById(R.id.detailSuppBtn);
        renameButton = findViewById(R.id.detailRenameBtn);
        newSeasonButton = findViewById(R.id.detailNewSeasonBtn);

        dispoSwitch = findViewById(R.id.detailDispo);

    }

    private void setSuppButtonListener() {

        suppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            askDelete(DataShared.getInstance().getCurrentFilmiz());

            }
        });

    }

    private void setRenameButtonListener(){

        renameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(FilmizDetails.this);

                final EditText edittext = new EditText(FilmizDetails.this);
                edittext.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                edittext.setText(fullTitle);
                alert.setMessage("Comment voulez-vous renommer " + fullTitle + " ?");
                alert.setTitle("Renommer");

                alert.setView(edittext);

                alert.setPositiveButton("Renommer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String newName = edittext.getText().toString();
                        Database.renameEntry(FilmizDetails.this, currentFilmiz, newName);
                        currentFilmiz.setTitle(newName);
                        Utils.openOtherActivity(FilmizDetails.class, FilmizDetails.this);

                    }
                });

                alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                    }
                });

                alert.show();

            }
        });

    }


    private void setNewSeasonButton(){

        if ((currentFilmiz.getType() == Filmiztype.SERIE) && (currentFilmiz.getStatus() == FilmizStatus.VU)) {
            newSeasonButton.setVisibility(View.VISIBLE);
        }

        newSeasonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentFilmiz.setStatus(FilmizStatus.AVOIR);
                Database.updateStatus(FilmizDetails.this, currentFilmiz);
                DataShared.getInstance().setSortMod(SortMods.ALAPHABETIC);
                Utils.openOtherActivity(MainActivity.class, FilmizDetails.this);
            }
        });

    }

    private void setSwitchListener(){
        dispoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                currentFilmiz.setDispo(b);
                Database.updateDispo(FilmizDetails.this, currentFilmiz);
            }
        });
    }

    private void setDatas(ResultModel choseFilmiz, MoreInfosModel details){


        System.out.println(createURL());
        setSuppButtonListener();
        setRenameButtonListener();
        setNewSeasonButton();
        setDispoSwitch();
        setSwitchListener();

        try {
            ResultModel filmiz = choseFilmiz;

            buildImage(filmiz.getImage());
            filmizTitle.setText(filmiz.getTitleFr());

            filmizDescription.setText(filmiz.getInfos());
            filmizVOTitle.setText("Titre d'origine : " + filmiz.getTitleVo());
            filmizLanguage.setText("Langue d'origine : " + setGoogLanguage(filmiz.getOriginalLanguage()));
            filmizDate.setText(filmiz.getDate());
            filmizGenres.setText("Genres : " + filmiz.getGenres());
            filmRuntime.setText(details.getDuree());
            filmizType.setText(Filmiztype.getString(currentFilmiz.getType()));
            filmizType.setBackgroundResource(Filmiztype.getBackgroundColor(currentFilmiz.getType()));

        }
        catch (NullPointerException e){

            filmizTitle.setText("Aucune information trouvée." +
                    "\n\nPeut être qu'il n'y pas de connection internet ?" +
                    "\n\nOu alors le nom ne correspond à aucun nom connu ; essayez de renommer !");
            filmizDescription.setVisibility(View.INVISIBLE);
            filmizLanguage.setVisibility(View.INVISIBLE);
            filmizVOTitle.setVisibility(View.INVISIBLE);
            filmizDate.setVisibility(View.INVISIBLE);
            filmizGenres.setVisibility(View.INVISIBLE);
            filmizType.setVisibility(View.INVISIBLE);
            filmRuntime.setVisibility(View.INVISIBLE);

        }


    }
    
    private String createURL(){

        String title = fullTitle;
        String JSON_URL = "";

        if (currentFilmiz.getType() == Filmiztype.FILM) {
            JSON_URL += "https://api.themoviedb.org/3/search/movie?api_key=8e65894c29a6dd9b0f230c2f0fe3bdb1&query=";
        }
        else {
            JSON_URL += "https://api.themoviedb.org/3/search/tv?api_key=8e65894c29a6dd9b0f230c2f0fe3bdb1&query=";
        }

        //JSON_URL += "https://api.themoviedb.org/3/search/multi?api_key=8e65894c29a6dd9b0f230c2f0fe3bdb1&query=";

        JSON_URL += title.replace(" ","+");
        JSON_URL += "&language=fr";

        return  JSON_URL;

    }

    private String createAdvancedURL(){

        try {

            String id = choseFilmiz.getId();
            String JSON_URL = "";

            if (currentFilmiz.getType() == Filmiztype.FILM) {
                JSON_URL += "https://api.themoviedb.org/3/movie/" + id + "?api_key=8e65894c29a6dd9b0f230c2f0fe3bdb1&language=fr";
            }
            else {
                JSON_URL += "https://api.themoviedb.org/3/tv/" + id + "?api_key=8e65894c29a6dd9b0f230c2f0fe3bdb1&language=fr";
            }

            //JSON_URL += "https://api.themoviedb.org/3/search/multi?api_key=8e65894c29a6dd9b0f230c2f0fe3bdb1&query=";

            return  JSON_URL;

        }

        catch (NullPointerException e){
            return "";
        }


    }

    private void buildImage(String URL){
        String imageURL = "";
        imageURL += "https://image.tmdb.org/t/p/w500";
        imageURL += URL;
        Glide.with(this).load(imageURL).into(filmizImage);
    }

    private ResultModel getTheGoodOne(List<ResultModel> results){

        final String comparableTitle = StringUtils.stripAccents(fullTitle).toLowerCase();

        try {

            for (ResultModel filmiz : results){

                final String comparableTitleFr = StringUtils.stripAccents(filmiz.getTitleFr()).toLowerCase();
                final String comparableTitleVO = StringUtils.stripAccents(filmiz.getTitleVo()).toLowerCase();

                if ((comparableTitle.equals(comparableTitleFr)) || comparableTitle.equals(comparableTitleVO)){
                    return filmiz;
                }

            }

            return results.get(0);

        }

         catch (IndexOutOfBoundsException e){

            return null;

        }


    }

    private void askDelete(Filmiz filmiz){

        AlertDialog.Builder popUp = new AlertDialog.Builder(this);
        popUp.setMessage("Voulez-vous supprimer cette entrée ?");
        popUp.setPositiveButton("Non", (dialog, which) -> {
        });
        popUp.setNegativeButton("Oui", (dialog, which) -> {


            Database.deleteEntry(this, filmiz);
            DataShared.getInstance().setSortMod(SortMods.ALAPHABETIC);
            Utils.openOtherActivity(MainActivity.class, this);

        });
        popUp.show();

    }

    private void setDispoSwitch (){

        if (currentFilmiz.isDispo()){
            dispoSwitch.setChecked(true);
        }
        else dispoSwitch.setChecked(false);

    }

    private String setGoogLanguage(String ISO){

        switch (ISO){
            case "fr":
                return "Français";
            case "en":
                return "Anglais";
            case "de":
                return "Allemand";
            case "ko" :
                return "Coréen";
            case "es" :
                return "Espagnol";
            case "it" : return "Italien";
            case "ja" : return "Japonais";
            case "tr" : return "Turc";
            case "no" : return "Norvégien";
            default:return ISO;
        }

    }


    @Override
    public void processFinish(List<ResultModel> output) {
        choseFilmiz = getTheGoodOne(output);
        GetAdvancedDatas getAdvancedDatas = new GetAdvancedDatas(createAdvancedURL(), this, this);
        getAdvancedDatas.execute();

    }

    @Override
    public void advancedProcessFinish(MoreInfosModel output) {
        instanciateView();
        setDatas(choseFilmiz, output);
    }
}
