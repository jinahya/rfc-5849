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

import static com.github.jinahya.rfc5849._Percent.encodePercent;
import java.security.SecureRandom;
import java.util.Random;
import static com.github.jinahya.rfc5849._Percent.encodePercent;
import static com.github.jinahya.rfc5849._Percent.encodePercent;
import static com.github.jinahya.rfc5849._Percent.encodePercent;

/**
 * A simple nonce builder.
 *
 * @author Jin Kwon &lt;jinahya at gmail.com&gt;
 * @see <a href="https://tools.ietf.org/html/rfc5849#section-3.3">3.3. Nonce and
 * Timestamp (RFC 5849)</a>
 */
public class SimpleOAuthNonce implements OAuthNonce {

    /**
     * Returns a nonce builder with given identifiers.
     *
     * @param identifiers identifies for specifying client
     * @return a nonce builder
     */
    public static OAuthNonce of(final String... identifiers) {
        final StringBuilder builder = new StringBuilder();
        for (final String identifier : identifiers) {
            builder.append(encodePercent(String.valueOf(identifier)))
                    .append("-");
        }
        final String prefix = builder.toString();
        return new SimpleOAuthNonce() {
            @Override
            public String generate() {
                return prefix + super.generate();
            }
        };
    }

    /**
     * {@inheritDoc}. This methods returns the value of
     * {@link Random#nextLong()} invoked on the value from {@link #random}.
     *
     * @return a nonce value
     */
    @Override
    public String generate() {
        return Long.toString(random().nextLong());
    }

    /**
     * Returns a random.
     *
     * @return a random
     */
    protected Random random() {
        if (random == null) {
            random = new SecureRandom();
        }
        return random;
    }

    private Random random;
}
