package edu.ucsd.cse110wi22.team6.bof;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NameEntryActivity extends AppCompatActivity {

    private ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();
    private Future<Void> future;

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

        EditText URLView = findViewById(R.id.URL_view);
        storage.setPhotoUrl(URLView.getText().toString());


        String nameEntered = firstNameView.getText().toString();
        String URLEntered = URLView.getText().toString();

        if (nameEntered.length() == 0) {
            Utilities.showAlert(this, "Please enter non-empty name!");
            return;
        }

        if (URLEntered.length() == 0) {
            Utilities.showAlert(this, "Please enter non-empty URL!");
            return;
        }

        if(!URLUtil.isValidUrl(URLEntered)) {
            Utilities.showAlert(this, "Please enter valid URL!");
            return;
        }


        this.future = backgroundThreadExecutor.submit(() -> {
            URL photo = new URL(URLEntered);
            URLConnection connection = photo.openConnection();
            String contentType = connection.getHeaderField("Content-Type");
            boolean img = contentType.startsWith("image/");
            if (!img) {
                runOnUiThread(() -> {
                    Utilities.showAlert(this, "Please enter valid photo URL!");
                    return;
                });

            } else {
                this.future.cancel(true);
                Intent intent = new Intent(this, CourseEntryActivity.class);
                startActivity(intent);
            }
            return null;
        });
    }
}