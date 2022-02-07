package edu.ucsd.cse110wi22.team6.bof;
import static org.junit.Assert.assertEquals;

import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import androidx.test.core.app.ActivityScenario;

@RunWith(AndroidJUnit4.class)
public class TestListBoF{
    @Test
    public void testListOfBoFsDisplayed(){
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        scenario.onActivity(activity -> {
            RecyclerView view = activity.findViewById(R.id.bof_list);
            FrameLayout framelayout = (FrameLayout) view.getChildAt(0);
            TextView editText = framelayout.findViewById(R.id.bof_row_name);
            String info = editText.getText().toString();
            assertEquals(info, "Rick");
            assertEquals(view.getAdapter().getItemCount(), 0);
        });
    }
}


