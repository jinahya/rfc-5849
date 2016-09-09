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
import java.util.Set;
import java.util.TreeMap;

/**
 * A builder for generating signature base strings.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see <a href="https://tools.ietf.org/html/rfc5849#section-3.4.1">3.4.1.
 * Signature Base String (RFC 5849)</a>
 */
public class BaseStringBuilder implements Builder<String> {

    static final String PROTOCOL_PARAMETER_PREFIX = "oauth_";

    static BaseStringBuilder of(final String prebuilt) {
        return new BaseStringBuilder() {
            @Override
            public String build() {
                return prebuilt;
            }
        };
    }

//    public static BaseStringBuilder parseBaseString(final String built) {
//        if (built == null) {
//            throw new NullPointerException("null built");
//        }
//        final String[] split1 = built.split("&");
//        if (split1.length != 3) {
//            throw new IllegalArgumentException("wrong base string: " + built);
//        }
//        final BaseStringBuilder parsed = new BaseStringBuilder();
//        parsed.httpMethod(decodePercent(split1[0]));
//        parsed.baseUri(decodePercent(split1[1]));
//        for (final String entry : decodePercent(split1[2]).split("&")) {
//            final String[] split2 = entry.split("&");
//            if (split2.length != 2) {
//                throw new IllegalArgumentException(
//                        "wrong base string: " + built);
//            }
//            parsed.add(decodePercent(split2[0]), decodePercent(split2[1]));
//        }
//        return null;
//    }
//
//    // -------------------------------------------------------------------------
//    private Map<?, ?> map(final Map<?, ?> map_) {
//        final Map<?, ?> map = new HashMap<?, ?>(map_);
//    }
//
//    @Override
//    public boolean equals(final Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final BaseStringBuilder other = (BaseStringBuilder) obj;
//        if ((httpMethod == null)
//            ? (other.httpMethod != null)
//            : !httpMethod.equals(other.httpMethod)) {
//            return false;
//        }
//        if ((baseUri == null)
//            ? (other.baseUri != null) : !baseUri.equals(other.baseUri)) {
//            return false;
//        }
//        if (map != other.map && (map == null || !map.equals(other.map))) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 7;
//        hash = 47 * hash + (httpMethod != null ? httpMethod.hashCode() : 0);
//        hash = 47 * hash + (baseUri != null ? baseUri.hashCode() : 0);
//        hash = 47 * hash + (map != null ? map.hashCode() : 0);
//        return hash;
//    }
    // -------------------------------------------------------------------------
    @Override
    public String build() {//throws Exception {
        if (httpMethod == null) {
            throw new IllegalStateException("no httpMethod set");
        }
        if (baseUri == null) {
            throw new IllegalStateException("no baseUri set");
        }
//        if (nonceBuilder != null) {
//            oauthNonce(nonceBuilder.build());
//        }
        if (!map.containsKey(Rfc5849Constants.OAUTH_NONCE)) {
            throw new IllegalStateException(
                    "no " + Rfc5849Constants.OAUTH_NONCE);
        }
//        if (timestampBuilder != null) {
//            oauthTimestamp(timestampBuilder.build());
//        }
        if (!map.containsKey(Rfc5849Constants.OAUTH_TIMESTAMP)) {
            throw new IllegalStateException(
                    "no " + Rfc5849Constants.OAUTH_TIMESTAMP);
        }
        final Map<String, List<String>> encoded
                = new TreeMap<String, List<String>>();
        for (final Entry<String, List<String>> entry : map.entrySet()) {
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
     * Sets a value for {@code httpMethod}.
     *
     * @param httpMethod the value of {@code httpMethod}.
     * @return this instance.
     * @see <a href="https://tools.ietf.org/html/rfc5849#section-3.4.1">3.4.1.
     * Signature Base String (RFC 5849)</a>
     */
    public BaseStringBuilder httpMethod(final String httpMethod) {
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
    public BaseStringBuilder protocolParameter(final String key,
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
    public BaseStringBuilder entityParameter(final String key,
                                             final String value) {
        return queryParameter(key, value);
    }

    // ----------------------------------------------------------- oauthCallback
    /**
     * Set a protocol parameter value for
     * {@value com.github.jinahya.rfc5849.Rfc5849Constants#OAUTH_CALLBACK}.
     *
     * @param oauthCallback the protocol parameter value for
     * {@value com.github.jinahya.rfc5849.Rfc5849Constants#OAUTH_CALLBACK}
     * @return this instance
     */
    public BaseStringBuilder oauthCallback(final String oauthCallback) {
        return protocolParameter(Rfc5849Constants.OAUTH_CALLBACK,
                                 oauthCallback);
    }

    // -------------------------------------------------------- oauthConsumerKey
    /**
     * Sets a protocol parameter value for
     * {@value com.github.jinahya.rfc5849.Rfc5849Constants#OAUTH_CONSUMER_KEY}.
     *
     * @param oauthConsumerKey the protocol parameter value for
     * {@value com.github.jinahya.rfc5849.Rfc5849Constants#OAUTH_CONSUMER_KEY}
     * @return this instance.
     */
    public BaseStringBuilder oauthConsumerKey(final String oauthConsumerKey) {
        return protocolParameter(Rfc5849Constants.OAUTH_CONSUMER_KEY,
                                 oauthConsumerKey);
    }

    // -------------------------------------------------------------- oauthNonce
    /**
     * Sets a protocol parameter value for
     * {@value com.github.jinahya.rfc5849.Rfc5849Constants#OAUTH_NONCE}
     *
     * @param oauthNonce the protocol parameter value for
     * {@value com.github.jinahya.rfc5849.Rfc5849Constants#OAUTH_NONCE}
     * @return this instance.
     */
    public BaseStringBuilder oauthNonce(final String oauthNonce) {
        return protocolParameter(Rfc5849Constants.OAUTH_NONCE, oauthNonce);
    }

//    // ------------------------------------------------------------ nonceBuilder
//    /**
//     * Sets a builder for
//     * {@value com.github.jinahya.rfc5849.Rfc5849Constants#OAUTH_NONCE}.
//     *
//     * @param nonceBuilder the builder for
//     * {@value com.github.jinahya.rfc5849.Rfc5849Constants#OAUTH_NONCE}
//     * @return this instance
//     * @see #oauthNonce(java.lang.String)
//     */
//    public BaseStringBuilder nonceBuilder(final NonceBuilder nonceBuilder) {
//        this.nonceBuilder = nonceBuilder;
//        return this;
//    }
    // ---------------------------------------------------- oauthSignatureMethod
    /**
     * Sets a protocol parameter value for
     * {@value com.github.jinahya.rfc5849.Rfc5849Constants#OAUTH_SIGNATURE_METHOD}.
     *
     * @param oauthSignatureMethod the protocol parameter value for
     * {@value com.github.jinahya.rfc5849.Rfc5849Constants#OAUTH_SIGNATURE_METHOD}
     * @return return this
     */
    public BaseStringBuilder oauthSignatureMethod(
            final String oauthSignatureMethod) {
        return protocolParameter(Rfc5849Constants.OAUTH_SIGNATURE_METHOD,
                                 oauthSignatureMethod);
    }

    // ---------------------------------------------------------- oauthTimestamp
    /**
     * Sets a protocol parameter value for
     * {@value com.github.jinahya.rfc5849.Rfc5849Constants#OAUTH_TIMESTAMP}.
     *
     * @param oauthTimestamp the protocol parameter value for
     * {@value com.github.jinahya.rfc5849.Rfc5849Constants#OAUTH_TIMESTAMP}
     * @return this instance
     */
    public BaseStringBuilder oauthTimestamp(final String oauthTimestamp) {
        return protocolParameter(Rfc5849Constants.OAUTH_TIMESTAMP,
                                 oauthTimestamp);
    }

//    // -------------------------------------------------------- timestampBuilder
//    /**
//     * Sets a builder for
//     * {@value com.github.jinahya.rfc5849.Rfc5849Constants#OAUTH_TIMESTAMP}.
//     *
//     * @param timestampBuilder the builder for
//     * {@value com.github.jinahya.rfc5849.Rfc5849Constants#OAUTH_TIMESTAMP}
//     * @return this instance.
//     */
//    public BaseStringBuilder timestampBuilder(
//            final TimestampBuilder timestampBuilder) {
//        this.timestampBuilder = timestampBuilder;
//        return this;
//    }
    // -------------------------------------------------------------- oauthToken
    /**
     * Sets a protocol parameter value for
     * {@value com.github.jinahya.rfc5849.Rfc5849Constants#OAUTH_TOKEN}.
     *
     * @param oauthToken the protocol parameter value for
     * {@value com.github.jinahya.rfc5849.Rfc5849Constants#OAUTH_TOKEN}
     * @return this instance
     */
    public BaseStringBuilder oauthToken(final String oauthToken) {
        return protocolParameter(Rfc5849Constants.OAUTH_TOKEN, oauthToken);
    }

    // ------------------------------------------------------------ oauthVersion
    /**
     * Sets a protocol parameter value for
     * {@value com.github.jinahya.rfc5849.Rfc5849Constants#OAUTH_VERSION}.
     *
     * @param oauthVersion the protocol parameter value for
     * {@value com.github.jinahya.rfc5849.Rfc5849Constants#OAUTH_VERSION}
     * @return this instance
     */
    public BaseStringBuilder oauthVersion(final String oauthVersion) {
        return protocolParameter(Rfc5849Constants.OAUTH_VERSION, oauthVersion);
    }

    // ----------------------------------------------------------- oauthVerifier
    /**
     * Sets a protocol parameter value for
     * {@value com.github.jinahya.rfc5849.Rfc5849Constants#OAUTH_VERIFIER}.
     *
     * @param oauthVerifier the protocol parameter value for
     * {@value com.github.jinahya.rfc5849.Rfc5849Constants#OAUTH_VERIFIER}
     * @return this instance
     */
    public BaseStringBuilder oauthVerifier(final String oauthVerifier) {
        return protocolParameter(Rfc5849Constants.OAUTH_VERIFIER,
                                 oauthVerifier);
    }

    // -------------------------------------------------------------------------
    private String httpMethod;

    private String baseUri;

    private final Map<String, List<String>> map
            = new HashMap<String, List<String>>();

//    private NonceBuilder nonceBuilder;
//    private TimestampBuilder timestampBuilder;
}
