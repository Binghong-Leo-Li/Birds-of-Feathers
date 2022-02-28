package edu.ucsd.cse110wi22.team6.bof;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gson.Gson;

public class Utilities {
    // Device persistence status
    private static boolean persistence = true;
    private static final int SIZE_OF_INT = 4;


    // Helper function to extract device storage if persistence is set
    public static IUserInfoStorage getStorageInstance(Context context) {
        if (persistence) {
            return new SharedPreferencesStorage(context.getSharedPreferences(
                    Constants.PREFERENCE_STRING, MODE_PRIVATE
            ));
        } else {
            return InMemoryStorage.getInstance();
        }
    }

    // Initializing the persistence setting
    public static void setPersistence(boolean persistence) {
        Utilities.persistence = persistence;
    }

    // Helper function to decode the encoded courseList string to retrieve list of courses
    public static List<Course> parseCourseList(String courseList) {
        if (courseList.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(courseList.split(",")).map(Utilities::parseCourse)
                .collect(Collectors.toList());
    }

    // Helper function to generate Alerts with selected message
    public static void showAlert(Activity activity, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);

        alertBuilder
                .setTitle("Alert!")
                .setMessage(message)
                .setPositiveButton("Ok", (dialog, id) -> dialog.cancel())
                .setCancelable(true);

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    // No input validation is performed
    // Helper function to parse Course Strings
    public static Course parseCourse(String course) {
        String[] fields = course.split(" ");
        return new Course(
                Integer.parseInt(fields[0]), // Year
                fields[1], // Quarter
                fields[2], // Subject
                fields[3]  //
        );
    }

    // Helper function to encode the course list into a hashable string
    public static String encodeCourseList(List<Course> courses) {
        return courses.stream().map(Course::toString).collect(Collectors.joining(","));
    }

    // Helper function to obtain number of overlapping courses between two people
    public static int numCoursesTogether(IPerson a, IPerson b) {
        return getCoursesTogether(a, b).size();
    }

    // Helper function to obtain the overlapping courses between two people
    public static List<Course> getCoursesTogether(IPerson a, IPerson b) {
        Set<Course> aCourseList = new HashSet<>(a.getCourseList());
        aCourseList.retainAll(new HashSet<>(b.getCourseList()));
        return new ArrayList<>(aCourseList);
    }

    // Helper function to obtain the list of BoFs from all nearbyPeople
    public static List<IPerson> getBofList(IPerson user, List<IPerson> nearbyPeople) {
        return nearbyPeople
                .stream()
                .filter(person -> Utilities.numCoursesTogether(user, person) > 0)
                                    // if have courses together then generate list
                .sorted(Comparator.comparingInt((IPerson person) ->
                        Utilities.numCoursesTogether(user, person)).reversed())
                .collect(Collectors.toList());
    }

    // Helper function to parse information from CSV file into a new person object
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

    public static IPerson parsePersonFromJSON(String PersonJSON) {
        Gson gson = new Gson();
        return gson.fromJson(PersonJSON, Person.class);
    }

    // Helper function to parse person into storable data into buffers
    public static byte[] serializePerson(IPerson person) {
        byte[] name = person.getName().getBytes(StandardCharsets.UTF_8);
        byte[] url = person.getUrl().getBytes(StandardCharsets.UTF_8);
        byte[] classList = encodeCourseList(person.getCourseList()).getBytes(StandardCharsets.UTF_8);
        int totalSize = 3 * SIZE_OF_INT + name.length + url.length + classList.length; // 3 fields
        ByteBuffer buffer = ByteBuffer.allocate(totalSize);
        buffer.putInt(name.length);
        buffer.put(name);
        buffer.putInt(classList.length);
        buffer.put(classList);
        buffer.putInt(url.length);
        buffer.put(url);
        return buffer.array();
    }

    // Helper function to read String from selected buffer
    private static String readString(ByteBuffer buffer) {
        int length = buffer.getInt();
        byte[] encodedString = new byte[length];
        buffer.get(encodedString, 0, length);
        return new String(encodedString, StandardCharsets.UTF_8);
    }

    // Helper function to separate extract Person information from stored data
    public static IPerson deserializePerson(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        return new Person(readString(buffer), Utilities.parseCourseList(
                readString(buffer)), readString(buffer)
        );
    }
}
