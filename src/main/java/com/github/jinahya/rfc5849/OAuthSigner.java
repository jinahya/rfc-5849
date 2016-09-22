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
 * An interface for generating values for
 * {@link OAuthConstants#OAUTH_SIGNATURE}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see <a href="https://tools.ietf.org/html/rfc5849#section-3.4">3.4. Signature
 * (RFC 5849)</a>
 */
public abstract class OAuthSigner {//implements Builder<String> {

    static OAuthSigner of(final String prebuilt) {
        return new OAuthSigner("irrelevant") {
            public String sign() {
                return prebuilt;
            }
        };
    }

    // ------------------------------------------------------------ constructors
    OAuthSigner(final String signatureMethod) {
        super();
        if (signatureMethod == null) {
            throw new NullPointerException("null signatureMethod");
        }
        this.signatureMethod = signatureMethod;
    }

    // -------------------------------------------------------------------------
    public abstract String sign() throws Exception;

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

    // ------------------------------------------------------- baseStringBuilder
    /**
     * Returns the {@code baseStringBuilder}.
     *
     * @return the {@code baseStringBuilder}
     */
    OAuthBaseString baseStringBuilder() {
        return baseStringBuilder;
    }

    /**
     * Sets the {@code baseStringBuilder}.
     *
     * @param baseStringBuilder a {@link OAuthBaseString}
     * @return this instance
     */
    public OAuthSigner baseStringBuilder(
            final OAuthBaseString baseStringBuilder) {
        if (baseStringBuilder == null) {
            throw new NullPointerException("null baseStringBuilder");
        }
        this.baseStringBuilder = baseStringBuilder;
        this.baseStringBuilder.oauthSignatureMethod(signatureMethod);
        return this;
    }

    // -------------------------------------------------------------- baseString
    @Deprecated
    OAuthSigner baseString(final String baseString) {
        if (baseString == null) {
            throw new NullPointerException("null baseString");
        }
        return baseStringBuilder(new OAuthBaseString() {
            @Override
            public String build() {
                return baseString;
            }
        });
    }

    // -------------------------------------------------------------------------
    private final String signatureMethod;

    private OAuthBaseString baseStringBuilder;
}
