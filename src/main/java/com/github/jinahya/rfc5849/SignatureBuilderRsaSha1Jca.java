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

import java.security.PrivateKey;
import java.security.Signature;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class SignatureBuilderRsaSha1Jca
        extends SignatureBuilderRsaSha1<PrivateKey> {

    protected static final String ALGORITHM = "SHA1withRSA";

    @Override
    protected byte[] build(final PrivateKey privateKey, final byte[] baseBytes)
            throws Exception {
        final Signature signature = Signature.getInstance(ALGORITHM);
        signature.initSign(privateKey);
        signature.update(baseBytes);
        return signature.sign();
    }

    @Override
    public SignatureBuilderRsaSha1Jca privateKey(final PrivateKey privateKey) {
        return (SignatureBuilderRsaSha1Jca) super.privateKey(privateKey);
    }

}
