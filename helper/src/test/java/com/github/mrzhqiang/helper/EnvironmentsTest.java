package com.github.mrzhqiang.helper;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class EnvironmentsTest {

    @Test
    public void testGetEnvironmentVariableAbsent() {
        final String name = "THIS_ENV_VAR_SHOULD_NOT_EXIST_FOR_THIS_TEST_TO_PASS";
        final String expected = System.getenv(name);
        assertNull(expected);
        final String value = Environments.getEnvironmentVariable(name, "DEFAULT");
        assertEquals("DEFAULT", value);
    }

    @Test
    public void testGetEnvironmentVariablePresent() {
        final String name = "PATH";
        final String expected = System.getenv(name);
        final String value = Environments.getEnvironmentVariable(name, null);
        assertEquals(expected, value);
    }

    @Test
    public void testGetHostName() {
        final String hostName = Environments.getHostName();
        final String expected = Environments.IS_OS_WINDOWS ? System.getenv("COMPUTERNAME") : System.getenv("HOSTNAME");
        assertEquals(expected, hostName);
    }

    /**
     * Assumes no security manager exists.
     */
    @Test
    public void testGetJavaHome() {
        final File dir = Environments.getJavaHome();
        assertNotNull(dir);
        assertTrue(dir.exists());
    }

    /**
     * Assumes no security manager exists.
     */
    @Test
    public void testGetJavaIoTmpDir() {
        final File dir = Environments.getJavaIoTmpDir();
        assertNotNull(dir);
        assertTrue(dir.exists());
    }

    /**
     * Assumes no security manager exists.
     */
    @Test
    public void testGetUserDir() {
        final File dir = Environments.getUserDir();
        assertNotNull(dir);
        assertTrue(dir.exists());
    }

    /**
     * Assumes no security manager exists.
     */
    @Test
    public void testGetUserHome() {
        final File dir = Environments.getUserHome();
        assertNotNull(dir);
        assertTrue(dir.exists());
    }

    /**
     * Assumes no security manager exists.
     */
    @Test
    public void testGetUserName() {
        assertEquals(System.getProperty("user.name"), Environments.getUserName());
        // Don't overwrite the system property in this test in case something goes awfully wrong.
        assertEquals(System.getProperty("user.name", "foo"), Environments.getUserName("foo"));
    }

    @Test
    public void testOSMatchesName() {
        String osName = null;
        assertFalse(Environments.isOSNameMatch(osName, "Windows"));
        osName = "";
        assertFalse(Environments.isOSNameMatch(osName, "Windows"));
        osName = "Windows 95";
        assertTrue(Environments.isOSNameMatch(osName, "Windows"));
        osName = "Windows NT";
        assertTrue(Environments.isOSNameMatch(osName, "Windows"));
        osName = "OS/2";
        assertFalse(Environments.isOSNameMatch(osName, "Windows"));
    }

    @Test
    public void testOSMatchesNameAndVersion() {
        String osName = null;
        String osVersion = null;
        assertFalse(Environments.isOSMatch(osName, osVersion, "Windows 9", "4.1"));
        osName = "";
        osVersion = "";
        assertFalse(Environments.isOSMatch(osName, osVersion, "Windows 9", "4.1"));
        osName = "Windows 95";
        osVersion = "4.0";
        assertFalse(Environments.isOSMatch(osName, osVersion, "Windows 9", "4.1"));
        osName = "Windows 95";
        osVersion = "4.1";
        assertTrue(Environments.isOSMatch(osName, osVersion, "Windows 9", "4.1"));
        osName = "Windows 98";
        osVersion = "4.1";
        assertTrue(Environments.isOSMatch(osName, osVersion, "Windows 9", "4.1"));
        osName = "Windows NT";
        osVersion = "4.0";
        assertFalse(Environments.isOSMatch(osName, osVersion, "Windows 9", "4.1"));
        osName = "OS/2";
        osVersion = "4.0";
        assertFalse(Environments.isOSMatch(osName, osVersion, "Windows 9", "4.1"));
    }

    @Test
    public void testOsVersionMatches() {
        String osVersion = null;
        assertFalse(Environments.isOSVersionMatch(osVersion, "10.1"));

        osVersion = "";
        assertFalse(Environments.isOSVersionMatch(osVersion, "10.1"));

        osVersion = "10";
        assertTrue(Environments.isOSVersionMatch(osVersion, "10.1"));
        assertTrue(Environments.isOSVersionMatch(osVersion, "10.1.1"));
        assertTrue(Environments.isOSVersionMatch(osVersion, "10.10"));
        assertTrue(Environments.isOSVersionMatch(osVersion, "10.10.1"));

        osVersion = "10.1";
        assertTrue(Environments.isOSVersionMatch(osVersion, "10.1"));
        assertTrue(Environments.isOSVersionMatch(osVersion, "10.1.1"));
        assertFalse(Environments.isOSVersionMatch(osVersion, "10.10"));
        assertFalse(Environments.isOSVersionMatch(osVersion, "10.10.1"));

        osVersion = "10.1.1";
        assertTrue(Environments.isOSVersionMatch(osVersion, "10.1"));
        assertTrue(Environments.isOSVersionMatch(osVersion, "10.1.1"));
        assertFalse(Environments.isOSVersionMatch(osVersion, "10.10"));
        assertFalse(Environments.isOSVersionMatch(osVersion, "10.10.1"));

        osVersion = "10.10";
        assertFalse(Environments.isOSVersionMatch(osVersion, "10.1"));
        assertFalse(Environments.isOSVersionMatch(osVersion, "10.1.1"));
        assertTrue(Environments.isOSVersionMatch(osVersion, "10.10"));
        assertTrue(Environments.isOSVersionMatch(osVersion, "10.10.1"));

        osVersion = "10.10.1";
        assertFalse(Environments.isOSVersionMatch(osVersion, "10.1"));
        assertFalse(Environments.isOSVersionMatch(osVersion, "10.1.1"));
        assertTrue(Environments.isOSVersionMatch(osVersion, "10.10"));
        assertTrue(Environments.isOSVersionMatch(osVersion, "10.10.1"));
    }

    @Test
    public void testJavaAwtHeadless() {
        final String expectedStringValue = System.getProperty("java.awt.headless");
        final String expectedStringValueWithDefault = System.getProperty("java.awt.headless", "false");
        assertNotNull(expectedStringValueWithDefault);
        final boolean expectedValue = Boolean.parseBoolean(expectedStringValue);
        if (expectedStringValue != null) {
            assertEquals(expectedStringValue, Environments.JAVA_AWT_HEADLESS);
        }
        assertEquals(expectedValue, Environments.isJavaAwtHeadless());
        assertEquals(expectedStringValueWithDefault, "" + Environments.isJavaAwtHeadless());
    }

    @Test
    public void debug() {
        String property = System.getProperty("intellij.debug.agent");
        boolean debug = Environments.debug();
        assertEquals(Boolean.parseBoolean(property), debug);
    }
}