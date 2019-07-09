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

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import static com.github.jinahya.rfc5849._Base64.encodeBase64;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Slf4j
public class _Base64Test {

    @Test(invocationCount = 128)
    public void encode() {
        final byte[] expected = new byte[current().nextInt(1024)];
        current().nextBytes(expected);
        final byte[] encoded = encodeBase64(expected);
        final byte[] actual = java.util.Base64.getDecoder().decode(encoded);
        assertEquals(actual, expected);
    }

    @Test
    public void encodeEmptyBytes() {
        final byte[] encoded = encodeBase64(new byte[0]);
        assertNotNull(encoded);
        assertEquals(encoded.length, 0);
    }

    @Test(expectedExceptions = {NullPointerException.class})
    public void encodeNullBytes() {
        encodeBase64(null);
    }

    @Test(invocationCount = 128)
    public void decode() {
        final byte[] expected = new byte[current().nextInt(1024)];
        current().nextBytes(expected);
        final byte[] encoded = java.util.Base64.getEncoder().encode(expected);
        final byte[] actual = _Base64.decodeBase64(encoded);
        assertEquals(actual, expected);
    }

    @Test(expectedExceptions = {NullPointerException.class})
    public void decodeNullBytes() {
        _Base64.decodeBase64((byte[]) null);
    }

    @Test
    public void decodeEmptyBytes() {
        final byte[] decoded = _Base64.decodeBase64(new byte[0]);
        assertNotNull(decoded);
        assertEquals(decoded.length, 0);
    }
}
