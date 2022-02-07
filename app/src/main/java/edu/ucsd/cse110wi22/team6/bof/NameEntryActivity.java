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
        IUserInfoStorage storage = Utilities.getStorageInstance(this);

        EditText firstNameView = findViewById(R.id.first_name_view);
        storage.setName(firstNameView.getText().toString());

        String nameEntered = firstNameView.getText().toString();

        if (nameEntered.length() == 0) {
            Utilities.showAlert(this, "Please enter non-empty name!");
            return;
        }
        // Sample Photo URL: Kevin's GitHub avatar
        storage.setPhotoUrl("https://avatars.githubusercontent.com/u/32375681?v=4");
        // TODO: actually ask for photo URL

        Intent intent = new Intent(this, CourseEntryActivity.class);
        startActivity(intent);
    }
}