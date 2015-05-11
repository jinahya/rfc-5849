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


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


/**
 * A multivalued map.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
public class Params {


    public Params() {

        super();

        map = new HashMap<String, List<String>>();
    }


    public Params add(final String key, final String value) {

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

        return this;
    }


    public Params putSingle(final String key, final String value) {

        if (key == null) {
            throw new NullPointerException("null key");
        }

        map.remove(key);

        return add(key, value);
    }


    public String getFirst(final String key) {

        if (key == null) {
            throw new NullPointerException("null key");
        }

        final List<String> values = map.get(key);

        if (values == null) {
            return null;
        }

        return values.get(0);
    }


    public Map<String, List<String>> map(final boolean sortedKey,
                                         final boolean sortedValues) {

        Map<String, List<String>> m
            = new HashMap<String, List<String>>(map.size());
        for (final Entry<String, List<String>> entry : map.entrySet()) {
            final String k = entry.getKey();
            final List<String> v = new ArrayList<String>(entry.getValue());
            if (sortedValues) {
                Collections.sort(v);
            }
            m.put(k, Collections.unmodifiableList(v));
        }

        if (sortedKey) {
            m = new TreeMap<String, List<String>>(m);
        }

        return Collections.unmodifiableMap(m);
    }


    private final Map<String, List<String>> map;


}

