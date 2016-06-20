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
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by namanhnguyen on 17/06/16.
 */

public class InfoDialogFragment extends DialogFragment {
    String openHours = "\n Open hours: ";
    String priser = "\n Prices: ";
    String lokation = "\n Location: ";
    Bar bar;

    public static InfoDialogFragment newInstance() {
        return new InfoDialogFragment();
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                // Sætter titlen
                .setTitle(bar.getName())
                // Sætter info om bar
                .setMessage(openHours + bar.getOpen() + " " + bar.getOpeningTime() + " - " + bar.getClosingTime() + priser + bar.getAmount() + " for " +Double.valueOf(bar.getPrice()).intValue() + " kr." + lokation + bar.getLocation())
                // Gør det muligt at trykke tilbage
                .setCancelable(true)
                // Sætter "cancel"-knappen og lukker dialogboksen
                .setNeutralButton(android.R.string.cancel, null)

                // Sætter "share"-knappen
                .setNegativeButton("Share",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    final DialogInterface dialog, int id) {

                                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)== PackageManager.PERMISSION_GRANTED&&
                                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS)==PackageManager.PERMISSION_GRANTED){
                                    Intent intent = new Intent(getActivity(), ChooseContact.class);
                                    intent.putExtra("custom extra", bar.getName());
                                    startActivity(intent);

                                }else{
                                    Toast.makeText(getActivity(), "DTU drinkIt needs access contacts and Sms to share location. Go to settings and enable permissions", Toast.LENGTH_LONG)
                                            .show();
                                }



                            }
                        })

                // Sætter "find"-knappen

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