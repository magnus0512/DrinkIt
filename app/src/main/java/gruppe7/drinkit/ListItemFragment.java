package gruppe7.drinkit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
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

        barNameButton = (Button) listItemView.findViewById(R.id.barName);
        barNameButton.setText(barName);

        // TODO: Open Dialog
        barNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogFragment.newInstance();
            }
        });

        // TODO: Open Google Maps
        // Få fat i geo adresse
        // Lav en intent?
        findButton = (Button) listItemView.findViewById(R.id.barFind);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // The last parameter is false because the returned view does not need to be attached to the container ViewGroup
        return listItemView;

    }

    public void setBarName(String name) {
        barNameButton.setText(name);

    }


    public static class AlertDialogFragment extends DialogFragment {

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
                    .setMessage("Åbningstider: Priser: Lokation: ")
                    //gør det muligt at trykke tilbage
                    .setCancelable(true)
                    //Sætter "cancel"-kanppen; lukker dialogboksen
                    .setNegativeButton(android.R.string.cancel, null)
                    //Sætter "share"-knappen
                    //Todo: tilføj share funktion
/*
                    .setNeutralButton("Share",
                            new DialogInterface.OnClickListener() {})
*/
                    //Sætter "find"-knappen, *mangler funktion til Maps*
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

