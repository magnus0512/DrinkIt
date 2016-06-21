package gruppe7.drinkit;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
//import android.app.Fragment;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.content.Context;
import android.location.LocationManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    private boolean firstRun = true;
    final private double[][] placeringer = {
            { 55.782378, 12.517101} , //Hegnet
            { 55.782692, 12.521126} , //Diamanten
            { 55.783614, 12.517722} , //Studentercaféen 325
            { 55.786469, 12.525771} , //S-Huset / Kælderbaren / Kaffestuen
            { 55.783709, 12.524161} , //Døgn netto
            { 55.785300, 12.518852} , //PF cafe i 302
            { 55.787490, 12.518530} , //Etheren
            { 55.789282, 12.525072} , //Diagonalen
            { 55.780230, 12.516851} , //Maskinen

    };

    ArrayList<Bar> beerBars = new ArrayList<Bar>();
    ArrayList<Bar> coffeeBars = new ArrayList<Bar>();

    private static final int READ_CONTACTS_PERMISSION_REQUEST = 1;
    private static final int SEND_SMS_PERMISSION_REQUEST = 2;
    private static final int PICK_SETTINGS = 0 ;
    public static final String PREFS_NAME = "MyPrefsFile";

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
        Log.i(TAG,"entered OnCreate");

        // Set up toolbar with title and settings button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(APP_TITLE);
        toolbar.setBackgroundColor(orangeColor);
        toolbar.setTitleTextColor(TITLE_COLOR);
        setSupportActionBar(toolbar);

       /* if(getIntent().getExtras() != null) {
            Log.i(TAG, "IKKE TOM");
            settingsOptions.sortBoolean = getIntent().getExtras().getBoolean("SortBoolean", true);
            settingsOptions.openBoolean = getIntent().getExtras().getBoolean("OpenBoolean", false);
        }else{
            Log.i(TAG, "TOM");
            settingsOptions.sortBoolean = true;

           settingsOptions.openBoolean = false;}
       */
        // Initialise list of bars in the two ArrayLists
        try {
            readFile(coffeeBars, beerBars);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        settingsOptions.sortBoolean = settings.getBoolean("sortSave", true);
        settingsOptions.openBoolean = settings.getBoolean("openSave", false);


        fragMan = getSupportFragmentManager();
        barFrag = new BeerFragment();

        // Show permissions
        // getPermissionToReadUserContacts();
        // getPermissionToSendTexts();
        // getPermissionToTrackUser();

        // Add bar names to the list of buttons
        // In this case, beer bar is the default screen
        // Sort the bars first

        //sortPrice(beerBars);

        for(int i = 0; i < beerBars.size(); i++) {
            barFrag.bars.add(beerBars.get(i));
        }

        // Set default screen to a BeerFragment (should probably be changed to Coffee)
        FragmentTransaction fragTrans = fragMan.beginTransaction();
        fragTrans.add(R.id.list_upper_container, barFrag);
        //fragTrans.addToBackStack(null);
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
                    //fragMan.popBackStack();
                    //Log.i(TAG, "popped BeerFragment");

                    // Sort list of coffee bars
                    if (settingsOptions.sortBoolean) {
                        sortDistance(coffeeBars);
                        Log.i(TAG, "DISTANCE");
                    } else {
                        sortPrice(coffeeBars);
                        Log.i(TAG, "PRICE");

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
                    //fragTrans.addToBackStack(null);
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
                    //fragMan.popBackStack();
                    coffeeActive = false;
                    //Log.i(TAG, "popped CoffeeFragment");

                    // Sort list of beer bars
                    if (settingsOptions.sortBoolean) {
                        sortDistance(beerBars);
                        Log.i(TAG, "DISTANCE");
                    } else {
                        sortPrice(beerBars);
                        Log.i(TAG, "Price");

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
                    //fragTrans.addToBackStack(null);
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

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("sortSave", settingsOptions.sortBoolean);
        editor.putBoolean("openSave", settingsOptions.sortBoolean);
        Log.i(TAG, "PAUSE");
        editor.commit();
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "Entered the onSaveInstanceState() method");
        savedInstanceState.putBoolean("SORT_BOOLEAN", settingsOptions.sortBoolean);
        savedInstanceState.putBoolean("OPEN_BOOLEAN", settingsOptions.openBoolean);
        super.onSaveInstanceState(savedInstanceState);

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
        if (id == R.id.action_settings) {

            Intent openSettings = new Intent(MainActivity.this, Settings.class);
            startActivityForResult(openSettings, PICK_SETTINGS);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // TODO: Opret fragmenterne med settingsOptions.sortBoolean
    // Dvs. hvis Price-sort er valgt, skal et nyt fragment laves med denne sortering.
    // Benyt evt. den boolske værdi "coffeeActive"
    // TODO: Husk at tage højde for "Show Only Open"

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
                Log.i(TAG, "DISTANCE");
            } else {
                sortPrice(barFrag.bars);
                Log.i(TAG, "Price");
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
            //fragTrans.addToBackStack(null);
            fragTrans.commit();

       /* if (settingsOptions.sortBoolean) {
               sortDistance( barFrag.bars);
        } else  {
                sortPrice( barFrag.bars);

        }*/
    }
    @Override
    protected void onActivityResult(int req, int res, Intent intent) {
        if (res == Activity.RESULT_OK && req == PICK_SETTINGS) {
            if (intent.getExtras() != null) {
                Log.i(TAG, "ONACTIVITY IKKE TOM");
                settingsOptions.sortBoolean = intent.getExtras().getBoolean("SortBoolean", true);
                settingsOptions.openBoolean = intent.getExtras().getBoolean("OpenBoolean", false);
                // Giv boolean videre til BeerFragment
                setSettingsOpenBoolean(barFrag);
            } else {
                Log.i(TAG, "ONACTIVITY TOM");
                settingsOptions.sortBoolean = true;
                settingsOptions.openBoolean = false;
            }
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
            bar.setButtonName(bar.getButtonName() + "  -  " + bar.getDistance() + " meter");
        } else {
            bar.setButtonName(bar.getButtonName().substring(0, bar.getButtonName().indexOf("-") - 1)
                    + "- " + bar.getDistance() + " meter");
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
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),1);
            return false;
        }
        return true;

    }

/*
    public void getPermissionToReadUserContacts(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},
                    READ_CONTACTS_PERMISSION_REQUEST);
        }
    }

    public void getPermissionToSendTexts(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)){

            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                    SEND_SMS_PERMISSION_REQUEST);
        }
    }

    public void getPermissionToTrackUser(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }
*/

    // TODO: Sæt felterne coffeeBars og beerBars (ArrayLists)
    public void readFile(ArrayList<Bar> coffeeBars, ArrayList<Bar> beerBars) throws IOException {
        String str;
        StringBuffer buf = new StringBuffer();
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
                URL url = new URL("http://www.student.dtu.dk/~s153200/databasetest.txt");
            InputStream is = url.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
           // InputStream is = this.getResources().openRawResource(R.raw.databasetest);
           // BufferedReader reader = new BufferedReader(new InputStreamReader(is));
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