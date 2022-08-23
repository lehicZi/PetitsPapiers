package com.example.petitspapiers.views.details;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.petitspapiers.DataShared;
import com.example.petitspapiers.constants.Filmiztype;
import com.example.petitspapiers.objects.Filmiz;
import com.example.petitspapiers.objects.MoreInfosModel;
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

public class GetAdvancedDatas extends AsyncTask<String, String, String> {

    private AsyncResponse asyncResponse;
    ProgressDialog progressDialog;

    private String runtime;

    private MoreInfosModel advancedInfos;

    private final Filmiz currentFilmiz;

    final String JSON_URL;
    final Context context;
    Boolean isOK = false;

    public GetAdvancedDatas(String JSON_URL, Context context, AsyncResponse asyncResponse) {

        this.JSON_URL = JSON_URL;
        this.context = context;
        this.asyncResponse = asyncResponse;
        this.advancedInfos = new MoreInfosModel();
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


                if (currentFilmiz.getType() == Filmiztype.SERIE){

                    String nbSeasons = jsonObject.getString("number_of_seasons");
                    String nbEp = jsonObject.getString("number_of_episodes");
                    runtime = translateSeriesRuntime(nbEp, nbSeasons);

                    //Add Series relative advanced fields


                }
                else {

                    runtime = jsonObject.getString("runtime") + " min.";

                    //Add Films relative advanced fields


                }

            //Add global advanced fields

            //Don't forget to update details arguments
            advancedInfos.setDuree(runtime);

            } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }

        asyncResponse.advancedProcessFinish(advancedInfos);

    }

    private String translateSeriesRuntime(String nbEp, String nbSeas){

        String toReturn = nbEp + " Épisodes sur " + nbSeas + " saisons.";

        return toReturn;
    }



    public interface AsyncResponse {
        void advancedProcessFinish(MoreInfosModel output);
    }
}
