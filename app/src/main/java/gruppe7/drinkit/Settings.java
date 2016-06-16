package gruppe7.drinkit;

/**
 * Created by Magnus on 14-06-2016.
 */
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class Settings extends Activity {
    private CheckBox OpenOnlyCheckBox;
    private Button OkButton, CancelButton;
    private RadioGroup SortRadioGroup, SearchRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        addListenerOkButton();
        addListenerCancelButton();
    }


    public void addListenerOkButton() {
        OkButton = (Button) findViewById(R.id.OkButton);
        OpenOnlyCheckBox = (CheckBox) findViewById(R.id.checkbox);
        SearchRadioGroup = (RadioGroup) findViewById(R.id.searchRadioGroup);
        SortRadioGroup = (RadioGroup) findViewById(R.id.sortRadioGroup);
        OkButton.setOnClickListener(new View.OnClickListener() {

            //Run when button is clicked
            @Override
            public void onClick(View v) {

                StringBuffer result = new StringBuffer();
                int searchId = SearchRadioGroup.getCheckedRadioButtonId();
                int sortId = SortRadioGroup.getCheckedRadioButtonId();
                if (OpenOnlyCheckBox.isChecked()) {
                    result.append("\nOnly Open :").append("Current");
                }else{
                    result.append("\nOnly Open :").append("Off");
                }
                if (sortId == R.id.DistanceRadiobutton) {
                    result.append("\nDistance :").append("Current");
                } else {
                    result.append("\nPrice :").append("Current");

                }
                if (searchId == R.id.BeerRadioButton) {
                    result.append("\nBeer :").append("Current");

                } else {
                    result.append("\nCoffee :").append("Current");

                }
                Toast.makeText(Settings.this, result.toString(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
    public void addListenerCancelButton() {
        CancelButton = (Button) findViewById(R.id.CancelButton);
        CancelButton.setOnClickListener(new View.OnClickListener() {
            //Run when button is clicked
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
