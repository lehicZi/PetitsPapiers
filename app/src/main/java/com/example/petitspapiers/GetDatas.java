package com.example.petitspapiers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.petitspapiers.constants.Filmiztype;
import com.example.petitspapiers.objects.Filmiz;
import com.example.petitspapiers.objects.ResultModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetDatas extends AsyncTask<String, String, String> {

    private AsyncResponse asyncResponse;
    ProgressDialog progressDialog;

    private String titleFr;
    private String titleVo;
    private String image;
    private String infos;
    private String originalLanguage;
    private String mediaType;
    private String date;
    private String genres;
    private String id;

    private List<ResultModel> filmizs;

    private final Filmiz currentFilmiz;

    final String JSON_URL;
    final Context context;
    Boolean isOK = false;

    public GetDatas(String JSON_URL, Context context, AsyncResponse asyncResponse) {

        this.JSON_URL = JSON_URL;
        this.context = context;
        this.asyncResponse = asyncResponse;
        this.filmizs = new ArrayList<>();
        this.currentFilmiz = DataShared.getInstance().getCurrentFilmiz();
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Chargement des données");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        String current = "";

        try {
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(JSON_URL);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();
                while (data != -1) {

                    current += (char) data;
                    //System.out.println(current);
                    data = inputStreamReader.read();

                }

                return current;

            } catch (IOException e) {
                System.out.println("Erreur URL1");
                e.printStackTrace();
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

            }

        }
        catch (Exception e){
            System.out.println("Erreur URL2");
            e.printStackTrace();
        }

        return current;

    }

    @Override
    protected void onPostExecute(String s) {
        progressDialog.dismiss();
        try {
        JSONObject jsonObject = new JSONObject(s);
        JSONArray jsonArray = jsonObject.getJSONArray("results");

            for (int i = 0; i< jsonArray.length(); i++) {

                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                if (currentFilmiz.getType() == Filmiztype.SERIE){

                    titleFr = jsonObject1.getString("name");
                    titleVo = jsonObject1.getString("original_name");

                    try {
                        date = "Date de parution du premier épidode : " + translateDate(jsonObject1.getString("first_air_date"));
                    }
                    catch (ArrayIndexOutOfBoundsException e){
                        date = "";
                    }

                }
                else {

                    titleFr = jsonObject1.getString("title");
                    titleVo = jsonObject1.getString("original_title");
                    try {
                        date = "Date de parution : " + translateDate(jsonObject1.getString("release_date"));
                    }
                    catch (ArrayIndexOutOfBoundsException e){
                        date = "";
                    }

                }

                JSONArray genres = jsonObject1.getJSONArray("genre_ids");
                this.genres = "";
                String separator = "";
                for (int genreIndex = 0 ;  genreIndex < genres.length() ; genreIndex ++ ){

                    int genreId = genres.getInt(genreIndex);
                    this.genres += separator;
                    this.genres += translateGenre(genreId);
                    separator = ", ";

                }

                id = jsonObject1.getString("id");
                image = jsonObject1.getString("poster_path");
                infos = jsonObject1.getString("overview");
                originalLanguage = jsonObject1.getString("original_language");

                ResultModel resultModel = new ResultModel(titleFr, titleVo, image, infos, originalLanguage, date, this.genres, id);

                this.filmizs.add(resultModel);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        asyncResponse.processFinish(filmizs);

    }

    public List<ResultModel> getFilmizs() {
        return filmizs;
    }

    public Boolean getOK() {
        return isOK;
    }

    private String translateDate(String enDate){

        String translatedDate = "";
        String[] separatedDate = enDate.split("-");
        translatedDate += separatedDate[2] += "/";
        translatedDate += separatedDate[1] += "/";
        translatedDate += separatedDate[0];

        return translatedDate;

    }

    private String translateGenre(int id){
        switch (id){

            case 28 : return "Action";
            case 16 : return "Animation";
            case 99 : return "Documentaire";
            case 18 : return "Drame";
            case 10751 : return "Familial";
            case 14 : return "Fantastique";
            case 36 : return  "Historique";
            case 35 : return "Comédie";
            case 10752 : return "Guerre";
            case 80 : return "Crime";
            case 10402 : return "Musique";
            case 9648 : return "Mystère";
            case 10749 : return "Amour";
            case 878 : return "Science-fiction";
            case 27 : return "Horreur";
            case 10770 : return "Téléfilm";
            case 53 : return "Thriller";
            case 37 : return "Western";
            case 12 : return "Aventure";
            case 10759 : return "Action & Aventure";
            case 10762 : return "Enfants";
            case 10763 : return "Actualités";
            case 10764 : return "Télé-Réalité";
            case 10765 : return "Science-Fiction & Fantastique";
            case 10766 : return "Sitcom";
            case 10767 : return "Talkshow";
            case 10768 : return "Guere & Politique";
            default: return "Unknown genre id : " + id;

        }
    }

    public interface AsyncResponse {
        void processFinish(List<ResultModel> output);
    }
}
