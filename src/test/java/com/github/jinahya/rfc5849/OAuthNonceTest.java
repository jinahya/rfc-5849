/*
 * Copyright 2016 Jin Kwon &lt;onacit at gmail.com&gt;.
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

/**
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 * @param <T> OAuthNonce type parameter
 */
public abstract class OAuthNonceTest<T extends OAuthNonce> {

    public OAuthNonceTest(final Class<T> oAuthNonceClass) {
        super();
        this.oAuthNonceClass
                = requireNonNull(oAuthNonceClass, "null oAuthNonceClass");
    }

    protected T newInstance() {
        try {
            return oAuthNonceClass.newInstance();
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException(roe);
        }
    }

    protected final Class<T> oAuthNonceClass;
}
