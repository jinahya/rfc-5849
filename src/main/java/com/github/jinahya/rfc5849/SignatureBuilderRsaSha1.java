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

import static com.github.jinahya.rfc5849.util.Base64.encodeBase64ToString;

/**
 * An abstract class for signature builder implementation whose signature method
 * is {@value #SIGNATURE_METHOD}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @param <T> init param type parameter
 */
public abstract class SignatureBuilderRsaSha1<T> extends SignatureBuilder {

    /**
     * The signature method value which is {@value #SIGNATURE_METHOD}.
     */
    public static final String SIGNATURE_METHOD = "RSA-SHA1";

    // -------------------------------------------------------------------------
    /**
     * Creates a new instance.
     */
    public SignatureBuilderRsaSha1() {
        super(SIGNATURE_METHOD);
    }

    // -------------------------------------------------------------------------
    @Override
    public String build() throws Exception {
        final BaseStringBuilder baseStringBuilder = baseStringBuilder();
        if (baseStringBuilder == null) {
            throw new IllegalStateException("no baseStringBuilder set");
        }
        if (initParam == null) {
            throw new IllegalStateException("no initParam set");
        }
        final String baseString = baseStringBuilder.build();
        final byte[] baseBytes = baseString.getBytes("ISO-8859-1");
        final byte[] built = build(initParam, baseBytes);
        return encodeBase64ToString(built);
    }

    abstract byte[] build(T initParam, byte[] baseBytes) throws Exception;

    // -------------------------------------------------------------------------
    /**
     * Sets a private key.
     *
     * @param initParam the private key.
     * @return this instance.
     */
    public SignatureBuilderRsaSha1<T> initParam(final T initParam) {
        this.initParam = initParam;
        return this;
    }

    // -------------------------------------------------------------------------
    /**
     * The init param.
     */
    private T initParam;
}
