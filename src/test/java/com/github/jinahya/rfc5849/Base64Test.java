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


import static java.lang.invoke.MethodHandles.lookup;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class Base64Test {


    @Test(invocationCount = 128)
    public void encode() {

        final byte[] expected = new byte[current().nextInt(1024)];
        current().nextBytes(expected);

        final byte[] encoded = Base64.encode(expected);

        final byte[] actual = java.util.Base64.getDecoder().decode(encoded);

        assertEquals(actual, expected);
    }


    @Test
    public void encodeEmptyBytes() {

        final byte[] encoded = Base64.encode(new byte[0]);

        assertNotNull(encoded);
        assertEquals(encoded.length, 0);
    }


    @Test(expectedExceptions = {NullPointerException.class})
    public void encodeNullBytes() {

        Base64.encode(null);
    }


    @Test(invocationCount = 128)
    public void decode() {

        final byte[] expected = new byte[current().nextInt(1024)];
        current().nextBytes(expected);

        final byte[] encoded = java.util.Base64.getEncoder().encode(expected);

        final byte[] actual = Base64.decode(encoded);

        assertEquals(actual, expected);
    }


    @Test(expectedExceptions = {NullPointerException.class})
    public void decodeNullBytes() {

        Base64.decode((byte[]) null);
    }


    @Test
    public void decodeEmptyBytes() {

        final byte[] decoded = Base64.decode(new byte[0]);

        assertNotNull(decoded);
        assertEquals(decoded.length, 0);
    }


    private transient final Logger logger = getLogger(lookup().lookupClass());


}

