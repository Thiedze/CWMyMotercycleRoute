package cw.de.cwmymotercycleroute;

import android.content.Intent;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sebastian on 01.08.17.
 * <p>
 * Class to set the new route.
 */

public class SetLocationActivity extends AppCompatActivity {

    private Geocoder coder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        coder = new Geocoder(this);
        setContentView(R.layout.set_location_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSetLocation);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final AutoCompleteTextView startLocation = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewStartLocation);
        startLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                ArrayAdapter<String> startLocationAdapter = new ArrayAdapter<>(SetLocationActivity.this, android.R.layout.simple_list_item_1, getAddressesTextByText(startLocation.getText().toString()));
                startLocation.setAdapter(startLocationAdapter);
                startLocationAdapter.notifyDataSetChanged();
            }
        });

        final AutoCompleteTextView endLocation = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewEndLocation);
        endLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                ArrayAdapter<String> endLocationAdapter = new ArrayAdapter<>(SetLocationActivity.this, android.R.layout.simple_list_item_1, getAddressesTextByText(endLocation.getText().toString()));
                endLocation.setAdapter(endLocationAdapter);
                endLocationAdapter.notifyDataSetChanged();
            }
        });

        Button startRouting = (Button) findViewById(R.id.start_routing);
        startRouting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();

                Address startAddress = getAddressesByText(startLocation.getText().toString()).get(0);
                data.putExtra("latStartLocation", startAddress.getLatitude());
                data.putExtra("longStartLocation", startAddress.getLongitude());

                Address endAddress = getAddressesByText(endLocation.getText().toString()).get(0);
                data.putExtra("latEndLocation", endAddress.getLatitude());
                data.putExtra("longEndLocation", endAddress.getLongitude());

                setResult(RESULT_OK, data);
                finish();
            }
        });

    }

    private List<String> getAddressesTextByText(String input) {
        List<String> addressNames = new ArrayList<>();

        for (Address address : getAddressesByText(input)) {
            addressNames.add(address.getAddressLine(0) + " " + address.getAddressLine(1));
        }

        return addressNames;
    }

    private List<Address> getAddressesByText(String input) {
        List<Address> addresses= new ArrayList<>();
        try {
            addresses = coder.getFromLocationName(input, 10);
        } catch (Exception exception) {
            Toast.makeText(SetLocationActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
        }

        return addresses;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_set_location, menu);
        return true;
    }

}
