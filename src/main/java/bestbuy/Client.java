/**
 * Best Buy SDK
 *
 * High level Java client for the Best Buy API
 */
package bestbuy;

import bestbuy.exception.AuthorizationException;
import bestbuy.exception.InvalidArgumentException;
import bestbuy.exception.ServiceException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Map;

/**
 * The main client for interacting with Best Buy APIs
 */
public class Client {
    /**
     * The possible types for the recommendations endpoint
     */
    public enum RecommendationType {
        MOSTVIEWED("mostViewed"),
        TRENDING("trendingViewed"),
        ALSOVIEWED("alsoViewed");

        /**
         * The type selected
         */
        private final String endpoint;

        /**
         * Sets the type to retrieve from the service
         *
         * @param endpoint The type to retrieve from the service
         */
        RecommendationType(String endpoint) {
            this.endpoint = endpoint;
        }

        /**
         * Gets the type that will be retrieved from the service
         *
         * @return The type that will be retrieved from the service
         */
        public String getEndpoint() {
            return endpoint;
        }
    }

    /**
     * The beta URL
     */
    public static final String URL_BETA = "https://api.bestbuy.com/beta";

    /**
     * The v1 URL
     */
    public static final String URL_V1 = "https://api.bestbuy.com/v1";

    /**
     * The stored config
     */
    protected ClientConfig config;

    /**
     * The base constructor (requires `BBY_API_KEY` environment var to be set)
     */
    public Client() {
        this(System.getenv("BBY_API_KEY"));
    }

    /**
     * The constructor with a given apiKey
     *
     * @param apiKey A Best Buy APIs apiKey
     */
    public Client(String apiKey) {
        this(new ClientConfig(apiKey));
    }

    /**
     * The constructor with a full config
     *
     * @param config The config for the client
     */
    public Client(ClientConfig config) {
        this.config = config;
    }

    /**
     * Sets the config
     * <p>
     * Allows updates to the config post-instantiation
     *
     * @param config The config to set for the client
     * @return Client
     */
    public Client setConfig(ClientConfig config) {
        this.config = config;
        return this;
    }

// <editor-fold desc="Availability - SKU input">

    /**
     * Gets availability of a single SKU in a single store (with default response configuration)
     *
     * @param sku   The SKU to find
     * @param store The store to look in
     * @return The availability response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#availability">Best Buy APIs Documentation</a>
     */
    public Map availability(int sku, int store)
            throws ServiceException, AuthorizationException {
        return availability(sku, store, new ResponseConfig());
    }

    /**
     * Gets availability of a single SKU in a single store (with custom response configuration)
     *
     * @param sku            The SKU to find
     * @param store          The store to look in
     * @param responseConfig The configuration for the response
     * @return The availability response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#availability">Best Buy APIs Documentation</a>
     */
    public Map availability(int sku, int store, ResponseConfig responseConfig)
            throws ServiceException, AuthorizationException {
        return availability(new int[]{sku}, new int[]{store}, responseConfig);
    }

    /**
     * Gets availability of a single SKU in a multiple stores (with default response configuration)
     *
     * @param sku    The SKU to find
     * @param stores The stores to look in
     * @return The availability response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#availability">Best Buy APIs Documentation</a>
     */
    public Map availability(int sku, int[] stores)
            throws ServiceException, AuthorizationException {
        return availability(sku, stores, new ResponseConfig());
    }

    /**
     * Gets availability of a single SKU in a multiple stores (with custom response configuration)
     *
     * @param sku            The SKU to find
     * @param stores         The stores to look in
     * @param responseConfig The configuration for the response
     * @return The availability response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#availability">Best Buy APIs Documentation</a>
     */
    public Map availability(int sku, int[] stores, ResponseConfig responseConfig)
            throws ServiceException, AuthorizationException {
        return availability(new int[]{sku}, stores, responseConfig);
    }

    /**
     * Gets availability of a single SKU in a set of stores matching the query (with default response configuration)
     *
     * @param sku        The SKU to find
     * @param storeQuery The query to locate stores
     * @return The availability response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#availability">Best Buy APIs Documentation</a>
     */
    public Map availability(int sku, String storeQuery)
            throws ServiceException, AuthorizationException {
        return availability(sku, storeQuery, new ResponseConfig());
    }

    /**
     * Gets availability of a single SKU in a set of stores matching the query (with default response configuration)
     *
     * @param sku            The SKU to find
     * @param storeQuery     The query to locate stores
     * @param responseConfig The configuration for the response
     * @return The availability response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#availability">Best Buy APIs Documentation</a>
     */
    public Map availability(int sku, String storeQuery, ResponseConfig responseConfig)
            throws ServiceException, AuthorizationException {
        return availability(new int[]{sku}, storeQuery, responseConfig);
    }
// </editor-fold>

// <editor-fold desc="Availability - SKUs input">

    /**
     * Gets availability of a multiple SKUs in a single store (with default response configuration)
     *
     * @param skus  The SKUs to find
     * @param store The store to look in
     * @return The availability response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#availability">Best Buy APIs Documentation</a>
     */
    public Map availability(int[] skus, int store)
            throws ServiceException, AuthorizationException {
        return availability(skus, store, new ResponseConfig());
    }

    /**
     * Gets availability of a multiple SKUs in a single store (with custom response configuration)
     *
     * @param skus           The SKUs to find
     * @param store          The store to look in
     * @param responseConfig The configuration for the response
     * @return The availability response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#availability">Best Buy APIs Documentation</a>
     */
    public Map availability(int[] skus, int store, ResponseConfig responseConfig)
            throws ServiceException, AuthorizationException {
        return availability(skus, new int[]{store}, responseConfig);
    }

    /**
     * Gets availability of a multiple SKUs in a multiple stores (with default response configuration)
     *
     * @param skus   The SKUs to find
     * @param stores The stores to look in
     * @return The availability response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#availability">Best Buy APIs Documentation</a>
     */
    public Map availability(int[] skus, int[] stores)
            throws ServiceException, AuthorizationException {
        return availability(skus, stores, new ResponseConfig());
    }

    /**
     * Gets availability of a multiple SKUs in a multiple stores (with custom response configuration)
     *
     * @param skus           The SKUs to find
     * @param stores         The stores to look in
     * @param responseConfig The configuration for the response
     * @return The availability response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#availability">Best Buy APIs Documentation</a>
     */
    public Map availability(int[] skus, int[] stores, ResponseConfig responseConfig)
            throws ServiceException, AuthorizationException {
        String storeIds = Arrays.toString(stores);
        return availability(skus, "storeId in(" + storeIds.substring(1, storeIds.length() - 1) + ")", responseConfig);
    }

    /**
     * Gets availability of a multiple SKUs in a set of stores matching the query (with default response configuration)
     *
     * @param skus       The SKUs to find
     * @param storeQuery The query to locate stores
     * @return The availability response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#availability">Best Buy APIs Documentation</a>
     */
    public Map availability(int[] skus, String storeQuery)
            throws ServiceException, AuthorizationException {
        return availability(skus, storeQuery, new ResponseConfig());
    }

    /**
     * Gets availability of a multiple SKUs in a set of stores matching the query (with custom response configuration)
     *
     * @param skus           The SKUs to find
     * @param storeQuery     The query to locate stores
     * @param responseConfig The configuration for the response
     * @return The availability response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#availability">Best Buy APIs Documentation</a>
     */
    public Map availability(int[] skus, String storeQuery, ResponseConfig responseConfig)
            throws ServiceException, AuthorizationException {
        String skuNos = Arrays.toString(skus);
        return availability("sku in(" + skuNos.substring(1, skuNos.length() - 1) + ")", storeQuery, responseConfig);
    }
// </editor-fold>

// <editor-fold desc="Availability - SKU query">

    /**
     * Gets availability of products matching the query in a single store (with default response configuration)
     *
     * @param skuQuery The query to locate products
     * @param store    The store to look in
     * @return The availability response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#availability">Best Buy APIs Documentation</a>
     */
    public Map availability(String skuQuery, int store)
            throws ServiceException, AuthorizationException {
        return availability(skuQuery, store, new ResponseConfig());
    }

    /**
     * Gets availability of products matching the query in a single store (with custom response configuration)
     *
     * @param skuQuery       The query to locate products
     * @param store          The store to look in
     * @param responseConfig The configuration for the response
     * @return The availability response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#availability">Best Buy APIs Documentation</a>
     */
    public Map availability(String skuQuery, int store, ResponseConfig responseConfig)
            throws ServiceException, AuthorizationException {
        return availability(skuQuery, new int[]{store}, responseConfig);
    }

    /**
     * Gets availability of products matching the query in multiple stores (with default response configuration)
     *
     * @param skuQuery The query to locate products
     * @param stores   The stores to look in
     * @return The availability response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#availability">Best Buy APIs Documentation</a>
     */
    public Map availability(String skuQuery, int[] stores)
            throws ServiceException, AuthorizationException {
        return availability(skuQuery, stores, new ResponseConfig());
    }

    /**
     * Gets availability of products matching the query in multiple stores (with custom response configuration)
     *
     * @param skuQuery       The query to locate products
     * @param stores         The stores to look in
     * @param responseConfig The configuration for the response
     * @return The availability response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#availability">Best Buy APIs Documentation</a>
     */
    public Map availability(String skuQuery, int[] stores, ResponseConfig responseConfig)
            throws ServiceException, AuthorizationException {
        String storeIds = Arrays.toString(stores);
        return availability(skuQuery, "storeId in(" + storeIds.substring(1, storeIds.length() - 1) + ")", responseConfig);
    }

    /**
     * Gets availability of products matching the query in stores matching the query (with default response configuration)
     *
     * @param skuQuery   The query to locate products
     * @param storeQuery The query to locate stores
     * @return The availability response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#availability">Best Buy APIs Documentation</a>
     */
    public Map availability(String skuQuery, String storeQuery)
            throws ServiceException, AuthorizationException {
        return availability(skuQuery, storeQuery, new ResponseConfig());
    }

    /**
     * Gets availability of products matching the query in stores matching the query (with custom response configuration)
     *
     * @param skuQuery       The query to locate products
     * @param storeQuery     The query to locate stores
     * @param responseConfig The configuration for the response
     * @return The availability response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#availability">Best Buy APIs Documentation</a>
     */
    public Map availability(String skuQuery, String storeQuery, ResponseConfig responseConfig)
            throws ServiceException, AuthorizationException {
        return doRequest(URL_V1, "/products(" + skuQuery + ")+stores(" + storeQuery + ")", responseConfig);
    }
// </editor-fold>

// <editor-fold desc="Categories">

    /**
     * Gets categories matching the query (with default response configuration)
     *
     * @param search The query to locate categories
     * @return The category response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#buying-options-open-box-api">Best Buy APIs Documentation</a>
     */
    public Map categories(String search)
            throws ServiceException, AuthorizationException {
        return categories(search, new ResponseConfig());
    }

    /**
     * Gets categories matching the query (with custom response configuration)
     *
     * @param search         The query to locate categories
     * @param responseConfig The configuration for the response
     * @return The category response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#buying-options-open-box-api">Best Buy APIs Documentation</a>
     */
    public Map categories(String search, ResponseConfig responseConfig)
            throws ServiceException, AuthorizationException {
        if (search.matches("^(ab|pcm)?cat[0-9]+$")) {
            return doRequest(URL_V1, "/categories/" + search + ".json", responseConfig);
        }

        return doRequest(URL_V1, "/categories(" + search + ")", responseConfig);
    }

    /**
     * Gets all categories (with default response configuration)
     *
     * @return The category response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#buying-options-open-box-api">Best Buy APIs Documentation</a>
     */
    public Map categories()
            throws ServiceException, AuthorizationException {
        return categories(new ResponseConfig());
    }

    /**
     * Gets all categories (with custom response configuration)
     *
     * @param responseConfig The configuration for the response
     * @return The category response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#buying-options-open-box-api">Best Buy APIs Documentation</a>
     */
    public Map categories(ResponseConfig responseConfig)
            throws ServiceException, AuthorizationException {
        return doRequest(URL_V1, "/categories", responseConfig);
    }
// </editor-fold>

// <editor-fold desc="Open Box">

    /**
     * Gets open box product listings by SKU (with default response configuration)
     *
     * @param sku The SKU to show open box options for
     * @return The open box response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#buying-options-open-box-api">Best Buy APIs Documentation</a>
     */
    public Map openBox(int sku)
            throws ServiceException, AuthorizationException {
        return openBox(sku, new ResponseConfig());
    }

    /**
     * Gets open box product listings by SKU (with custom response configuration)
     *
     * @param sku            The SKU to show open box options for
     * @param responseConfig The configuration for the response
     * @return The open box response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#buying-options-open-box-api">Best Buy APIs Documentation</a>
     */
    public Map openBox(int sku, ResponseConfig responseConfig)
            throws ServiceException, AuthorizationException {
        return doRequest(URL_BETA, "/products/" + sku + "/openBox", responseConfig);
    }

    /**
     * Gets open box product listings by multiple SKUs (with default response configuration)
     *
     * @param skus The SKUs to show open box options for
     * @return The open box response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#buying-options-open-box-api">Best Buy APIs Documentation</a>
     */
    public Map openBox(int[] skus)
            throws ServiceException, AuthorizationException {
        return openBox(skus, new ResponseConfig());
    }

    /**
     * Gets open box product listings by multiple SKUs (with custom response configuration)
     *
     * @param skus           The SKUs to show open box options for
     * @param responseConfig The configuration for the response
     * @return The open box response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#buying-options-open-box-api">Best Buy APIs Documentation</a>
     */
    public Map openBox(int[] skus, ResponseConfig responseConfig)
            throws ServiceException, AuthorizationException {
        String skuNos = Arrays.toString(skus);
        return openBox("sku in(" + skuNos.substring(1, skuNos.length() - 1) + ")", responseConfig);
    }

    /**
     * Gets open box product listings matching the query (with default response configuration)
     *
     * @param search The query to find products to show open box options for
     * @return The open box response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#buying-options-open-box-api">Best Buy APIs Documentation</a>
     */
    public Map openBox(String search)
            throws ServiceException, AuthorizationException {
        return openBox(search, new ResponseConfig());
    }

    /**
     * Gets open box product listings matching the query (with custom response configuration)
     *
     * @param search         The query to find products to show open box options for
     * @param responseConfig The configuration for the response
     * @return The open box response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#buying-options-open-box-api">Best Buy APIs Documentation</a>
     */
    public Map openBox(String search, ResponseConfig responseConfig)
            throws ServiceException, AuthorizationException {
        return doRequest(URL_BETA, "/products/openBox(" + search + ")", responseConfig);
    }

    /**
     * Gets all open box product (with default response configuration)
     *
     * @return The open box response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#buying-options-open-box-api">Best Buy APIs Documentation</a>
     */
    public Map openBox()
            throws ServiceException, AuthorizationException {
        return openBox(new ResponseConfig());
    }

    /**
     * Gets all open box product (with custom response configuration)
     *
     * @param responseConfig The configuration for the response
     * @return The open box response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#buying-options-open-box-api">Best Buy APIs Documentation</a>
     */
    public Map openBox(ResponseConfig responseConfig)
            throws ServiceException, AuthorizationException {
        return doRequest(URL_BETA, "/products/openBox", responseConfig);
    }
// </editor-fold>

// <editor-fold desc="Products">

    /**
     * Gets a product by its SKU (with default response configuration)
     *
     * @param sku The SKU of the product to retrieve
     * @return The product response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#products-api">Best Buy APIs Documentation</a>
     */
    public Map products(int sku)
            throws ServiceException, AuthorizationException {
        return products(sku, new ResponseConfig());
    }

    /**
     * Gets a product by its SKU (with custom response configuration)
     *
     * @param sku            The SKU of the product to retrieve
     * @param responseConfig The configuration for the response
     * @return The product response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#products-api">Best Buy APIs Documentation</a>
     */
    public Map products(int sku, ResponseConfig responseConfig)
            throws ServiceException, AuthorizationException {
        return doRequest(URL_V1, "/products/" + Integer.toString(sku) + ".json", responseConfig);
    }

    /**
     * Gets a product matching the query (with default response configuration)
     *
     * @param search The query to find products by
     * @return The product response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#products-api">Best Buy APIs Documentation</a>
     */
    public Map products(String search)
            throws ServiceException, AuthorizationException {
        return products(search, new ResponseConfig());
    }

    /**
     * Gets a product matching the query (with custom response configuration)
     *
     * @param search         The query to find products by
     * @param responseConfig The configuration for the response
     * @return The product response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#products-api">Best Buy APIs Documentation</a>
     */
    public Map products(String search, ResponseConfig responseConfig)
            throws ServiceException, AuthorizationException {
        return doRequest(URL_V1, "/products(" + search + ")", responseConfig);
    }

    /**
     * Gets all products (with default response configuration)
     *
     * @return The product response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#products-api">Best Buy APIs Documentation</a>
     */
    public Map products()
            throws ServiceException, AuthorizationException {
        return products(new ResponseConfig());
    }

    /**
     * Gets all products (with custom response configuration)
     *
     * @param responseConfig The configuration for the response
     * @return The product response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#products-api">Best Buy APIs Documentation</a>
     */
    public Map products(ResponseConfig responseConfig)
            throws ServiceException, AuthorizationException {
        return doRequest(URL_V1, "/products", responseConfig);
    }
// </editor-fold>

// <editor-fold desc="Recommendations">

    /**
     * Gets all recommendations by type for a product (with default response configuration)
     *
     * @param type The type of recommendation to retrieve
     * @param sku  The SKU of the product to find
     * @return The recommendation response from Best Buy APIs
     * @throws ServiceException         When there's an issue communicating with the service
     * @throws AuthorizationException   If your API Key is not valid
     * @throws InvalidArgumentException If the type is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#recommendations-api">Best Buy APIs Documentation</a>
     */
    public Map recommendations(RecommendationType type, int sku)
            throws ServiceException, AuthorizationException, InvalidArgumentException {
        return recommendations(type, sku, new ResponseConfig());
    }

    /**
     * Gets all recommendations by type for a product (with custom response configuration)
     *
     * @param type           The type of recommendation to retrieve
     * @param sku            The SKU of the product to find
     * @param responseConfig The configuration for the response
     * @return The recommendation response from Best Buy APIs
     * @throws ServiceException         When there's an issue communicating with the service
     * @throws AuthorizationException   If your API Key is not valid
     * @throws InvalidArgumentException If the type is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#recommendations-api">Best Buy APIs Documentation</a>
     */
    public Map recommendations(RecommendationType type, int sku, ResponseConfig responseConfig)
            throws ServiceException, AuthorizationException, InvalidArgumentException {
        if (type.equals(RecommendationType.ALSOVIEWED)) {
            return doRequest(URL_BETA, "/products/" + Integer.toString(sku) + "/" + type.getEndpoint(), responseConfig);
        }

        throw new InvalidArgumentException("For `RecommendationType.SIMILAR` & `RecommendationType.ALSOVIEWED`, a SKU is required");
    }

    /**
     * Gets all recommendations by type in a category (with default response configuration)
     *
     * @param type       The type of recommendation to retrieve
     * @param categoryId The category to find recommendations within
     * @return The recommendation response from Best Buy APIs
     * @throws ServiceException         When there's an issue communicating with the service
     * @throws AuthorizationException   If your API Key is not valid
     * @throws InvalidArgumentException If the type is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#recommendations-api">Best Buy APIs Documentation</a>
     */
    public Map recommendations(RecommendationType type, String categoryId)
            throws ServiceException, AuthorizationException, InvalidArgumentException {
        return recommendations(type, categoryId, new ResponseConfig());
    }

    /**
     * Gets all recommendations by type in a category (with custom response configuration)
     *
     * @param type           The type of recommendation to retrieve
     * @param categoryId     The category to find recommendations within
     * @param responseConfig The configuration for the response
     * @return The recommendation response from Best Buy APIs
     * @throws ServiceException         When there's an issue communicating with the service
     * @throws AuthorizationException   If your API Key is not valid
     * @throws InvalidArgumentException If the type is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#recommendations-api">Best Buy APIs Documentation</a>
     */
    public Map recommendations(RecommendationType type, String categoryId, ResponseConfig responseConfig)
            throws ServiceException, AuthorizationException, InvalidArgumentException {
        if (type.equals(RecommendationType.TRENDING) || type.equals(RecommendationType.MOSTVIEWED)) {
            return doRequest(URL_BETA, "/products/" + type.getEndpoint() + "(categoryId=" + categoryId + ")", responseConfig);
        }

        throw new InvalidArgumentException("Only `RecommendationType.TRENDING` & `RecommendationType.MOSTVIEWED` work for categories");
    }

    /**
     * Gets all recommendations by type (with default response configuration)
     *
     * @param type The type of recommendation to retrieve
     * @return The recommendation response from Best Buy APIs
     * @throws ServiceException         When there's an issue communicating with the service
     * @throws AuthorizationException   If your API Key is not valid
     * @throws InvalidArgumentException If the type is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#recommendations-api">Best Buy APIs Documentation</a>
     */
    public Map recommendations(RecommendationType type)
            throws ServiceException, AuthorizationException, InvalidArgumentException {
        return recommendations(type, new ResponseConfig());
    }

    /**
     * Gets all recommendations by type (with custom response configuration)
     *
     * @param type           The type of recommendation to retrieve
     * @param responseConfig The configuration for the response
     * @return The recommendation response from Best Buy APIs
     * @throws ServiceException         When there's an issue communicating with the service
     * @throws AuthorizationException   If your API Key is not valid
     * @throws InvalidArgumentException If the type is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#recommendations-api">Best Buy APIs Documentation</a>
     */
    public Map recommendations(RecommendationType type, ResponseConfig responseConfig)
            throws ServiceException, AuthorizationException, InvalidArgumentException {
        if (type.equals(RecommendationType.TRENDING) || type.equals(RecommendationType.MOSTVIEWED)) {
            return doRequest(URL_BETA, "/products/" + type.getEndpoint(), responseConfig);
        }

        throw new InvalidArgumentException("Only `RecommendationType.TRENDING` & `RecommendationType.MOSTVIEWED` work globally");
    }
// </editor-fold>

// <editor-fold desc="Reviews">

    /**
     * Gets a review by its ID (with default response configuration)
     *
     * @param reviewId The ID of the review to retrieve
     * @return The review response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/">Best Buy APIs Documentation</a>
     */
    public Map reviews(int reviewId)
            throws ServiceException, AuthorizationException {
        return reviews(reviewId, new ResponseConfig());
    }

    /**
     * Gets a review by its ID (with custom response configuration)
     *
     * @param reviewId       The ID of the review to retrieve
     * @param responseConfig The configuration for the response
     * @return The review response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/">Best Buy APIs Documentation</a>
     */
    public Map reviews(int reviewId, ResponseConfig responseConfig)
            throws ServiceException, AuthorizationException {
        return doRequest(URL_V1, "/reviews/" + Integer.toString(reviewId) + ".json", responseConfig);
    }

    /**
     * Gets reviews matching the query (with default response configuration)
     *
     * @param search The query to find reviews
     * @return The review response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/">Best Buy APIs Documentation</a>
     */
    public Map reviews(String search)
            throws ServiceException, AuthorizationException {
        return doRequest(URL_V1, "/reviews(" + search + ")", new ResponseConfig());
    }

    /**
     * Gets reviews matching the query (with custom response configuration)
     *
     * @param search         The query to find reviews
     * @param responseConfig The configuration for the response
     * @return The review response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/">Best Buy APIs Documentation</a>
     */
    public Map reviews(String search, ResponseConfig responseConfig)
            throws ServiceException, AuthorizationException {
        return doRequest(URL_V1, "/reviews(" + search + ")", responseConfig);
    }

    /**
     * Gets all reviews (with default response configuration)
     *
     * @return The review response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/">Best Buy APIs Documentation</a>
     */
    public Map reviews()
            throws ServiceException, AuthorizationException {
        return reviews(new ResponseConfig());
    }

    /**
     * Gets all reviews (with custom response configuration)
     *
     * @param responseConfig The configuration for the response
     * @return The review response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/">Best Buy APIs Documentation</a>
     */
    public Map reviews(ResponseConfig responseConfig)
            throws ServiceException, AuthorizationException {
        return doRequest(URL_V1, "/reviews", responseConfig);
    }
// </editor-fold>

// <editor-fold desc="Stores">

    /**
     * Gets a store by its ID (with custom response configuration)
     *
     * @param storeId The ID of the store to find
     * @return The store response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#stores-api">Best Buy APIs Documentation</a>
     */
    public Map stores(int storeId)
            throws ServiceException, AuthorizationException {
        return stores(storeId, new ResponseConfig());
    }

    /**
     * Gets a store by its ID (with custom response configuration)
     *
     * @param storeId        The ID of the store to find
     * @param responseConfig The configuration for the response
     * @return The store response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#stores-api">Best Buy APIs Documentation</a>
     */
    public Map stores(int storeId, ResponseConfig responseConfig)
            throws ServiceException, AuthorizationException {
        return doRequest(URL_V1, "/stores/" + Integer.toString(storeId) + ".json", responseConfig);
    }

    /**
     * Gets stores matching the query (with default response configuration)
     *
     * @param search The query to find stores
     * @return The store response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#stores-api">Best Buy APIs Documentation</a>
     */
    public Map stores(String search)
            throws ServiceException, AuthorizationException {
        return stores(search, new ResponseConfig());
    }

    /**
     * Gets stores matching the query (with custom response configuration)
     *
     * @param search         The query to find stores
     * @param responseConfig The configuration for the response
     * @return The store response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#stores-api">Best Buy APIs Documentation</a>
     */
    public Map stores(String search, ResponseConfig responseConfig)
            throws ServiceException, AuthorizationException {
        return doRequest(URL_V1, "/stores(" + search + ")", responseConfig);
    }

    /**
     * Gets all stores (with custom response configuration)
     *
     * @return The store response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#stores-api">Best Buy APIs Documentation</a>
     */
    public Map stores()
            throws ServiceException, AuthorizationException {
        return stores(new ResponseConfig());
    }

    /**
     * Gets all stores (with custom response configuration)
     *
     * @param responseConfig The configuration for the response
     * @return The store response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#stores-api">Best Buy APIs Documentation</a>
     */
    public Map stores(ResponseConfig responseConfig)
            throws ServiceException, AuthorizationException {
        return doRequest(URL_V1, "/stores", responseConfig);
    }
// </editor-fold>

// <editor-fold desc="Warranties">

    /**
     * Gets warranties for a product identified by SKU (with default response configuration)
     *
     * @param sku The SKU of the product to find warranties for
     * @return The store response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#stores-api">Best Buy APIs Documentation</a>
     */
    public Map warranties(int sku) throws ServiceException, AuthorizationException {
        return warranties(sku, new ResponseConfig());
    }

    /**
     * Gets warranties for a product identified by SKU (with custom response configuration)
     *
     * @param sku            The SKU of the product to find warranties for
     * @param responseConfig The configuration for the response
     * @return The store response from Best Buy APIs
     * @throws ServiceException       When there's an issue communicating with the service
     * @throws AuthorizationException If your API Key is not valid
     * @see <a href="https://bestbuyapis.github.io/api-documentation/#stores-api">Best Buy APIs Documentation</a>
     */
    public Map warranties(int sku, ResponseConfig responseConfig) throws ServiceException, AuthorizationException {
        return doRequest(URL_V1, "/products/" + Integer.toString(sku) + "/warranties.json", responseConfig);
    }
// </editor-fold>

// <editor-fold desc="Internal">

    /**
     * Build the URL to make the request to
     *
     * @param root           The root (protocol+host)
     * @param path           The path (endpoint)
     * @param responseConfig The configuration for the response
     * @return The built URL
     * @throws AuthorizationException When there's an issue communicating with the service
     * @throws MalformedURLException  If there are errors building the URL
     */
    protected URL buildUrl(String root, String path, ResponseConfig responseConfig)
            throws AuthorizationException, MalformedURLException {
        // Verify the client has a apiKey
        if (this.config.apiKey == null || this.config.apiKey.isEmpty()) {
            throw new AuthorizationException(
                    "A Best Buy developer API apiKey is required. Register for one at developer.bestbuy.com, "
                            + "call `new BestBuy.Client(YOUR_API_KEY)`, or specify a BBY_API_KEY system environment variable."
            );
        }

        // apply the apiKey to the config
        responseConfig.setApiKey(config.getApiKey());

        // If we're loading just a single resource ({sku}.json), remove the format from the querystring--it'll 400
        responseConfig.setFormat(path.endsWith(".json") ? "" : "json");

        // generate the URL
        return new URL(root + path.replace(" ", "%20") + "?" + responseConfig.toString());
    }

    /**
     * Perform the actual request to the service
     *
     * @param root           The root (protocol+host)
     * @param path           The path (endpoint)
     * @param responseConfig The configuration for the response
     * @return The decoded JSON
     * @throws AuthorizationException When there's an issue communicating with the service
     * @throws ServiceException       If your API Key is not valid
     */
    protected Map doRequest(String root, String path, ResponseConfig responseConfig)
            throws AuthorizationException, ServiceException {
        try {
            // build the URL
            URLConnection connection = buildUrl(root, path, responseConfig).openConnection();
            // get the headers from the config & make sure they'll be sent
            Map<String, String> httpHeaders = config.getHttpHeaders();
            for (String key : httpHeaders.keySet()) {
                connection.setRequestProperty(key, httpHeaders.get(key));
            }

            // read the stream
            BufferedReader contentReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            // set up the responsebuilder (we'll read the stream into a string)
            StringBuilder responseBuilder = new StringBuilder();

            // read all the content into a string
            String line;
            while ((line = contentReader.readLine()) != null) {
                responseBuilder.append(line);
            }
            contentReader.close();

            // have GSON parse the JSON into a map
            Type type = new TypeToken<Map<String, Object>>() {
            }.getType();
            return (new Gson()).fromJson(responseBuilder.toString(), type);
        } catch (IOException e) {
            throw new ServiceException("An error occurred when communicating with the service");
        }
    }
// </editor-fold>
}