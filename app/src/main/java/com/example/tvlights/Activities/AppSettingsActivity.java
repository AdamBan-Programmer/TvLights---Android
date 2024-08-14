package com.example.tvlights.Activities;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.tvlights.Settings.AppSettings;
import com.example.tvlights.File.MemoryOperations;
import com.example.tvlights.R;
import com.example.tvlights.Utils.ScaleLayouts;

public class AppSettingsActivity extends Activity implements ActivityBuildInterface,View.OnClickListener {

    MemoryOperations memoryController = new MemoryOperations();
    AppSettings appSettingsController = new AppSettings();
    ScaleLayouts scallingController = new ScaleLayouts();

    private static final int PERMISSION_REQUEST_CODE = 111;

    TextView titleBarTV,ipAddressTV,portTV;
    EditText ipAddressET,portET;
    Button saveBT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);

        matchControlsById();
        setControlsParams();
        setControlsToListeners();

        //if permissions not granted
        if (!Environment.isExternalStorageManager()) {
            showPermissionsRequest();
        }
        setSettingsValues();
    }

    @Override
    public void matchControlsById() {
        titleBarTV = findViewById(R.id.titleBarTV);
        ipAddressTV = findViewById(R.id.ipAddressTV);
        ipAddressET = findViewById(R.id.ipAddressET);
        portTV = findViewById(R.id.portTV);
        portET = findViewById(R.id.portET);
        saveBT = findViewById(R.id.saveBT);
    }

    @Override
    public void setControlsParams() {
        scallingController.setScallingParams(100, 7, 40, 0, 0, titleBarTV);
        scallingController.setScallingParams(30,10,20,15,5,ipAddressTV);
        scallingController.setScallingParams(40,10,30,15,35,ipAddressET);
        scallingController.setScallingParams(30,10,20,30,5,portTV);
        scallingController.setScallingParams(40,10,30,30,35,portET);
        scallingController.setScallingParams(80,10,30,85,10,saveBT);
    }

    @Override
    public void setControlsToListeners() {
        saveBT.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == saveBT.getId())
        {
            try {
                saveSettings();
            }
            catch (Exception e)
            {
                Toast.makeText(this, "ERROR! Serialization failed!", Toast.LENGTH_SHORT).show();
            }
            finally {
                this.finish();
            }
        }
    }

    //manage all files permission
    private void showPermissionsRequest() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        } else {
            //below android 11=======
            startActivity(new Intent(this, AppSettingsActivity.class));
            ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    //into controls
    private void setSettingsValues() {
        ipAddressET.setText(String.valueOf(appSettingsController.getCurrentAppSettings().getClientAddress().getIpAddress()));
        portET.setText(String.valueOf(appSettingsController.getCurrentAppSettings().getClientAddress().getPort()));
    }

    private void saveSettings() throws Exception {
        getSettingsValues();
        AppSettings settings = appSettingsController.getCurrentAppSettings();
        memoryController.SerializeObjectToFile(settings);
        Toast.makeText(getApplicationContext(), "Settings Updated Succesfully!", Toast.LENGTH_SHORT).show();
    }

    //from controls
    private void getSettingsValues()
    {
        appSettingsController.getCurrentAppSettings().getClientAddress().setIpAddress(ipAddressET.getText().toString());
        appSettingsController.getCurrentAppSettings().getClientAddress().setPort(Integer.parseInt(portET.getText().toString()));
    }
}