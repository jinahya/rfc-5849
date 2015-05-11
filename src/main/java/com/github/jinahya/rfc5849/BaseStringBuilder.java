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


import com.github.jinahya.rfc5849.util.Percent;
import com.github.jinahya.rfc5849.net.Form;
import com.github.jinahya.rfc5849.util.Params;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class BaseStringBuilder extends Params implements Builder<String> {


    private static final String PROTOCOL_PARAMETER_PREFIX = "oauth_";


    /**
     * Returns an instance whose {@link #build()} always returns given value.
     *
     * @param prebuilt the value.
     *
     * @return an instance.
     */
    public static BaseStringBuilder ofPrebuilt(final String prebuilt) {

        if (prebuilt == null) {
            throw new NullPointerException("null prebuilt");
        }

        return new BaseStringBuilder() {

            @Override
            public String build() throws Exception {

                return prebuilt;
            }

        };
    }


    @Override
    public String build() throws Exception {

        if (httpMethod == null) {
            throw new IllegalStateException("no httpMethod set");
        }

        if (baseUri == null) {
            throw new IllegalStateException("no baseUri set");
        }

        final Map<String, List<String>> map = map(false, false);

        if (!map.containsKey(Constants.OAUTH_NONCE)) {
//            if (nonceBuilder == null) {
//                nonceBuilder = NonceBuilder.newInstance();
//            }
            if (nonceBuilder != null) {
                oauthNonce(nonceBuilder.build());
            }
        }

        if (!map.containsKey(Constants.OAUTH_TIMESTAMP)) {
//            if (timestampBuilder == null) {
//                timestampBuilder = new TimestampBuilder();
//            }
            if (nonceBuilder != null) {
                oauthTimestamp(timestampBuilder.build());
            }
        }

        final Map<String, List<String>> encodedParameters
            = new TreeMap<String, List<String>>();

        for (final Entry<String, List<String>> e : map.entrySet()) {
            final String decodedKey = e.getKey();
            final List<String> decodedValues = e.getValue();
            final String encodedKey = Percent.encode(decodedKey);
            final List<String> encodedValues
                = new ArrayList<String>(decodedValues.size());
            for (final String decodedValue : decodedValues) {
                encodedValues.add(
                    Percent.encode(decodedValue == null ? "" : decodedValue));
            }
            Collections.sort(encodedValues);
            encodedParameters.put(encodedKey, encodedValues);
        }

        final StringBuilder buffer = new StringBuilder();
        {
            for (final Entry<String, List<String>> e
                 : encodedParameters.entrySet()) {
                final String key = e.getKey();
                final List<String> values = e.getValue();
                for (final String value : values) {
                    if (buffer.length() > 0) {
                        buffer.append("&");
                    }
                    buffer.append(key).append("=").append(value);
                }
            }
        }

        final String built = httpMethod.toUpperCase()
                             + "&" + Percent.encode(baseUri)
                             + "&" + Percent.encode(buffer.toString());

        if (printer != null) {
            printer.println("built: " + built);
        }

        return built;
    }


    /**
     * Replaces the current value of HTTP method with given and returns this
     * instance.
     *
     * @param httpMethod new value of HTTP method.
     *
     * @return this instance.
     */
    public BaseStringBuilder httpMethod(final String httpMethod) {

        this.httpMethod = httpMethod;

        return this;
    }


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


    /**
     * Puts a request parameter entry and returns this instance.
     *
     * @param key parameter key
     * @param value parameter value.
     *
     * @return this instance.
     *
     */
    public BaseStringBuilder requestParameter(final String key,
                                              final String value) {

        if (key == null) {
            throw new NullPointerException("null key");
        }

        if (value == null) {
            throw new NullPointerException("null value");
        }

        if (key.startsWith(PROTOCOL_PARAMETER_PREFIX)) {
            putSingle(key, value);
            return this;
        }

        add(key, value);

        return this;
    }


    public BaseStringBuilder queryParameter(final String key,
                                            final String value) {

        return requestParameter(key, value);
    }


    public String protocolParameter(final String key) {

        if (key == null) {
            throw new NullPointerException("null key");
        }

        if (!key.startsWith(PROTOCOL_PARAMETER_PREFIX)) {
            throw new IllegalArgumentException(
                "key(" + key + ") doesn start with "
                + PROTOCOL_PARAMETER_PREFIX);
        }

        return getFirst(key);
    }


    public BaseStringBuilder protocolParameter(final String key,
                                               final String value) {

        return requestParameter(key, value);
    }


    public void copyProtocolParameters(final Map<String, String> map) {

        if (map == null) {
            throw new NullPointerException("null map");
        }

        for (final Entry<String, List<String>> e
             : map(true, false).entrySet()) {
            final String key = e.getKey();
            if (key.startsWith(PROTOCOL_PARAMETER_PREFIX)) {
                map.put(key, e.getValue().get(0));
            }
        }
    }


    public String oauthCallback() {

        return protocolParameter(Constants.OAUTH_CALLBACK);
    }


    public BaseStringBuilder oauthCallback(final String oauthCallback) {

        return protocolParameter(Constants.OAUTH_CALLBACK, oauthCallback);
    }


    public String oauthConsumerKey() {

        return protocolParameter(Constants.OAUTH_CONSUMER_KEY);
    }


    /**
     * Sets a protocol parameter value for
     * {@value Constants#OAUTH_CONSUMER_KEY}.
     *
     * @param oauthConsumerKey the value of
     * {@value Constants#OAUTH_CONSUMER_KEY}.
     *
     * @return this instance.
     */
    public BaseStringBuilder oauthConsumerKey(final String oauthConsumerKey) {

        return protocolParameter(
            Constants.OAUTH_CONSUMER_KEY, oauthConsumerKey);
    }


    public String oauthNonce() {

        return protocolParameter(Constants.OAUTH_NONCE);
    }


    public BaseStringBuilder oauthNonce(final String oauthNonce) {

        return protocolParameter(Constants.OAUTH_NONCE, oauthNonce);
    }


    public BaseStringBuilder nonceBuilder(final NonceBuilder nonceBuilder) {

        this.nonceBuilder = nonceBuilder;

        return this;
    }


    public String oauthSignatureMethod() {

        return protocolParameter(Constants.OAUTH_SIGNATURE_METHOD);
    }


    public BaseStringBuilder oauthSignatureMethod(
        final String oauthSignatureMethod) {

        return protocolParameter(Constants.OAUTH_SIGNATURE_METHOD,
                                 oauthSignatureMethod);
    }


    public String getOauthTimestamp() {

        return protocolParameter(Constants.OAUTH_TIMESTAMP);
    }


    public BaseStringBuilder oauthTimestamp(final String oauthTimestamp) {

        return protocolParameter(Constants.OAUTH_TIMESTAMP, oauthTimestamp);
    }


    public BaseStringBuilder timestampBuilder(
        final TimestampBuilder timestampBuilder) {

        this.timestampBuilder = timestampBuilder;

        return this;
    }


    public String oauthToken() {

        return protocolParameter(Constants.OAUTH_TOKEN);
    }


    public BaseStringBuilder oauthToken(final String oauthToken) {

        return protocolParameter(Constants.OAUTH_TOKEN, oauthToken);
    }


    public String oauthVersion() {

        return protocolParameter(Constants.OAUTH_VERSION);
    }


    public BaseStringBuilder oauthVersion(final String oauthVersion) {

        return protocolParameter(Constants.OAUTH_VERSION, oauthVersion);
    }


    public String oauthVerifier() {

        return protocolParameter(Constants.OAUTH_VERIFIER);
    }


    public BaseStringBuilder oauthVerifier(final String oauthVerifier) {

        return protocolParameter(Constants.OAUTH_VERIFIER, oauthVerifier);
    }


    public BaseStringBuilder entityParameter(final String key,
                                             final String value) {

        return requestParameter(key, value);
    }


    public BaseStringBuilder entity(final Form entity) {

        if (entity == null) {
            throw new NullPointerException("null entity");
        }

        for (final Entry<String, List<String>> e
             : entity.map(false, false).entrySet()) {
            final String key = e.getKey();
            for (String value : e.getValue()) {
                if (value == null) {
                    value = "";
                }
                entityParameter(key, value);
            }
        }

        return this;
    }


    public BaseStringBuilder printer(final java.io.PrintStream printer) {

        this.printer = printer;

        return this;
    }


    private String httpMethod;


    private String baseUri;


    private NonceBuilder nonceBuilder;


    private TimestampBuilder timestampBuilder;


    private transient java.io.PrintStream printer;


}

