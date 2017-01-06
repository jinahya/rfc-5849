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
import java.util.TreeMap;

/**
 * A builder for building request authentication information.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see <a href="https://tools.ietf.org/html/rfc5849#section-3.5.1">3.5.1.
 * Authorization Header (RFC 5849)</a>
 */
public class OAuthProtocolParameters {

    public static final String AUTH_SCHEME = "OAuth";

    private static final String REALM = "realm";

    // ------------------------------------------------------------------- realm
    /**
     * Sets the realm value.
     *
     * @param realm the realm value
     * @return this instance
     */
    public OAuthProtocolParameters realm(final String realm) {
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
    private Map<String, String> protocolParameters() throws Exception {
        if (signature == null) {
            throw new IllegalStateException("no signature set");
        }
        final OAuthBaseString baseString = signature.baseString();
        if (baseString == null) {
            throw new IllegalStateException("no baseString set on signature");
        }
        final Map<String, String> protocolParameters
                = new TreeMap<String, String>();
        final Map<String, List<String>> requestParameters
                = baseString.requestParameters();
        for (final Iterator<Entry<String, List<String>>> i
                = requestParameters.entrySet().iterator(); i.hasNext();) {
            final Entry<String, List<String>> entry = i.next();
            final String key = entry.getKey();
            if (!key.startsWith(OAuthBaseString.PROTOCOL_PARAMETER_PREFIX)) {
                continue;
            }
            if (key.equals(OAuthConstants.OAUTH_SIGNATURE)) {
                i.remove();
                continue;
            }
            protocolParameters.put(key, entry.getValue().get(0));
        }
        final String oauthSignature = signature.get();
        protocolParameters.put(OAuthConstants.OAUTH_SIGNATURE, oauthSignature);
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
    public String authorizationHeader() throws Exception {
        final Map<String, String> protocolParameters = protocolParameters();
        final StringBuilder builder = new StringBuilder(AUTH_SCHEME);
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
    public String formEncodedBody() throws Exception {
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
    public String requestUriQuery() throws Exception {
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

    // --------------------------------------------------------------- signature
    /**
     * Sets a signature.
     *
     * @param signature the signature.
     * @return this instance
     */
    public OAuthProtocolParameters signature(final OAuthSignature signature) {
        this.signature = signature;
        return this;
    }

    // -------------------------------------------------------------------------
    private String realm;

    private OAuthSignature signature;
}
