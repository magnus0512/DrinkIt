package gruppe7.drinkit;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class InfoDialogFragment extends DialogFragment {
    String openHours = "\n Open hours: ";
    String priser = "\n Prices: ";
    String lokation = "\n Location: ";
    Bar bar;

    public static InfoDialogFragment newInstance() {
        return new InfoDialogFragment();
    }



    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle(bar.getName())
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
                        });

        if(bar.getOpeningTime().equals("Always open")) {

            builder.setMessage(openHours + bar.getOpeningTime() + priser + bar.getAmount() +
                    " for " + Double.valueOf(bar.getPrice()).intValue() + " kr." + lokation +
                    bar.getLocation());
        }
        else{

            builder.setMessage(openHours + bar.getOpen() + " " + bar.getOpeningTime() + " - " +
                    bar.getClosingTime() + priser + bar.getAmount() + " for " +
                    Double.valueOf(bar.getPrice()).intValue() + " kr." + lokation + bar.getLocation());
        }
        return builder.create();

    }

}