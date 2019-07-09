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

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * Tests {@link SimpleOAuthTimestamp}.
 *
 * @author Jin Kwon &lt;jinahya at gmail.com&gt;
 */
@Slf4j
public class SimpleOAuthTimestampTest extends OAuthTimestampTest<SimpleOAuthTimestamp> {

    public SimpleOAuthTimestampTest() {
        super(SimpleOAuthTimestamp.class);
    }

    @Test
    public void get() throws Exception {
        final String timestamp = new SimpleOAuthTimestamp().get();
    }
}
