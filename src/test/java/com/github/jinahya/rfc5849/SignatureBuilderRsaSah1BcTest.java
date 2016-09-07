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
import java.util.function.BiFunction;
import java.util.function.Function;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Tests {@link SignatureBuilderRsaSha1Bc}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class SignatureBuilderRsaSah1BcTest
        extends SignatureBuilderRsaSha1Test<SignatureBuilderRsaSha1Bc, RSAKeyParameters> {

    private static final Logger logger = getLogger(lookup().lookupClass());

    static <R> R applyKeyPairBc(
            final Function<AsymmetricCipherKeyPair, R> function) {
        final RSAKeyPairGenerator generator = new RSAKeyPairGenerator();
        generator.init(new RSAKeyGenerationParameters(
                exponent(), new SecureRandom(), modulus().intValue(), 80
        ));
        final AsymmetricCipherKeyPair pair = generator.generateKeyPair();
        return function.apply(pair);
    }

    static <R> R applyKeyPairBc(
            final BiFunction<CipherParameters, CipherParameters, R> function) {
        return applyKeyPairBc(
                p -> function.apply(p.getPublic(), p.getPrivate()));
    }

    static <R> R applyPublicKeyBc(
            final Function<CipherParameters, R> function) {
        return applyKeyPairBc((p, i) -> function.apply(p));
    }

    static <R> R applyPrivateKeyBc(
            final Function<CipherParameters, R> function) {
        return applyKeyPairBc((i, p) -> function.apply(p));
    }

    public SignatureBuilderRsaSah1BcTest() {
        super(SignatureBuilderRsaSha1Bc.class);
    }

    @Override
    RSAKeyParameters newInitParam() throws Exception {
        return (RSAKeyParameters) applyPrivateKeyBc(p -> p);
    }
}
