package gruppe7.drinkit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Toast;


public class Settings extends Activity {
    private CheckBox openOnlyCheckBox;
    private Button okButton, cancelButton;
    private RadioGroup sortRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("DTU DrinkIt");
        toolbar.setBackgroundColor(Color.rgb(250,150,0));

        // Setup Listen-methods for buttons
        addListenerOkButton();
        addListenerCancelButton();

        // Load Earlier Preferences, if no earlier preferences found select default
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        openOnlyCheckBox.setChecked(sharedPreferences.getBoolean("CheckButton", false));
        sortRadioGroup.check(sharedPreferences.getInt("SortRadio", R.id.DistanceRadiobutton));
    }

    public void addListenerOkButton() {
        okButton = (Button) findViewById(R.id.OkButton);
        openOnlyCheckBox = (CheckBox) findViewById(R.id.checkbox);
        sortRadioGroup = (RadioGroup) findViewById(R.id.sortRadioGroup);
        okButton.setOnClickListener(new View.OnClickListener() {

            //Run when button is clicked
            @Override
            public void onClick(View v) {
                // Check Check- and radiobutton preferences, set sorting type for tabs
                Intent optionsIntent = new Intent(getApplicationContext(),MainActivity.class);
                int sortId = sortRadioGroup.getCheckedRadioButtonId();
                if (openOnlyCheckBox.isChecked()) {
                    optionsIntent.putExtra("OpenBoolean", true);
                }else{
                    optionsIntent.putExtra("OpenBoolean", false);
                }

                if (sortId == R.id.DistanceRadiobutton) {
                    optionsIntent.putExtra("SortBoolean", true);
                } else {
                    optionsIntent.putExtra("SortBoolean", false);
                }

                // Save Selected Preferences, and exit Settings
                SharedPreferences sharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("CheckButton", openOnlyCheckBox.isChecked());
                editor.putInt("SortRadio", sortRadioGroup.getCheckedRadioButtonId());
                editor.commit();
                setResult(RESULT_OK,optionsIntent);
                finish();
            }
        });
    }

    public void addListenerCancelButton() {
        // Exit Settings
        cancelButton = (Button) findViewById(R.id.CancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            //Run when button is clicked
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
}
