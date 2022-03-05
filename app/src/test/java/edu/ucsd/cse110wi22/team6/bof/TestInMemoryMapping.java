package edu.ucsd.cse110wi22.team6.bof;

import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import edu.ucsd.cse110wi22.team6.bof.model.InMemoryMapping;

@RunWith(AndroidJUnit4.class)
public class TestInMemoryMapping {
    @Test
    public void testInMemoryMappingString() {
        InMemoryMapping map = new InMemoryMapping(new HashMap<String, Object>());
        map.putString("Kevin", "19273234");
        assertEquals("19273234",map.getString("Kevin", "0"));
    }
    @Test
    public void testInMemoryMappingStringSet() {
        InMemoryMapping map = new InMemoryMapping(new HashMap<String, Object>());
        HashSet<String> s = new HashSet<>();
        map.putStringSet("Robert", s);
        assertEquals(s,map.getStringSet("Robert", null));
    }
    @Test
    public void testInMemoryMappingStringBool() {
        InMemoryMapping map = new InMemoryMapping(new HashMap<String, Object>());
        map.putBoolean("Leo", false);
        assertEquals(false,map.getBoolean( "Leo", false));
    }
}
