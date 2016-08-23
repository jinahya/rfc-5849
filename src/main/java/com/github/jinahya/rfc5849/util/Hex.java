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

/**
 * A utility class for encoding/decoding hex.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
class Hex {

    private static int encodeHalf(final int decoded) {
        switch (decoded) {
            case 0x00:
            case 0x01:
            case 0x02:
            case 0x03:
            case 0x04:
            case 0x05:
            case 0x06:
            case 0x07:
            case 0x08:
            case 0x09:
                return decoded + 0x30; // 0x30('0') ~ 0x39('9')
            case 0x0A:
            case 0x0B:
            case 0x0C:
            case 0x0D:
            case 0x0E:
            case 0x0F:
                return decoded + 0x37; // 0x41('A') ~ 0x46('F')
            default:
                throw new IllegalArgumentException(
                        "illegal nibble: " + decoded);
        }
    }

    static void encodeHexSingle(final int input, final byte[] output,
                                final int outoff) {
        output[outoff] = (byte) encodeHalf((input >> 4) & 0x0F);
        output[outoff + 1] = (byte) encodeHalf(input & 0x0F);
    }

    private static int decodeHalf(final int input) {
        switch (input) {
            case 0x30: // '0'
            case 0x31: // '1'
            case 0x32: // '2'
            case 0x33: // '3'
            case 0x34: // '4'
            case 0x35: // '5'
            case 0x36: // '6'
            case 0x37: // '7'
            case 0x38: // '8'
            case 0x39: // '9'
                return input - 0x30;
            case 0x41: // 'A'
            case 0x42: // 'B'
            case 0x43: // 'C'
            case 0x44: // 'D'
            case 0x45: // 'E'
            case 0x46: // 'F'
                return input - 0x37;
            case 0x61: // 'a'
            case 0x62: // 'b'
            case 0x63: // 'c'
            case 0x64: // 'd'
            case 0x65: // 'e'
            case 0x66: // 'f'
                return input - 0x57;
            default:
                throw new IllegalArgumentException("illegal input: " + input);
        }
    }

    static int decodeHexSingle(final byte[] input, final int inoff) {
        return (decodeHalf(input[inoff] & 0xFF) << 4)
               | decodeHalf(input[inoff + 1] & 0xFF);
    }

    private Hex() {
        super();
    }
}
