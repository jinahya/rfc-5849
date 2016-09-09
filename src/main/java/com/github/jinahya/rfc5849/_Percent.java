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
package com.github.jinahya.rfc5849;

import static com.github.jinahya.rfc5849._Hex.decodeHexSingle;
import static com.github.jinahya.rfc5849._Hex.encodeHexSingle;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class _Percent {

    private static boolean unreserved(final int b) {
        return (b >= 0x30 && b <= 0x39) // digits
               || (b >= 0x41 && b <= 0x5A) // uppercase letters
               || (b >= 0x61 && b <= 0x7A) // lowercase letters
               || b == 0x2D // '-'
               || b == 0x2E // '.'
               || b == 0x5F // '_'
               || b == 0x7E; // '~'
    }

    static String encodePercent(final String s, final String enc)
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
            encodeHexSingle(source[i], auxiliary, j);
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
    static String encodePercent(final String s) {
        try {
            return encodePercent(s, "UTF-8");
        } catch (final UnsupportedEncodingException uee) {
            throw new RuntimeException(uee.getMessage());
        }
    }

    static String decodePercent(final String s, final String enc)
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
            auxiliary[j++] = (byte) decodeHexSingle(source, i);
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
    static String decodePercent(final String s) {
        try {
            return decodePercent(s, "UTF-8");
        } catch (final UnsupportedEncodingException uee) {
            throw new RuntimeException(uee.getMessage());
        }
    }

    private _Percent() {
        super();
    }
}
