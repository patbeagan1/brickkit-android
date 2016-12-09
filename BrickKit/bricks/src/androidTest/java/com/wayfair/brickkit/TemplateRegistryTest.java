package com.wayfair.brickkit;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class TemplateRegistryTest {
    private static final String FIRST = "first";
    private static final String SECOND = "second";

    @Before
    public void setup() {
        TemplateRegistry.getInstance().reset();
    }

    @Test
    public void testGet() {
        TemplateRegistry templateRegistry = TemplateRegistry.getInstance();

        assertEquals(0, templateRegistry.get(FIRST));
        assertEquals(1, templateRegistry.get(SECOND));
        assertEquals(1, templateRegistry.get(SECOND));

        assertEquals(SECOND, templateRegistry.get(1));
    }

    @Test
    public void testGetInstance() {
        assertEquals(TemplateRegistry.getInstance(), TemplateRegistry.getInstance());
    }
}
