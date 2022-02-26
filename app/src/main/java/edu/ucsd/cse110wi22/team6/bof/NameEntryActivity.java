package edu.ucsd.cse110wi22.team6.bof;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// Activity handling the entry of person information, e.g. name and photo url
public class NameEntryActivity extends AppCompatActivity {
    private static final String TAG = "NameEntryActivity";

    // Creating a new thread
    private final ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();
    private Future<Void> future;

    private GoogleSignInClient mGoogleSignInClient;

    // Handling extracting google account information
    ActivityResultLauncher<Intent> signInFormLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }
    );

    private static boolean noGoogleAutoFill;

    // Updating state to no autofill
    public static void noAutoFill() {
        noGoogleAutoFill = true;
    }

    // Validating/Checking google account information
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    // Handling auto-filling name with google account
    private void updateUI(GoogleSignInAccount account) {
        if (account != null && !noGoogleAutoFill)
            this.<TextView>findViewById(R.id.first_name_view).setText(account.getGivenName());
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Only shown on first time
        super.onCreate(savedInstanceState);
        setTitle("First time setup");
        setContentView(R.layout.activity_name_entry);

        Button confirmButton = findViewById(R.id.first_name_confirm_button);
        confirmButton.setOnClickListener(this::onConfirm);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.sign_in_button).setOnClickListener((view) -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            signInFormLauncher.launch(signInIntent);
        });
    }

    // Handling when confirm button is clicked
    private void onConfirm(View view) {
        // Obtaining stored information from storage
        IUserInfoStorage storage = Utilities.getStorageInstance(this);

        EditText firstNameView = findViewById(R.id.first_name_view);
        storage.setName(firstNameView.getText().toString());

        EditText URLView = findViewById(R.id.URL_view);
        storage.setPhotoUrl(URLView.getText().toString());

        String nameEntered = firstNameView.getText().toString();
        String URLEntered = URLView.getText().toString();

        // Check name entered is non-empty
        if (nameEntered.length() == 0) {
            Utilities.showAlert(this, "Please enter non-empty name!");
            return;
        }

        // Check Url entered is non-empty
        if (URLEntered.length() == 0) {
            Utilities.showAlert(this, "Please enter non-empty URL!");
            return;
        }

        // Check Url entered is of valid format
        if(!URLUtil.isValidUrl(URLEntered)) {
            Utilities.showAlert(this, "Please enter valid URL!");
            return;
        }


        // Move from main thread to background thread to enable network connection
        // Checking Url is a valid photo url
        this.future = backgroundThreadExecutor.submit(() -> {

            // Connecting to network
            URL photo = new URL(URLEntered);
            URLConnection connection = photo.openConnection();
            String contentType = connection.getHeaderField("Content-Type");
            boolean img = contentType.startsWith("image/");

            if (!img) { // Check if img is type image
                runOnUiThread(() -> Utilities.showAlert(
                        this, "Please enter valid photo URL!"
                ));
            } else { // If not return and show alert
                this.future.cancel(true);
                Intent intent = new Intent(this, CourseEntryActivity.class);
                startActivity(intent);
            }
            return null;
        });
    }
}