package gruppe7.drinkit;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * Created by namanhnguyen on 17/06/16.
 */

public class AlertDialogFragment extends DialogFragment {
    String titel = "Titel";
    String openHours = "Open Hours";
    String priser = "Priser";
    String lokation = "Lokation";
    Bar bar;

    public static AlertDialogFragment newInstance() {
        return new AlertDialogFragment();
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                //Sætter titlen
                //Todo: hent titel
                .setTitle("Diagonalen")
                //Sætter info om bar
                //Todo hent info
                .setMessage(openHours +"\n"+ priser+ "\n" + lokation)
                //gør det muligt at trykke tilbage
                .setCancelable(true)
                //Sætter "cancel"-kanppen; lukker dialogboksen
                .setNeutralButton(android.R.string.cancel, null)

                //Sætter "share"-knappen
                //Todo: tilføj share funktion
                .setNegativeButton("share",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    final DialogInterface dialog, int id) {



                                Intent intent = new Intent(getActivity(), ChooseContact.class);
                                startActivity(intent);


                            }
                        })

                //Sætter "find"-knappen
                //Todo: Åben Maps med rute

                .setPositiveButton("Find",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    final DialogInterface dialog, int id) {
                                double i = bar.getLatitude();
                                double j = bar.getLongitude();

                                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                        Uri.parse("http://maps.google.com/maps?&daddr=" + i + ", " + j));
                                startActivity(intent);

                            }
                        })
                .create();
    }

}