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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.io.File.createTempFile;

/**
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class SignatureBuilderRsaSha1CrossmatchingTest {

    private static File store(final byte[] bytes, final File file) throws IOException {
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

    static <R> R applyKeyFiles(final BiFunction<File, File, R> function) throws NoSuchAlgorithmException, IOException {
        final KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        final int keysize = ThreadLocalRandom.current().nextBoolean() ? 1024 : 2048;
        generator.initialize(keysize);
        final KeyPair pair = generator.generateKeyPair();
        final File publicKeyFile = store(new X509EncodedKeySpec(pair.getPublic().getEncoded()).getEncoded());
        final File privateKeyFile = store(new PKCS8EncodedKeySpec(pair.getPrivate().getEncoded()).getEncoded());
        return function.apply(publicKeyFile, privateKeyFile);
    }

    static <R> R applyPublicKeyFile(final Function<File, R> function) throws NoSuchAlgorithmException, IOException {
        return applyKeyFiles((f, i) -> function.apply(f));
    }

    static <R> R applyPrivateKeyFile(final Function<File, R> function) throws NoSuchAlgorithmException, IOException {
        return applyKeyFiles((i, f) -> function.apply(f));
    }
}
