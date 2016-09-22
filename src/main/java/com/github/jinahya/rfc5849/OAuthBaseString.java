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

    static final String PROTOCOL_PARAMETER_PREFIX = "oauth_";

    static OAuthBaseString of(final String prebuilt) {
        return new OAuthBaseString() {
            @Override
            public String build() {
                return prebuilt;
            }
        };
    }

    // -------------------------------------------------------------------------
//    @Override
    public String build() {//throws Exception {
        if (httpMethod == null) {
            throw new IllegalStateException("no httpMethod set");
        }
        if (baseUri == null) {
            throw new IllegalStateException("no baseUri set");
        }
        if (!map().containsKey(OAuthConstants.OAUTH_NONCE)) {
            throw new IllegalStateException(
                    "no " + OAuthConstants.OAUTH_NONCE);
        }
        if (!map().containsKey(OAuthConstants.OAUTH_TIMESTAMP)) {
            throw new IllegalStateException(
                    "no " + OAuthConstants.OAUTH_TIMESTAMP);
        }
        final Map<String, List<String>> encoded
                = new TreeMap<String, List<String>>();
        for (final Entry<String, List<String>> entry : map().entrySet()) {
            final String key = entry.getKey();
            final String encodedKey = encodePercent(key);
            final List<String> values = entry.getValue();
            final List<String> encodedValues
                    = new ArrayList<String>(values.size());
            for (final String value : values) {
                encodedValues.add(encodePercent(value));
            }
            Collections.sort(encodedValues);
            encoded.put(encodedKey, encodedValues);
        }
        final StringBuilder builder = new StringBuilder();
        for (final Entry<String, List<String>> entry : encoded.entrySet()) {
            final String key = entry.getKey();
            for (final String value : entry.getValue()) {
                if (builder.length() > 0) {
                    builder.append("&");
                }
                builder.append(key).append("=").append(value);
            }
        }
        return encodePercent(httpMethod)
               + "&" + encodePercent(baseUri)
               + "&" + encodePercent(builder.toString());
    }

    // --------------------------------------------------------------------- map
    Map<String, List<String>> map() {
        if (map == null) {
            map = new HashMap<String, List<String>>();
        }
        return map;
    }

    private void add(final String key, final String value) {
        if (key == null) {
            throw new NullPointerException("null key");
        }
        if (value == null) {
            throw new NullPointerException("null value");
        }
        List<String> values = map().get(key);
        if (values == null) {
            values = new ArrayList<String>();
            map().put(key, values);
        }
        values.add(value);
    }

    private void put(final String key, final String value) {
        map().remove(key);
        add(key, value);
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

    // ---------------------------------------------------------- queryParameter
    /**
     * Adds a query parameter.
     *
     * @param key key
     * @param value value
     * @return this instance
     */
    public OAuthBaseString queryParameter(final String key,
                                          final String value) {
        if (key == null) {
            throw new NullPointerException("null key");
        }
        if (value == null) {
            throw new NullPointerException("null value");
        }
        if (key.startsWith(PROTOCOL_PARAMETER_PREFIX)) {
            throw new IllegalArgumentException(
                    "query parameter's key(" + key + ") starts with "
                    + PROTOCOL_PARAMETER_PREFIX);
        }
        add(key, value);
        return this;
    }

    // ------------------------------------------------------- protocolParameter
    public OAuthBaseString protocolParameter(final String key,
                                             final String value) {
        if (key == null) {
            throw new NullPointerException("null key");
        }
        if (value == null) {
            throw new NullPointerException("null value");
        }
        if (!key.startsWith(PROTOCOL_PARAMETER_PREFIX)) {
            throw new IllegalArgumentException(
                    "key(" + key + ") doesn't start with "
                    + PROTOCOL_PARAMETER_PREFIX);
        }
        put(key, value);
        return this;
    }

    // -------------------------------------------------------- entityParameters
    /**
     * Adds an entity parameter.
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
    public OAuthBaseString oauthSignatureMethod(
            final String oauthSignatureMethod) {
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
        return protocolParameter(OAuthConstants.OAUTH_VERIFIER,
                                 oauthVerifier);
    }

    // -------------------------------------------------------------------------
    private String httpMethod;

    private String baseUri;

    private Map<String, List<String>> map;
}
