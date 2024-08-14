package com.example.tvlights.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tvlights.Network.Connection;
import com.example.tvlights.File.MemoryOperations;
import com.example.tvlights.R;
import com.example.tvlights.Utils.JsonParser;
import com.example.tvlights.Utils.ScaleLayouts;

public class MainActivity extends Activity implements ActivityBuildInterface,View.OnClickListener {
    ScaleLayouts scaleLayoutsController = new ScaleLayouts();
    Connection connectionController = new Connection();
    MemoryOperations memoryContorller = new MemoryOperations();


    private View exitView;
    private TextView exitTitleTV,exitInfoTV,appTitleTV;
    private Button confirmExitBT,cancelExitBT,ledsColorBT,settingsBT;
    private ImageView appLogoIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        matchControlsById();
        setControlsParams();
        setControlsToListeners();

        try {
            memoryContorller.ReadSerializedObject();
            connectionController.startConnection();
        }
        catch (InterruptedException e) {
            Toast.makeText(this,"Couldn't connect to ledStrip controller.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this,"Couldn't load settings.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void matchControlsById() {
        exitView = findViewById(R.id.exitView);
        exitTitleTV = findViewById(R.id.exitTitleTV);
        exitInfoTV = findViewById(R.id.exitInfoTV);
        confirmExitBT = findViewById(R.id.confirmBT);
        cancelExitBT = findViewById(R.id.cancelBT);
        appTitleTV = findViewById(R.id.appTitleTV);
        appLogoIV = findViewById(R.id.appLogoIV);
        ledsColorBT = findViewById(R.id.ledsColorBT);
        settingsBT = findViewById(R.id.appSettingsBT);
    }

    @Override
    public void setControlsParams() {
        scaleLayoutsController.setScallingParams(100, 10, 50, 10, 0, appTitleTV);
        scaleLayoutsController.setScallingParams(98, 40, 0, 30, 1, appLogoIV);
        scaleLayoutsController.setScallingParams(60, 7, 30, 37, 20, ledsColorBT);
        scaleLayoutsController.setScallingParams(60, 7, 30, 50, 20, settingsBT);

        scaleLayoutsController.setScallingParams(80, 30, 0, 35, 10, exitView);
        scaleLayoutsController.setScallingParams(100, 5, 40, 0, 0, exitTitleTV);
        scaleLayoutsController.setScallingParams(100, 15, 20, 7, 2, exitInfoTV);
        scaleLayoutsController.setScallingParams(15, 5, 30, 23, 15, confirmExitBT);
        scaleLayoutsController.setScallingParams(15, 5, 20, 23, 50, cancelExitBT);
    }

    @Override
    public void setControlsToListeners() {
        ledsColorBT.setOnClickListener(this);
        settingsBT.setOnClickListener(this);
        confirmExitBT.setOnClickListener(this);
        cancelExitBT.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        exitView.setVisibility(View.VISIBLE);
        exitView.requestFocus();
        cancelExitBT.requestFocus();
    }

    public void onDestroy() {
        connectionController.stopConnection();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        //opens colorPickActivity
        if (id == ledsColorBT.getId()) {
            startActivity(new Intent(this, ColorPickActivity.class));
        }

        //opens AppSettingsActivity
        if (id == settingsBT.getId()) {
            startActivity(new Intent(this, AppSettingsActivity.class));
        }

        //exit view, confirms exiting app
        if (id == confirmExitBT.getId()) {
            this.finish();
        }

        //exit view, cancel exiting from app
        if (id == cancelExitBT.getId()) {
            exitView.setVisibility(View.GONE);
        }
    }
}