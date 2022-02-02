package edu.ucsd.cse110wi22.team6.bof;

import org.junit.Test;

import static org.junit.Assert.*;

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
        BoF tmp = new BoF("Alex");
        assertEquals(tmp.getName(), "Alex");
    }
}