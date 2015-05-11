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


import com.github.jinahya.rfc5849.util.Params;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map.Entry;
import java.util.StringTokenizer;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class Form extends Params {


    public StringBuilder encode(final StringBuilder builder) {

        if (builder == null) {
            throw new NullPointerException("null builder");
        }

        for (final Entry<String, List<String>> e
             : map(false, false).entrySet()) {
            final String keys = e.getKey();
            final List<String> values = e.getValue();
            for (final String value : values) {
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


    public String encode() {

        return encode(new StringBuilder()).toString();
    }


    public Form decode(final String encoded) {

        if (encoded == null) {
            throw new NullPointerException("null encoded");
        }

        for (final StringTokenizer t = new StringTokenizer(encoded, "&");
             t.hasMoreTokens();) {
            final String token = t.nextToken();
            final int i = token.indexOf('=');
            String key = token;
            String value = "";
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

