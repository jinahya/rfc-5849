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

/**
 * An interface for signing requests and generating values for
 * {@link OAuthConstants#OAUTH_SIGNATURE}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see <a href="https://tools.ietf.org/html/rfc5849#section-3.4">3.4. Signature
 * (RFC 5849)</a>
 */
public abstract class OAuthSignature {

    // ------------------------------------------------------------ constructors
    OAuthSignature(final String signatureMethod) {
        super();
        if (signatureMethod == null) {
            throw new NullPointerException("null signatureMethod");
        }
        this.signatureMethod = signatureMethod;
    }

    // -------------------------------------------------------------------------
    /**
     * Generates a signature value for {@link OAuthConstants#OAUTH_SIGNATURE}.
     *
     * @return a signature value
     * @throws Exception if an error occurs.
     */
    abstract String get() throws Exception;

    // --------------------------------------------------------- signatureMethod
    /**
     * Returns signature method.
     *
     * @return signature method.
     */
    @Deprecated
    String signatureMethod() {
        return signatureMethod;
    }

    // -------------------------------------------------------------- baseString
    /**
     * Returns the {@code baseString}.
     *
     * @return the {@code baseString}
     */
    OAuthBaseString baseString() {
        return baseString;
    }

    /**
     * Sets the {@code baseString}.
     *
     * @param baseString the {@code baseString}.
     * @return this instance
     */
    public OAuthSignature baseString(final OAuthBaseString baseString) {
        if (baseString == null) {
            throw new NullPointerException("null baseString");
        }
        this.baseString = baseString;
        this.baseString.oauthSignatureMethod(signatureMethod);
        return this;
    }

    // -------------------------------------------------------------------------
    private final String signatureMethod;

    private OAuthBaseString baseString;
}
