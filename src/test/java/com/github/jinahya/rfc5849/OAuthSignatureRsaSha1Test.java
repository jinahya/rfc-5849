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
import java.math.BigInteger;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.concurrent.ThreadLocalRandom;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.testng.annotations.Test;
import static com.github.jinahya.rfc5849.OAuthBaseStringTest.baseStringBuilderOf;

/**
 * Test class for {@link OAuthSignerRsaSha1}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @param <T> implementation type parameter
 * @param <P> initParam type parameter
 */
public abstract class OAuthSignatureRsaSha1Test<T extends OAuthSignatureRsaSha1<P>, P>
        extends OAuthSignatureTest<T> {

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
//
//    private static File store(final byte[] bytes, final File file)
//            throws IOException {
//        try (OutputStream output = new FileOutputStream(file)) {
//            output.write(bytes);
//            output.flush();
//        }
//        return file;
//    }
//
//    private static File store(final byte[] bytes) throws IOException {
//        final File file = createTempFile("rfc5849", null);
//        file.deleteOnExit();
//        return store(bytes, file);
//    }
//
//    static <R> R applyKeyFiles(final BiFunction<File, File, R> function)
//            throws NoSuchAlgorithmException, IOException {
//        final KeyPairGenerator keyPairGenerator
//                = KeyPairGenerator.getInstance("RSA");
//        final int keysize = random().nextBoolean() ? 1024 : 2048;
//        keyPairGenerator.initialize(keysize);
//        final KeyPair keyPair = keyPairGenerator.generateKeyPair();
//        final PublicKey publicKey = keyPair.getPublic();
//        final File publicKeyFile = store(
//                new X509EncodedKeySpec(publicKey.getEncoded()).getEncoded());
//        final PrivateKey privateKey = keyPair.getPrivate();
//        final File privateKeyFile = store(
//                new PKCS8EncodedKeySpec(privateKey.getEncoded()).getEncoded());
//        return function.apply(publicKeyFile, privateKeyFile);
//    }
//
//    static <R> R applyPublicKeyFile(final Function<File, R> function)
//            throws NoSuchAlgorithmException, IOException {
//        return applyKeyFiles((f, i) -> function.apply(f));
//    }
//
//    static <R> R applyPrivateKeyFile(final Function<File, R> function)
//            throws NoSuchAlgorithmException, IOException {
//        return applyKeyFiles((i, f) -> function.apply(f));
//    }

    /**
     * Creates a new instance
     *
     * @param builderClass implementation class
     */
    public OAuthSignatureRsaSha1Test(final Class<T> builderClass) {
        super(builderClass);
    }

    abstract P initParam() throws Exception;

    @Test
    public void test() throws Exception {
        final OAuthSignature oauthSignature
                = instance()
                .initParam(initParam())
                .baseString(baseStringBuilderOf(
                        "POST"
                        + "&https%3A%2F%2Fapi.twitter.com%2F1%2Fstatuses%2Fupdate.json"
                        + "&include_entities%3Dtrue"
                        + "%26oauth_consumer_key%3Dxvz1evFS4wEEPTGEFPHBog"
                        + "%26oauth_nonce%3DkYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg"
                        + "%26oauth_signature_method%3DHMAC-SHA1"
                        + "%26oauth_timestamp%3D1318622958"
                        + "%26oauth_token%3D370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb"
                        + "%26oauth_version%3D1.0"
                        + "%26status%3DHello%2520Ladies%2520%252B%2520Gentlemen%252C%2520a%2520signed%2520OAuth%2520request%2521"
                ));
        final String signature = oauthSignature.get();
        logger.debug("signature: {}", signature);
    }

    @Test
    public void get() throws Exception {
        final OAuthSignature oauthSignature
                = instance()
                .initParam(initParam())
                .baseString(baseStringBuilderOf(
                        "POST"
                        + "&https%3A%2F%2Fapi.twitter.com%2F1%2Fstatuses%2Fupdate.json"
                        + "&include_entities%3Dtrue"
                        + "%26oauth_consumer_key%3Dxvz1evFS4wEEPTGEFPHBog"
                        + "%26oauth_nonce%3DkYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg"
                        + "%26oauth_signature_method%3DHMAC-SHA1"
                        + "%26oauth_timestamp%3D1318622958"
                        + "%26oauth_token%3D370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb"
                        + "%26oauth_version%3D1.0"
                        + "%26status%3DHello%2520Ladies%2520%252B%2520Gentlemen%252C%2520a%2520signed%2520OAuth%2520request%2521"
                ));
        final String signature = oauthSignature.get();
        logger.debug("signature: {}", signature);
    }
}
