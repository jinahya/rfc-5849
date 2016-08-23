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

import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 * An abstract class for testing {@link SignatureBuilder} for {@code HMAC-SHA1}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @param <T> signature builder type parameter
 */
public abstract class SignatureBuilderHmacSha1Test<T extends SignatureBuilderHmacSha1>
        extends SignatureBuilderTest<T> {

    public SignatureBuilderHmacSha1Test(final Class<T> builderType) {
        super(builderType);
    }

    @Test
    public void twitterExample() throws Exception {
        final String baseString
                = "POST"
                  + "&https%3A%2F%2Fapi.twitter.com%2F1%2Fstatuses%2Fupdate.json"
                  + "&include_entities%3Dtrue"
                  + "%26oauth_consumer_key%3Dxvz1evFS4wEEPTGEFPHBog"
                  + "%26oauth_nonce%3DkYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg"
                  + "%26oauth_signature_method%3DHMAC-SHA1"
                  + "%26oauth_timestamp%3D1318622958"
                  + ("%26oauth_token"
                     + "%3D370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb")
                  + "%26oauth_version%3D1.0"
                  + ("%26status%3DHello%2520Ladies%2520%252B%2520Gentlemen%252C"
                     + "%2520a%2520signed%2520OAuth%2520request%2521");
        final String consumerSecret
                = "kAcSOqF21Fu85e7zjz7ZN2U4ZRhfV3WpwPAoE3Z7kBw";
        final String tokenSecret = "LswwdoUaIvS8ltyTt5jkRh4J50vUPVVHtR2YPi5kE";

        final T builder = newInstance();

        builder
                .consumerSecret(consumerSecret)
                .tokenSecret(tokenSecret)
                .baseStringBuilder(new BaseStringBuilder().prebuilt(baseString));

        final String expected = "tnnArxj06cWHq44gCs1OSKk/jLY=";
        final String actual = builder.build();

        assertEquals(actual, expected);
    }

    @Test
    public void nouncerExample() throws Exception {

        final String baseString
                = "GET"
                  + "&http%3A%2F%2Fphotos.example.net%2Fphotos"
                  + "&file%3Dvacation.jpg"
                  + "%26oauth_consumer_key%3Ddpf43f3p2l4k3l03"
                  + "%26oauth_nonce%3Dkllo9940pd9333jh"
                  + "%26oauth_signature_method%3DHMAC-SHA1"
                  + "%26oauth_timestamp%3D1191242096"
                  + "%26oauth_token%3Dnnch734d00sl2jdk"
                  + "%26oauth_version%3D1.0"
                  + "%26size%3Doriginal";
        final String consumerSecret = "kd94hf93k423kf44";
        final String tokenSecret = "pfkkdhi9sl3r4s00";

        final T builder = newInstance();

        builder
                .consumerSecret(consumerSecret)
                .tokenSecret(tokenSecret)
                .baseStringBuilder(new BaseStringBuilder().prebuilt(baseString));

        final String expected = "tR3+Ty81lMeYAr/Fid0kMTYa/WM=";
        final String actual = builder.build();

        assertEquals(actual, expected);
    }

}
