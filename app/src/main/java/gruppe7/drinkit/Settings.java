package gruppe7.drinkit;

/**
 * Created by Magnus on 14-06-2016.
 */
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.nfc.Tag;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class Settings extends Activity {
    private CheckBox OpenOnlyCheckBox;
    private Button OkButton, CancelButton;
    private RadioGroup SortRadioGroup;

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
        OpenOnlyCheckBox.setChecked(sharedPreferences.getBoolean("CheckButton", false));
        SortRadioGroup.check(sharedPreferences.getInt("SortRadio", R.id.DistanceRadiobutton));
    }
    public void addListenerOkButton() {
        OkButton = (Button) findViewById(R.id.OkButton);
        OpenOnlyCheckBox = (CheckBox) findViewById(R.id.checkbox);
        SortRadioGroup = (RadioGroup) findViewById(R.id.sortRadioGroup);
        OkButton.setOnClickListener(new View.OnClickListener() {

            //Run when button is clicked
            @Override
            public void onClick(View v) {
                // Check Check- and radiobutton preferences, set sorting type for tabs
                Intent optionsIntent = new Intent(getApplicationContext(),MainActivity.class);
                StringBuffer result = new StringBuffer();
                int sortId = SortRadioGroup.getCheckedRadioButtonId();
                if (OpenOnlyCheckBox.isChecked()) {
                    result.append("Only Open :").append("On");
                    optionsIntent.putExtra("OpenBoolean", true);
                }else{
                    result.append("Only Open :").append("Off");
                    optionsIntent.putExtra("OpenBoolean", false);
                }

                if (sortId == R.id.DistanceRadiobutton) {
                    result.append("\n Distance :").append("Current");
                    optionsIntent.putExtra("SortBoolean", true);
                    Log.i("HEJ", "SETTINGS SORT TRUE");
                } else {
                    result.append("\n Price :").append("Current");
                    optionsIntent.putExtra("SortBoolean", false);
                    Log.i("HEJ", "SETTINGS SORT FALSE");
                }

                Toast.makeText(Settings.this, result.toString(),
                        Toast.LENGTH_LONG).show();

                // Save Selected Preferences, and exit Settings
                SharedPreferences sharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("CheckButton", OpenOnlyCheckBox.isChecked());
                editor.putInt("SortRadio", SortRadioGroup.getCheckedRadioButtonId());
                editor.commit();
                setResult(RESULT_OK,optionsIntent);
                finish();
            }
        });
    }
    public void addListenerCancelButton() {
        // Exit Settings
        CancelButton = (Button) findViewById(R.id.CancelButton);
        CancelButton.setOnClickListener(new View.OnClickListener() {
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
