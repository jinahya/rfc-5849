/*
 * Copyright 2015 Jin Kwon &lt;jinahya at gmail.com&gt;.
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
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;jinahya at gmail.com&gt;
 */
public class NonceBuilderTest {

    private static final Logger logger = getLogger(lookup().lookupClass());

    @Test//(invocationCount = 128)
    public void build() throws Exception {
        final Set<String> set = new HashSet<>();
        for (int i = 0; i < 1048576; i++) {
            final String built = new SimpleNonceBuilder().build();
            assertTrue(set.add(built));
        }
    }

//    @Test
    public void test() throws Exception {
        final String built1 = new SimpleNonceBuilder().build();
        final String built2 = new SimpleNonceBuilder().build();
        assertNotEquals(built1, built2);
    }
}
