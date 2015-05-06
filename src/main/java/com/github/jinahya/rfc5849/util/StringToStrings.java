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


/**
 * A multivalued map.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
public class StringToStrings {


    public StringToStrings() {

        super();

        map = new HashMap();
    }


    public void add(final String key, final String value) {

        List values = (List) map.get(key);
        if (values == null) {
            values = new ArrayList();
            map.put(key, values);
        }

        values.add(value);
    }


    public void putSingle(final String key, final String value) {

        final List values = new ArrayList();
        values.add(value);

        map.put(key, values);
    }


    public String getFirst(final String key) {

        final List values = (List) map.get(key);

        if (values == null) {
            return null;
        }

        return (String) values.get(0);
    }


    /**
     * Returns an unmodifiable map.
     *
     * @return an unmodifiable map.
     */
    public Map getMap() {

        return Collections.unmodifiableMap(map);
    }


    private final Map map;


}

