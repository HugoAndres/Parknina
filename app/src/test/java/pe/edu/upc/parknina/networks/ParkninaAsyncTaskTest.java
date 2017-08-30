package pe.edu.upc.parknina.networks;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.HttpURLConnection;

import static org.junit.Assert.*;

/**
 * Created by hugo_ on 23/08/2017.
 */
public class ParkninaAsyncTaskTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void connectionConfigure() throws Exception {
        HttpURLConnection prove = ParkninaAsyncTask.connectionConfigure("1", "POST");

        assertNull(String.valueOf(prove), null);
    }

    @Test
    public void sendHeadersAndMethod() throws Exception {

    }

    @Test
    public void convertInputStreamToString() throws Exception {

    }

}