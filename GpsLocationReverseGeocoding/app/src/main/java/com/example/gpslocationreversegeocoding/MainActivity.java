package com.example.gpslocationreversegeocoding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient flc;
    private TextView coordinateText;
    private Geocoder geocoder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coordinateText = (TextView) findViewById(R.id.coordinateText);

        flc = LocationServices.getFusedLocationProviderClient(this);

        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
                else
                {
                    GetLocation();
                }
            }
        });
    }

    private void GetLocation()
    {
        flc.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    ChangeGPSText(location);
                }
            }
        });
    }

    private void ChangeGPSText(Location location)
    {
        double lati = location.getLatitude();
        double longi = location.getLongitude();
        String latiS = String.format("%.2f", lati);
        String longiS = String.format("%.2f", longi);

        coordinateText.setText(latiS + ", " + longiS);
        geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> address;
        try
        {
            address = geocoder.getFromLocation(lati, longi, 1);
            coordinateText.append("\n" + address.get(0).getAddressLine(0));
        }
        catch (Exception e)
        {
            Log.d("EXCEPTION", e.toString());
        }

    }
}
