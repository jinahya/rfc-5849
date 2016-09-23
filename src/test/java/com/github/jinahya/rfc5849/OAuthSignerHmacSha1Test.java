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

import static com.github.jinahya.rfc5849.OAuthBaseStringTest.baseString_nouncer;
import static com.github.jinahya.rfc5849.OAuthBaseStringTest.baseString_twitter;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 * An abstract class for testing {@link OAuthSigner} for {@code HMAC-SHA1}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @param <T> implementation type parameter
 */
public abstract class OAuthSignerHmacSha1Test<T extends OAuthSignerHmacSha1>
        extends OAuthSignerTest<T> {

    public OAuthSignerHmacSha1Test(final Class<T> singerClass) {
        super(singerClass);
    }

    @Test
    public void twitterExample() throws Exception {
        final String consumerSecret
                = "kAcSOqF21Fu85e7zjz7ZN2U4ZRhfV3WpwPAoE3Z7kBw";
        final String tokenSecret = "LswwdoUaIvS8ltyTt5jkRh4J50vUPVVHtR2YPi5kE";
        final T signer = newInstance();
        signer.consumerSecret(consumerSecret).tokenSecret(tokenSecret)
                .baseString(baseString_twitter());
        final String expected = "tnnArxj06cWHq44gCs1OSKk/jLY=";
        final String actual = signer.sign();
        assertEquals(actual, expected);
    }

    @Test
    public void nouncerExample() throws Exception {
        final String consumerSecret = "kd94hf93k423kf44";
        final String tokenSecret = "pfkkdhi9sl3r4s00";
        final T signer = newInstance();
        signer.consumerSecret(consumerSecret).tokenSecret(tokenSecret)
                .baseString(baseString_nouncer());
        final String expected = "tR3+Ty81lMeYAr/Fid0kMTYa/WM=";
        final String actual = signer.sign();
        assertEquals(actual, expected);
    }
}
