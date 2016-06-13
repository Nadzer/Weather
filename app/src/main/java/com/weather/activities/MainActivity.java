package com.weather.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;
import com.weather.R;
import com.weather.adapters.WeatherListAdapter;
import com.weather.core.AsyncWeatherData;
import com.weather.core.Weather;
import com.weather.listeners.ResultListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ResultListener {
    private AsyncWeatherData mAsyncWeatherData;
    private ListView mListView;
    private TextView mCity;
    private SlidingUpPanelLayout mLayout;
    private ArrayList<Weather> mWeathers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mListView = (ListView) findViewById(R.id.weather_list);
        mCity = (TextView) findViewById(R.id.city);
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.setPanelHeight(0);

        // Retrieving weather's data
        mAsyncWeatherData = new AsyncWeatherData();
        mAsyncWeatherData.setOnResultListener(this);
        mAsyncWeatherData.execute();
    }

    /**
     * MENU
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_location:
                getLocation();
                break;
            case R.id.menu_city:
                setDialogBox();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setDialogBox() {
        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setHint(R.string.enter_city);
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle(getResources().getString(R.string.your_city))
                .setView(input)
                .setCancelable(true)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AsyncWeatherData newData = new AsyncWeatherData();

                        newData.getData(mAsyncWeatherData);
                        newData.execute(input.getText().toString());
                    }
                })
                .show();
    }

    /**
     * RETRIEVE
     * OR LET USER ENTER HIS LOCATION
     */

    private void getLocation() {
        Double lat;
        Double lon;
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int REQUEST_CODE_ASK_PERMISSIONS = 123;
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
                getLocation();
            }
            return;
        }

        Boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        Boolean isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle(getString(R.string.gps_settings));
            alertDialog.setMessage(getString(R.string.gps_not_activated));

            alertDialog.setPositiveButton(getString(R.string.settings), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    MainActivity.this.startActivity(intent);
                }
            });

            alertDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        } else {
            Location currentLocation = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            lat = currentLocation.getLatitude();
            lon = currentLocation.getLongitude();
            Geocoder gcd = new Geocoder(MainActivity.this, Locale.getDefault());
            List<Address> addresses = null;

            try {
                addresses = gcd.getFromLocation(lat, lon, 1);
                if (addresses.size() > 0) {
                    AsyncWeatherData newData = new AsyncWeatherData();
                    newData.getData(mAsyncWeatherData);
                    newData.execute(addresses.get(0).getLocality());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * HANDLING DATA RETURN
     * FORMATTING & DISPLAY
     */

    @Override
    public void onSuccessfulReturn(ArrayList<Weather> weathers) {
        this.mWeathers = weathers;
        if (!weathers.isEmpty()) {
            mCity.setText(weathers.get(0).getCity());

            WeatherListAdapter adapter = new WeatherListAdapter(weathers, MainActivity.this);
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mLayout.setPanelState(PanelState.EXPANDED);
                    setSlidingUI(position);
                }
            });
        }
    }

    private void setSlidingUI(int position) {
        TextView city = (TextView) findViewById(R.id.sliding_city);
        TextView morn = (TextView) findViewById(R.id.sliding_morn);
        TextView eve = (TextView) findViewById(R.id.sliding_eve);
        TextView night = (TextView) findViewById(R.id.sliding_night);
        TextView min = (TextView) findViewById(R.id.sliding_min);
        TextView max = (TextView) findViewById(R.id.sliding_max);

        city.setText(String.format("%s", mWeathers.get(position).getCity()));
        morn.setText(String.format("%s %d %s", getString(R.string.morn),
                mWeathers.get(position).getMorning(), "°C"));
        eve.setText(String.format("%s %d %s", getString(R.string.eve),
                mWeathers.get(position).getEve(), "°C"));
        night.setText(String.format("%s %d %s", getString(R.string.night_sliding),
                mWeathers.get(position).getNight(), "°C"));
        min.setText(String.format("%s %d %s", getString(R.string.min),
                mWeathers.get(position).getMin(), "°C"));
        max.setText(String.format("%s %d %s", getString(R.string.max),
                mWeathers.get(position).getMax(), "°C"));
    }
}
