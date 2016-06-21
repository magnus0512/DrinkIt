package gruppe7.drinkit;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ListItemFragment extends Fragment {
    Button barNameButton;
    Button findButton;
    View listItemView;
    Bar bar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        listItemView = inflater.inflate(R.layout.list_item, container, false);
        // The last parameter is false because the returned view does not need to be attached to the container ViewGroup

        barNameButton = (Button) listItemView.findViewById(R.id.barName);
        barNameButton.setText(bar.getButtonName());

        //Marks if a bar is closed with gray text color
        if (bar.isOpen()) {
            barNameButton.setTextColor(Color.BLACK);
        } else {
            barNameButton.setTextColor(Color.GRAY);
        }

        // Opens dialog with bar-info
        barNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                InfoDialogFragment dialog = InfoDialogFragment.newInstance();
                dialog.bar = bar;
                dialog.show(getActivity().getFragmentManager(), "InfoDialogFragment");

            }
        });

        // Opens Google Maps through geo address
        findButton = (Button) listItemView.findViewById(R.id.barFind);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double i = bar.getLatitude();
                double j = bar.getLongitude();

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?&daddr=" + i + ", " + j));
                startActivity(intent);
            }
        });
        return listItemView;

    }

}

