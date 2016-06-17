package gruppe7.drinkit;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Bruger on 16-06-2016.
 */
public class ChooseContact extends AppCompatActivity {

    private static final Uri PHONE_CONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    private static final String DISPLAY_NAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
    private static final String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

    ArrayList<ContactItemFragment> contactFrags = new ArrayList<ContactItemFragment>();

    ArrayList<String> chosenNames = new ArrayList<String>();
    ArrayList<String> chosenNumbers = new ArrayList<String>();

    //private static final int READ_CONTACTS_PERMISSION_REQUEST = 1;
    //private static final int SEND_SMS_PERMISSION_REQUEST = 2;


    final private static String APP_TITLE = "DTU DrinkIt";



    FragmentManager fragmentManager;
    ChooseContactFragment chooseContactFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_contact_activity);

        //getPermissionToReadUserContacts();
        //getPermissionToSendTexts();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(APP_TITLE);
        toolbar.setBackgroundColor(Color.rgb(250,150,0));

        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();
        chooseContactFragment = new ChooseContactFragment();

        // Set screen to chooseContactFragment
        FragmentTransaction fragTrans = fragmentManager.beginTransaction();
        fragTrans.add(R.id.list_upper_container_contact, chooseContactFragment);
        fragTrans.addToBackStack(null);
        fragTrans.commit();

        // The cancel and ok button
        final Button cancelButton = (Button) findViewById(R.id.cancelContactButton);
        final Button okButton = (Button) findViewById(R.id.OkContactButton);


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                contactFrags = chooseContactFragment.getContactItemFragments();

                String message = "Are you sure you want to send a textmessage to: ";

                for(int i = 0; i<contactFrags.size(); i++){
                    if(contactFrags.get(i).getIsClicked()){
                        chosenNames.add(contactFrags.get(i).getContactName());
                        chosenNumbers.add(contactFrags.get(i).getContactNumber());
                        message += contactFrags.get(i).getContactNumber()+ ", ";
                    }
                }



                if (0 != chosenNumbers.size()) {

                    PendingIntent pi = PendingIntent.getActivity(ChooseContact.this, 0,
                            new Intent(ChooseContact.this, MainActivity.class), 0);
                    SmsManager sms = SmsManager.getDefault();
                    for(int j = 0; j<chosenNumbers.size(); j++){
                        sms.sendTextMessage(chosenNumbers.get(j), null, "hey come join me", pi, null);
                    }

                    Toast.makeText(ChooseContact.this, "SMS send",
                            Toast.LENGTH_SHORT).show();

                    finish();

                }



            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });



    }
/*
    public void getPermissionToReadUserContacts(){
        if(ContextCompat.checkSelfPermission(ChooseContact.this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    READ_CONTACTS_PERMISSION_REQUEST);
        }
    }

    public void getPermissionToSendTexts(){
        if(ContextCompat.checkSelfPermission(ChooseContact.this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.SEND_SMS},
                    SEND_SMS_PERMISSION_REQUEST);
        }
    }

*/


    public void addNumberAndName(String number, String name){
        chosenNames.add(name);
        chosenNumbers.add(number);
    }

    public void removeNumberAndName(String number, String name){
        chosenNames.remove(name);
        chosenNumbers.remove(number);
    }


}
