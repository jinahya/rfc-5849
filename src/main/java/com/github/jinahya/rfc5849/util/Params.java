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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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


//    public StringBuilder printFormUrlencoded(final StringBuilder builder) {
//
//        if (builder == null) {
//            throw new NullPointerException("null builder");
//        }
//
//        for (final Entry<String, List<String>> entry : entrySet()) {
//            final String keys = entry.getKey();
//            for (final String value : entry.getValue()) {
//                if (builder.length() > 0) {
//                    builder.append("&");
//                }
//                try {
//                    builder
//                        .append(URLEncoder.encode(keys, "UTF-8"))
//                        .append("=")
//                        .append(URLEncoder.encode(value, "UTF-8"));
//                } catch (final UnsupportedEncodingException uee) {
//                    uee.printStackTrace(System.err);
//                    throw new RuntimeException(uee.getMessage());
//                }
//            }
//        }
//
//        return builder;
//    }
//    public String printFormUrlencoded() {
//
//        return printFormUrlencoded(new StringBuilder()).toString();
//    }
//
//
//    public Params parseFormUrlencoded(final String formUrlencoded) {
//
//        if (formUrlencoded == null) {
//            throw new NullPointerException("null formUrlencoded");
//        }
//
//        for (final StringTokenizer t = new StringTokenizer(formUrlencoded, "&");
//             t.hasMoreTokens();) {
//            final String token = t.nextToken();
//            String key = token;
//            String value = "";
//            final int i = token.indexOf('=');
//            if (i != -1) {
//                key = token.substring(0, i);
//                value = token.substring(i + 1);
//            }
//            try {
//                add(URLDecoder.decode(key, "UTF-8"),
//                    URLDecoder.decode(value, "UTF-8"));
//            } catch (final UnsupportedEncodingException uee) {
//                uee.printStackTrace(System.err);
//                throw new RuntimeException(uee.getMessage());
//            }
//        }
//
//        return this;
//    }
    private List<String> s(final String concatenated) {

        final List<String> split = new ArrayList<String>();

        for (final StringTokenizer t = new StringTokenizer(concatenated, "&");
             t.hasMoreTokens();) {
            final String token = t.nextToken();
            String key = token;
            String value = "";
            final int index = token.indexOf('=');
            if (index != -1) {
                key = token.substring(0, index);
                value = token.substring(index + 1);
            }
            split.add(key);
            split.add(value);
        }

        return split;
    }


    public StringBuilder printPercentEncoded(final StringBuilder builder) {

        if (builder == null) {
            throw new NullPointerException("null builder");
        }

        final int length = builder.length();

        for (final Entry<String, List<String>> entry : entrySet()) {
            final String key = Percent.encode(entry.getKey());
            for (final String value : entry.getValue()) {
                builder
                    .append("&")
                    .append(key)
                    .append("=")
                    .append(Percent.encode(value));
            }
        }
        if (builder.length() > length) {
            builder.delete(length, length + 1);
        }

        return builder;
    }


    public String printPercentEncoded() {

        return printPercentEncoded(new StringBuilder()).toString();
    }


    public Params parsePercentDecoded(final String printed) {

        if (printed == null) {
            throw new NullPointerException("null printed");
        }

        for (final Iterator<String> i = s(printed).iterator(); i.hasNext();) {
            add(Percent.decode(i.next()), Percent.decode(i.next()));
        }

        return this;
    }


    public StringBuilder printFormurlEncoded(final StringBuilder builder) {

        if (builder == null) {
            throw new NullPointerException("null builder");
        }

        final int length = builder.length();

        for (final Entry<String, List<String>> entry : entrySet()) {
            final String key = Formurl.encode(entry.getKey());
            for (final String value : entry.getValue()) {
                builder
                    .append("&")
                    .append(key)
                    .append("=")
                    .append(Formurl.encode(value));
            }
        }
        if (builder.length() > length) {
            builder.delete(length, length + 1);
        }

        return builder;
    }


    public String printFormurlEncoded() {

        return printFormurlEncoded(new StringBuilder()).toString();
    }


    public Params parseFormurlDecoded(final String printed) {

        if (printed == null) {
            throw new NullPointerException("null printed");
        }

        for (final Iterator<String> i = s(printed).iterator(); i.hasNext();) {
            add(Formurl.decode(i.next()), Formurl.decode(i.next()));
        }

        return this;
    }

//
//    public Map<String, List<String>> urlEncoded() {
//
//        final Map<String, List<String>> map
//            = new HashMap<String, List<String>>(size());
//
//        for (final Entry<String, List<String>> entry : entrySet()) {
//            map.put(Formurl.encode(entry.getKey()), Formurl.encode(entry.getValue()));
//        }
//
//        return map;
//    }
//
//
//    public Params urlEncoded(final Map<String, List<String>> map) {
//
//        if (map == null) {
//            throw new NullPointerException("null map");
//        }
//
//        for (final Entry<String, List<String>> entry : map.entrySet()) {
//            put(Formurl.decode(entry.getKey()), Formurl.decode(entry.getValue()));
//        }
//
//        return this;
//    }
//
//
//    public Map<String, List<String>> percentEncoded() {
//
//        final Map<String, List<String>> map
//            = new HashMap<String, List<String>>(size());
//
//        for (final Entry<String, List<String>> entry : entrySet()) {
//            map.put(Percent.encode(entry.getKey()),
//                    Percent.encode(entry.getValue()));
//        }
//
//        return map;
//    }
//
//
//    public Params percentEncoded(final Map<String, List<String>> map) {
//
//        if (map == null) {
//            throw new NullPointerException("null map");
//        }
//
//        for (final Entry<String, List<String>> entry : map.entrySet()) {
//            final String key = Percent.decode(entry.getKey());
//            for (final String value : Percent.decode(entry.getValue())) {
//                add(key, value);
//            }
//        }
//
//        return this;
//    }

}

