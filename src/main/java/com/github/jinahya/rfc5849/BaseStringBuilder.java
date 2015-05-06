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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class BaseStringBuilder implements Builder {


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

        if (httpMethod == null) {
            throw new IllegalStateException("no httpMethod set");
        }

        if (baseUri == null) {
            throw new IllegalStateException("no baseUri set");
        }

        if (!requestParameters.containsKey(Constants.OAUTH_NONCE)) {
            if (nonceBuilder == null) {
                nonceBuilder = new NonceBuilder();
            }
            oauthNonce(nonceBuilder.build());
        }

        if (!requestParameters.containsKey(Constants.OAUTH_TIMESTAMP)) {
            if (timestampBuilder == null) {
                timestampBuilder = new TimestampBuilder();
            }
            oauthTimestamp(timestampBuilder.build());
        }
        //System.out.println(requestParameters);

        final Map encodedParameters = new TreeMap();

        for (final Iterator iterator = requestParameters.entrySet().iterator();
             iterator.hasNext();) {
            final Map.Entry entry = (Map.Entry) iterator.next();
            final String decodedKey = (String) entry.getKey();
            final List decodedValues = (List) entry.getValue();
            final String encodedKey = Percent.encode(decodedKey);
            final List encodedValues = new ArrayList(decodedValues.size());
            for (final Iterator j = decodedValues.iterator(); j.hasNext();) {
                encodedValues.add(Percent.encode((String) j.next()));
            }
            Collections.sort(encodedValues);
            encodedParameters.put(encodedKey, encodedValues);
        }
        //System.out.println(encodedParameters);

        final StringBuffer buffer = new StringBuffer();
        {
            final Iterator i = encodedParameters.entrySet().iterator();
            while (i.hasNext()) {
                final Map.Entry e = (Map.Entry) i.next();
                final String key = (String) e.getKey();
                final List values = (List) e.getValue();
                for (final Iterator j = values.iterator(); j.hasNext();) {
                    buffer.append("&").append(key).append("=").append(j.next());
                }
            }
            if (buffer.length() > 0) {
                buffer.deleteCharAt(0);
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
     */
    public BaseStringBuilder requestParameter(final String key,
                                              final String value) {

        if (key == null) {
            throw new NullPointerException("null key");
        }

        if (value == null) {
            throw new NullPointerException("null value");
        }

        if (key.startsWith("oauth_")) {
            requestParameters.put(key, Collections.singletonList(value));
            return this;
        }

        List values = (List) requestParameters.get(key);
        if (values == null) {
            values = new ArrayList();
            requestParameters.put(key, values);
        }
        values.add(value);

        return this;
    }


    public String protocolParameter(final String key) {

        if (key == null) {
            throw new NullPointerException("null key");
        }

        if (!key.startsWith("oauth_")) {
            throw new IllegalArgumentException(
                "key(" + key + ") doesn start with oauth_");
        }

        final List list = (List) requestParameters.get(key);

        return list == null ? null : (String) list.get(0);
    }


    public BaseStringBuilder protocolParameter(final String key,
                                               final String value) {

        if (key == null) {
            throw new NullPointerException("null key");
        }

        if (!key.startsWith("oauth_")) {
            throw new IllegalArgumentException(
                "key(" + key + ") doesn start with oauth_");
        }

        if (value == null) {
            throw new NullPointerException("null value");
        }

        requestParameters.put(key, Collections.singletonList(value));

        return this;
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
     * Puts a request parameter value for {@value #KEY_OAUTH_CONSUMER_KEY}.
     *
     * @param oauthConsumerKey the value of {@value #KEY_OAUTH_CONSUMER_KEY}.
     *
     * @return
     */
    public BaseStringBuilder oauthConsumerKey(final String oauthConsumerKey) {

        return protocolParameter(Constants.OAUTH_CONSUMER_KEY, oauthConsumerKey);
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


    public BaseStringBuilder form(final Form form) {

        if (form == null) {
            throw new NullPointerException("null form");
        }

        for (final Iterator i = form.asMap().entrySet().iterator();
             i.hasNext();) {
            final Map.Entry e = (Map.Entry) i.next();
            final String key = (String) e.getKey();
            String value = (String) e.getValue();
            if (value == null) {
                value = "";
            }
            requestParameter(key, value);
        }

        return this;
    }


    public BaseStringBuilder printer(final java.io.PrintStream printer) {

        this.printer = printer;

        return this;
    }


    public void copyProtocolParameters(final Map map) {

        if (map == null) {
            throw new NullPointerException("null map");
        }

        for (final Iterator i = requestParameters.entrySet().iterator();
             i.hasNext();) {

            final Map.Entry e = (Map.Entry) i.next();
            final String key = (String) e.getKey();
            if (key.startsWith("oauth_")) {
                map.put(key, ((List) e.getValue()).get(0));
            }
        }
    }


    private String httpMethod;


    private String baseUri;


    private final Map requestParameters = new HashMap();


    private NonceBuilder nonceBuilder;


    private TimestampBuilder timestampBuilder;


    private java.io.PrintStream printer;


}

