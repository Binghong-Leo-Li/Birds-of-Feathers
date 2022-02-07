package edu.ucsd.cse110wi22.team6.bof;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Utilities {
    private static boolean persistence = false;

    public static IUserInfoStorage getStorageInstance(Context context) {
        if (persistence) {
            return new SharedPreferencesStorage(context.getSharedPreferences(Constants.PREFERENCE_STRING, MODE_PRIVATE));
        } else {
            return InMemoryStorage.getInstance();
        }
    }

    public static void setPersistence(boolean persistence) {
        Utilities.persistence = persistence;
    }

    // TODO: write unit tests for this
    public static List<Course> parseCourseList(String courseList) {
        if (courseList.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(courseList.split(",")).map(Utilities::parseCourse)
                .collect(Collectors.toList());
    }

    public static void showAlert(Activity activity, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);

        alertBuilder
                .setTitle("Alert!")
                .setMessage(message)
                .setPositiveButton("Ok", (dialog, id) -> {
                    dialog.cancel();
                })
                .setCancelable(true);

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    // No input validation is performed
    public static Course parseCourse(String course) {
        String[] fields = course.split(" ");
        return new Course(
                Integer.parseInt(fields[0]),
                fields[1],
                fields[2],
                fields[3]
        );
    }

    // TODO: do the inverse: construct string from course list

    public static String encodeCourseList(List<Course> courses) {
        return courses.stream().map(Course::toString).collect(Collectors.joining(","));
    }

    public static int numCoursesTogether(IPerson a, IPerson b) {
        Set<Course> aCourseList = new HashSet<>(a.getCourseList());
        aCourseList.retainAll(new HashSet<>(b.getCourseList()));
        return aCourseList.size();
    }
}
