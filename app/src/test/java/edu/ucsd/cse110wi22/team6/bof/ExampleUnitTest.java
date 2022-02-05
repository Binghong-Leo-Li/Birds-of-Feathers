package edu.ucsd.cse110wi22.team6.bof;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void name_isCorrect(){
        List<Course> courses = new ArrayList<>();
        IPerson tmp = new Person("Alex", courses, "https://www.example.com/nothing");
        assertEquals(tmp.getName(), "Alex");
    }
}