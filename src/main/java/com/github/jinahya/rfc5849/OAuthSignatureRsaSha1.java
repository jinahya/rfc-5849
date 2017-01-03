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
 * An abstract class for signing the request with {@value #SIGNATURE_METHOD}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @param <T> initParam type parameter
 * @see <a href="https://tools.ietf.org/html/rfc5849#section-3.4.3">3.4.3.
 * RSA-SHA1 (RFC 5849)</a>
 */
public abstract class OAuthSignatureRsaSha1<T> extends OAuthSignature {

    // -------------------------------------------------------------------------
    /**
     * Creates a new instance.
     */
    public OAuthSignatureRsaSha1() {
        super(OAuthConstants.SIGNATURE_METHOD_RSA_SHA1);
    }

    // -------------------------------------------------------------------------
    @Override
    public String get() throws Exception {
        final OAuthBaseString baseStringBuilder = baseString();
        if (baseStringBuilder == null) {
            throw new IllegalStateException("no baseString set");
        }
        if (initParam == null) {
            throw new IllegalStateException("no initParam set");
        }
        final String baseString = baseStringBuilder.get();
        final byte[] baseBytes = baseString.getBytes("ISO-8859-1");
        final byte[] signature = get(initParam, baseBytes);
        return encodeBase64ToString(signature);
    }

    abstract byte[] get(T initParam, byte[] baseBytes) throws Exception;

    // -------------------------------------------------------------------------
    /**
     * Sets a initialization parameter.
     *
     * @param initParam a initialization parameter.
     * @return this instance.
     */
    public OAuthSignatureRsaSha1<T> initParam(final T initParam) {
        this.initParam = initParam;
        return this;
    }

    // -------------------------------------------------------------------------
    /**
     * The init param.
     */
    private T initParam;
}
