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


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class BaseStringBuilder implements Builder {


    public static final String KEY_OAUTH_CONSUMER_KEY = "oauth_consumer_key";


    public static final String KEY_OAUTH_NONCE = "oauth_nonce";


    public static final String KEY_OAUTH_SIGNATURE_METHOD
        = "oauth_signature_method";


    public static final String KEY_OAUTH_TIMESTAMP = "oauth_timestamp";


    public static final String KEY_OAUTH_TOKEN = "oauth_token";


    public static final String KEY_OAUTH_VERSION = "oauth_version";


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

            public String build() throws Exception {

                return prebuilt;
            }

        };
    }


    public String build() throws Exception {

        if (!requestParameters.containsKey(KEY_OAUTH_NONCE)) {
            if (nonceBuilder == null) {
                nonceBuilder = new NonceBuilder();
            }
            oauthNonce(nonceBuilder.build());
        }

        if (!requestParameters.containsKey(KEY_OAUTH_TIMESTAMP)) {
            if (timestampBuilder == null) {
                timestampBuilder = new TimestampBuilder();
            }
            oauthTimestamp(timestampBuilder.build());
        }

        if (!requestParameters.containsKey(KEY_OAUTH_VERSION)) {
            requestParameters.put(KEY_OAUTH_VERSION, "1.0");
        }

        final Map encodedParameters = new TreeMap();
        for (final Iterator iterator = requestParameters.entrySet().iterator();
             iterator.hasNext();) {
            final Map.Entry entry = (Map.Entry) iterator.next();
            final String key = (String) entry.getKey();
            final String value = (String) entry.getValue();
            encodedParameters.put(Percent.encode(key), Percent.encode(value));
        }

        final StringBuffer buffer = new StringBuffer();
        {
            final Iterator i = encodedParameters.entrySet().iterator();
            if (i.hasNext()) {
                final Map.Entry e = (Map.Entry) i.next();
                buffer
                    .append((String) e.getKey())
                    .append("=")
                    .append((String) e.getValue());
            }
            while (i.hasNext()) {
                final Map.Entry e = (Map.Entry) i.next();
                buffer
                    .append("&")
                    .append((String) e.getKey())
                    .append("=")
                    .append((String) e.getValue());
            }
        }

        return httpMethod.toUpperCase()
               + "&" + Percent.encode(baseUri)
               + "&" + Percent.encode(buffer.toString());
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
     */
    public BaseStringBuilder requestParameter(final String key,
                                              final String value) {

        requestParameters.put(key, value);

        return this;
    }


    public String getOAuthConsumerKey() {

        return (String) requestParameters.get(KEY_OAUTH_CONSUMER_KEY);
    }


    public void setOAuthConsumerKey(final String oauthConsumerKey) {

        requestParameter(KEY_OAUTH_CONSUMER_KEY, oauthConsumerKey);
    }


    /**
     * Puts a request parameter value for {@value #KEY_OAUTH_CONSUMER_KEY}.
     *
     * @param oauthConsumerKey the value of {@value #KEY_OAUTH_CONSUMER_KEY}.
     *
     * @return
     */
    public BaseStringBuilder oauthConsumerKey(final String oauthConsumerKey) {

        setOAuthConsumerKey(oauthConsumerKey);

        return this;
    }


    public String getOauthNonce() {

        return (String) requestParameters.get(KEY_OAUTH_NONCE);
    }


    public void setOauthNonce(final String oauthNonce) {

        requestParameters.put(KEY_OAUTH_NONCE, oauthNonce);
    }


    public BaseStringBuilder oauthNonce(final String oauthNonce) {

        setOauthNonce(oauthNonce);

        return this;
    }


    public BaseStringBuilder nonceBuilder(final NonceBuilder nonceBuilder) {

        this.nonceBuilder = nonceBuilder;

        return this;
    }


    public String getOauthSignatureMethod() {

        return (String) requestParameters.get(KEY_OAUTH_SIGNATURE_METHOD);
    }


    public void setOauthSignatureMethod(final String oauthSignatureMethod) {

        requestParameters.put(KEY_OAUTH_SIGNATURE_METHOD, oauthSignatureMethod);
    }


    public BaseStringBuilder oauthSignatureMethod(
        final String oauthSignatureMethod) {

        setOauthSignatureMethod(oauthSignatureMethod);

        return this;
    }


    public String getOauthTimestamp() {

        return (String) requestParameters.get(KEY_OAUTH_TIMESTAMP);
    }


    public void setOAuthTimestamp(final String oauthTimestamp) {

        requestParameters.put(KEY_OAUTH_TIMESTAMP, oauthTimestamp);
    }


    public BaseStringBuilder oauthTimestamp(final String oauthTimestamp) {

        setOAuthTimestamp(oauthTimestamp);

        return this;
    }


    public BaseStringBuilder timestampBuilder(
        final TimestampBuilder timestampBuilder) {

        this.timestampBuilder = timestampBuilder;

        return this;
    }


    public String getOauthToken() {

        return (String) requestParameters.get(KEY_OAUTH_TOKEN);
    }


    public void setOauthToken(final String oauthToken) {

        requestParameters.put(KEY_OAUTH_TOKEN, oauthToken);
    }


    public BaseStringBuilder oauthToken(final String oauthToken) {

        setOauthToken(oauthToken);

        return this;
    }


    public String getOauthVersion() {

        return (String) requestParameters.get(KEY_OAUTH_VERSION);
    }


    public void setOauthVersion(final String oauthVersion) {

        requestParameters.put(KEY_OAUTH_VERSION, oauthVersion);
    }


    public BaseStringBuilder oauthVersion(final String oauthVersion) {

        setOauthVersion(oauthVersion);

        return this;
    }


    private String httpMethod;


    private String baseUri;


    private final Map requestParameters = new HashMap();


    private NonceBuilder nonceBuilder;


    private TimestampBuilder timestampBuilder;


}

