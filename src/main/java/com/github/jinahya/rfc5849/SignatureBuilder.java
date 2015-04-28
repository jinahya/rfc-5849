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


import java.io.UnsupportedEncodingException;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public abstract class SignatureBuilder {


    public SignatureBuilder() {

        super();
    }


    /**
     * Builds the signature.
     *
     * @return the signature.
     *
     * @throws Exception an any error occurs.
     */
    public abstract String build() throws Exception;


    /**
     * Returns the current consumer secret.
     *
     * @return the current consumer secret.
     */
    public String getConsumerSecret() {

        return consumerSecret;
    }


    /**
     * Replaces the consumer secret with given.
     *
     * @param consumerSecret new consumer secret.
     */
    public void setConsumerSecret(final String consumerSecret) {

        this.consumerSecret = consumerSecret;
    }


    /**
     * Replaces the consumer secret with given and return self.
     *
     * @param consumerSecret new consumer secret.
     *
     * @return self.
     */
    public SignatureBuilder consumerSecret(final String consumerSecret) {

        setConsumerSecret(consumerSecret);

        return this;
    }


    public String getTokenSecret() {

        return tokenSecret;
    }


    public void setTokenSecret(final String tokenSecret) {

        this.tokenSecret = tokenSecret;
    }


    public SignatureBuilder tokenSecret(final String tokenSecret) {

        setTokenSecret(tokenSecret);

        return this;
    }


    /**
     * Returns the key string.
     *
     * @return the key string.
     */
    public String getKey() {

        return Percent.encode(getConsumerSecret())
               + "&"
               + Percent.encode(getTokenSecret());
    }


    /**
     * Returns key bytes.
     *
     * @return key bytes.
     */
    public byte[] getKeyBytes() {

        try {
            return getKey().getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException(uee);
        }
    }


    public String getBaseString() {

        return baseString;
    }


    public void setBaseString(final String baseString) {

        this.baseString = baseString;
    }


    public SignatureBuilder baseString(final String baseString) {

        setBaseString(baseString);

        return this;
    }


    public byte[] getBaseStringBytes() {

        try {
            return getBaseString().getBytes("ISO-8859-1");
        } catch (final UnsupportedEncodingException uee) {
            throw new RuntimeException(uee);
        }
    }


    private String consumerSecret;


    private String tokenSecret;


    private String baseString;


}

