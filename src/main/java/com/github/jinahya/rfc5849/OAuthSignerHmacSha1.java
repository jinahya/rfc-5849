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
 * A signature builder for {@code HMAC-SHA1}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see <a href="https://tools.ietf.org/html/rfc5849#section-3.4.2">3.4.2.
 * HMAC-SHA1</a>
 */
public abstract class OAuthSignerHmacSha1 extends OAuthSignerPlaintext {

    /**
     * The signature method value.
     */
    private static final String SIGNATURE_METHOD = "HMAC-SHA1";

    /**
     * Creates a new instance.
     */
    public OAuthSignerHmacSha1() {
        super(SIGNATURE_METHOD);
    }

    // -------------------------------------------------------------------------
    @Override
    public String sign() throws Exception {
        final OAuthBaseString baseStringBuilder = baseStringBuilder();
        if (baseStringBuilder == null) {
            throw new IllegalStateException("no baseStringBuilder set");
        }
        final String keyString = super.sign(); // consumerSecret&tokenSecret
        final byte[] keyBytes = keyString.getBytes("ISO-8859-1");
        final String baseString = baseStringBuilder.build();
        final byte[] baseBytes = baseString.getBytes("ISO-8859-1");
        final byte[] built = build(keyBytes, baseBytes);
        return encodeBase64ToString(built);
    }

    abstract byte[] build(byte[] keyBytes, byte[] baseBytes) throws Exception;
}
