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

import static com.github.jinahya.rfc5849.util.Percent.encodePercent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class BaseStringBuilder
        //        extends Params
        implements Builder<String> {

    static final String PROTOCOL_PARAMETER_PREFIX = "oauth_";

    @Override
    public String build() throws Exception {
        if (httpMethod == null) {
            throw new IllegalStateException("no httpMethod");
        }
        if (baseUri == null) {
            throw new IllegalStateException("no baseUri");
        }
        if (!map.containsKey(Rfc5849Constants.OAUTH_NONCE)
            && nonceBuilder != null) {
            oauthNonce(nonceBuilder.build());
        }
        if (!map.containsKey(Rfc5849Constants.OAUTH_TIMESTAMP)
            && timestampBuilder != null) {
            oauthTimestamp(timestampBuilder.build());
        }
        final Map<String, List<String>> encoded
                = new TreeMap<String, List<String>>();
        for (final Entry<String, List<String>> entry : map.entrySet()) {
            final String decodedKey = entry.getKey();
            final String encodedKey = encodePercent(decodedKey);
            final List<String> decodedValues = entry.getValue();
            final List<String> encodedValues
                    = new ArrayList<String>(decodedValues.size());
            for (final String decodedValue : decodedValues) {
                encodedValues.add(encodePercent(decodedValue));
            }
            Collections.sort(encodedValues);
            encoded.put(encodedKey, encodedValues);
        }
        final StringBuilder builder = new StringBuilder();
        {
            for (final Entry<String, List<String>> entry : encoded.entrySet()) {
                final String key = entry.getKey();
                for (final String value : entry.getValue()) {
                    if (builder.length() > 0) {
                        builder.append("&");
                    }
                    builder.append(key).append("=").append(value);
                }
            }
        }
        final String built = httpMethod.toUpperCase()
                             + "&" + encodePercent(baseUri)
                             + "&" + encodePercent(builder.toString());
        return built;
    }

    // --------------------------------------------------------------------- map
    private void add(final String key, final String value) {
        if (key == null) {
            throw new NullPointerException("null key");
        }
        if (value == null) {
            throw new NullPointerException("null value");
        }
        List<String> values = map.get(key);
        if (values == null) {
            values = new ArrayList<String>();
            map.put(key, values);
        }
        values.add(value);
    }

    private void put(final String key, final String value) {
        map.remove(key);
        add(key, value);
    }

    Set<Entry<String, List<String>>> entries() {
        return new HashMap<String, List<String>>(map).entrySet();
    }

    // -------------------------------------------------------------- httpMethod
    /**
     * Replaces the current value of {@code httpMethod}with given and returns
     * this instance.
     *
     * @param httpMethod new value of {@code httpMethod}.
     *
     * @return this instance.
     */
    public BaseStringBuilder httpMethod(final String httpMethod) {
        this.httpMethod = httpMethod;
        return this;
    }

    // ----------------------------------------------------------------- baseUri
    /**
     * Replaces the value of base URI with given and returns this instance.
     *
     * @param baseUri new value of base URI.
     *
     * @return this instance.
     */
    public BaseStringBuilder baseUri(final String baseUri) {
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
    public BaseStringBuilder queryParameter(final String key,
                                            final String value) {
        if (key != null && key.startsWith(PROTOCOL_PARAMETER_PREFIX)) {
            throw new IllegalArgumentException(
                    "query parameter's key(" + key + ") starts with "
                    + PROTOCOL_PARAMETER_PREFIX);
        }
        add(key, value);
        return this;
    }

    // ------------------------------------------------------- protocolParameter
    public BaseStringBuilder protocolParameter(final String key,
                                               final String value) {
        if (key != null && !key.startsWith(PROTOCOL_PARAMETER_PREFIX)) {
            throw new IllegalArgumentException(
                    "key(" + key + ") doesn't start with "
                    + PROTOCOL_PARAMETER_PREFIX);
        }
        put(key, value);
        return this;
    }

    // ----------------------------------------------------------- oauthCallback
    public BaseStringBuilder oauthCallback(final String oauthCallback) {
        return protocolParameter(Rfc5849Constants.OAUTH_CALLBACK, oauthCallback);
    }

    // -------------------------------------------------------- oauthConsumerKey
    /**
     * Sets a protocol parameter value for
     * {@value Rfc5849Constants#OAUTH_CONSUMER_KEY}.
     *
     * @param oauthConsumerKey the value of
     * {@value Rfc5849Constants#OAUTH_CONSUMER_KEY}.
     *
     * @return this instance.
     */
    public BaseStringBuilder oauthConsumerKey(final String oauthConsumerKey) {
        return protocolParameter(Rfc5849Constants.OAUTH_CONSUMER_KEY,
                                 oauthConsumerKey);
    }

    // -------------------------------------------------------------- oauthNonce
    public BaseStringBuilder oauthNonce(final String oauthNonce) {
        return protocolParameter(Rfc5849Constants.OAUTH_NONCE, oauthNonce);
    }

    // ------------------------------------------------------------ nonceBuilder
    public BaseStringBuilder nonceBuilder(final NonceBuilder nonceBuilder) {
        this.nonceBuilder = nonceBuilder;
        return this;
    }

    // ---------------------------------------------------- oauthSignatureMethod
    public BaseStringBuilder oauthSignatureMethod(
            final String oauthSignatureMethod) {
        return protocolParameter(Rfc5849Constants.OAUTH_SIGNATURE_METHOD,
                                 oauthSignatureMethod);
    }

    // ---------------------------------------------------------- oauthTimestamp
    public BaseStringBuilder oauthTimestamp(final String oauthTimestamp) {
        return protocolParameter(Rfc5849Constants.OAUTH_TIMESTAMP,
                                 oauthTimestamp);
    }

    // -------------------------------------------------------- timestampBuilder
    public BaseStringBuilder timestampBuilder(
            final TimestampBuilder timestampBuilder) {
        this.timestampBuilder = timestampBuilder;
        return this;
    }

    // -------------------------------------------------------------- oauthToken
    public BaseStringBuilder oauthToken(final String oauthToken) {
        return protocolParameter(Rfc5849Constants.OAUTH_TOKEN, oauthToken);
    }

    // ------------------------------------------------------------ oauthVersion
    public BaseStringBuilder oauthVersion(final String oauthVersion) {
        return protocolParameter(Rfc5849Constants.OAUTH_VERSION, oauthVersion);
    }

    // ----------------------------------------------------------- oauthVerifier
    public BaseStringBuilder oauthVerifier(final String oauthVerifier) {
        return protocolParameter(Rfc5849Constants.OAUTH_VERIFIER,
                                 oauthVerifier);
    }

    // -------------------------------------------------------- entityParameters
    public BaseStringBuilder entityParameter(final String key,
                                             final String value) {
        return queryParameter(key, value);
    }

    // -------------------------------------------------------------------------
    private final Map<String, List<String>> map
            = new HashMap<String, List<String>>();

    private String httpMethod;

    private String baseUri;

    private NonceBuilder nonceBuilder;

    private TimestampBuilder timestampBuilder;
}
