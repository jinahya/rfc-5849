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


import com.github.jinahya.rfc5849.util.Base64;


/**
 * An abstract class for signature builder implementation whose signature method
 * is {@value #SIGNATURE_METHOD}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @param <T> private key type parameter
 */
public abstract class SignatureBuilderRsaSha1<T> extends SignatureBuilder {


    /**
     * The signature method value which is {@value #SIGNATURE_METHOD}.
     */
    public static final String SIGNATURE_METHOD = "RSA-SHA1";


    /**
     * Creates a new instance.
     */
    public SignatureBuilderRsaSha1() {

        super(SIGNATURE_METHOD);
    }


    /**
     * Returns current value of {@link #privateKey}.
     *
     * @return current value of {@link #privateKey}.
     */
    public T getPrivateKey() {

        return privateKey;
    }


    /**
     * Replaces current value of {@link #privateKey} with given.
     *
     * @param privateKey the new value for {@link #privateKey}.
     */
    public void setPrivateKey(final T privateKey) {

        this.privateKey = privateKey;
    }


    /**
     * Replaces current value of {@link #privateKey} with given.
     *
     * @param privateKey the new value for {@link #privateKey}.
     *
     * @return this instance.
     *
     * @see #setPrivateKey(java.lang.Object)
     */
    public SignatureBuilderRsaSha1<T> privateKey(final T privateKey) {

        //this.privateKey = privateKey;
        setPrivateKey(privateKey);

        return this;
    }


    @Override
    public String build() throws Exception {

        final String prebuilt = getPrebuilt();
        if (prebuilt != null) {
            return prebuilt;
        }

        final BaseStringBuilder baseStringBuilder = getBaseStringBuilder();
        if (baseStringBuilder == null) {
            throw new IllegalStateException("no baseStringBuilder set");
        }

        if (privateKey == null) {
            throw new IllegalStateException("no privateKey set");
        }

        final String baseString = baseStringBuilder.build();
        final byte[] baseBytes = baseString.getBytes("ISO-8859-1");

        final byte[] built = build(privateKey, baseBytes);

        return Base64.encodeToString(built);
    }


    protected abstract byte[] build(final T privateKey, final byte[] baseBytes)
        throws Exception;


    /**
     * The private key to use.
     */
    private T privateKey;


}

