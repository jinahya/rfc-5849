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

import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.signers.RSADigestSigner;

/**
 * A signature builder using Bouncy Castle.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class SignatureBuilderRsaSha1Bc
        extends SignatureBuilderRsaSha1<RSAKeyParameters> {

    private static final Logger logger
            = getLogger(SignatureBuilderRsaSha1Bc.class.getName());

    @Override
    byte[] build(final RSAKeyParameters initParam, final byte[] baseBytes)
            throws Exception {
        logger.log(Level.INFO, "baseBytes.length: {0}", baseBytes.length);
        final Signer signer = new RSADigestSigner(new SHA1Digest());
        signer.init(true, initParam);
        signer.update(baseBytes, 0, baseBytes.length);
        return signer.generateSignature();
    }
}
