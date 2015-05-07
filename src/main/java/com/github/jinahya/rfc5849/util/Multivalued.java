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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * A multivalued map.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 * @param <K> key type parameter
 * @param <V> value type parameter
 */
public class Multivalued<K, V> {


    public Multivalued() {

        super();

        map = new HashMap<K, List<V>>();
    }


    public Multivalued<K, V> add(final K key, final V value) {

        if (key == null) {
            throw new NullPointerException("null key");
        }

        if (value == null) {
            throw new NullPointerException("null value");
        }

        List<V> values = map.get(key);
        if (values == null) {
            values = new ArrayList<V>();
            map.put(key, values);
        }

        values.add(value);

        return this;
    }


    public Multivalued<K, V> putSingle(final K key, final V value) {

        map.remove(key);

        return add(key, value);
    }


    public V getFirst(final K key) {

        final List<V> values = map.get(key);

        if (values == null) {
            return null;
        }

        return values.get(0);
    }


    public Map<K, List<V>> map() {

        return Collections.unmodifiableMap(map);
    }


    public Set<Entry<K, List<V>>> entrySet() {

        return map().entrySet();
    }


    public Iterator<Entry<K, List<V>>> entryIterator() {

        return entrySet().iterator();
    }


    private final Map<K, List<V>> map;


}

