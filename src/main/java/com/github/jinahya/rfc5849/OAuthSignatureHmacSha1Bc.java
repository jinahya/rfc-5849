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
 * A signature builder using the <b>Legion of the Bouncy Castle</b> Java
 * cryptography APIs.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see <a href="https://www.bouncycastle.org/java.html">The Legion of the
 * Bouncy Castle</a>
 */
public class OAuthSignatureHmacSha1Bc extends OAuthSignatureHmacSha1 {

    @Override
    byte[] get(final byte[] keyBytes, final byte[] baseBytes) throws Exception {
        final Mac mac = new HMac(new SHA1Digest());
        mac.init(new KeyParameter(keyBytes));
        mac.update(baseBytes, 0, baseBytes.length);
        final byte[] output = new byte[mac.getMacSize()];
        mac.doFinal(output, 0);
        return output;
    }
}
