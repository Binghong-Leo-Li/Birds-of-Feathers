package edu.ucsd.cse110wi22.team6.bof;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MockingPasting extends AppCompatActivity {
    private static final String TAG = "MockingPasting";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mocking_pasting);
        setTitle("Nearby Messages Mock");

        this.<Button>findViewById(R.id.mocking_return_home_button)
                .setOnClickListener((view) -> finish());
        this.<Button>findViewById(R.id.mocking_enter_button)
                .setOnClickListener(this::onEnterClicked);
    }

    private void onEnterClicked(View view) {
        EditText input = findViewById(R.id.bof_info_pasted);
        IPerson person;
        try {
            person = Utilities.parsePersonFromCSV(input.getText().toString());
        } catch (RuntimeException exception) {
            Log.w(TAG, "Invalid CSV format!");
            exception.printStackTrace();
            Utilities.showAlert(this, "Something went wrong. Check the format.");
            return;
        }
        // TODO: actually mock arrival
        Log.d(TAG, "Mock person arrival: " + person);
        input.getText().clear();
    }
}