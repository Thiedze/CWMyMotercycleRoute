package cw.de.cwmymotercycleroute;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sebastian on 01.08.17.
 */

public class SetLocationActivity extends AppCompatActivity {

    private Geocoder coder;

    private ArrayAdapter<String> startLocationAdapter;

    private ArrayAdapter<String> endLocationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        coder = new Geocoder(this);
        setContentView(R.layout.set_location_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSetLocation);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button button = (Button) findViewById(R.id.start_routing);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });



        final AutoCompleteTextView startLocation = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewStartLocation);
        startLocationAdapter = new ArrayAdapter<String>(SetLocationActivity.this, android.R.layout.simple_list_item_1, getAddressesByText(startLocation.getText().toString()));
        startLocation.setAdapter(startLocationAdapter);
        startLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                startLocationAdapter.clear();
                for(String address : getAddressesByText(startLocation.getText().toString())) {
                    startLocationAdapter.add(address);
                }
                startLocationAdapter.notifyDataSetInvalidated();
            }
        });

        final AutoCompleteTextView endLocation = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewEndLocation);
        endLocationAdapter = new ArrayAdapter<String>(SetLocationActivity.this, android.R.layout.simple_list_item_1, getAddressesByText(endLocation.getText().toString()));
        endLocation.setAdapter(endLocationAdapter);
        endLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                endLocationAdapter.clear();
                for(String address : getAddressesByText(endLocation.getText().toString())) {
                    endLocationAdapter.add(address);
                }
                endLocationAdapter.notifyDataSetInvalidated();
            }
        });

    }

    private List<String> getAddressesByText(String input) {
        List<String> addressNames = new ArrayList<>();
        try {
            List<Address> addresses = coder.getFromLocationName(input, 10);

            for(Address address : addresses) {
                addressNames.add(address.getAddressLine(0) + " " + address.getAddressLine(1));
            }
        } catch (Exception exception) {
            Toast.makeText(SetLocationActivity.this, exception.getCause().toString(), Toast.LENGTH_LONG);
        }

        return addressNames;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_set_location, menu);
        return true;
    }

}
