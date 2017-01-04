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

import static com.github.jinahya.rfc5849.OAuthSignatureTest.random;
import java.io.IOException;
import static java.lang.invoke.MethodHandles.lookup;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAKey;
import java.util.function.Function;
import javax.crypto.Cipher;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class OAuthSignatureRsaSah1JcaTest
        extends OAuthSignatureRsaSha1Test<OAuthSignatureRsaSha1Jca, PrivateKey> {

    private static final Logger logger = getLogger(lookup().lookupClass());

    private static final String ALGORITHM = "RSA";

    private static KeyFactory KEY_FACTORY;

    static {
        try {
            KEY_FACTORY = KeyFactory.getInstance(ALGORITHM);
        } catch (final NoSuchAlgorithmException nsae) {
            throw new InstantiationError(nsae.getMessage());
        }
    }

    static <R> R applyKeyPair(
            final Function<KeyPair, R> function)
            throws NoSuchAlgorithmException, IOException {
        final KeyPairGenerator keyPairGenerator
                = KeyPairGenerator.getInstance("RSA");
        final int keysize = random().nextBoolean() ? 1024 : 2048;
        keyPairGenerator.initialize(keysize);
        final KeyPair keyPair = keyPairGenerator.generateKeyPair();
        final PublicKey publicKey = keyPair.getPublic();
        final PrivateKey privateKey = keyPair.getPrivate();
        return function.apply(new KeyPair(publicKey, privateKey));
    }

    static <R> R applyPublicKey(final Function<PublicKey, R> function)
            throws NoSuchAlgorithmException, IOException {
        return applyKeyPair(keyPair -> function.apply(keyPair.getPublic()));
    }

    static <R> R applyPrivateKey(final Function<PrivateKey, R> function)
            throws NoSuchAlgorithmException, IOException {
        return applyKeyPair(keyPair -> function.apply(keyPair.getPrivate()));
    }

    /**
     * Creates a new instance.
     */
    public OAuthSignatureRsaSah1JcaTest() {
        super(OAuthSignatureRsaSha1Jca.class);
    }

    @Override
    PrivateKey initParam() throws Exception {
        return applyPrivateKey(k -> k);
    }

    @Test
    public void encodePrivateDecodePublic()
            throws NoSuchAlgorithmException, IOException {
        applyKeyPair(keyPair -> {
            final PublicKey publicKey = keyPair.getPublic();
            final PrivateKey privateKey = keyPair.getPrivate();
            final int bits = ((RSAKey) privateKey).getModulus().bitLength();
            final byte[] expected = new byte[random().nextInt(bits / 8 - 11)];
            try {
                final Cipher cipher = Cipher.getInstance(ALGORITHM);
                cipher.init(Cipher.ENCRYPT_MODE, privateKey);
                final byte[] encrypted = cipher.doFinal(expected);
                cipher.init(Cipher.DECRYPT_MODE, publicKey);
                final byte[] actual = cipher.doFinal(encrypted);
                assertEquals(actual, expected);
            } catch (final Exception e) {
                logger.error("failed", e);
            }
            return null;
        });
    }

    @Test
    public void encodePublicDecodePrivate()
            throws NoSuchAlgorithmException, IOException {
        applyKeyPair(keyPair -> {
            final PublicKey publicKey = keyPair.getPublic();
            final PrivateKey privateKey = keyPair.getPrivate();
            final int bits = ((RSAKey) publicKey).getModulus().bitLength();
            final byte[] expected = new byte[random().nextInt(bits / 8 - 11)];
            try {
                final Cipher cipher = Cipher.getInstance(ALGORITHM);
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);
                final byte[] encrypted = cipher.doFinal(expected);
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
                final byte[] actual = cipher.doFinal(encrypted);
                assertEquals(actual, expected);
            } catch (final Exception e) {
                logger.error("failed", e);
            }
            return null;
        }
        );
    }
}
