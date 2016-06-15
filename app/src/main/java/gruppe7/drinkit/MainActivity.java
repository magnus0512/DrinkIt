package gruppe7.drinkit;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    String locationProvider = LocationManager.NETWORK_PROVIDER;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
    protected void onResume() {
        super.onResume();




    }









}
