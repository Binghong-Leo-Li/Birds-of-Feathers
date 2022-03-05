package edu.ucsd.cse110wi22.team6.bof;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import edu.ucsd.cse110wi22.team6.bof.model.IDMapping;
import edu.ucsd.cse110wi22.team6.bof.model.IPersonFactory;
import edu.ucsd.cse110wi22.team6.bof.model.InMemoryMapping;

@RunWith(AndroidJUnit4.class)
public class TestIDMapping {
    @Test
    public void testGetPersonByUUID(){
        InMemoryMapping mem = new InMemoryMapping(new HashMap<String, Object>());
        IPersonFactory pf = new IPersonFactory();
        IDMapping<IPerson> map = new IDMapping<IPerson>(mem, "IDMap", pf);

        Person p1 = new Person("Josh", Utilities.parseCourseList("2019 SP CSE 101,2021 WI SYN 2"), "");
        map.registerObject(p1);
        String uuid = p1.getUUID().toString();

        assertEquals(p1, map.getObjectByID(uuid));
    }

    @Test
    public void testNameSpace(){
        InMemoryMapping mem = new InMemoryMapping(new HashMap<String, Object>());
        IPersonFactory pf = new IPersonFactory();
        IDMapping<IPerson> map = new IDMapping<IPerson>(mem, "IDMap", pf);

        IDMapping<IPerson> map2 = new IDMapping<IPerson>(mem, "IDMap2", pf);

        Person p1 = new Person("Josh", Utilities.parseCourseList("2019 SP CSE 101,2021 WI SYN 2"), "");
        map.registerObject(p1);
        map2.registerObject(p1);

        assertNotEquals(map, map2);
    }
}
