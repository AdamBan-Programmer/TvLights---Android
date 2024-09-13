package com.example.tvlights.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tvlights.Utils.BitmapModifier;
import com.example.tvlights.Network.Connection;
import com.example.tvlights.Utils.JsonParser;
import com.example.tvlights.R;
import com.example.tvlights.Utils.ScaleLayouts;

import java.io.IOException;

public class ColorPickActivity extends AppCompatActivity implements ActivityBuildInterface, View.OnClickListener,View.OnTouchListener {

    ScaleLayouts scallingController = new ScaleLayouts();
    JsonParser jsonParser = new JsonParser();

    TextView pickedColorTV,titleTV;
    ImageView colorPickerIV;
    View colorPreview;
    Button offLedsBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_calibration);

        matchControlsById();
        setControlsParams();
        setControlsToListeners();
    }

    @Override
    public void matchControlsById() {
        titleTV = findViewById(R.id.titleTV);
        pickedColorTV = findViewById(R.id.pickedColorTV);
        colorPickerIV = findViewById(R.id.colorPickerIV);
        colorPreview = findViewById(R.id.colorPreview);
        offLedsBT = findViewById(R.id.offLedsBT);
    }

    @Override
    public void setControlsParams() {
        scallingController.setScallingParams(80, 7, 70, 3, 10, titleTV);
        scallingController.setScallingParams(80, 60, 0, 10, 10, colorPickerIV);
        scallingController.setScallingParams(80, 5, 70, 70, 10, pickedColorTV);
        scallingController.setScallingParams(80, 10, 0, 75, 10, colorPreview);
        scallingController.setScallingParams(80, 7, 30, 88, 10, offLedsBT);
    }

    @Override
    public void setControlsToListeners() {
        colorPickerIV.setOnTouchListener(this);
        offLedsBT.setOnClickListener(this);
    }

    public void onStop() {
        Connection.stopConnection();
        Toast.makeText(this, "connection closed.", Toast.LENGTH_LONG).show();
        super.onStop();
    }

    public void onDestroy() {
        Connection.stopConnection();
        Toast.makeText(this, "connection closed.", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        try {
            if (Connection.connectionIsAvailable()) {
                Bitmap bmp = ((BitmapDrawable) colorPickerIV.getDrawable()).getBitmap();
                Bitmap scaled = BitmapModifier.resizeBitmap(bmp, colorPickerIV.getMeasuredHeight(), colorPickerIV.getMeasuredWidth());

                int pixels = scaled.getPixel((int) event.getX(), (int) event.getY());
                String colorHex = "#" + Integer.toHexString(pixels).substring(2);
                String colorJSON = jsonParser.toJsonFormat(colorHex);
                updateActivity(colorHex);
                Connection.sendLedsColor(colorJSON);
            }
        } catch (IOException | InterruptedException e) {
            Toast.makeText(this, "connection failed.", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    private void updateActivity(String color)
    {
        colorPreview.setBackgroundColor(Color.parseColor(color));
        pickedColorTV.setText("color: " + color);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        //off leds = set black color
        if (id == offLedsBT.getId()) {
            try {
                if (Connection.connectionIsAvailable()) {
                    String blackColor = jsonParser.toJsonFormat("#000000");
                    Connection.sendLedsColor(blackColor);
                    updateActivity("#000000");
                }
            } catch (IOException | InterruptedException e) {
                Toast.makeText(this, "connection failed.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
