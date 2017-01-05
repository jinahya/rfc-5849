/*
 * Copyright 2015 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jinahya.rfc5849;

import static com.github.jinahya.rfc5849._Percent.encodePercent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * A builder for generating signature base strings.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see <a href="https://tools.ietf.org/html/rfc5849#section-3.4.1">3.4.1.
 * Signature Base String (RFC 5849)</a>
 */
public class OAuthBaseString {//implements Builder<String> {

    private static final String PROTOCOL_PARAMETER_PREFIX = "oauth_";

    // -------------------------------------------------------------------------
    /**
     * Builds the signature base string.
     *
     * @return the signature base string.
     */
    public String get() {
        if (httpMethod == null) {
            throw new IllegalStateException("no httpMethod set");
        }
        if (baseUri == null) {
            throw new IllegalStateException("no baseUri set");
        }
//        if (!requestParameters().containsKey(OAuthConstants.OAUTH_NONCE)) {
//            throw new IllegalStateException(
//                    "no " + OAuthConstants.OAUTH_NONCE + " set");
//        }
//        if (!requestParameters().containsKey(OAuthConstants.OAUTH_TIMESTAMP)) {
//            throw new IllegalStateException(
//                    "no " + OAuthConstants.OAUTH_TIMESTAMP + " set");
//        }
        final Map<String, List<String>> encodedRequestParameters
                = new TreeMap<String, List<String>>();
        for (final Entry<String, List<String>> entry
             : requestParameters().entrySet()) {
            final String key = entry.getKey();
            final String key_ = encodePercent(key);
            final List<String> values = entry.getValue();
            final List<String> values_ = new ArrayList<String>(values.size());
            for (final String value : values) {
                values_.add(encodePercent(value));
            }
            Collections.sort(values_);
            encodedRequestParameters.put(key_, values_);
        }
        final StringBuilder builder = new StringBuilder();
        for (final Entry<String, List<String>> entry
             : encodedRequestParameters.entrySet()) {
            final String key = entry.getKey();
            for (final String value : entry.getValue()) {
                if (builder.length() > 0) {
                    builder.append("&");
                }
                builder.append(key).append("=").append(value);
            }
        }
        return encodePercent(httpMethod)
               + "&"
               + encodePercent(baseUri)
               + "&"
               + encodePercent(builder.toString());
    }

    // -------------------------------------------------------------- httpMethod
    /**
     * Sets a value for {@code httpMethod}.
     *
     * @param httpMethod the value of {@code httpMethod}.
     * @return this instance.
     * @see <a href="https://tools.ietf.org/html/rfc5849#section-3.4.1">3.4.1.
     * Signature Base String (RFC 5849)</a>
     */
    public OAuthBaseString httpMethod(final String httpMethod) {
        if (httpMethod == null) {
            throw new NullPointerException("null httpMethod");
        }
        this.httpMethod = httpMethod.toUpperCase();
        return this;
    }

    // ----------------------------------------------------------------- baseUri
    /**
     * Set a value for {@code baseUri}.
     *
     * @param baseUri the value for {@code baseUri}.
     * @return this instance.
     * @see <a href="https://tools.ietf.org/html/rfc5849#section-3.4.1">3.4.1.
     * Signature Base String (RFC 5849)</a>
     */
    public OAuthBaseString baseUri(final String baseUri) {
        this.baseUri = baseUri;
        return this;
    }

    // ------------------------------------------------------- requestParameters
    /**
     * Returns the request parameters.
     *
     * @return the request parameters.
     */
    Map<String, List<String>> requestParameters() {
        if (requestParameters == null) {
            requestParameters = new HashMap<String, List<String>>();
        }
        return requestParameters;
    }

    /**
     * Adds a request parameter.
     *
     * @param key the key of the request parameter.
     * @param value the value of the request parameter.
     * @return this instance.
     */
    public OAuthBaseString requestParameter(final String key,
                                            final String value) {
        if (key == null) {
            throw new NullPointerException("null key");
        }
        if (value == null) {
            throw new NullPointerException("null value");
        }
        List<String> values = requestParameters().get(key);
        if (values == null) {
            values = new ArrayList<String>();
            requestParameters().put(key, values);
        }
        if (key.startsWith(PROTOCOL_PARAMETER_PREFIX)) {
            values.clear();
        }
        values.add(value);
        return this;
    }

    // ---------------------------------------------------------- queryParameter
    /**
     * Adds a query parameter.
     *
     * @param key key
     * @param value value
     * @return this instance
     * @throws IllegalArgumentException if the {@code key} start with
     * {@value #PROTOCOL_PARAMETER_PREFIX}.
     */
    public OAuthBaseString queryParameter(final String key,
                                          final String value) {
        if (key == null) {
            throw new NullPointerException("null key");
        }
        if (key.startsWith(PROTOCOL_PARAMETER_PREFIX)) {
            throw new IllegalArgumentException(
                    "query parameter's key(" + key + ") starts with "
                    + PROTOCOL_PARAMETER_PREFIX);
        }
        return requestParameter(key, value);
    }

    // ------------------------------------------------------- protocolParameter
    /**
     * Sets a protocol parameter.
     *
     * @param key protocol parameter key
     * @param value protocol parameter value.
     * @return this instance
     * @throws IllegalArgumentException if the {@code key} does not start with
     * {@value #PROTOCOL_PARAMETER_PREFIX}.
     */
    public OAuthBaseString protocolParameter(final String key,
                                             final String value) {
        if (key == null) {
            throw new NullPointerException("null key");
        }
        if (!key.startsWith(PROTOCOL_PARAMETER_PREFIX)) {
            throw new IllegalArgumentException(
                    "key(" + key + ") doesn't start with "
                    + PROTOCOL_PARAMETER_PREFIX);
        }
        return requestParameter(key, value);
    }

    /**
     * Puts protocol parameters to given map.
     *
     * @param map the map to which protocol parameters are put.
     * @return given map.
     */
    Map<String, String> pickProtocolParameters(final Map<String, String> map) {
        for (final Entry<String, List<String>> entry
             : requestParameters().entrySet()) {
            final String key = entry.getKey();
            if (!key.startsWith(PROTOCOL_PARAMETER_PREFIX)) {
                continue;
            }
            map.put(key, entry.getValue().get(0));
        }
        return map;
    }

    // -------------------------------------------------------- entityParameters
    /**
     * Adds an entity parameter. This method simply invokes
     * {@link #queryParameter(java.lang.String, java.lang.String)} with given
     * arguments.
     *
     * @param key key of the entity parameter
     * @param value value of the entity parameter
     * @return this instance
     */
    public OAuthBaseString entityParameter(final String key,
                                           final String value) {
        return queryParameter(key, value);
    }

    // ----------------------------------------------------------- oauthCallback
    /**
     * Set a protocol parameter value for
     * {@value com.github.jinahya.rfc5849.OAuthConstants#OAUTH_CALLBACK}.
     *
     * @param oauthCallback the protocol parameter value for
     * {@value com.github.jinahya.rfc5849.OAuthConstants#OAUTH_CALLBACK}
     * @return this instance
     */
    public OAuthBaseString oauthCallback(final String oauthCallback) {
        return protocolParameter(OAuthConstants.OAUTH_CALLBACK,
                                 oauthCallback);
    }

    // -------------------------------------------------------- oauthConsumerKey
    /**
     * Sets a protocol parameter value for
     * {@value com.github.jinahya.rfc5849.OAuthConstants#OAUTH_CONSUMER_KEY}.
     *
     * @param oauthConsumerKey the protocol parameter value for
     * {@value com.github.jinahya.rfc5849.OAuthConstants#OAUTH_CONSUMER_KEY}
     * @return this instance.
     */
    public OAuthBaseString oauthConsumerKey(final String oauthConsumerKey) {
        return protocolParameter(OAuthConstants.OAUTH_CONSUMER_KEY,
                                 oauthConsumerKey);
    }

    // -------------------------------------------------------------- oauthNonce
    /**
     * Sets a protocol parameter value for
     * {@value com.github.jinahya.rfc5849.OAuthConstants#OAUTH_NONCE}
     *
     * @param oauthNonce the protocol parameter value for
     * {@value com.github.jinahya.rfc5849.OAuthConstants#OAUTH_NONCE}
     * @return this instance.
     */
    public OAuthBaseString oauthNonce(final String oauthNonce) {
        return protocolParameter(OAuthConstants.OAUTH_NONCE, oauthNonce);
    }

    // ---------------------------------------------------- oauthSignatureMethod
    /**
     * Sets a protocol parameter value for
     * {@value com.github.jinahya.rfc5849.OAuthConstants#OAUTH_SIGNATURE_METHOD}.
     *
     * @param oauthSignatureMethod the protocol parameter value for
     * {@value com.github.jinahya.rfc5849.OAuthConstants#OAUTH_SIGNATURE_METHOD}
     * @return return this
     */
    OAuthBaseString oauthSignatureMethod(final String oauthSignatureMethod) {
        return protocolParameter(OAuthConstants.OAUTH_SIGNATURE_METHOD,
                                 oauthSignatureMethod);
    }

    // ---------------------------------------------------------- oauthTimestamp
    /**
     * Sets a protocol parameter value for
     * {@value com.github.jinahya.rfc5849.OAuthConstants#OAUTH_TIMESTAMP}.
     *
     * @param oauthTimestamp the protocol parameter value for
     * {@value com.github.jinahya.rfc5849.OAuthConstants#OAUTH_TIMESTAMP}
     * @return this instance
     */
    public OAuthBaseString oauthTimestamp(final String oauthTimestamp) {
        return protocolParameter(OAuthConstants.OAUTH_TIMESTAMP,
                                 oauthTimestamp);
    }

    // -------------------------------------------------------------- oauthToken
    /**
     * Sets a protocol parameter value for
     * {@value com.github.jinahya.rfc5849.OAuthConstants#OAUTH_TOKEN}.
     *
     * @param oauthToken the protocol parameter value for
     * {@value com.github.jinahya.rfc5849.OAuthConstants#OAUTH_TOKEN}
     * @return this instance
     */
    public OAuthBaseString oauthToken(final String oauthToken) {
        return protocolParameter(OAuthConstants.OAUTH_TOKEN, oauthToken);
    }

    // ------------------------------------------------------------ oauthVersion
    /**
     * Sets a protocol parameter value for
     * {@value com.github.jinahya.rfc5849.OAuthConstants#OAUTH_VERSION}.
     *
     * @param oauthVersion the protocol parameter value for
     * {@value com.github.jinahya.rfc5849.OAuthConstants#OAUTH_VERSION}
     * @return this instance
     */
    public OAuthBaseString oauthVersion(final String oauthVersion) {
        return protocolParameter(OAuthConstants.OAUTH_VERSION, oauthVersion);
    }

    // ----------------------------------------------------------- oauthVerifier
    /**
     * Sets a protocol parameter value for
     * {@value com.github.jinahya.rfc5849.OAuthConstants#OAUTH_VERIFIER}.
     *
     * @param oauthVerifier the protocol parameter value for
     * {@value com.github.jinahya.rfc5849.OAuthConstants#OAUTH_VERIFIER}
     * @return this instance
     */
    public OAuthBaseString oauthVerifier(final String oauthVerifier) {
        return protocolParameter(OAuthConstants.OAUTH_VERIFIER, oauthVerifier);
    }

    // -------------------------------------------------------------------------
    private String httpMethod;

    private String baseUri;

    private Map<String, List<String>> requestParameters;
}
