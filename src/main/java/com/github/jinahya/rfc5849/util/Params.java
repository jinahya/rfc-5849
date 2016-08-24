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

import static com.github.jinahya.rfc5849.util.Formurl.decodeFormurl;
import static com.github.jinahya.rfc5849.util.Formurl.encodeFormurl;
import static com.github.jinahya.rfc5849.util.Percent.decodePercent;
import static com.github.jinahya.rfc5849.util.Percent.encodePercent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * A multivalued map.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@Deprecated
public class Params {//extends HashMap<String, List<String>> {

    /**
     * Adds an entry.
     *
     * @param key the key.
     * @param value the value.
     */
    protected void add(final String key, final String value) {
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

    /**
     * Adds an singleton entry. All previous values mapped to {@code key} are
     * removed.
     *
     * @param key the key.
     * @param value the value.
     */
    protected void putSingle(final String key, final String value) {
        if (key == null) {
            throw new NullPointerException("null key");
        }
        map.remove(key);
        add(key, value);
    }

    public String getFirst(final String key) {
        if (key == null) {
            throw new NullPointerException("null key");
        }
        final List<String> values = map.get(key);
        if (values == null || values.isEmpty()) {
            return null;
        }
        return values.get(0);
    }

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

    // ---------------------------------------------------------- percentEncoded
    public StringBuilder printPercentEncoded(final StringBuilder builder) {
        if (builder == null) {
            throw new NullPointerException("null builder");
        }
        final int length = builder.length();
        for (final Entry<String, List<String>> entry : map.entrySet()) {
            final String key = encodePercent(entry.getKey());
            for (final String value : entry.getValue()) {
                builder
                        .append("&")
                        .append(key)
                        .append("=")
                        .append(encodePercent(value));
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
            add(decodePercent(i.next()), decodePercent(i.next()));
        }
        return this;
    }

    // ---------------------------------------------------------- formurlEncoded
    /**
     * Prints entries in a form-urlencoded and appends to given builder.
     *
     * @param builder the builder to append.
     *
     * @return given {@code builder}.
     */
    public StringBuilder printFormurlEncoded(final StringBuilder builder) {
        if (builder == null) {
            throw new NullPointerException("null builder");
        }
        final int length = builder.length();
        for (final Entry<String, List<String>> entry : map.entrySet()) {
            final String key = encodeFormurl(entry.getKey());
            for (final String value : entry.getValue()) {
                builder
                        .append("&")
                        .append(key)
                        .append("=")
                        .append(encodeFormurl(value));
            }
        }
        if (builder.length() > length) {
            builder.delete(length, length + 1);
        }
        return builder;
    }

    /**
     * Prints entries to a for-urlencoded string.
     *
     * @return a url-encoded string.
     */
    public String printFormurlEncoded() {
        return printFormurlEncoded(new StringBuilder()).toString();
    }

    /**
     * Parse entries from a form-urlencoded string.
     *
     * @param printed the string to parse.
     *
     * @return this instance.
     */
    public Params parseFormurlDecoded(final String printed) {
        if (printed == null) {
            throw new NullPointerException("null printed");
        }
        for (final Iterator<String> i = s(printed).iterator(); i.hasNext();) {
            add(decodeFormurl(i.next()), decodeFormurl(i.next()));
        }
        return this;
    }

    // ----------------------------------------------------------------- entries
    public Set<Entry<String, List<String>>> entries() {
        return new HashMap<String, List<String>>(map).entrySet();
    }

    // -------------------------------------------------------------------------
    private final Map<String, List<String>> map
            = new HashMap<String, List<String>>();
}
