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


import java.security.SecureRandom;
import java.util.Random;


/**
 *
 * @author Jin Kwon &lt;jinahya at gmail.com&gt;
 */
public class NonceBuilder implements Builder<String> {


    public static NonceBuilder newInstance() {

        return new NonceBuilder(new SecureRandom());
    }


    public NonceBuilder(final Random random) {

        super();

        if (random == null) {
            throw new NullPointerException("null random");
        }

        this.random = random;
    }


    @Override
    public String build() throws Exception {

        return Long.toString((System.currentTimeMillis() << 20)
                             | random.nextInt(1048576));
    }


    protected final Random random;


}

