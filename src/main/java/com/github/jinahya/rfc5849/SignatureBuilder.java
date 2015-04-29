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
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public abstract class SignatureBuilder implements Builder {


    public static final String KEY_OAUTH_SIGNATURE = "oauth_signature";


    public SignatureBuilder(final String signatureMethod) {

        super();

        if (signatureMethod == null) {
            throw new NullPointerException("null signatureMethod");
        }

        this.signatureMethod = signatureMethod;
    }


    /**
     * Returns signature method.
     *
     * @return signature method.
     */
    public String getSignatureMethod() {

        return signatureMethod;
    }


    /**
     * Replaces the consumer secret with given and return self.
     *
     * @param consumerSecret new consumer secret.
     *
     * @return self.
     */
    public SignatureBuilder consumerSecret(final String consumerSecret) {

        this.consumerSecret = consumerSecret;

        return this;
    }


    public SignatureBuilder tokenSecret(final String tokenSecret) {

        this.tokenSecret = tokenSecret;

        return this;
    }


    public BaseStringBuilder getBaseStringBuilder() {

        return baseStringBuilder;
    }


    public void setBaseStringBuilder(
        final BaseStringBuilder baseStringBuilder) {

        this.baseStringBuilder = baseStringBuilder;
    }


    public SignatureBuilder baseStringBuilder(
        final BaseStringBuilder baseStringBuilder) {

        setBaseStringBuilder(baseStringBuilder);

        return this;
    }


    public String build() throws Exception {

        if (consumerSecret == null) {
            throw new IllegalStateException("no consumerSecret set");
        }

        if (tokenSecret == null) {
            throw new IllegalStateException("no tokenSecret set");
        }

        if (baseStringBuilder == null) {
            throw new IllegalStateException("no baseStringBuilder set");
        }

        if (baseStringBuilder.getOauthSignatureMethod() == null) {
            baseStringBuilder.setOauthSignatureMethod(signatureMethod);
        }

        final String keyString
            = Percent.encode(consumerSecret) + "&"
              + Percent.encode(tokenSecret);
        final byte[] keyBytes = keyString.getBytes("ISO-8859-1");

        final String baseString = baseStringBuilder.build();
        final byte[] baseStringBytes = baseString.getBytes("ISo-8859-1");

        final byte[] signature = signature(keyBytes, baseStringBytes);

        return Base64.encodeToString(signature);
    }


    protected abstract byte[] signature(byte[] keyBytes, byte[] baseStringBytes)
        throws Exception;


    private final String signatureMethod;


    private String consumerSecret;


    private String tokenSecret;


    private BaseStringBuilder baseStringBuilder;


}

