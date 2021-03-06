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
 * An abstract class generating signatures using
 * {@link OAuthConstants#SIGNATURE_METHOD_HMAC_SHA1}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see <a href="https://tools.ietf.org/html/rfc5849#section-3.4.2">3.4.2.
 * HMAC-SHA1</a>
 */
public abstract class OAuthSignatureHmacSha1 extends OAuthSignaturePlaintext {

    /**
     * Creates a new instance.
     */
    public OAuthSignatureHmacSha1() {
        super(OAuthConstants.SIGNATURE_METHOD_HMAC_SHA1);
    }

    // -------------------------------------------------------------------------
    @Override
    public String get() throws Exception {
        final OAuthBaseString baseString = baseString();
        if (baseString == null) {
            throw new IllegalStateException("no baseString set");
        }
        final String keyString = super.get(); // consumerSecret&tokenSecret
        final byte[] keyBytes = keyString.getBytes("ISO-8859-1");
        final byte[] baseBytes = baseString.get().getBytes("ISO-8859-1");
        final byte[] signature = get(keyBytes, baseBytes);
        return encodeBase64ToString(signature);
    }

    abstract byte[] get(byte[] keyBytes, byte[] baseBytes) throws Exception;

    // -------------------------------------------------------------- baseString
    @Override
    public OAuthSignatureHmacSha1 baseString(final OAuthBaseString baseString) {
        return (OAuthSignatureHmacSha1) super.baseString(baseString);
    }

    // ---------------------------------------------------------- consumerSecret
    @Override
    public OAuthSignatureHmacSha1 consumerSecret(final String consumerSecret) {
        return (OAuthSignatureHmacSha1) super.consumerSecret(consumerSecret);
    }

    // ------------------------------------------------------------- tokenSecret
    @Override
    public OAuthSignatureHmacSha1 tokenSecret(final String tokenSecret) {
        return (OAuthSignatureHmacSha1) super.tokenSecret(tokenSecret);
    }
}
