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
public abstract class SignatureBuilderHmacSha1
    extends SignatureBuilderPlaintext {


    private static final String SIGNATURE_METHOD = "HMAC-SHA1";


    public SignatureBuilderHmacSha1() {

        super(SIGNATURE_METHOD);
    }


    public String build() throws Exception {

        if (baseStringBuilder == null) {
            throw new IllegalStateException("no baseStringBuilder set");
        }

        final String keyString = super.build();
        final byte[] keyBytes = keyString.getBytes("ISO-8859-1");

        final String baseString = baseStringBuilder.build();
        final byte[] baseStringBytes = baseString.getBytes("ISO-8859-1");

        final byte[] signature = signature(keyBytes, baseStringBytes);

        return Base64.encodeToString(signature);
    }


    protected abstract byte[] signature(byte[] keyBytes, byte[] baseStringBytes)
        throws Exception;


}

