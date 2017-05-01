/**
 * Best Buy SDK
 *
 * High level Java client for the Best Buy API
 */
package bestbuy;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * The configuration for formatting the response
 */
public class ResponseConfig {
    /**
     * Your API Key
     *
     * @see <a href="https://developer.bestbuy.com/">developer.bestbuy.com</a>
     */
    protected String apiKey = "";

    /**
     * The format of the response (will always be "json")
     */
    protected String format = "json";

    /**
     * Facets to return from the service
     */
    protected String facets;

    /**
     * The page to request
     */
    protected int page;

    /**
     * The results per page
     */
    protected int pageSize;

    /**
     * Fields to show for each result
     */
    protected String show;

    /**
     * Sort the results from the service
     */
    protected String sort;

    /**
     * Gets {@link ResponseConfig#apiKey}
     * 
     * @return Your API key
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Sets {@link ResponseConfig#apiKey}
     *
     * @param apiKey Your API key
     * @return `this` for chaining
     */
    public ResponseConfig setApiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    /**
     * Gets {@link ResponseConfig#format}
     *
     * @return The selected format
     */
    public String getFormat() {
        return format;
    }

    /**
     * Sets {@link ResponseConfig#format}
     *
     * @param format The format for the response (only "json" is allowed)
     * @return `this` for chaining
     */
    public ResponseConfig setFormat(String format) {
        this.format = format;
        return this;
    }

    /**
     * Gets {@link ResponseConfig#facets}
     *
     * @return The returned facets
     */
    public String getFacets() {
        return facets;
    }

    /**
     * Sets {@link ResponseConfig#facets}
     *
     * @param facets The facets to return
     * @return `this` for chaining
     */
    public ResponseConfig setFacets(String facets) {
        this.facets = facets;
        return this;
    }

    /**
     * Gets {@link ResponseConfig#page}
     *
     * @return The current page
     */
    public int getPage() {
        return page;
    }

    /**
     * Sets {@link ResponseConfig#page}
     *
     * @param page The page to request
     * @return `this` for chaining
     */
    public ResponseConfig setPage(int page) {
        this.page = page;
        return this;
    }

    /**
     * Gets {@link ResponseConfig#pageSize}
     *
     * @return The requested page size
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Sets {@link ResponseConfig#pageSize}
     *
     * @param pageSize The number of results to return
     * @return `this` for chaining
     */
    public ResponseConfig setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    /**
     * Gets {@link ResponseConfig#show}
     *
     * @return The fields to show in the response
     */
    public String getShow() {
        return show;
    }

    /**
     * Sets {@link ResponseConfig#show}
     *
     * @param show The fields to return
     * @return `this` for chaining
     */
    public ResponseConfig setShow(String show) {
        this.show = show;
        return this;
    }

    /**
     * Gets {@link ResponseConfig#sort}
     *
     * @return The sort params for the response
     */
    public String getSort() {
        return sort;
    }

    /**
     * Sets {@link ResponseConfig#sort}
     *
     * @param sort The sort params
     * @return `this` for chaining
     */
    public ResponseConfig setSort(String sort) {
        this.sort = sort;
        return this;
    }

    /**
     * Formats this into a valid querystring
     *
     * @return A querystring for Best Buy APIs
     */
    public String toString() {
        // everything must have an API key
        StringBuilder query = new StringBuilder("apiKey=" + getApiKey());

        // go through each of the params
        String[][] parameters = new String[][]{
                {"facets", getFacets()},
                {"format", getFormat()},
                {"page", Integer.toString(getPage())},
                {"pageSize", Integer.toString(getPageSize())},
                {"show", getShow()},
                {"sort", getSort()}
        };
        for (String[] parameter : parameters) {
            // If there's a value for a parameter, make sure to add it to the querystring
            if (parameter[1] != null && !parameter[1].isEmpty() && !parameter[1].equals("0")) {
                try {
                    query.append("&").append(parameter[0]).append("=").append(URLEncoder.encode(parameter[1], "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                }
            }
        }

        return query.toString();
    }
}
