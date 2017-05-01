/**
 * Best Buy SDK
 *
 * High level Java client for the Best Buy API
 */
package bestbuy;

import java.util.HashMap;
import java.util.Map;

/**
 * The configuration for the Best Buy APIs client
 */
public class ClientConfig {
    /**
     * Your API Key
     *
     * @see <a href="https://developer.bestbuy.com/">developer.bestbuy.com</a>
     */
    protected String apiKey;

    /**
     * Whether to run the client in debug mode
     */
    protected boolean debug;

    /**
     * The headers to send along with the request to the API
     */
    protected Map<String, String> httpHeaders = new HashMap<>();

    /**
     * Create a new config with your apiKey
     *
     * @param apiKey Your API apiKey
     */
    public ClientConfig(String apiKey) {
        this(apiKey, false);
    }

    /**
     * Create a new config with your apiKey and debug flag
     *
     * @param apiKey Your API apiKey
     * @param debug Whether to set the client to debug mode
     */
    public ClientConfig(String apiKey, boolean debug) {
        this(apiKey, debug, new HashMap<String, String>());
    }

    /**
     * Create a new config with your apiKey, debug flag, and headers for each request
     *
     * @param apiKey Your API apiKey
     * @param debug Whether to set the client to debug mode
     * @param httpHeaders HTTP headers to send with every request
     */
    public ClientConfig(String apiKey, boolean debug, Map<String, String> httpHeaders) {
        this.apiKey = apiKey;
        this.debug = debug;
        this.httpHeaders = httpHeaders;

        this.httpHeaders.put("User-Agent", "bestbuy-sdk-php/1.0.0;java");
    }

    /**
     * Gets {@link ClientConfig#apiKey}
     *
     * @return Your API key
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Sets {@link ClientConfig#apiKey}
     *
     * @param apiKey Your API key
     * @return `this` for chaining
     */
    public ClientConfig setApiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    /**
     * Gets {@link ClientConfig#debug}
     *
     * @return Whether to debug the client
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * Sets {@link ClientConfig#debug}
     *
     * @param debug Whether to run in debug mode
     * @return `this` for chaining
     */
    public ClientConfig setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    /**
     * Adds a header to {@link ClientConfig#httpHeaders}
     *
     * @param name The name of the header
     * @param value The value of the header
     * @return `this` for chaining
     */
    public ClientConfig addHttpHeader(String name, String value) {
        httpHeaders.put(name, value);
        return this;
    }

    /**
     * Gets {@link ClientConfig#debug}
     *
     * @return The HTTP headers that will be sent with the request
     */
    public Map<String, String> getHttpHeaders() {
        return httpHeaders;
    }

    /**
     * Sets {@link ClientConfig#httpHeaders}
     *
     * @param httpHeaders The headers to send with the request
     * @return `this` for chaining
     */
    public ClientConfig setHttpHeaders(Map<String, String> httpHeaders) {
        this.httpHeaders = httpHeaders;
        return this;
    }
}