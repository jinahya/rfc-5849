/*
 * Copyright 2016 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
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

import static java.lang.invoke.MethodHandles.lookup;
import java.security.SecureRandom;
import java.util.function.Function;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Tests {@link OAuthSignatureRsaSha1Bc}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class OAuthSignatureRsaSah1BcTest
        extends OAuthSignatureRsaSha1Test<OAuthSignatureRsaSha1Bc, CipherParameters> {

    private static final Logger logger = getLogger(lookup().lookupClass());

    static <R> R applyKeyPair(
            final Function<AsymmetricCipherKeyPair, R> function) {
        final RSAKeyPairGenerator generator = new RSAKeyPairGenerator();
        generator.init(new RSAKeyGenerationParameters(
                exponent(), // publicExponent
                new SecureRandom(), // random
                modulus().intValue(), // strength
                80 // centainty
        ));
        final AsymmetricCipherKeyPair pair = generator.generateKeyPair();
        return function.apply(pair);
    }

    static <R> R applyPublicKey(final Function<CipherParameters, R> function) {
        return applyKeyPair(pair -> function.apply(pair.getPublic()));
    }

    static <R> R applyPrivateKey(final Function<CipherParameters, R> function) {
        return applyKeyPair(pair -> function.apply(pair.getPrivate()));
    }
//    static <R> R applyKeyPair_(
//            final BiFunction<CipherParameters, CipherParameters, R> function)
//            throws NoSuchAlgorithmException, IOException {
//        return applyKeyFiles((publicKeyFile, privateKeyFile) -> {
//            try {
//                final byte[] publicKeyBytes
//                        = readAllBytes(publicKeyFile.toPath());
//                final AsymmetricKeyParameter publicKey
//                        = PublicKeyFactory.createKey(publicKeyBytes);
//                final byte[] privateKeyBytes
//                        = readAllBytes(privateKeyFile.toPath());
//                final AsymmetricKeyParameter privateKey
//                        = PrivateKeyFactory.createKey(privateKeyBytes);
//                return function.apply(publicKey, privateKey);
//            } catch (final IOException ioe) {
//                fail("fail", ioe);
//                return null;
//            }
//        });
//    }
//
//    static <R> R applyPublicKey_(final Function<CipherParameters, R> function)
//            throws NoSuchAlgorithmException, IOException {
//        return applyKeyPair_((p, i) -> function.apply(p));
//    }
//
//    static <R> R applyPrivateKey_(final Function<CipherParameters, R> function)
//            throws NoSuchAlgorithmException, IOException {
//        return applyKeyPair_((i, p) -> function.apply(p));
//    }

    /**
     * Creates a new instance.
     */
    public OAuthSignatureRsaSah1BcTest() {
        super(OAuthSignatureRsaSha1Bc.class);
    }

    @Override
    CipherParameters initParam() throws Exception {
        return applyPrivateKey(p -> p);
    }
}
