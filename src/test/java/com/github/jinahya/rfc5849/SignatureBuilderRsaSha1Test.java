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
