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

import java.security.Key;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class SignatureBuilderHmacSha1Jc extends SignatureBuilderHmacSha1 {

    /**
     * Standard algorithm name for HMAC-SHA1.
     */
    public static final String ALGORITHM = "HmacSHA1";

    @Override
    protected byte[] build(final byte[] keyBytes, final byte[] baseBytes)
            throws Exception {
        final Key key = new SecretKeySpec(keyBytes, ALGORITHM);
        final Mac mac = Mac.getInstance(ALGORITHM);
        mac.init(key);
        final byte[] output = mac.doFinal(baseBytes);
        return output;
    }
}
