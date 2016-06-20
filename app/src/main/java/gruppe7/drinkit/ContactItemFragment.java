package gruppe7.drinkit;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ContactItemFragment extends Fragment{

    final private int selectedOrange = Color.rgb(225,125,0);

    View contactListItemView;
    String contact = "default";
    String number;
    Button contactButton;
    boolean isClicked;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contactListItemView = inflater.inflate(R.layout.contact_list_item, container, false);
        // The last parameter is false because the returned view does not need to be attached to the container ViewGroup

        contactButton = (Button) contactListItemView.findViewById(R.id.contact);
        contactButton.setText(contact);

        //Marks that the contact has been selected
        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isClicked){
                    isClicked = true;
                    contactButton.setTextColor(selectedOrange);

                }else{
                    isClicked = false;
                    contactButton.setTextColor(Color.BLACK);
                }

            }
        });
        return contactListItemView;

    }

    public void setContactNumber(String number){
        this.number = number;
    }

    public String getContactNumber(){
        return number;
    }

    public String getContactName(){
        return contact;
    }

    public boolean getIsClicked(){
        return isClicked;
    }


}
