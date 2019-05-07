package com.pnuproject.travellog.Main.MapFragment.Controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.pnuproject.travellog.R;

public class AuthenticationDialog extends Activity {

    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.present_image);

        ImageButton start_camera = (ImageButton)findViewById(R.id.start_camera);
        start_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cameraIntent = new Intent(getApplicationContext(),CameraFunction.class);
                startActivity(cameraIntent);
            }
        });

    }
}
