/*
 * Copyright 2016 Jin Kwon &lt;onacit_at_gmail.com&gt;.
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

/**
 * Constants for signature methods.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see <a href="https://tools.ietf.org/html/rfc5849#section-3.4">3.4.
 * Signature</a>
 */
@Deprecated
public enum SignatureMethod {

    /**
     * Constant for {@code HMAC-SHA1} signature method.
     *
     * @see <a href="https://tools.ietf.org/html/rfc5849#section-3.4.2">3.4.2.
     * HMAC-SHA1</a>
     */
    HMAC_SHA1("HMAC-SHA1"),
    /**
     * Constant for {@code RSA-SHA1} signature method.
     *
     * @see <a href="https://tools.ietf.org/html/rfc5849#section-3.4.3">3.4.3.
     * RSA-SHA1</a>
     */
    RSA_SHA1("RSA-SHA1"),
    /**
     * Constant for {@code PLAINTEXT} signature method.
     *
     * @see <a href="https://tools.ietf.org/html/rfc5849#section-3.4.4">3.4.4.
     * PLAINTEXT</a>
     */
    PLAINTEXT("PLAINTEXT");

    public static SignatureMethod valueOfSignatureMethod(
            final String signatureMethod) {
        for (final SignatureMethod value : values()) {
            if (value.signatureMethod.equals(signatureMethod)) {
                return value;
            }
        }
        throw new IllegalArgumentException("no value for " + signatureMethod);
    }

    private SignatureMethod(final String signatureMethod) {
        this.signatureMethod = signatureMethod;
    }

    public String signatureMethod() {
        return signatureMethod;
    }

    private final String signatureMethod;
}
