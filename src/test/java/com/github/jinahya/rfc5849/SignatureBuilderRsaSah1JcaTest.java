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

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class SignatureBuilderRsaSah1JcaTest
        extends SignatureBuilderRsaSha1Test<SignatureBuilderRsaSha1Jca, RSAPrivateKey> {

    static <R> R applyKeyPair(final Function<KeyPair, R> function)
            throws NoSuchAlgorithmException {
        final KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(
                ThreadLocalRandom.current().nextBoolean() ? 1024 : 2048);
        final KeyPair pair = generator.generateKeyPair();
        return function.apply(pair);
    }

    static <R> R applyKeyPair(
            final BiFunction<PublicKey, PrivateKey, R> function)
            throws NoSuchAlgorithmException {
        return applyKeyPair(p -> function.apply(p.getPublic(), p.getPrivate()));
    }

    static void acceptKeyPair(final Consumer<KeyPair> consumer)
            throws NoSuchAlgorithmException {
        applyKeyPair(p -> {
            consumer.accept(p);
            return null;
        });
    }

    static void acceptKeyPair(final BiConsumer<PublicKey, PrivateKey> consumer)
            throws NoSuchAlgorithmException {
        acceptKeyPair(p -> consumer.accept(p.getPublic(), p.getPrivate()));
    }

    public SignatureBuilderRsaSah1JcaTest() {
        super(SignatureBuilderRsaSha1Jca.class);
    }

    @Override
    RSAPrivateKey newInitParam() throws Exception {
        return (RSAPrivateKey) applyKeyPair(KeyPair::getPrivate);
    }
}
