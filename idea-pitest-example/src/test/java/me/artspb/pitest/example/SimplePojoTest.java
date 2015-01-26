package me.artspb.pitest.example;

import org.junit.Before;
import org.junit.Test;

public class SimplePojoTest {

    private SimplePojo pojo;

    @Before
    public void setUp() throws Exception {
        pojo = new SimplePojo();
    }

    @Test
    public void testGetField() throws Exception {
        pojo.getField();
    }

    @Test
    public void testSetField() throws Exception {
        pojo.setField("field");
    }
}