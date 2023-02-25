package com.example.switchlanguage;

import static com.example.switchlanguage.MainActivity.loadlocale;
import static com.example.switchlanguage.MainActivity.setLocale;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AppRunTester extends AppCompatActivity {
    Button languageSwitchButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadlocale(AppRunTester.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_run_tester);
        languageSwitchButton=findViewById(R.id.languageSwitchButton);
        languageSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangelanguage(AppRunTester.this);
            }
        });
    }

    public void showChangelanguage(Activity activity) {
        try {
            final String[] items = {"Hindi", "English"};
            android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(activity);
            mBuilder.setTitle("Choose Language");
            mBuilder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (i == 0) {
                        setLocale(activity, "hi");
                        recreate();
                    } else if (i == 1) {
                        setLocale(activity, "en");
                        recreate();
                    }
                    dialogInterface.dismiss();
                }
            });
            android.app.AlertDialog mDialog = mBuilder.create();
            mDialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(activity, ex.toString(), Toast.LENGTH_SHORT).show();
            return;
        }
    }
}