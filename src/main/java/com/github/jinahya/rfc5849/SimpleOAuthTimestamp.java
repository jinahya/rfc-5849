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

import static java.lang.System.currentTimeMillis;

/**
 * A simple timestamp generator.
 *
 * @author Jin Kwon &lt;jinahya at gmail.com&gt;
 * @see <a href="https://tools.ietf.org/html/rfc5849#section-3.3">3.3. Nonce and
 * Timestamp (RFC 5849)</a>
 */
public class SimpleOAuthTimestamp implements OAuthTimestamp {

    /**
     * {@inheritDoc} This method returns the value
     * {@link System#currentTimeMillis()} divided by {@code 1000}.
     *
     * @return {@inheritDoc}
     */
    @Override
    public String get() {
        return Long.toString(currentTimeMillis() / 1000L);
    }
}
