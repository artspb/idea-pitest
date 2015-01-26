package me.artspb.pitest.example;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SecondClassTest {

    private SecondClass secondClass;

    @Before
    public void setUp() throws Exception {
        secondClass = new SecondClass();
    }

    @Test
    public void testUncoveredMethod() throws Exception {
        secondClass.uncoveredMethod();
    }

    @Test
    public void testPartiallyCoveredMethod() throws Exception {
        int output = secondClass.partiallyCoveredMethod(-2);
        assertEquals(output, 42);
    }
}