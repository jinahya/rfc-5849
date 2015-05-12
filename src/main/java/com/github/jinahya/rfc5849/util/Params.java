/*
 * Copyright 2015 onacit.
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


package com.github.jinahya.rfc5849.util;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.StringTokenizer;


/**
 * A multivalued map.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
public class Params extends HashMap<String, List<String>> {


    public void add(final String key, final String value) {

        if (key == null) {
            throw new NullPointerException("null key");
        }

        if (value == null) {
            throw new NullPointerException("null value");
        }

        List<String> values = get(key);
        if (values == null) {
            values = new ArrayList<String>();
            put(key, values);
        }

        values.add(value);
    }


    public void putSingle(final String key, final String value) {

        if (key == null) {
            throw new NullPointerException("null key");
        }

        remove(key);

        add(key, value);
    }


    public String getFirst(final String key) {

        if (key == null) {
            throw new NullPointerException("null key");
        }

        final List<String> values = get(key);

        if (values == null || values.isEmpty()) {
            return null;
        }

        return values.get(0);
    }


    public StringBuilder printFormUrlencoded(final StringBuilder builder) {

        if (builder == null) {
            throw new NullPointerException("null builder");
        }

        for (final Entry<String, List<String>> entry : entrySet()) {
            final String keys = entry.getKey();
            for (final String value : entry.getValue()) {
                if (builder.length() > 0) {
                    builder.append("&");
                }
                try {
                    builder
                        .append(URLEncoder.encode(keys, "UTF-8"))
                        .append("=")
                        .append(URLEncoder.encode(value, "UTF-8"));
                } catch (final UnsupportedEncodingException uee) {
                    uee.printStackTrace(System.err);
                    throw new RuntimeException(uee.getMessage());
                }
            }
        }

        return builder;
    }


    public String printFormUrlencoded() {

        return printFormUrlencoded(new StringBuilder()).toString();
    }


    public Params parseFormUrlencoded(final String formUrlencoded) {

        if (formUrlencoded == null) {
            throw new NullPointerException("null formUrlencoded");
        }

        for (final StringTokenizer t = new StringTokenizer(formUrlencoded, "&");
             t.hasMoreTokens();) {
            final String token = t.nextToken();
            String key = token;
            String value = "";
            final int i = token.indexOf('=');
            if (i != -1) {
                key = token.substring(0, i);
                value = token.substring(i + 1);
            }
            try {
                add(URLDecoder.decode(key, "UTF-8"),
                    URLDecoder.decode(value, "UTF-8"));
            } catch (final UnsupportedEncodingException uee) {
                uee.printStackTrace(System.err);
                throw new RuntimeException(uee.getMessage());
            }
        }

        return this;
    }


}

