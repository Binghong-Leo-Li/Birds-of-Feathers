package edu.ucsd.cse110wi22.team6.bof;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.nearby.messages.Message;

public class MockingTime extends AppCompatActivity{
    private static final String TAG = "MockingTime";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mocking_time);
        setTitle("Nearby Messages Mock");

        Spinner time_dropdown =findViewById(R.id.time_spinner);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this, R.array.time, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        time_dropdown.setAdapter(adapter);
    }
}
