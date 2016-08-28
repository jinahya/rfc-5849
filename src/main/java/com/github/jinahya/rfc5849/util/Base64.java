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
import static java.lang.Math.ceil;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public final class Base64 {

    private static final byte[] ENCODE = {
        0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48,
        0x49, 0x4A, 0x4B, 0x4C, 0x4D, 0x4E, 0x4F, 0x50,
        0x51, 0x52, 0x53, 0x54, 0x55, 0x56, 0x57, 0x58,
        0x59, 0x5A, 0x61, 0x62, 0x63, 0x64, 0x65, 0x66,
        0x67, 0x68, 0x69, 0x6A, 0x6B, 0x6C, 0x6D, 0x6E,
        0x6F, 0x70, 0x71, 0x72, 0x73, 0x74, 0x75, 0x76,
        0x77, 0x78, 0x79, 0x7A, 0x30, 0x31, 0x32, 0x33,
        0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x2B, 0x2F
    };

    private static final byte[] DECODE = new byte[80];

    private static final byte DELTA = 0x2B; // 43

    static {
        for (byte i = 0; i < ENCODE.length; i++) {
            DECODE[ENCODE[i] - DELTA] = i;
        }
    }

    private static final byte PAD = 0x3D; // '='

    /**
     * Encodes given input.
     *
     * @param input the input.
     *
     * @return encoding output.
     */
    public static byte[] encodeBase64(final byte[] input) {
        if (input == null) {
            throw new NullPointerException("null input");
        }
        if (input.length == 0) {
            return new byte[0];
        }
//        final byte[] output = new byte[((input.length / 3)
//                                        + (input.length % 3 > 0 ? 1 : 0)) * 4];
        final byte[] output = new byte[((int) ceil(input.length / 3.0d)) * 4];
        int index = 0; // output index
        for (int i = 0; i < input.length; i += 3) {
            int word = 0;
            int pads = 0;
            for (int j = 0; j < 3; j++) {
                word <<= 8;
                final int k = i + j;
                if (k < input.length) {
                    word |= input[k] & 0xFF;
                } else {
                    pads++;
                }
            }
            for (int j = 3; j >= 0; j--) {
                output[index + j] = pads-- > 0 ? PAD : ENCODE[word & 0x3F];
                word >>= 6;
            }
            index += 4;
        }
        return output;
    }

    /**
     * Encodes given input and returns a string.
     *
     * @param input the input
     *
     * @return a string
     */
    public static String encodeBase64ToString(final byte[] input) {
        if (input == null) {
            throw new NullPointerException("null input");
        }
        try {
            return new String(encodeBase64(input), "ISO-8859-1");
        } catch (final UnsupportedEncodingException uee) {
            throw new RuntimeException(uee.getMessage());
        }
    }

    /**
     * Decodes given input.
     *
     * @param input the input.
     *
     * @return decoding output.
     */
    public static byte[] decodeBase64(final byte[] input) {
        if (input == null) {
            throw new NullPointerException("null input");
        }
        if (input.length == 0) {
            return new byte[0];
        }
        final byte[] output
                = new byte[input.length / 4 * 3
                           - (input[input.length - 2] == PAD
                              ? 2 : input[input.length - 1] == PAD ? 1 : 0)];
        int index = 0; // output index
        for (int i = 0; i < input.length; i += 4) {
            int word = 0;
            int pads = 0;
            for (int j = 0; j < 4; j++) {
                word <<= 6;
                final int k = i + j;
                if (input[k] == PAD) {
                    pads++;
                } else {
                    word |= DECODE[input[k] - DELTA];
                }
            }
            for (int j = 2; j >= 0; j--) {
                if (pads-- <= 0) {
                    output[index + j] = (byte) (word & 0xFF);
                }
                word >>= 8;
            }
            index += 3;
        }
        return output;
    }

    /**
     * Decodes given string.
     *
     * @param input the input.
     *
     * @return decoding output.
     */
    public static byte[] decodeBase64(final String input) {
        if (input == null) {
            throw new NullPointerException("null input");
        }
        try {
            return decodeBase64(input.getBytes("ISO-8859-1"));
        } catch (final UnsupportedEncodingException uee) {
            throw new RuntimeException(uee.getMessage());
        }
    }

    private Base64() {
        super();
    }
}
