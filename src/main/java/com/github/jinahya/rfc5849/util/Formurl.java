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
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public final class Formurl {


    public static String encode(final String decoded) {

        if (decoded == null) {
            throw new NullPointerException("null decoded");
        }

        try {
            return URLEncoder.encode(decoded, "UTF-8");
        } catch (final UnsupportedEncodingException uee) {
            uee.printStackTrace(System.err);
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
//
//
//    public static List<String> encode(final List<String> encodedList) {
//
//        if (encodedList == null) {
//            throw new NullPointerException("null encodedList");
//        }
//
//        return encode(encodedList, new ArrayList<String>(encodedList.size()));
//    }
    public static String decode(final String encoded) {

        if (encoded == null) {
            throw new NullPointerException("null encoded");
        }

        try {
            return URLDecoder.decode(encoded, "UTF-8");
        } catch (final UnsupportedEncodingException uee) {
            uee.printStackTrace(System.err);
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
//
//
//    public static List<String> decode(final List<String> encodedList) {
//
//        if (encodedList == null) {
//            throw new NullPointerException("null encodedList");
//        }
//
//        return decode(encodedList, new ArrayList<String>(encodedList.size()));
//    }
    private Formurl() {

        super();
    }


}

