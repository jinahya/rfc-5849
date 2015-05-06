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


package com.github.jinahya.rfc5849.util;


import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public final class Percent {


    private static Method replaceAll = null;


    static {
        try {
            replaceAll = String.class.getMethod(
                "replaceAll", new Class[]{String.class, String.class});
        } catch (final NoSuchMethodException nsme) {
            nsme.printStackTrace();
        }
    }


    /**
     * Percent-encodes given string.
     *
     * @param s the string to encode
     *
     * @return encoding result.
     */
    public static String encode(String s) {

        try {
            s = URLEncoder.encode(s, "UTF-8");
        } catch (final UnsupportedEncodingException uee) {
            throw new RuntimeException(uee);
        }

        if (replaceAll != null) {
            try {
                s = (String) replaceAll.invoke(s, new Object[]{"\\+", "%20"});
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }

        if (s.indexOf('+') != -1) {
            final StringBuffer buffer = new StringBuffer(s);
            for (int i = buffer.length() - 1; i >= 0; i--) {
                if (buffer.charAt(i) == '+') {
                    buffer.deleteCharAt(i);
                    buffer.insert(i, "%20");
                }
            }
            s = buffer.toString();
        }

        return s;
    }


    /**
     * Percent-decodes given string.
     *
     * @param s the string to decode
     *
     * @return decoding result.
     */
    public static String decode(String s) {

        if (replaceAll != null) {
            try {
                s = (String) replaceAll.invoke(s, new Object[]{"%20", "+"});
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }

        if (s.indexOf("%20") != -1) {
            final StringBuffer buffer = new StringBuffer(s);
            for (int i = buffer.length() - 1; i >= 0; i--) {
                try {
                    if (buffer.charAt(i) == '0'
                        && buffer.charAt(--i) == '2'
                        && buffer.charAt(--i) == '%') {
                        buffer.replace(i, i + 3, "+");
                    }
                } catch (final IndexOutOfBoundsException ioobe) {
                }
            }
        }

        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (final UnsupportedEncodingException uee) {
            throw new RuntimeException(uee);
        }
    }


    private Percent() {

        super();
    }


}

