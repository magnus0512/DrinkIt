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
    private RadioButton BeerSearchRadioButton, CoffeeSearchRadioButton, DistanceSortRadioButton, PriceSortRadioButton;
    private Button OkButton, CancelButton;
    private RadioGroup SortRadioGroup, SearchRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        addListenerOpenOnlyCheckBox();
        addListenerOkButton();
        addListenerCancelButton();
        addListenersearchRadioGroup();
        addListenersortRadioGroup();
    }
    // Get a reference to the Radio Buttons and CheckBox
    // Set an OnClickListener on the CheckBox
    public void addListenerOpenOnlyCheckBox() {
        OpenOnlyCheckBox = (CheckBox) findViewById(R.id.checkbox);
        OpenOnlyCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is OpenOnlyCheckBox checked?
                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(Settings.this,
                            "REMOVED CLOSED PLACES", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(Settings.this,
                            "ADDED CLOSED PLACES", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void addListenerOkButton() {
        OkButton = (Button) findViewById(R.id.OkButton);
        OpenOnlyCheckBox = (CheckBox) findViewById(R.id.checkbox);
        SearchRadioGroup = (RadioGroup) findViewById(R.id.searchRadioGroup);
        SortRadioGroup = (RadioGroup) findViewById(R.id.sortRadioGroup);
        BeerSearchRadioButton = (RadioButton) findViewById(R.id.BeerRadiobutton);
        CoffeeSearchRadioButton = (RadioButton) findViewById(R.id.CoffeeRadiobutton);
        DistanceSortRadioButton = (RadioButton) findViewById(R.id.DistanceRadiobutton);
        PriceSortRadioButton = (RadioButton) findViewById(R.id.PriceRadiobutton);

        OkButton.setOnClickListener(new View.OnClickListener() {

            //Run when button is clicked
            @Override
            public void onClick(View v) {

                StringBuffer result = new StringBuffer();
                int searchId = SearchRadioGroup.getCheckedRadioButtonId();
                int sortId = SortRadioGroup.getCheckedRadioButtonId();
                //result.append("Only Open : ").append(OpenOnlyCheckBox.isChecked());
               // result.append("\nBeer : ").append(BeerSearchRadioButton.isChecked());
               // result.append("\nCoffee :").append(CoffeeSearchRadioButton.isChecked());
              // result.append("\nDistance :").append(DistanceSortRadioButton.isChecked());
               // result.append("\nPrice :").append(PriceSortRadioButton.isChecked());
                //result.append("\nID :").append(searchId + sortId);
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

    public void addListenersearchRadioGroup() {
        SearchRadioGroup = (RadioGroup) findViewById(R.id.searchRadioGroup);
        BeerSearchRadioButton = (RadioButton) findViewById(R.id.BeerRadiobutton);
        CoffeeSearchRadioButton = (RadioButton) findViewById(R.id.CoffeeRadiobutton);
        SearchRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.BeerRadioButton) {
                    Toast.makeText(getApplicationContext(), "Beer",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Coffee",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addListenersortRadioGroup() {
        DistanceSortRadioButton = (RadioButton) findViewById(R.id.DistanceRadiobutton);
        PriceSortRadioButton = (RadioButton) findViewById(R.id.PriceRadiobutton);
        SortRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.DistanceRadiobutton) {
                    Toast.makeText(getApplicationContext(), "Distance",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Price",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
}
