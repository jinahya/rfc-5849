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
 * Constants for <a href="https://tools.ietf.org/html/rfc5849">The OAuth 1.0
 * Protocol (RFC 5849)</a>
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see <a href="https://tools.ietf.org/html/rfc5849">The OAuth 1.0 Protocol
 * (RFC 5849)</a>
 */
public final class OAuthConstants {

    /**
     * A protocol parameter name whose value is {@value #OAUTH_CALLBACK}.
     */
    public static final String OAUTH_CALLBACK = "oauth_callback";

    /**
     * A protocol parameter name whose value is
     * {@value #OAUTH_CALLBACK_CONFIRMED}.
     */
    public static final String OAUTH_CALLBACK_CONFIRMED
            = "oauth_callback_confirmed";

    /**
     * A protocol parameter value for {@link #OAUTH_CALLBACK} which is
     * {@value #OAUTH_CALLBACK_OUT_OF_BAND}.
     */
    public static final String OAUTH_CALLBACK_OUT_OF_BAND = "oob";

    /**
     * A protocol parameter name whose value is {@value #OAUTH_CONSUMER_KEY}.
     */
    public static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";

    /**
     * A protocol parameter name whose value is {@value #OAUTH_NONCE}.
     */
    public static final String OAUTH_NONCE = "oauth_nonce";

    /**
     * A protocol parameter name whose value is {@value #OAUTH_SIGNATURE}.
     */
    public static final String OAUTH_SIGNATURE = "oauth_signature";

    /**
     * A protocol parameter name whose value is
     * {@value #OAUTH_SIGNATURE_METHOD}.
     */
    public static final String OAUTH_SIGNATURE_METHOD
            = "oauth_signature_method";

    /**
     * A protocol parameter name whose value is {@value #OAUTH_TIMESTAMP}.
     */
    public static final String OAUTH_TIMESTAMP = "oauth_timestamp";

    /**
     * A protocol parameter name whose value is {@value #OAUTH_TOKEN}.
     */
    public static final String OAUTH_TOKEN = "oauth_token";

    /**
     * A protocol parameter name whose value is {@value #OAUTH_TOKEN_SECRET}.
     */
    public static final String OAUTH_TOKEN_SECRET = "oauth_token_secret";

    /**
     * A protocol parameter name whose value is {@value #OAUTH_VERIFIER}.
     */
    public static final String OAUTH_VERIFIER = "oauth_verifier";

    /**
     * A protocol parameter name whose value is {@value #OAUTH_VERSION}.
     */
    public static final String OAUTH_VERSION = "oauth_version";

    private OAuthConstants() {
        super();
    }
}
