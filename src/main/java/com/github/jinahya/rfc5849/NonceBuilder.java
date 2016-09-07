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

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

/**
 *
 * @author Jin Kwon &lt;jinahya at gmail.com&gt;
 */
public class NonceBuilder implements Builder<String> {

    @Override
    public String build() throws Exception {
        return nonce();
    }

    /**
     * Generates a nonce.
     *
     * @return a nonce
     */
    protected String nonce() {
        if (true) {
            return Long.toString(random().nextLong());
        }
        // http://stackoverflow.com/a/41156/330457
        return new BigInteger(130, random()).toString(32);
    }

    /**
     * Returns a random.
     *
     * @return a random
     */
    protected Random random() {
        if (true) {
            if (random == null) {
                random = new SecureRandom();
            }
            return random;
        }
        Random random_ = random;
        if (random_ == null) {
            random = random_ = new SecureRandom();
        }
        return random_;
    }

    private volatile Random random;
}
