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










    public double afstandsberegner(Location newLocation) {
        try {
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
            double R = 6371000;
            double lat1 = lastKnownLocation.getLatitude();
            double lat2 = newLocation.getLatitude();
            double long1 = lastKnownLocation.getLongitude();
            double long2 = newLocation.getLongitude();

            double deltaLat = Math.abs(lat2-lat1)*Math.PI/180;
            double deltaLong = Math.abs(long2-long1)*Math.PI/180;
            double a = Math.sin(deltaLat/2) * Math.sin(deltaLat/2) +
                    Math.cos(lat1) * Math.cos(lat2) *
                            Math.sin(deltaLong/2) * Math.sin(deltaLong/2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            return (R * c);

            // Se http://www.movable-type.co.uk/scripts/latlong.html for formler
        } catch (SecurityException e){
            return 0;
        }
    }




}
