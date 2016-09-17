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

import static com.github.jinahya.rfc5849._Base64.encodeBase64ToString;

/**
 * An abstract class for signature builder implementation whose signature method
 * is {@value #SIGNATURE_METHOD}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @param <T> initParam type parameter
 * @see <a href="https://tools.ietf.org/html/rfc5849#section-3.4.3">3.4.3.
 * RSA-SHA1 (RFC 5849)</a>
 */
public abstract class SignatureBuilderRsaSha1<T> extends SignatureBuilder {

    public static final String SIGNATURE_METHOD = "RSA-SHA1";

    // -------------------------------------------------------------------------
    /**
     * Creates a new instance.
     */
    public SignatureBuilderRsaSha1() {
        super(SIGNATURE_METHOD);
    }

    // -------------------------------------------------------------------------
    /**
     * Builds the signature value.
     *
     * @return this instance
     * @throws Exception if failed to build
     */
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
     * Sets a initialization parameter.
     *
     * @param initParam a initialization parameter.
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
