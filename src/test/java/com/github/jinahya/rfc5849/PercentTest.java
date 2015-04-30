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
import java.lang.reflect.Field;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class PercentTest {


    @Test(invocationCount = 128)
    public void encodeDecode() {

        final String expected
            = RandomStringUtils.random(current().nextInt(0, 1024));

        final String encoded = Percent.encode(expected);

        final String actual = Percent.decode(encoded);

        assertEquals(actual, expected);
    }


    @Test(invocationCount = 128)
    public void encodeDecodeWithoutReplaceAll()
        throws ReflectiveOperationException {

        final Field field = Percent.class.getDeclaredField("replaceAll");
        field.setAccessible(true);
        field.set(null, null);

        final String expected
            = RandomStringUtils.random(current().nextInt(0, 1024));

        final String encoded = Percent.encode(expected);

        final String actual = Percent.decode(encoded);

        assertEquals(actual, expected);
    }


    private transient final Logger logger = getLogger(lookup().lookupClass());


}

