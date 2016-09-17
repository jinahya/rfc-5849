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

import java.io.IOException;
import static java.lang.invoke.MethodHandles.lookup;
import static java.nio.file.Files.readAllBytes;
import java.security.NoSuchAlgorithmException;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.fail;

/**
 * Tests {@link SignatureBuilderRsaSha1Bc}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class SignatureBuilderRsaSah1BcTest
        extends SignatureBuilderRsaSha1Test<SignatureBuilderRsaSha1Bc, CipherParameters> {

    private static final Logger logger = getLogger(lookup().lookupClass());

//    static <R> R applyKeyPair(
//            final Function<AsymmetricCipherKeyPair, R> function) {
//        final RSAKeyPairGenerator generator = new RSAKeyPairGenerator();
//        generator.init(new RSAKeyGenerationParameters(
//                exponent(), new SecureRandom(), modulus().intValue(), 80
//        ));
//        final AsymmetricCipherKeyPair pair = generator.generateKeyPair();
//        return function.apply(pair);
//    }
//
//    static <R> R applyKeyPair(
//            final BiFunction<CipherParameters, CipherParameters, R> function) {
//        return SignatureBuilderRsaSah1BcTest.applyKeyPair(
//                p -> function.apply(p.getPublic(), p.getPrivate()));
//    }
//
//    static <R> R applyPublicKey(final Function<CipherParameters, R> function) {
//        return applyKeyPair((p, i) -> function.apply(p));
//    }
//
//    static <R> R applyPrivateKey(final Function<CipherParameters, R> function) {
//        return applyKeyPair((i, p) -> function.apply(p));
//    }
    static <R> R applyKeyPair_(
            final BiFunction<CipherParameters, CipherParameters, R> function)
            throws NoSuchAlgorithmException, IOException {
        return applyKeyFiles((publicKeyFile, privateKeyFile) -> {
            try {
                final byte[] publicKeyBytes
                        = readAllBytes(publicKeyFile.toPath());
                final AsymmetricKeyParameter publicKey
                        = PublicKeyFactory.createKey(publicKeyBytes);
                final byte[] privateKeyBytes
                        = readAllBytes(privateKeyFile.toPath());
                final AsymmetricKeyParameter privateKey
                        = PrivateKeyFactory.createKey(privateKeyBytes);
                return function.apply(publicKey, privateKey);
            } catch (final IOException ioe) {
                fail("fail", ioe);
                return null;
            }
        });
    }

    static <R> R applyPublicKey_(final Function<CipherParameters, R> function)
            throws NoSuchAlgorithmException, IOException {
        return applyKeyPair_((p, i) -> function.apply(p));
    }

    static <R> R applyPrivateKey_(final Function<CipherParameters, R> function)
            throws NoSuchAlgorithmException, IOException {
        return applyKeyPair_((i, p) -> function.apply(p));
    }

    /**
     * Creates a new instance.
     */
    public SignatureBuilderRsaSah1BcTest() {
        super(SignatureBuilderRsaSha1Bc.class);
    }

    @Override
    CipherParameters newInitParam() throws Exception {
        return applyPrivateKey_(p -> p);
    }
}
