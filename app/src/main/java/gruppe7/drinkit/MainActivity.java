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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    final private static String TAG = "MainActivity";
    final private static String APP_TITLE = "DTU DrinkIt";
    final private int orangeColor = Color.rgb(250,150,0);
    final private int selectedOrange = Color.rgb(225,125,0);
    final private int TITLE_COLOR = Color.BLACK;
    private boolean firstRun = true;
    final public double[][] placeringer = {
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
        getPermissionToTrackUser();

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


    private void Distance (Bar baren){
        baren.setDistance(afstandsberegner(baren.getLatitude(), baren.getLongitude()));
        baren.setName(baren.getName() + " - " + baren.getDistance() + "meter");

        if (baren.getName().indexOf("-") == -1){
            baren.setName(baren.getName() + " - " + baren.getDistance() + "meter");
        }
        else{
            baren.setName(baren.getName().substring(0, baren.getName().indexOf("-")-1)
                    + "- " + baren.getDistance() + "meter");
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

    public void getPermissionToTrackUser(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }
    public ArrayList<Bar> ReadFile() throws IOException {
        ArrayList<Bar> bars = new ArrayList<>();
        String str;
        StringBuffer buf = new StringBuffer();
        try {
            InputStream is = this.getResources().openRawResource(R.raw.databasetest);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            if (is != null) {
                while ((str = reader.readLine()) != null) {
                    Bar bar = new Bar();
                    bar.setName(str);
                    bar.setLocation(reader.readLine());
                    bar.setDrinkType(reader.readLine());
                    bar.setLatitude(Double.parseDouble(reader.readLine()));
                    bar.setLongitude(Double.parseDouble(reader.readLine()));
                    bar.setOpen(reader.readLine());
                    bar.setOpeningTime(reader.readLine());
                    bar.setClosingTime(reader.readLine());
                    bar.setPrice(Double.parseDouble(reader.readLine()));
                    bar.setAmount(Integer.parseInt(reader.readLine()));
                    bars.add(bar);

                }


            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return bars;
    }
    public boolean isOpen (Bar bar){
        if (bar.getOpen().equals("Altid åben")){
            return true;
        }
        String[] openArray = bar.getOpen().split(":");
        String[] closedArray = bar.getOpen().split(":");
        int openHours = Integer.parseInt(openArray[0]);
        int openMin = Integer.parseInt(openArray[1]);
        int openTimeSec = openHours * 360 + openMin * 60;

        int closedHours = Integer.parseInt(closedArray[0]);
        int closedMin = Integer.parseInt(closedArray[1]);
        int closedTimeSec = closedHours * 360 + closedMin * 60;

        Calendar c = Calendar.getInstance();
        int currentTimeSec = c.get(Calendar.HOUR_OF_DAY) * 360 + c.get(Calendar.MINUTE) * 60;
        c.get(Calendar.MINUTE);

        if (currentTimeSec > openTimeSec && currentTimeSec < closedTimeSec) {
            return true;
        }
        return false;
    }
    public static void sortPrice(ArrayList<Bar> bars ) {
        for(int i = 0; i<bars.size(); i++){
            bars.get(i).setSortBy("price");
        }

        //}

        Collections.sort(bars);
    }

    public static void sortDistance(ArrayList<Bar> bars) {
        for(int i = 0; i<bars.size(); i++){
            bars.get(i).setSortBy("");
        }

        Collections.sort(bars);
    }
    public static class Bar implements Comparable<Bar> {

        private String name;
        private String openingTime;
        private Double price;
        private String closingTime;
        private String location;
        private String drinkType;
        private double latitude;
        private double longitude;
        private String open;
        private String type;
        private int amount;
        private Integer distance;
        private String sortBy = "price";

        public String getSortBy() {
            return sortBy;
        }

        public void setSortBy(String sortBy) {
            this.sortBy = sortBy;
        }


        public int getDistance() {
            return distance;
        }

        public void setDistance(Integer distance) {
            this.distance = distance;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getOpen() {
            return open;
        }

        public void setOpen(String open) {
            this.open = open;
        }


        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }


        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }


        public String getDrinkType() {
            return drinkType;
        }

        public void setDrinkType(String drinkType) {
            this.drinkType = drinkType;
        }


        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public void setClosingTime(String closingTime) {
            this.closingTime = closingTime;
        }

        public String getClosingTime() {
            return closingTime;
        }

        public void setOpeningTime(String openingTime) {
            this.openingTime = openingTime;
        }

        public String getOpeningTime() {
            return openingTime;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Double getPrice() {
            return price;
        }


        @Override
        public int compareTo(Bar o) {
            if (o.sortBy.equals("price")) {
                return this.price.compareTo(o.getPrice());
            }
            return this.distance.compareTo(o.getDistance());
        }
    }


}