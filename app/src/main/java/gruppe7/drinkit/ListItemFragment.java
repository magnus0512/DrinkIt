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
 * Created by namanhnguyen on 15/06/16.
 */
public class ListItemFragment extends Fragment {
    View listItemView;
    Button barNameButton;
    Button findButton;
    String barName = "testing";




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        listItemView = inflater.inflate(R.layout.list_item, container, false);
        // The last parameter is false because the returned view does not need to be attached to the container ViewGroup

        barNameButton = (Button) listItemView.findViewById(R.id.barName);
        barNameButton.setText(barName);
        barNameButton.setTextColor(Color.BLACK);
        //barNameButton.setBackgroundColor(Color.LTGRAY);

        // TODO: Open Dialog
        barNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialogFragment dialog = AlertDialogFragment.newInstance();
                dialog.show(getActivity().getFragmentManager(), "AlertDialogFragment");

            }
        });

        // TODO: Open Google Maps
        // Få fat i geo adresse
        // Lav en intent?
        findButton = (Button) listItemView.findViewById(R.id.barFind);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double i = 55.782378;
                double j = 12.517101;
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?&daddr="+i+", " + j));
                startActivity(intent);
            }
        });


        return listItemView;

    }

    public void setBarName(String name) {
        barNameButton.setText(name);

    }
    public static class AlertDialogFragment extends DialogFragment {
        String titel = "Titel";
        String openHours = "Open Hours";
        String priser = "Priser";
        String lokation = "Lokation";

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

                                }
                            })
                    .create();
        }

    }


}

