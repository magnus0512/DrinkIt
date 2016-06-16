package gruppe7.drinkit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
//import android.app.Fragment;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    final private static String APP_TITLE = "DTU DrinkIt";

    //LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    //String locationProvider = LocationManager.NETWORK_PROVIDER;

    FragmentManager fragMan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout frame = (RelativeLayout) findViewById(R.id.frame);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(APP_TITLE);
        setSupportActionBar(toolbar);

        fragMan = getSupportFragmentManager();

        // Default screen is BeerFragment (should probably be changed to Coffee)
        //fragMan = getSupportFragmentManager();
        FragmentTransaction fragTrans = fragMan.beginTransaction();
        BeerFragment beerFrag = new BeerFragment();
        fragTrans.add(R.id.list_upper_container, beerFrag);
        fragTrans.addToBackStack(null);
        fragTrans.commit();

        // Make buttons for toggling the bar list
        final Button coffeeButton = (Button) findViewById(R.id.coffeeButton);
        //coffeeButton.setBackgroundColor(Color.LTGRAY);
        final Button beerButton = (Button) findViewById(R.id.beerButton);
        //beerButton.setBackgroundColor(Color.LTGRAY);

        // Remember to change this, if default button is coffee
        beerButton.setTextColor(Color.BLUE);

        coffeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Install coffeeFragment
                CoffeeFragment coffeeFrag = new CoffeeFragment();
                FragmentTransaction fragTrans = fragMan.beginTransaction();
                fragTrans.replace(R.id.list_upper_container, coffeeFrag);
                fragTrans.addToBackStack(null);
                fragTrans.commit();

                coffeeButton.setTextColor(Color.BLUE);
                beerButton.setTextColor(Color.DKGRAY);
            }
        });

        beerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Install beerFragment
                BeerFragment beerFrag = new BeerFragment();
                FragmentTransaction fragTrans = fragMan.beginTransaction();
                fragTrans.replace(R.id.list_upper_container, beerFrag);
                fragTrans.addToBackStack(null);
                fragTrans.commit();

                beerButton.setTextColor(Color.BLUE);
                coffeeButton.setTextColor(Color.DKGRAY);
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

    }

}