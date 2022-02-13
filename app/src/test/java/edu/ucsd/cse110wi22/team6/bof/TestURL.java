package edu.ucsd.cse110wi22.team6.bof;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.app.AlertDialog;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.shadows.ShadowAlertDialog;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import androidx.test.core.app.ActivityScenario;


//Other non trivial test are done by espresso test
@RunWith(AndroidJUnit4.class)
public class TestURL{
    @Test
    public void testValidURL() {

        ActivityScenario<NameEntryActivity> scenario = ActivityScenario.launch(NameEntryActivity.class);

        scenario.onActivity(activity -> {

            EditText URLView = activity.findViewById(R.id.URL_view);
            URLView.setText("https://avatars.githubusercontent.com/u/32375681?v=4");
            activity.findViewById(R.id.first_name_confirm_button).performClick();
            IUserInfoStorage storage = Utilities.getStorageInstance(activity);
            String url = storage.getPhotoUrl();
            assertEquals("https://avatars.githubusercontent.com/u/32375681?v=4", url);
            assertTrue(URLUtil.isValidUrl(url));

        });
    }



}

