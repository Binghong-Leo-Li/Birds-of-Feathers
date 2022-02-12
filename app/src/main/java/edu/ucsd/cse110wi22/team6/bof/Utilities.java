package edu.ucsd.cse110wi22.team6.bof;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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

    public static String encodeCourseList(List<Course> courses) {
        return courses.stream().map(Course::toString).collect(Collectors.joining(","));
    }

    public static int numCoursesTogether(IPerson a, IPerson b) {
        return getCoursesTogether(a, b).size();
    }

    public static List<Course> getCoursesTogether(IPerson a, IPerson b) {
        Set<Course> aCourseList = new HashSet<>(a.getCourseList());
        aCourseList.retainAll(new HashSet<>(b.getCourseList()));
        return new ArrayList<>(aCourseList);
    }

    public static List<IPerson> getBofList(IPerson user, List<IPerson> nearbyPeople) {
        return nearbyPeople
                .stream()
                .filter(person -> Utilities.numCoursesTogether(user, person) > 0)
                .sorted(Comparator.comparingInt((IPerson person) ->
                        Utilities.numCoursesTogether(user, person)).reversed())
                .collect(Collectors.toList());
    }

    public static IPerson parsePersonFromCSV(String csv) {
        String[] lines = csv.split("\r?\n");
        String name = lines[0].split(",")[0];
        String photoURL = lines[1].split(",")[0];
        List<Course> courses = new ArrayList<>();
        for (int i = 2; i < lines.length; i++) {
            String[] lineSplit = lines[i].split(",");
            courses.add(new Course(
                    Integer.parseInt(lineSplit[0]), // Year
                    lineSplit[1], // Quarter
                    lineSplit[2], // Subject
                    lineSplit[3])); // Number
        }
        return new Person(name, courses, photoURL);
    }
}
