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


    static final String PROTOCOL_PARAMETER_PREFIX = "oauth_";


    @Override
    public String build() throws Exception {

        if (prebuilt != null) {
            return prebuilt;
        }

        if (httpMethod == null) {
            throw new IllegalStateException("no httpMethod set");
        }

        if (baseUri == null) {
            throw new IllegalStateException("no baseUri set");
        }

        if (getFirst(Constants.OAUTH_NONCE) == null && nonceBuilder != null) {
            oauthNonce(nonceBuilder.build());
        }

        if (getFirst(Constants.OAUTH_TIMESTAMP) == null
            && timestampBuilder != null) {
            oauthTimestamp(timestampBuilder.build());
        }

        final Map<String, List<String>> map
            = new TreeMap<String, List<String>>();

        for (final Entry<String, List<String>> entry : entrySet()) {
            final String decodedKey = entry.getKey();
            final String encodedKey = Percent.encode(decodedKey);
            final List<String> decodedValues = entry.getValue();
            final List<String> encodedValues
                = new ArrayList<String>(decodedValues.size());
            for (final String decodedValue : decodedValues) {
                encodedValues.add(Percent.encode(decodedValue));
            }
            Collections.sort(encodedValues);
            map.put(encodedKey, encodedValues);
        }

        final StringBuilder builder = new StringBuilder();
        {
            for (final Entry<String, List<String>> entry : map.entrySet()) {
                final String key = entry.getKey();
                for (final String value : entry.getValue()) {
                    if (builder.length() > 0) {
                        builder.append("&");
                    }
                    builder
                        .append(key)
                        .append("=")
                        .append(value);
                }
            }
        }

        final String built = httpMethod.toUpperCase()
                             + "&" + Percent.encode(baseUri)
                             + "&" + Percent.encode(builder.toString());

        if (printer != null) {
            printer.println("baseString: " + built);
        }

        return built;
    }


    protected String getPrebuilt() {

        return prebuilt;
    }


    public void setPrebuilt(final String prebuilt) {

        this.prebuilt = prebuilt;
    }


    protected BaseStringBuilder prebuilt(final String prebuilt) {

        setPrebuilt(prebuilt);

        return this;
    }


    public String getHttpMethod() {

        return httpMethod;
    }


    public void setHttpMethod(final String httpMethod) {

        this.httpMethod = httpMethod;
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

        setHttpMethod(httpMethod);

        return this;
    }


    public String getBaseUri() {

        return baseUri;
    }


    public void setBaseUri(final String baseUri) {

        this.baseUri = baseUri;
    }


    /**
     * Replaces the value of base URI with given and returns this instance.
     *
     * @param baseUri new value of base URI.
     *
     * @return this instance.
     */
    public BaseStringBuilder baseUri(final String baseUri) {

        setBaseUri(baseUri);

        return this;
    }


    public void addQueryParameter(final String key, final String value) {

        if (key != null && key.startsWith(PROTOCOL_PARAMETER_PREFIX)) {
            throw new IllegalArgumentException(
                "query parameter's key(" + key + ") starts with "
                + PROTOCOL_PARAMETER_PREFIX);
        }

        add(key, value);
    }


    public BaseStringBuilder queryParameter(final String key,
                                            final String value) {

        addQueryParameter(key, value);

        return this;
    }


    public String getProtocolParameter(final String key) {

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


    public void setProtocolParameter(final String key, final String value) {

        if (key != null && !key.startsWith(PROTOCOL_PARAMETER_PREFIX)) {
            throw new IllegalArgumentException(
                "key(" + key + ") doesn't start with "
                + PROTOCOL_PARAMETER_PREFIX);
        }

        putSingle(key, value);
    }


    public BaseStringBuilder protocolParameter(final String key,
                                               final String value) {

        setProtocolParameter(key, value);

        return this;
    }


    public String getOauthCallback() {

        return getProtocolParameter(Constants.OAUTH_CALLBACK);
    }


    public void setOauthCallback(final String oauthCallback) {

        setProtocolParameter(Constants.OAUTH_CALLBACK, oauthCallback);
    }


    public BaseStringBuilder oauthCallback(final String oauthCallback) {

        setOauthCallback(oauthCallback);

        return this;
    }


    public String getOauthConsumerKey() {

        return getProtocolParameter(Constants.OAUTH_CONSUMER_KEY);
    }


    public void setOauthConsumerKey(final String oauthConsumerKey) {

        setProtocolParameter(Constants.OAUTH_CONSUMER_KEY, oauthConsumerKey);
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

        setOauthConsumerKey(oauthConsumerKey);

        return this;
    }


    public String getOauthNonce() {

        return getProtocolParameter(Constants.OAUTH_NONCE);
    }


    public void setOauthNonce(final String oauthNonce) {

        setProtocolParameter(Constants.OAUTH_NONCE, oauthNonce);
    }


    public BaseStringBuilder oauthNonce(final String oauthNonce) {

        setOauthNonce(oauthNonce);

        return this;
    }


    public NonceBuilder getOauthNonceBuilder() {

        return nonceBuilder;
    }


    public void setOauthNonceBuilder(final NonceBuilder oauthNonceBuilder) {

        this.nonceBuilder = oauthNonceBuilder;
    }


    public BaseStringBuilder oauthNonce(final NonceBuilder nonceBuilder) {

        this.nonceBuilder = nonceBuilder;

        return this;
    }


    public String getOauthSignatureMethod() {

        return getProtocolParameter(Constants.OAUTH_SIGNATURE_METHOD);
    }


    public void setOauthSignatureMethod(final String oauthSignatureMethod) {

        setProtocolParameter(Constants.OAUTH_SIGNATURE_METHOD,
                             oauthSignatureMethod);
    }


    public BaseStringBuilder oauthSignatureMethod(
        final String oauthSignatureMethod) {

        setOauthSignatureMethod(oauthSignatureMethod);

        return this;
    }


    public String getOauthTimestamp() {

        return getProtocolParameter(Constants.OAUTH_TIMESTAMP);
    }


    public void setOauthTimestamp(final String oauthTimestamp) {

        setProtocolParameter(Constants.OAUTH_TIMESTAMP, oauthTimestamp);
    }


    public BaseStringBuilder oauthTimestamp(final String oauthTimestamp) {

        setOauthTimestamp(oauthTimestamp);

        return this;
    }


    public TimestampBuilder getOauthTimestampBuilder() {

        return timestampBuilder;
    }


    public void setOauthTimestampBuilder(
        final TimestampBuilder timestampBuilder) {

        this.timestampBuilder = timestampBuilder;
    }


    public BaseStringBuilder oauthTimestampBuilder(
        final TimestampBuilder timestampBuilder) {

        setOauthTimestampBuilder(timestampBuilder);

        return this;
    }


    public String getOauthToken() {

        return getProtocolParameter(Constants.OAUTH_TOKEN);
    }


    public void setOauthToken(final String oauthToken) {

        setProtocolParameter(Constants.OAUTH_TOKEN, oauthToken);
    }


    public BaseStringBuilder oauthToken(final String oauthToken) {

        setOauthToken(oauthToken);

        return this;
    }


    public String getOauthVersion() {

        return getProtocolParameter(Constants.OAUTH_VERSION);
    }


    public void setOauthVersion(final String oauthVersion) {

        setProtocolParameter(Constants.OAUTH_VERSION, oauthVersion);
    }


    public BaseStringBuilder oauthVersion(final String oauthVersion) {

        setOauthVersion(oauthVersion);

        return this;
    }


    public String getOauthVerifier() {

        return getProtocolParameter(Constants.OAUTH_VERIFIER);
    }


    public void setOauthVerifier(final String oauthVerifier) {

        setProtocolParameter(Constants.OAUTH_VERIFIER, oauthVerifier);
    }


    public BaseStringBuilder oauthVerifier(final String oauthVerifier) {

        setOauthVerifier(oauthVerifier);

        return this;
    }


    public void addEntityParameter(final String key, final String value) {

        if (key != null && key.startsWith(PROTOCOL_PARAMETER_PREFIX)) {
            throw new IllegalArgumentException(
                "key(" + key + ") starts with " + PROTOCOL_PARAMETER_PREFIX);
        }

        add(key, value);
    }


    public BaseStringBuilder entityParameter(final String key,
                                             final String value) {

        addEntityParameter(key, value);

        return this;
    }


    public BaseStringBuilder entityParameters(final Params params) {

        if (params == null) {
            throw new NullPointerException("null params");
        }

        for (final Entry<String, List<String>> entry : params.entrySet()) {
            final String key = entry.getKey();
            for (String value : entry.getValue()) {
                entityParameter(key, value);
            }
        }

        return this;
    }


    public BaseStringBuilder printer(final java.io.PrintStream printer) {

        this.printer = printer;

        return this;
    }


    private String prebuilt;


    private String httpMethod;


    private String baseUri;


    private NonceBuilder nonceBuilder;


    private TimestampBuilder timestampBuilder;


    private transient java.io.PrintStream printer;


}

