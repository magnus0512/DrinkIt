package gruppe7.drinkit;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by Frederik on 14-06-2016.
 */
public class Locations extends Activity {

    LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    String locationProvider = LocationManager.NETWORK_PROVIDER;
    Location[] places = new Location[50];




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    public void setup(){
        Location Hegnet = new Location("Hegnet");
        Hegnet.setLatitude(55.783144);
        Hegnet.setLongitude(12.518172);
        places[0] = Hegnet;
    }

}

