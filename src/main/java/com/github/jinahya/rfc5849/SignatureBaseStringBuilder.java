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


import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class SignatureBaseStringBuilder {


    public SignatureBaseStringBuilder() {

        super();
    }


    public String build() {

        final String[] keys
            = (String[]) requestParameters.keySet().toArray(
                new String[requestParameters.size()]);
        for (int i = 0; i < keys.length; i++) {
            final String key = keys[i];
            final String value = (String) requestParameters.remove(key);
            requestParameters.put(Percent.encode(key), Percent.encode(value));
        }

        final StringBuffer buffer = new StringBuffer();
        final Iterator i = requestParameters.entrySet().iterator();
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

        return getHttpMethod().toUpperCase()
               + "&" + Percent.encode(getBaseUri())
               + "&" + Percent.encode(buffer.toString());
    }


    public String getHttpMethod() {

        return httpMethod;
    }


    public void setHttpMethod(final String httpMethod) {

        this.httpMethod = httpMethod;
    }


    public SignatureBaseStringBuilder httpMethod(final String httpMethod) {

        setHttpMethod(httpMethod);

        return this;
    }


    public String getBaseUri() {

        return baseUri;
    }


    public void setBaseUri(final String baseUri) {

        this.baseUri = baseUri;
    }


    public SignatureBaseStringBuilder baseUri(final String baseUri) {

        setBaseUri(baseUri);

        return this;
    }


    public Map getRequestParameters() {

        if (requestParameters == null) {
            requestParameters = new TreeMap();
        }

        return requestParameters;
    }


    /**
     * Puts a request parameter entry and returns this instance.
     *
     * @param key parameter key
     * @param value parameter value.
     *
     * @return this instance.
     */
    public SignatureBaseStringBuilder requestParameter(final String key,
                                                       final String value) {

        getRequestParameters().put(key, value);

        return this;
    }


    private String httpMethod;


    private String baseUri;


    private SortedMap requestParameters;


}

