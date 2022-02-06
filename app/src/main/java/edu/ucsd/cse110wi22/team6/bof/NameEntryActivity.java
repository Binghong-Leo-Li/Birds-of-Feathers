package edu.ucsd.cse110wi22.team6.bof;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NameEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("First time setup");
        setContentView(R.layout.activity_name_entry);

        Button confirmButton = findViewById(R.id.first_name_confirm_button);
        confirmButton.setOnClickListener(this::onConfirm);
    }

    private void onConfirm(View view) {
        SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCE_STRING, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        EditText firstNameView = findViewById(R.id.first_name_view);
        editor.putString("name", firstNameView.getText().toString());

        editor.apply();

        // Sample Photo URL: Kevin's GitHub avatar
        editor.putString("photoUrl", "https://avatars.githubusercontent.com/u/32375681?v=4");
        // TODO: actually ask for photo URL

        Intent intent = new Intent(this, CourseEntryActivity.class);
        startActivity(intent);
    }
}