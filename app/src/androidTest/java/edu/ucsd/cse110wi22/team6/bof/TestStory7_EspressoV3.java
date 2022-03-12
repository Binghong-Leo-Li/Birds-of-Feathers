package edu.ucsd.cse110wi22.team6.bof;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import edu.ucsd.cse110wi22.team6.bof.model.AppStorage;
import edu.ucsd.cse110wi22.team6.bof.model.CourseSize;


//User Story BDD Scenario Test 7: Favorites list [2]
//As a student I want my favorites to be shown in a list like the other lists so I can easily reference all my class friends
@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestStory7_EspressoV3 {

    private Context context;


    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class, false, false);

    @Before
    public void setup() {
        Utilities.setPersistence(false);
        NameEntryActivity.noAutoFill();
        Person bof;
        // Setting up
        AppStorage storage = Utilities.getStorageInstance(null);
        storage.setInitialized(true);
        storage.setName("Ava");
        storage.setPhotoUrl("https://www.example.com");
        storage.setCourseList(Utilities.parseCourseList("2022 WI CSE 110,2021 FA CSE 100,2019 SP CSE 101,2021 WI SYN 2"));
        List<Course> courseList = Utilities.parseCourseList("2022 WI CSE 110,2021 FA ECE 65,2020 FA CSE 11");
       

        // Ava's course sizes
        storage.registerCourse(Utilities.parseCourse("2021 FA CSE 100"), CourseSize.HUGE);
        storage.registerCourse(Utilities.parseCourse("2019 SP CSE 101"), CourseSize.LARGE);
        storage.registerCourse(Utilities.parseCourse("2021 WI SYN 2"), CourseSize.TINY);
        storage.registerCourse(Utilities.parseCourse("2022 WI CSE 110"), CourseSize.TINY);
        // Setting up end
        bof = new Person(UUID.randomUUID(), "Rick", courseList, "https://avatars.githubusercontent.com/u/32375681?v=4");
        storage.registerPerson(bof);
        storage.addToFavorites(bof);
    }



    @Test
    public void testStory7_EspressoV3() {

        mActivityTestRule.launchActivity(new Intent());



        ViewInteraction materialButton12 = onView(
                allOf(withId(R.id.buttonFavorites), withText("Favorites"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton12.perform(click());

        ActivityScenario.launch(FavoriteListActivity.class);

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.favorite_list),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                0)));
        recyclerView2.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.bof_course_info), withText("2022 WI CSE 110"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class))),
                        isDisplayed()));
        textView.check(matches(withText("2022 WI CSE 110")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
