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


import com.github.jinahya.rfc5849.util.Multivalued;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class Form {


    public Form() {

        super();

        params = new Multivalued<String, String>();
    }


    public Multivalued<String, String> params() {

        return params;
    }


    /**
     *
     * @param name the name of the parameter.
     * @param value the value of the parameter.
     *
     * @return this instance.
     */
    public Form param(final String name, final String value) {

        if (name == null) {
            throw new NullPointerException("null name");
        }

        params.add(name, value);

        return this;
    }


    public String encode() throws UnsupportedEncodingException {

        final StringBuffer buffer = new StringBuffer();

        for (final Iterator<Entry<String, List<String>>> i
            = params.map().entrySet().iterator();
             i.hasNext();) {
            final Entry<String, List<String>> e = i.next();
            final String keys = e.getKey();
            final List<String> values = e.getValue();
            for (final String value : values) {
                if (buffer.length() > 0) {
                    buffer.append("&");
                }
                buffer.append(URLEncoder.encode(keys, "UTF-8"));
                if (value != null) {
                    buffer.append("=")
                        .append(URLEncoder.encode(value, "UTF-8"));
                }
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


    private final Multivalued<String, String> params;


}

