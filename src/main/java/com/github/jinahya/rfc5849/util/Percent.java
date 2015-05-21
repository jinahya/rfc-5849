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


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public final class Percent {


    private static boolean unreserved(final int b) {

        return (b >= 0x30 && b <= 0x39) // digits
               || (b >= 0x41 && b <= 0x5A) // uppercase letters
               || (b >= 0x61 && b <= 0x7A) // lowercase letters
               || b == 0x2D // '-'
               || b == 0x2E // '.'
               || b == 0x5F // '_'
               || b == 0x7E; // '~'
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
            if (unreserved(source[i])) {
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
     * Percent-encodes given string.
     *
     * @param s the string to encode
     *
     * @return encoding result.
     */
    public static String encode(final String s) {

        try {
            return encode(s, "UTF-8");
        } catch (final UnsupportedEncodingException uee) {
            throw new RuntimeException(uee.getMessage());
        }
    }


//    public static List<String> encode(final List<String> decodedList,
//                                      final List<String> encodedList) {
//
//        if (decodedList == null) {
//            throw new NullPointerException("null decodedList");
//        }
//
//        if (encodedList == null) {
//            throw new NullPointerException("null encodedList");
//        }
//
//        for (final String decoded : decodedList) {
//            encodedList.add(encode(decoded));
//        }
//
//        return encodedList;
//    }
//    public static List<String> encode(final List<String> encodedList) {
//
//        if (encodedList == null) {
//            throw new NullPointerException("null encodedList");
//        }
//
//        return encode(encodedList, new ArrayList<String>(encodedList.size()));
//    }
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
            if (unreserved(source[i])) {
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


    /**
     * Percent-decodes given string.
     *
     * @param s the string to decode
     *
     * @return decoding result.
     */
    public static String decode(final String s) {

        try {
            return decode(s, "UTF-8");
        } catch (final UnsupportedEncodingException uee) {
            throw new RuntimeException(uee.getMessage());
        }
    }


//    public static List<String> decode(final List<String> encodedList,
//                                      final List<String> decodedList) {
//
//        if (encodedList == null) {
//            throw new NullPointerException("null encodedList");
//        }
//
//        if (decodedList == null) {
//            throw new NullPointerException("null decodedList");
//        }
//
//        for (final String encoded : encodedList) {
//            decodedList.add(decode(encoded));
//        }
//
//        return decodedList;
//    }
//    public static List<String> decode(final List<String> encodedList) {
//
//        if (encodedList == null) {
//            throw new NullPointerException("null encodedList");
//        }
//
//        final List<String> decodedList
//            = decode(encodedList, new ArrayList<String>(encodedList.size()));
//
//        return decodedList;
//    }
    private Percent() {

        super();
    }


}

