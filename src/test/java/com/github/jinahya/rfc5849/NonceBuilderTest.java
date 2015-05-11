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
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertNotEquals;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon &lt;jinahya at gmail.com&gt;
 */
public class NonceBuilderTest {


    @Test
    public void build() throws Exception {

        final String built = new NonceBuilder().build();
    }


    @Test
    public void test() throws Exception {

        final String built1 = new NonceBuilder().build();
        final String built2 = new NonceBuilder().build();
        logger.debug("built1: {}", built1);
        logger.debug("built2: {}", built2);
        assertNotEquals(built1, built2);
    }


    private transient final Logger logger = getLogger(lookup().lookupClass());


}

