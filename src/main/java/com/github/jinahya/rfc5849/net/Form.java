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


package com.github.jinahya.rfc5849.net;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class Form {


    public Map asMap() {

        if (params == null) {
            params = new HashMap();
        }

        return params;
    }


    public Form param(final String name, final String value) {

        if (name == null) {
            throw new NullPointerException("null name");
        }

        List values = (List) asMap().get(name);
        if (values == null) {
            values = new ArrayList();
            asMap().put(name, values);
        }

        values.add(value);

        return this;
    }


    public String encode() throws UnsupportedEncodingException {

        final StringBuffer buffer = new StringBuffer();

        for (final Iterator i = asMap().entrySet().iterator(); i.hasNext();) {
            final Map.Entry e = (Map.Entry) i.next();
            final String k = (String) e.getKey();
            final String v = (String) e.getValue();
            if (buffer.length() > 0) {
                buffer.append("&");
            }
            buffer.append(URLEncoder.encode(k, "UTF-8"));
            if (v != null) {
                buffer.append("=").append(URLEncoder.encode(v, "UTF-8"));
            }
        }

        return buffer.toString();
    }


    public Form decode(final String encoded)
        throws UnsupportedEncodingException {

        if (encoded == null) {
            throw new NullPointerException("null encoded");
        }

        for (final StringTokenizer tokenizer
            = new StringTokenizer(encoded, "&");
             tokenizer.hasMoreTokens();) {
            final String token = tokenizer.nextToken();
            final int i = token.indexOf('=');
            if (i == -1) {
                param(URLDecoder.decode(token, "UTF-8"), null);
            } else {
                param(URLDecoder.decode(token.substring(0, i), "UTF-8"),
                      URLDecoder.decode(token.substring(i + 1), "UTF-8"));
            }
        }

        return this;
    }


    private Map params; // = new HashMap();


}

