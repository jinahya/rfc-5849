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

import static java.util.Objects.requireNonNull;
import java.util.concurrent.ThreadLocalRandom;

/**
 * An abstract class for testing {@link OAuthSignature} implementations.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @param <T> signature builder type parameter.
 */
public abstract class OAuthSignatureTest<T extends OAuthSignature> {

    static ThreadLocalRandom random() {
        return ThreadLocalRandom.current();
    }

    /**
     * Creates a new instance.
     *
     * @param signatureClass implementation class.
     */
    public OAuthSignatureTest(final Class<T> signatureClass) {
        super();
        this.signatureClass
                = requireNonNull(signatureClass, "null signatureClass");
    }

    /**
     * Creates a new instance of {@link #signatureClass}. The
     * {@code newInstance()} method of {@code OAuthSignerTest} class returns the
     * result of {@code signerClass#newInstance()}.
     *
     * @return a new instance of {@link #signatureClass}.
     */
    protected T instance() {
        try {
            return signatureClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * the implementation class.
     */
    protected final Class<T> signatureClass;
}
