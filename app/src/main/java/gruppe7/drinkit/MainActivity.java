package gruppe7.drinkit;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.content.Context;
import android.location.LocationManager;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

final class SettingsOptions{
    public boolean sortBoolean = true, openBoolean = false;}

public class MainActivity extends AppCompatActivity {
    final private static String TAG = "MainActivity";
    final private static String APP_TITLE = "DTU DrinkIt";
    final private int orangeColor = Color.rgb(250,150,0);
    final private int selectedOrange = Color.rgb(225,125,0);
    final private int TITLE_COLOR = Color.BLACK;
    final private static SettingsOptions settingsOptions = new SettingsOptions();
    public static final int progress_bar_type = 0;
    private ProgressDialog pDialog;
    boolean hasDownloadedFile = false;






    ArrayList<Bar> beerBars = new ArrayList<>();
    ArrayList<Bar> coffeeBars = new ArrayList<>();

    private static final int PICK_SETTINGS = 0 ;

    InputStream endeligInput;
    BeerFragment barFrag;

    FragmentManager fragMan;
    // Used to determine if Coffee-button has been clicked
    // so that the screen reverts to Beer-fragment
    boolean coffeeActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAllPermissions();
        setContentView(R.layout.activity_main);

        // Set up toolbar with title and settings button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(APP_TITLE);
        toolbar.setBackgroundColor(orangeColor);
        toolbar.setTitleTextColor(TITLE_COLOR);
        setSupportActionBar(toolbar);

        // Initialise list of bars in the two ArrayLists
        try {
            readFile(coffeeBars, beerBars);
        } catch (IOException e) {
            e.printStackTrace();
        }
       // Load saved sortoptions
        new DownloadFilesTask().execute();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        settingsOptions.sortBoolean = settings.getBoolean("sortSave", true);
        settingsOptions.openBoolean = settings.getBoolean("openSave", false);


        fragMan = getSupportFragmentManager();
        barFrag = new BeerFragment();

        for(int i = 0; i < beerBars.size(); i++) {
            barFrag.bars.add(beerBars.get(i));
        }

        // Set default screen to a BeerFragment (should probably be changed to Coffee)
        FragmentTransaction fragTrans = fragMan.beginTransaction();
        fragTrans.add(R.id.list_upper_container, barFrag);
        fragTrans.commit();

        if (settingsOptions.sortBoolean) {
            sortDistance(barFrag.bars);
        } else {
            sortPrice(barFrag.bars);
        }

        // Make buttons for toggling the bar list
        final Button coffeeButton = (Button) findViewById(R.id.coffeeButton);
        coffeeButton.setBackgroundColor(orangeColor);
        final Button beerButton = (Button) findViewById(R.id.beerButton);
        beerButton.setBackgroundColor(orangeColor);

        // Remember to change colors, if default button is coffee
        coffeeButton.setBackgroundColor(orangeColor);
        beerButton.setBackgroundColor(selectedOrange);


        coffeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Install coffeeFragment

                if (!coffeeActive) {
                    // Sort list of coffee bars
                    if (settingsOptions.sortBoolean) {
                        sortDistance(coffeeBars);
                    } else {
                        sortPrice(coffeeBars);
                    }

                    BeerFragment updatedBarFrag = new BeerFragment();
                    setSettingsOpenBoolean(updatedBarFrag);

                    // Add bar names to the list of buttons
                    for(int i = 0; i < coffeeBars.size(); i++) {
                        updatedBarFrag.bars.add(coffeeBars.get(i));
                    }

                    // Install beerFragment
                    FragmentTransaction fragTrans = fragMan.beginTransaction();
                    fragTrans.replace(R.id.list_upper_container, updatedBarFrag);
                    fragTrans.commit();

                    beerButton.setBackgroundColor(orangeColor);
                    coffeeButton.setBackgroundColor(selectedOrange);

                    coffeeActive = true;
                }
            }
        });

        beerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (coffeeActive) {
                    coffeeActive = false;

                    // Sort list of beer bars
                    if (settingsOptions.sortBoolean) {
                        sortDistance(beerBars);
                    } else {
                        sortPrice(beerBars);
                    }

                    // Update ArrayList to beerbars
                    BeerFragment updatedBarFrag = new BeerFragment();
                    setSettingsOpenBoolean(updatedBarFrag);


                    // Add bar names to the list of buttons
                    for(int i = 0; i < beerBars.size(); i++) {
                        updatedBarFrag.bars.add(beerBars.get(i));
                    }

                    // Install beerFragment
                    FragmentTransaction fragTrans = fragMan.beginTransaction();
                    fragTrans.replace(R.id.list_upper_container, updatedBarFrag);
                    fragTrans.commit();

                    coffeeButton.setBackgroundColor(orangeColor);
                    beerButton.setBackgroundColor(selectedOrange);
                }
            }
        });
    }
    @Override
    protected void onPause(){
        super.onPause();
        // Saves sortings options, on pause
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("sortSave", settingsOptions.sortBoolean);
        editor.putBoolean("openSave", settingsOptions.openBoolean);
        editor.commit();
    }

    // Opretter layout for toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    // Åbner settings menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //Checkes, what options button is pressed
        // Settings-button pressed
        if (id == R.id.action_settings) {

            Intent openSettings = new Intent(MainActivity.this, Settings.class);
            startActivityForResult(openSettings, PICK_SETTINGS);
            return true;
        }
        // Refresh-button pressed
        if (id == R.id.action_refresh) {

            coffeeBars.clear();
            beerBars.clear();

            try {
                readFile(coffeeBars, beerBars);
            } catch (IOException e) {
                e.printStackTrace();
            }

            new DownloadFilesTask().execute();
            fragMan = getSupportFragmentManager();

                 barFrag = new BeerFragment();
                setSettingsOpenBoolean(barFrag);

            if (coffeeActive) {
                for(int i = 0; i < coffeeBars.size(); i++) {
                    barFrag.bars.add(coffeeBars.get(i));}
            } else {
                for (int i = 0; i < beerBars.size(); i++) {
                    barFrag.bars.add(beerBars.get(i));
                }
            }

            FragmentTransaction fragTrans = fragMan.beginTransaction();
            fragTrans.replace(R.id.list_upper_container, barFrag);
            fragTrans.commit();



            setSettingsOpenBoolean(barFrag);
            if (settingsOptions.sortBoolean) {
                sortDistance(barFrag.bars);
            } else {
                sortPrice(barFrag.bars);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (coffeeActive) {
            barFrag.bars = coffeeBars;
        } else {
            barFrag.bars = beerBars;
        }

            // Sort list of beer bars
            if (settingsOptions.sortBoolean) {
                sortDistance(barFrag.bars);
            } else {
                sortPrice(barFrag.bars);
            }

            // Update ArrayList to beerbars
            BeerFragment updatedBarFrag = new BeerFragment();
             setSettingsOpenBoolean(updatedBarFrag);



        // Add bar names to the list of buttons
            for (int i = 0; i < barFrag.bars.size(); i++) {
                updatedBarFrag.bars.add(barFrag.bars.get(i));
            }

            // Install beerFragment
            FragmentTransaction fragTrans = fragMan.beginTransaction();
            fragTrans.replace(R.id.list_upper_container, updatedBarFrag);
            fragTrans.commit();
    }
    @Override
    protected void onActivityResult(int req, int res, Intent intent) {
        // Checkes if resultat originates from Settings
        if (res == Activity.RESULT_OK && req == PICK_SETTINGS) {
           //Gets the extras from the results intent
            if (intent.getExtras() != null) {
                settingsOptions.sortBoolean = intent.getExtras().getBoolean("SortBoolean", true);
                settingsOptions.openBoolean = intent.getExtras().getBoolean("OpenBoolean", false);
                // Passes the boolean on to BeerFragment
                setSettingsOpenBoolean(barFrag);
            } else {
                settingsOptions.sortBoolean = true;
                settingsOptions.openBoolean = false;
            }
            // Set sorting after current active tab.
            if (settingsOptions.sortBoolean) {
                if (coffeeActive) {
                    sortDistance(coffeeBars);
                } else {
                    sortDistance(beerBars);
                }
            } else  {
                if (coffeeActive) {
                    sortPrice(coffeeBars);
                } else {
                    sortPrice(beerBars);
                }
            }
        }
    }

    private void displayDistance(Bar bar) {

        bar.setDistance(afstandsberegner(bar.getLatitude(), bar.getLongitude()));
        if (bar.getButtonName().indexOf("-") == -1) {
            bar.setButtonName(bar.getButtonName() + "  -  " + bar.getDistance() + " m");
        } else {
            bar.setButtonName(bar.getButtonName().substring(0, bar.getButtonName().indexOf("-") - 1)
                    + "- " + bar.getDistance() + " m");
        }

    }

    public int afstandsberegner(double lat, double longti) {

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        try {
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
            double R = 6372800;
            double lat1 = lastKnownLocation.getLatitude();
            double lat2 = lat;
            double long1 = lastKnownLocation.getLongitude();
            double long2 = longti;

            double deltaLat = Math.toRadians(lat2 - lat1);
            double deltaLong = Math.toRadians(long2 - long1);
            lat1 = Math.toRadians(lat1);
            lat2 = Math.toRadians(lat2);

            double a = Math.pow(Math.sin(deltaLat / 2),2) + Math.pow(Math.sin(deltaLong / 2),2) * Math.cos(lat1) * Math.cos(lat2);
            double c = 2 * Math.asin(Math.sqrt(a));
            double afstand = R*c;
            return ((int) afstand);

            // Se http://www.movable-type.co.uk/scripts/latlong.html for formler
        } catch (SecurityException e){
            return 0;
        }catch (NullPointerException e){
            return 0;
        }
    }

    public boolean getAllPermissions(){
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int ContactPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        int InternetPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        int WritePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        ArrayList<String> listPermissionsNeeded = new ArrayList<>();

        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if(ContactPermission!= PackageManager.PERMISSION_GRANTED){
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }
        if(InternetPermission != PackageManager.PERMISSION_GRANTED){
            listPermissionsNeeded.add(Manifest.permission.INTERNET);
        }
        if(WritePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),1);
            return false;
        }
        return true;

    }

    public void readFile(ArrayList<Bar> coffeeBars, ArrayList<Bar> beerBars) throws IOException {
        String str;
        StringBuffer buf = new StringBuffer();
        try {

            InputStream is;
            BufferedReader reader;
            File file = new File(Environment.getExternalStorageDirectory().toString() + "/database.txt");
            if (file.exists()){
                is = new FileInputStream(file);
            } else {
                is = this.getResources().openRawResource(R.raw.databasetest);
            }
            reader = new BufferedReader(new InputStreamReader(is));


            if (is != null) {
                while ((str = reader.readLine()) != null) {
                    Bar bar = new Bar();
                    bar.setName(str);
                    bar.setButtonName(str);
                    bar.setLocation(reader.readLine());
                    bar.setLatitude(Double.parseDouble(reader.readLine()));
                    bar.setLongitude(Double.parseDouble(reader.readLine()));
                    displayDistance(bar);
                    bar.setOpen(reader.readLine());
                    bar.setOpeningTime(reader.readLine());
                    bar.setClosingTime(reader.readLine());
                    bar.setPrice(Double.parseDouble(reader.readLine()));
                    bar.setAmount(Integer.parseInt(reader.readLine()));
                    if (reader.readLine().equals("ØL")) {
                        beerBars.add(bar);
                    } else{
                        coffeeBars.add(bar);
                    }


                }
            }

        }catch (MalformedURLException e){
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private class DownloadFilesTask extends AsyncTask<Void, String, InputStream> {

        protected InputStream doInBackground(Void... urls) {
            try {
                String placeringITelefon = Environment.getExternalStorageDirectory().toString();
                URL url = new URL("http://www.student.dtu.dk/~s153200/databasetest.txt");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                int fileSize = urlConnection.getContentLength();
                InputStream is = new BufferedInputStream(url.openStream(),
                        8192);

                OutputStream out = new FileOutputStream(placeringITelefon + "/database.txt");

                byte data[] = new byte[1024];
                int deleHentet;
                long total = 0;
                while ((deleHentet = is.read(data)) != 1){
                    out.write(data, 0 , deleHentet);
                }

                out.flush();
                out.close();
                is.close();

            } catch (Exception e){

            }
            return null;
        }

    }

    public static void sortPrice(ArrayList<Bar> bars ) {
        for(int i = 0; i<bars.size(); i++){
            bars.get(i).setSortBy("price");
        }

        Collections.sort(bars);
    }

    public static void sortDistance(ArrayList<Bar> bars) {
        for(int i = 0; i<bars.size(); i++){
            bars.get(i).setSortBy("");
        }

        Collections.sort(bars);
    }

    public static void setSettingsOpenBoolean(BeerFragment frag){
        frag.settingsOptionsOpen = settingsOptions.openBoolean;
    }

}