package gruppe7.drinkit;

/**
 * Created by Magnus on 14-06-2016.
 */
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Settings extends AppCompatActivity {
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
    final CheckBox OpenOnlyCheckBox = (CheckBox) findViewById(R.id.checkbox);
    final RadioButton BeerSearchRadioButton = (RadioButton) findViewById(R.id.BeerRadiobutton);
    final RadioButton CoffeeSearchRadioButton = (RadioButton) findViewById(R.id.CoffeeRadiobutton);
    final RadioButton DistanceSortRadioButton = (RadioButton) findViewById(R.id.DistanceRadiobutton);
    final RadioButton PriceSortRadioButton = (RadioButton) findViewById(R.id.PriceRadiobutton);
    final Button OkButton = (Button) findViewById(R.id.OkButton);
    final Button CancelButton = (Button) findViewById(R.id.CancelButton);
    final RadioGroup SearchRadioGroup = (RadioGroup) findViewById(R.id.searchRadioGroup);
    final RadioGroup SortRadioGroup = (RadioGroup) findViewById(R.id.sortRadioGroup);

    // Set an OnClickListener on the CheckBox
    public void addListenerOpenOnlyCheckBox() {
        OpenOnlyCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is OpenOnlyCheckBox checked?
                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(Settings.this,
                            "REMOVED CLOSED", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
    public void addListenerOkButton() {
        OkButton.setOnClickListener(new View.OnClickListener() {

            //Run when button is clicked
            @Override
            public void onClick(View v) {

                StringBuffer result = new StringBuffer();
                result.append("Only Open : ").append(OpenOnlyCheckBox.isChecked());
                result.append("\nBeer : ").append(BeerSearchRadioButton.isChecked());
                result.append("\nCoffee :").append(CoffeeSearchRadioButton.isChecked());
                result.append("\nDistance :").append(DistanceSortRadioButton.isChecked());
                result.append("\nPrice :").append(PriceSortRadioButton.isChecked());

                Toast.makeText(Settings.this, result.toString(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void addListenerCancelButton() {
        CancelButton.setOnClickListener(new View.OnClickListener() {
            //Run when button is clicked
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void addListenersearchRadioGroup() {
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
