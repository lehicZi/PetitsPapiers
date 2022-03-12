package com.example.petitspapiers.views;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petitspapiers.Database;
import com.example.petitspapiers.R;
import com.example.petitspapiers.Utils;

import java.io.IOException;

public class ImportExportView extends AppCompatActivity {

    Button importButton;
    Button exportButton;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_export_view);

        instantiateView();
        setlisteners();
    }

    private void instantiateView(){

        importButton = findViewById(R.id.buttonImport);
        exportButton = findViewById(R.id.buttonExport);

    }

    private void setlisteners(){


        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Database db = new Database(ImportExportView.this);
                try {
                    db.importDatabase("Backup");
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Database db = new Database(ImportExportView.this);
                try {
                    db.exportDatabase("Backup");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });



    }

    @Override
    public  void onBackPressed(){

        Utils.openOtherActivity(MainActivity.class, this);


    }


}
