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

import static com.github.jinahya.rfc5849._Percent.encodePercent;

/**
 * A signature builder for {@link OAuthConstants#SIGNATURE_METHOD_PLAINTEXT}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see <a href="https://tools.ietf.org/html/rfc5849#section-3.4.4">3.4.4.
 * PLAINTEXT (RFC-5849)</a>
 */
public class OAuthSignaturePlaintext extends OAuthSignature {

    // -------------------------------------------------------------------------
    OAuthSignaturePlaintext(final String signatureMethod) {
        super(signatureMethod);
    }

    /**
     * Creates a new instance.
     */
    public OAuthSignaturePlaintext() {
        this(OAuthConstants.SIGNATURE_METHOD_PLAINTEXT);
    }

    // -------------------------------------------------------------------------
    @Override
    public String get() throws Exception {
        if (consumerSecret == null) {
            throw new IllegalStateException("no consumerSecret set");
        }
        if (tokenSecret == null) {
            throw new IllegalStateException("no tokenSecret set");
        }
        return encodePercent(consumerSecret) + "&" + encodePercent(tokenSecret);
    }

    // -------------------------------------------------------------- baseString
    @Override
    public OAuthSignaturePlaintext baseString(
            final OAuthBaseString baseString) {
        return (OAuthSignaturePlaintext) super.baseString(baseString);
    }

    // ---------------------------------------------------------- consumerSecret
    /**
     * Replaces the value of {@code consumerSecret} with given and return this
     * instance.
     *
     * @param consumerSecret the consumer secret.
     * @return this instance
     */
    public OAuthSignaturePlaintext consumerSecret(final String consumerSecret) {
        this.consumerSecret = consumerSecret;
        return this;
    }

    // ------------------------------------------------------------- tokenSecret
    /**
     * Replaces the value of {@code tokenSecrent} with given value and returns
     * this instance.
     *
     * @param tokenSecret the token secret
     * @return this instance
     */
    public OAuthSignaturePlaintext tokenSecret(final String tokenSecret) {
        this.tokenSecret = tokenSecret;
        return this;
    }

    // -------------------------------------------------------------------------
    private String consumerSecret;

    private String tokenSecret;
}
