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
 * A signature builder for {@code PLAINTEXT}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class SignatureBuilderPlaintext extends SignatureBuilder {


    private static final String SIGNATURE_METHOD = "PLAINTEXT";


    protected SignatureBuilderPlaintext(final String signatureMethod) {

        super(signatureMethod);
    }


    public SignatureBuilderPlaintext() {

        this(SIGNATURE_METHOD);
    }


    /**
     * Replaces the consumer secret with given and return self.
     *
     * @param consumerSecret new consumer secret.
     *
     * @return self.
     */
    public SignatureBuilderPlaintext consumerSecret(
        final String consumerSecret) {

        this.consumerSecret = consumerSecret;

        return this;
    }


    public SignatureBuilderPlaintext tokenSecret(final String tokenSecret) {

        this.tokenSecret = tokenSecret;

        return this;
    }


    public String build() throws Exception {

        if (consumerSecret == null) {
            throw new IllegalStateException("no consumerSecret set");
        }

        if (tokenSecret == null) {
            throw new IllegalStateException("no tokenSecret set");
        }

        if (baseStringBuilder.oauthSignatureMethod() == null) {
            baseStringBuilder.oauthSignatureMethod(signatureMethod);
        }

        return Percent.encode(consumerSecret) + "&"
               + Percent.encode(tokenSecret);
    }


    private String consumerSecret;


    private String tokenSecret;


}

