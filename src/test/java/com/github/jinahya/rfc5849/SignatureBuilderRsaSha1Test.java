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

import java.io.File;
import static java.io.File.createTempFile;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import static java.lang.invoke.MethodHandles.lookup;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.testng.annotations.Test;

/**
 * Test class for {@link SignatureBuilderRsaSha1}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @param <T> implementation type parameter
 * @param <P> initParam type parameter
 */
public abstract class SignatureBuilderRsaSha1Test<T extends SignatureBuilderRsaSha1<P>, P>
        extends SignatureBuilderTest<T> {

    private static final Logger logger = getLogger(lookup().lookupClass());

    static BigInteger modulus() {
        if (true) {
            return BigInteger.valueOf(2048L);
        }
        return BigInteger.valueOf(
                ThreadLocalRandom.current().nextBoolean() ? 1024L : 2048L);
    }

    static BigInteger exponent() {
        return ThreadLocalRandom.current().nextBoolean()
               ? RSAKeyGenParameterSpec.F0 : RSAKeyGenParameterSpec.F4;
    }

    private static File store(final byte[] bytes, final File file)
            throws IOException {
        try (OutputStream output = new FileOutputStream(file)) {
            output.write(bytes);
            output.flush();
        }
        return file;
    }

    private static File store(final byte[] bytes) throws IOException {
        final File file = createTempFile("rfc5849", null);
        file.deleteOnExit();
        return store(bytes, file);
    }

    static <R> R applyKeyFiles(final BiFunction<File, File, R> function)
            throws NoSuchAlgorithmException, IOException {
        final KeyPairGenerator keyPairGenerator
                = KeyPairGenerator.getInstance("RSA");
        final int keysize
                = ThreadLocalRandom.current().nextBoolean() ? 1024 : 2048;
        keyPairGenerator.initialize(keysize);
        final KeyPair keyPair = keyPairGenerator.generateKeyPair();
        final PublicKey publicKey = keyPair.getPublic();
        final File publicKeyFile = store(
                new X509EncodedKeySpec(publicKey.getEncoded()).getEncoded());
        final PrivateKey privateKey = keyPair.getPrivate();
        final File privateKeyFile = store(
                new PKCS8EncodedKeySpec(privateKey.getEncoded()).getEncoded());
        return function.apply(publicKeyFile, privateKeyFile);
    }

    static <R> R applyPublicKeyFile(final Function<File, R> function)
            throws NoSuchAlgorithmException, IOException {
        return applyKeyFiles((f, i) -> function.apply(f));
    }

    static <R> R applyPrivateKeyFile(final Function<File, R> function)
            throws NoSuchAlgorithmException, IOException {
        return applyKeyFiles((i, f) -> function.apply(f));
    }

    /**
     * Creates a new instance
     *
     * @param builderClass implementation class
     */
    public SignatureBuilderRsaSha1Test(final Class<T> builderClass) {
        super(builderClass);
    }

    abstract P newInitParam() throws Exception;

    @Test
    public void test() throws Exception {
        final SignatureBuilder signatureBuilder
                = newInstance()
                .initParam(newInitParam())
                .baseStringBuilder(new BaseStringBuilder() {
                    public String build() {
                        return "abcd";
                    }
                });
        final String signature = signatureBuilder.build();
        logger.debug("signature: {}", signature);
    }
}
