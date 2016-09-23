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

import static com.github.jinahya.rfc5849._Formurl.encodeFormurl;
import static com.github.jinahya.rfc5849._Percent.encodePercent;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A builder for building request authentication information.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see <a href="https://tools.ietf.org/html/rfc5849#section-3.5.1">3.5.1.
 * Authorization Header (RFC 5849)</a>
 */
public class OAuthAuthentication {//implements Builder<String> {

    private static final String REALM = "realm";

    /**
     * Creates a new authorization builder whose {@link #toHeader()} method
     * always returns given value.
     *
     * @param prebuilt the value
     * @return a new authorization builder.
     */
    static OAuthAuthentication of(final String prebuilt) {
        return new OAuthAuthentication() {
            @Override
            public String toHeader() {
                return prebuilt;
            }
        };
    }

    // ------------------------------------------------------------------- realm
    /**
     * Sets the realm value.
     *
     * @param realm the realm value
     * @return this instance
     */
    public OAuthAuthentication realm(final String realm) {
        this.realm = realm;
        return this;
    }

    // -------------------------------------------------------------------------
    /**
     * Returns all protocol parameters including an entry for
     * {@link OAuthConstants#OAUTH_SIGNATURE}.
     *
     * @return all protocol parameters
     * @throws Exception if an error occurs.
     */
    public SortedMap<String, String> protocolParameters() throws Exception {
        if (signer == null) {
            throw new IllegalStateException("no signer set");
        }
        final OAuthBaseString baseString = signer.baseString();
        if (baseString == null) {
            throw new IllegalStateException("no baseString on the signer");
        }
        final SortedMap<String, String> protocolParameters
                = new TreeMap<String, String>();
        final String oauthSignature = signer.sign();
        protocolParameters.put(OAuthConstants.OAUTH_SIGNATURE, oauthSignature);
        for (final Entry<String, List<String>> entiry
             : baseString.requestParameters().entrySet()) {
            final String key = entiry.getKey();
            if (!key.startsWith(OAuthBaseString.PROTOCOL_PARAMETER_PREFIX)) {
                continue;
            }
            protocolParameters.put(key, entiry.getValue().get(0));
        }
        return protocolParameters;
    }

    /**
     * Builds protocol parameters for Authorization Header.
     *
     * @return protocol parameter for Authorization Header.
     * @throws Exception if an error occurs.
     * @see <a href="https://tools.ietf.org/html/rfc5849#section-3.5.1">3.5.1.
     * Authorization Header (RFC 5849)</a>
     */
    public String toHeader() throws Exception {
        final Map<String, String> protocolParameters = protocolParameters();
        final StringBuilder builder = new StringBuilder("OAuth");
        {
            if (realm != null) {
                builder
                        .append(" ")
                        .append(REALM)
                        .append("=\"")
                        .append(realm)
                        .append("\"");
            }
            final Iterator<Entry<String, String>> entries
                    = protocolParameters.entrySet().iterator();
            if (entries.hasNext()) {
                if (realm != null) {
                    builder.append(",");
                }
                final Entry<String, String> entry = entries.next();
                builder
                        .append(" ")
                        .append(encodePercent(entry.getKey()))
                        .append("=\"")
                        .append(encodePercent(entry.getValue()))
                        .append("\"");
            }
            while (entries.hasNext()) {
                final Entry<String, String> entry = entries.next();
                builder
                        .append(", ")
                        .append(encodePercent(entry.getKey()))
                        .append("=\"")
                        .append(encodePercent(entry.getValue()))
                        .append("\"");
            }
        }
        return builder.toString();
    }

    /**
     * Builds protocol parameters for Form-Encoded Body.
     *
     * @return protocol parameters for Form-Encoded Body.
     * @throws Exception if an error occurs.
     * @see <a href="https://tools.ietf.org/html/rfc5849#section-3.5.2">3.5.2.
     * Form-Encoded Body (RFC 5849)</a>
     */
    public String toBody() throws Exception {
        final Map<String, String> protocolParameters = protocolParameters();
        final StringBuilder builder = new StringBuilder();
        {
            final Iterator<Entry<String, String>> entries
                    = protocolParameters.entrySet().iterator();
            if (entries.hasNext()) {
                final Entry<String, String> entry = entries.next();
                builder
                        .append(encodeFormurl(entry.getKey()))
                        .append("=")
                        .append(encodeFormurl(entry.getValue()));
            }
            while (entries.hasNext()) {
                final Entry<String, String> entry = entries.next();
                builder
                        .append("&")
                        .append(encodeFormurl(entry.getKey()))
                        .append("=")
                        .append(encodeFormurl(entry.getValue()));
            }
        }
        return builder.toString();
    }

    /**
     * Builds protocol parameters for Request URI Query.
     *
     * @return protocol parameters for Request URI Query
     * @throws Exception if an error occurs.
     * @see <a href="https://tools.ietf.org/html/rfc5849#section-3.5.3">3.5.3.
     * Request URI Query (RFC 5849)</a>
     */
    public String toQuery() throws Exception {
        final Map<String, String> protocolParameters = protocolParameters();
        final StringBuilder builder = new StringBuilder();
        {
            final Iterator<Entry<String, String>> entries
                    = protocolParameters.entrySet().iterator();
            if (entries.hasNext()) {
                final Entry<String, String> entry = entries.next();
                builder
                        .append(encodePercent(entry.getKey()))
                        .append("=")
                        .append(encodePercent(entry.getValue()));
            }
            while (entries.hasNext()) {
                final Entry<String, String> entry = entries.next();
                builder
                        .append("&")
                        .append(encodePercent(entry.getKey()))
                        .append("=")
                        .append(encodePercent(entry.getValue()));
            }
        }
        return builder.toString();
    }

    // --------------------------------------------------------- sinatureBuilder
    /**
     * Sets a signer.
     *
     * @param signer the signer.
     * @return this instance
     */
    public OAuthAuthentication signer(final OAuthSigner signer) {
        this.signer = signer;
        return this;
    }

    // -------------------------------------------------------------------------
    private String realm;

    private OAuthSigner signer;
}
