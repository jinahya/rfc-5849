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


import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class SignatureBuilderHmacSha1Bc extends SignatureBuilderHmacSha1 {


    protected byte[] signature(final byte[] keyBytes,
                               final byte[] baseStringBytes)
        throws Exception {

        final Mac mac = new HMac(new SHA1Digest());
        mac.init(new KeyParameter(keyBytes));

        mac.update(baseStringBytes, 0, baseStringBytes.length);

        final byte[] output = new byte[mac.getMacSize()];
        mac.doFinal(output, 0);

        return output;
    }


}

