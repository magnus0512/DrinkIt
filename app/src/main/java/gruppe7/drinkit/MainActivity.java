package gruppe7.drinkit;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.app.Fragment;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final private static String TAG = "MainActivity";
    final private static String APP_TITLE = "DTU DrinkIt";
    final private int orangeColor = Color.rgb(250,150,0);
    final private int selectedOrange = Color.rgb(225,125,0);
    final private int TITLE_COLOR = Color.BLACK;
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

    };;
    ArrayList<Integer> barAfstande = new ArrayList<Integer>();
    ArrayList<String> barNames = new ArrayList<String>();
    ArrayList<String> originale = new ArrayList<String>();
    //{{        add("A");        add("B");        add("C");    }};

    private static final int READ_CONTACTS_PERMISSION_REQUEST = 1;
    private static final int SEND_SMS_PERMISSION_REQUEST = 2;

    BeerFragment beerFrag;
    CoffeeFragment coffeeFrag;

    //LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    //String locationProvider = LocationManager.NETWORK_PROVIDER;

    FragmentManager fragMan;
    boolean coffeeActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //RelativeLayout frame = (RelativeLayout) findViewById(R.id.frame);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(APP_TITLE);
        toolbar.setBackgroundColor(orangeColor);
        toolbar.setTitleTextColor(TITLE_COLOR);
        //toolbar.setBackgroundColor(getResources().getColor(R.color.orange));
        setSupportActionBar(toolbar);

        fragMan = getSupportFragmentManager();
        beerFrag = new BeerFragment();

        Log.i(TAG,"entered OnCreate");

        // TODO: Load location
        // getUserLocation(); eller placer denne i loadBarNames
        // TODO: Load barNames from Location
        // loadBarNames();

        // just for testing
        barNames.add("Hegnet");
        barNames.add("Diamanten");
        barNames.add("Studentercaféen 325");
        barNames.add("S Huset");
        barNames.add("Døgn netto");
        barNames.add("PF cafe i 302");
        barNames.add("Etheren");
        barNames.add("Diagonalen");
        barNames.add("Maskinen");
        originale = barNames;
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
        new Distance().execute();

        beerFrag.barNames = barNames;


        coffeeFrag = new CoffeeFragment();

        // Set default screen to a BeerFragment (should probably be changed to Coffee)
        FragmentTransaction fragTrans = fragMan.beginTransaction();
        fragTrans.add(R.id.list_upper_container, beerFrag);
        fragTrans.addToBackStack(null);
        fragTrans.commit();

        // Used to determine if Coffee-button has been clicked
        // so that the screen reverts to Beer-fragment

        getPermissionToReadUserContacts();
        getPermissionToSendTexts();

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
                    fragMan.popBackStack();
                    Log.i(TAG, "popped BeerFragment");


                    FragmentTransaction fragTrans = fragMan.beginTransaction();
                    fragTrans.replace(R.id.list_upper_container, coffeeFrag);
                    fragTrans.addToBackStack(null);
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
                    fragMan.popBackStack();
                    coffeeActive = false;
                    Log.i(TAG, "popped CoffeeFragment");


                    // Install beerFragment
                    FragmentTransaction fragTrans = fragMan.beginTransaction();
                    fragTrans.replace(R.id.list_upper_container, beerFrag);
                    fragTrans.addToBackStack(null);
                    fragTrans.commit();

                    coffeeButton.setBackgroundColor(orangeColor);
                    beerButton.setBackgroundColor(selectedOrange);
                }
            }
        });


    }


    // Husk at ændre til ContextMenu hvis settings skal være sådan i stedet

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
            MainActivity.this.startActivity(openSettings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // TODO: Lav en metode, måske her, som opdaterer ArrayListen i BeerFragment/CoffeeFragment

    @Override
    public void onResume() {
        super.onResume();
        beerFrag.barNames = barNames;
        // barNames has been updated by ..... (sorting algorithm)
/*
        barNames.add("Hello");
        barNames.add("out");
        barNames.add("there");
*/

        //beerFrag.barNames = barNames;

    }


    private class Distance extends AsyncTask<Void, Void, Void>{


        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0 ; i < barNames.size(); i++){
                int afstand = afstandsberegner(placeringer[i][0],placeringer[i][1]);
                if (firstRun){
                    barAfstande.add(afstand);
                } else{
                    barAfstande.set(i, afstand);
                }
                barNames.set(i, originale.get(i) + "  -  " + afstand + " meter" );

            }
            /* Ekstra loop til kaffe stederne
            for (int i = 0 ; i < barNames.size(); i++){
                int afstand = afstandsberegner(placeringer[i][0],placeringer[i][1]);
                if (firstRun){
                    barAfstande.add(afstand);
                }
                if (barNames.get(i).indexOf("-") == -1){
                    barNames.set(i, barNames.get(i) + "  -  " + afstand + " meter" );
                }
                else{
                    int index = barNames.get(i).indexOf("-");
                    String nytBarNavn = barNames.get(i).substring(0, index-1);
                    barNames.set(i, nytBarNavn + "- " + afstand + " meter");
                }
            } */
            firstRun = false;
            return null;
        }
        protected void onPostExecute() {
        //    beerFrag.barNames = barNames;
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
            double test = R*c;
            return ((int) test);

            // Se http://www.movable-type.co.uk/scripts/latlong.html for formler
        } catch (SecurityException e){
            return 0;
        }
    }


    public void getPermissionToReadUserContacts(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    READ_CONTACTS_PERMISSION_REQUEST);
        }
    }

    public void getPermissionToSendTexts(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.SEND_SMS},
                    SEND_SMS_PERMISSION_REQUEST);
        }
    }


}