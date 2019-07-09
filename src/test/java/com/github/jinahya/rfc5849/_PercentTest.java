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
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;

import static com.github.jinahya.rfc5849._Percent.decodePercent;
import static com.github.jinahya.rfc5849._Percent.encodePercent;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.testng.Assert.assertEquals;

/**
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Slf4j
public class _PercentTest {

    @Test(invocationCount = 128)
    public void encodeDecode() {
        final String expected = RandomStringUtils.random(current().nextInt(0, 1024));
        final String encoded = encodePercent(expected);
        final String actual = decodePercent(encoded);
        assertEquals(actual, expected);
    }
}
