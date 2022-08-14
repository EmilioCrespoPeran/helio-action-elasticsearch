import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HostPatternTests {

    @Test
    public void test01_UrlWithProtocolAndHostnameAndPortAndResource() {
        String sample = "http://localhost:9200/example";
        String hostnameExpected = "localhost";
        Integer portExpected = 9200;
        test(sample, hostnameExpected, portExpected);
    }

    @Test
    public void test02_UrlWithProtocolAndHostnameAndPort() {
        String sample = "http://localhost:9200/";
        String hostnameExpected = "localhost";
        Integer portExpected = 9200;
        test(sample, hostnameExpected, portExpected);
    }

    @Test
    public void test03_UrlWithProtocolAndHostnameAndResource() {
        String sample = "http://localhost/example";
        String hostnameExpected = "localhost";
        Integer portExpected = null;
        test(sample, hostnameExpected, portExpected);
    }

    @Test
    public void test04_UrlWithProtocolAndHostname() {
        String sample = "http://localhost";
        String hostnameExpected = "localhost";
        Integer portExpected = null;
        test(sample, hostnameExpected, portExpected);
    }

    @Test
    public void test05_UrlWithHostnameAndPortAndResource() {
        String sample = "localhost:9200/example";
        String hostnameExpected = null;
        Integer portExpected = null;
        test(sample, hostnameExpected, portExpected);
    }

    @Test
    public void test06_UrlWithHostnameAndPort() {
        String sample = "localhost:9200";
        String hostnameExpected = null;
        Integer portExpected = null;
        test(sample, hostnameExpected, portExpected);
    }

    @Test
    public void test07_MalformedUrl() {
        String sample = ":/7dkio:dasd";
        String hostnameExpected = null;
        Integer portExpected = null;
        test(sample, hostnameExpected, portExpected);
    }

    private void test(String sample, String hostnameExpected, Integer portExpected) {
        try {
            URL url = new URL(sample);
            String hostname = url.getHost();
            Integer port = url.getPort() != -1 ? url.getPort() : null;

            assertEquals(hostnameExpected, hostname);
            assertEquals(portExpected, port);
        } catch (MalformedURLException e) {
            if (hostnameExpected == null && portExpected == null) {
                assertTrue(true);
            }
            else {
                assertTrue(false);
            }
        }
    }

}
