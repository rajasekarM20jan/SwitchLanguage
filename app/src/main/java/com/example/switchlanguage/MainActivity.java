package com.example.switchlanguage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(MainActivity.this,AppRunTester.class));
    }



    public static void setLocale(Activity activity,String lang){
            try{
                Locale locale = new Locale(lang);
                Locale.setDefault(locale);
                Resources resources = activity.getResources();
                Configuration config = resources.getConfiguration();
                config.setLocale(locale);
                resources.updateConfiguration(config, resources.getDisplayMetrics());
                //            Locale locale = new Locale(lang);
                //            Locale.setDefault(locale);
                //            Configuration config = new Configuration();
                //            config.locale = locale;
                // activity.getBaseContext().getResources().updateConfiguration(config,activity.getBaseContext().getResources().getDisplayMetrics());
                SharedPreferences pref=activity.getSharedPreferences("Settings", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("My_Lang",lang);
                editor.apply();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                Toast.makeText(activity, ex.toString(), Toast.LENGTH_SHORT).show();
                return;
            }
        }

    public static void loadlocale(Activity activity) {
        try {
            SharedPreferences pref = activity.getSharedPreferences("Settings", MODE_PRIVATE);
            String language = pref.getString("My_Lang","");
            setLocale(activity,language);
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(activity, ex.toString(), Toast.LENGTH_SHORT).show();
            return;
        }
    }

}