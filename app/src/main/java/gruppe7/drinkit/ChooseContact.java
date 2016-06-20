package gruppe7.drinkit;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class ChooseContact extends AppCompatActivity {

    ArrayList<ContactItemFragment> contactFrags = new ArrayList<>();
    ArrayList<String> chosenNumbers = new ArrayList<>();

    final private static String APP_TITLE = "DTU DrinkIt";

    FragmentManager fragmentManager;
    ChooseContactFragment chooseContactFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_contact_activity);

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

                for(int i = 0; i<contactFrags.size(); i++){
                    if(contactFrags.get(i).getIsClicked()){
                        chosenNumbers.add(contactFrags.get(i).getContactNumber());
                    }
                }

                if (0 != chosenNumbers.size()) {

                    //Få barnavnet fra den String extra der er
                    //Sendt med intenten
                    Bundle extras = getIntent().getExtras();
                    String barName = "";

                    if(extras != null){
                        barName = extras.getString("custom extra");
                    }

                    //Send sms
                    PendingIntent pi = PendingIntent.getActivity(ChooseContact.this, 0,
                            new Intent(ChooseContact.this, MainActivity.class), 0);
                    SmsManager sms = SmsManager.getDefault();
                    for(int j = 0; j<chosenNumbers.size(); j++){
                        sms.sendTextMessage(chosenNumbers.get(j), null, "Hey come join me at " + barName + "!!", pi, null);
                    }

                    //Informer brugeren om at sms'en er sendt
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

}
