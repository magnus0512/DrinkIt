package gruppe7.drinkit;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Bruger on 16-06-2016.
 */
public class ContactItemFragment extends Fragment{

    final private int selectedOrange = Color.rgb(225,125,0);

    View contactListItemView;
    Button contactButton;
    String contact = "testing";
    String number;
    boolean isClicked;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contactListItemView = inflater.inflate(R.layout.contact_list_item, container, false);

        contactButton = (Button) contactListItemView.findViewById(R.id.contact);
        contactButton.setText(contact);

        final Drawable d = contactButton.getBackground();


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

    public void setContactName(String name) {
        contact = name;
        contactButton.setText(name);

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

    public void setClicked(boolean boo){
        isClicked = boo;
    }

    public boolean getIsClicked(){
        return isClicked;
    }


}
