package com.example.mahes_000.moviesapp_udacity.data;

import android.test.AndroidTestCase;

/**
 * Created by mahes_000 on 7/6/2016.
 */
public class TestPractice extends AndroidTestCase {

    public static final String LOG_TAG = TestPractice.class.getSimpleName();

    /*
    *   This gets to run before every test.
    * */

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testThatDemonstratesAssertions() throws Throwable {

        int a = 5;
        int b = 3;
        int c = 5;
        int d = 10;

        assertEquals("X should be equal", a, c);
        assertTrue("'d' should be greater than 'a' ", d > a);
        assertFalse("'b' is greather than 'a'", b > a);

        if(b > d)
        {
            fail("'b' should never be equal");
        }
    }

    /*
    *   Tears down the test case and this runs after each test
    * */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
