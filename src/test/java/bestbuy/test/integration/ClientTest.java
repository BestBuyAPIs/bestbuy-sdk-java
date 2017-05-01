package bestbuy.test.integration;

import bestbuy.Client;
import bestbuy.exception.AuthorizationException;
import bestbuy.exception.ServiceException;
import junit.framework.TestCase;

import java.util.List;
import java.util.Map;

public class ClientTest extends TestCase {
    /**
     * The apiKey (grabbed from env for easier use)
     */
    protected String apiKey;

    /**
     * The client
     */
    protected Client client;

    /**
     * Sets up our client and apiKey before each test
     */
    public void setUp() throws Exception {
        super.setUp();

        apiKey = System.getenv("BBY_API_KEY");
        client = new Client();
    }

    /**
     * Verifies store availability endpoint
     */
    public void testAvailability() throws ServiceException, AuthorizationException {
        Map availability = client.availability(6354884, 611);

        assertEquals(
                "/v1/products(sku in(6354884))+stores(storeId in(611))?format=json&apiKey=" + apiKey,
                availability.get("canonicalUrl")
        );
        assertEquals(0, ((List)availability.get("products")).size());

        throttle();
    }

    /**
     * Throttles calls so we don't get nastygrams. This makes it so we're < 4 calls / second
     */
    protected void throttle() {
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {}
    }
}