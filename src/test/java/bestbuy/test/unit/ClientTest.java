package bestbuy.test.unit;

import bestbuy.Client;
import bestbuy.ClientConfig;
import bestbuy.ResponseConfig;
import bestbuy.exception.AuthorizationException;
import bestbuy.exception.InvalidArgumentException;
import bestbuy.exception.ServiceException;
import junit.framework.TestCase;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tests the generation of URLs
 */
public class ClientTest extends TestCase {
    /**
     * A test client which overrides the request method to fake the request
     */
    protected class TestClient extends Client {
        public TestClient() {
            super();
        }

        public TestClient(String key) {
            super(key);
        }

        /** {@inheritDoc} */
        public URL buildUrl(String root, String path, ResponseConfig responseConfig)
                throws MalformedURLException, AuthorizationException {
            return super.buildUrl(root, path, responseConfig);
        }

        /** {@inheritDoc} */
        public Map doRequest(String root, String path, ResponseConfig responseConfig)
        {
            Map<String, URL> responseMap = new HashMap<>();
            try {
                responseMap.put("url", buildUrl(root, path, responseConfig));
            } catch (AuthorizationException | MalformedURLException e) {
                e.printStackTrace();
            }
            return responseMap;
        }
    }

    /**
     * The apiKey (grabbed from env for easier use)
     */
    protected String apiKey;

    /**
     * The fake client
     */
    protected TestClient client;

    /**
     * Sets up our client and apiKey before each test
     */
    public void setUp() throws Exception {
        super.setUp();

        apiKey = System.getenv("BBY_API_KEY");
        client = new TestClient();
    }

    /**
     * Make sure the apiKey is gathered in each way to create the client
     */
    public void testConstruct() throws NoSuchFieldException, IllegalAccessException {
        List<Client> clients = new ArrayList<>();
        clients.add(new Client());
        clients.add(new Client(apiKey));
        clients.add(new Client(new ClientConfig(apiKey)));

        for (Client client : clients) {
            Field config = client.getClass().getDeclaredField("config");
            config.setAccessible(true);
            ClientConfig setConfig = (ClientConfig)config.get(client);

            assertEquals(apiKey, setConfig.getApiKey());
        }
    }

    /**
     * Make sure the URLs are generated successfully
     */
    public void testBuildUrl() throws MalformedURLException, AuthorizationException {
        ResponseConfig responseConfig = new ResponseConfig();
        responseConfig.setShow("all");

        assertEquals(
            new URL("https://api.bestbuy.com/v1/products/123.json?apiKey=" + apiKey + "&show=all"),
            client.buildUrl(Client.URL_V1, "/products/123.json", responseConfig)
        );

        assertEquals(
            new URL("https://api.bestbuy.com/beta/openBox(sku%20in(123,456))?apiKey=" + apiKey + "&format=json&show=all"),
            client.buildUrl(Client.URL_BETA, "/openBox(sku in(123,456))", responseConfig)
        );
    }

    /**
     * Tests generated URLs for all endpoints
     */
    public void testGeneratedUrls() throws ServiceException, AuthorizationException, InvalidArgumentException {
        // we're not using @dataProvider so we can access class props
        String host = "https://api.bestbuy.com";

        Object[][] callsAndUrls = new Object[][]{
            {
                host + "/v1/products(sku%20in(4312001))+stores(storeId%20in(611))?apiKey=" + apiKey + "&format=json",
                client.availability(4312001, 611)
            }, {
                host + "/v1/products(sku%20in(4312001,%206120183))+stores(storeId%20in(611,%20482))?apiKey=" + apiKey + "&format=json",
                client.availability(new int[]{4312001, 6120183}, new int[]{611, 482})
            }, {
                host + "/v1/products(name=Star*)+stores(area(55347,%2025))?apiKey=" + apiKey + "&format=json",
                client.availability("name=Star*", "area(55347, 25)")
            }, {
                host + "/v1/products(fdafsd)+stores(storeId%20in(611))?apiKey=" + apiKey + "&format=json",
                client.availability("fdafsd", 611)
            }, {
                host + "/v1/categories?apiKey=" + apiKey + "&format=json",
                client.categories()
            }, {
                host + "/v1/categories/cat00000.json?apiKey=" + apiKey + "",
                client.categories("cat00000")
            }, {
                host + "/v1/categories(name=Home*)?apiKey=" + apiKey + "&format=json",
                client.categories("name=Home*")
            }, {
                host + "/beta/products/openBox?apiKey=" + apiKey + "&format=json",
                client.openBox()
            }, {
                host + "/beta/products/2206525/openBox?apiKey=" + apiKey + "&format=json",
                client.openBox(2206525)
            }, {
                host + "/beta/products/openBox(sku%20in(8610161,%202206525))?apiKey=" + apiKey + "&format=json",
                client.openBox(new int[]{8610161, 2206525})
            }, {
                host + "/beta/products/openBox(categoryId=abcat0400000)?apiKey=" + apiKey + "&format=json",
                client.openBox("categoryId=abcat0400000")
            }, {
                host + "/v1/products?apiKey=" + apiKey + "&format=json",
                client.products()
            }, {
                host + "/v1/products/4312001.json?apiKey=" + apiKey + "",
                client.products(4312001)
            }, {
                host + "/v1/products(name=Star*)?apiKey=" + apiKey + "&format=json",
                client.products("name=Star*")
            }, {
                host + "/beta/products/trendingViewed?apiKey=" + apiKey + "&format=json",
                client.recommendations(Client.RecommendationType.TRENDING)
            }, {
                host + "/beta/products/trendingViewed(categoryId=abcat0400000)?apiKey=" + apiKey + "&format=json",
                client.recommendations(Client.RecommendationType.TRENDING, "abcat0400000")
            }, {
                host + "/v1/reviews?apiKey=" + apiKey + "&format=json",
                client.reviews()
            }, {
                host + "/v1/reviews/69944141.json?apiKey=" + apiKey + "",
                client.reviews(69944141)
            }, {
                host + "/v1/reviews(comment=purchase*)?apiKey=" + apiKey + "&format=json",
                client.reviews("comment=purchase*")
            }, {
                host + "/v1/stores?apiKey=" + apiKey + "&format=json",
                client.stores()
            }, {
                host + "/v1/stores/611.json?apiKey=" + apiKey + "",
                client.stores(611)
            }, {
                host + "/v1/stores(name=eden%20prairie)?apiKey=" + apiKey + "&format=json",
                client.stores("name=eden prairie")
            }, {
                host + "/v1/products/6354884/warranties.json?apiKey=" + apiKey,
                client.warranties(6354884)
            }
        };

        for (Object[] callAndUrl : callsAndUrls) {
            assertEquals(callAndUrl[0], ((Map) callAndUrl[1]).get("url").toString());
        }
    }

    /**
     * Tests checking for a apiKey before making a call
     */
    public void testNoKey() throws ServiceException, AuthorizationException {
        Client client = new Client("");
        boolean thrown = false;
        try {
            client.products(6354884);
        } catch (AuthorizationException e) {
            thrown = true;
        }

        assertTrue(thrown);
    }
}