package edu.ucsd.cse110wi22.team6.bof;

import static org.junit.Assert.assertEquals;

import android.content.Intent;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;
import java.util.UUID;

@RunWith(AndroidJUnit4.class)
public class TestBoFDetails {

    private void assertTextAt(String expected, RecyclerView view, int position) {
        FrameLayout framelayout = (FrameLayout) view.getChildAt(position);
        TextView editText = framelayout.findViewById(R.id.bof_course_info);
        String info = editText.getText().toString();
        assertEquals(expected, info);
    }

    @Test
    public void testListOfBoFsDisplayed() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), BoFsDetails.class);
        intent.putExtra("uuid", UUID.randomUUID().toString());
        intent.putExtra("name", "Rick");
        intent.putExtra("courseListParsing", "2021 FA ECE 65,2020 FA CSE 11");
        intent.putExtra("url", "https://avatars.githubusercontent.com/u/32375681?v=4");

        ActivityScenario<BoFsDetails> scenario = ActivityScenario.launch(intent);

        scenario.onActivity(activity -> {
            RecyclerView view = activity.findViewById(R.id.bof_courses_info);
            assertEquals(2, Objects.requireNonNull(view.getAdapter()).getItemCount());
            assertTextAt("2021 FA ECE 65", view, 0);
            assertTextAt("2020 FA CSE 11", view, 1);
        });
    }
}


