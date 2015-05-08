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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public final class Percent {


    private static Method replaceAll = null;


    static {
        try {
            replaceAll = String.class.getMethod(
                "replaceAll", new Class<?>[]{String.class, String.class});
        } catch (final NoSuchMethodException nsme) {
            nsme.printStackTrace(System.out);
        }
    }


    private static final Method ENCODE;


    static {
        try {
            ENCODE = Class.forName("java.net.URLEncoder").getMethod(
                "encode", new Class<?>[]{String.class, String.class});
        } catch (final Exception e) {
            throw new InstantiationError(e.getMessage());
        }
    }


    private static final Method DECODE;


    static {
        try {
            DECODE = Class.forName("java.net.URLDecoder").getMethod(
                "decode", new Class<?>[]{String.class, String.class});
        } catch (final Exception e) {
            throw new InstantiationError(e.getMessage());
        }
    }


    public static final List<Boolean> STRING_BUILDER_FOUND;


    static {
        final List<Boolean> list = new ArrayList<Boolean>(1);
        try {
            Class.forName("java.lang.StringBuilder");
            list.add(Boolean.TRUE);
        } catch (final ClassNotFoundException snfe) {
            list.add(Boolean.FALSE);
        }
        STRING_BUILDER_FOUND = Collections.unmodifiableList(list);
    }


    /**
     * Percent-encodes given string.
     *
     * @param s the string to encode
     *
     * @return encoding result.
     */
    public static String encode(String s) {

        if (true) {
            try {
                return encode(s, "UTF-8");
            } catch (final UnsupportedEncodingException uee) {
                throw new RuntimeException(uee.getMessage());
            }
        }

        try {
            s = (String) ENCODE.invoke(null, s, "UTF-8");
        } catch (final Exception e) {
            //throw new RuntimeException(e);
            e.printStackTrace(System.err);
            throw new RuntimeException(e.getMessage());
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


    public static String encode(final String s, final String enc)
        throws UnsupportedEncodingException {

        if (s == null) {
            throw new NullPointerException("null decoded");
        }

        if (enc == null) {
            throw new NullPointerException("null enc");
        }

        final byte[] source = s.getBytes(enc);
        final byte[] auxiliary = new byte[source.length * 3];

        int j = 0;
        for (int i = 0; i < source.length; i++) {
            if ((source[i] >= 0x30 && source[i] <= 0x39)
                || (source[i] >= 0x41 && source[i] <= 0x5A)
                || (source[i] >= 0x61 && source[i] <= 0x7A)
                || source[i] == 0x2D
                || source[i] == 0x2E
                || source[i] == 0x5F
                || source[i] == 0x7E) {
                auxiliary[j++] = source[i];
                continue;
            }
            auxiliary[j++] = 0x25;
            Hex.encodeSingle(source[i], auxiliary, j);
            j += 2;
        }

        final byte[] target = new byte[j];
        System.arraycopy(auxiliary, 0, target, 0, j);

        return new String(target, "US-ASCII");
    }


    /**
     * Percent-decodes given string.
     *
     * @param s the string to decode
     *
     * @return decoding result.
     */
    public static String decode(String s) {

        if (true) {
            try {
                return decode(s, "UTF-8");
            } catch (final UnsupportedEncodingException uee) {
                throw new RuntimeException(uee.getMessage());
            }
        }

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
            return (String) DECODE.invoke(null, s, "UTF-8");
        } catch (final Exception e) {
            //throw new RuntimeException(e);
            e.printStackTrace(System.err);
            throw new RuntimeException(e.getMessage());
        }
    }


    public static String decode(final String s, final String enc)
        throws UnsupportedEncodingException {

        if (s == null) {
            throw new NullPointerException("null s");
        }

        if (enc == null) {
            throw new NullPointerException("null enc");
        }

        final byte[] source = s.getBytes("US-ASCII");
        final byte[] auxiliary = new byte[source.length];

        int j = 0;
        for (int i = 0; i < source.length; i++) {
            if ((source[i] >= 0x30 && source[i] <= 0x39)
                || (source[i] >= 0x41 && source[i] <= 0x5A)
                || (source[i] >= 0x61 && source[i] <= 0x7A)
                || source[i] == 0x2D
                || source[i] == 0x2E
                || source[i] == 0x5F
                || source[i] == 0x7E) {
                auxiliary[j++] = source[i];
                continue;
            }
            i++; // 0x25
            auxiliary[j++] = (byte) Hex.decodeSingle(source, i);
            i++;
        }

        final byte[] target = new byte[j];
        System.arraycopy(auxiliary, 0, target, 0, j);

        return new String(target, enc);
    }


    private Percent() {

        super();
    }


}

