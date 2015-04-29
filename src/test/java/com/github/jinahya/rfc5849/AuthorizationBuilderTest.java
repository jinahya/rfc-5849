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
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class AuthorizationBuilderTest {


    /**
     * Tests against example form {@code dev.twitter.com/oauth}.
     *
     * @throws Exception if an error occurs.
     *
     * @see
     * <a href="https://dev.twitter.com/oauth/overview/creating-signatures">Creating
     * a signature Overview</a>
     * @see
     * <a href="https://dev.twitter.com/oauth/overview/authorizing-requests">Authorizing
     * a request Overview</a>
     */
    @Test
    public void testTwitterExample() throws Exception {

        final AuthorizationBuilder builder = new AuthorizationBuilder()
            .signatureBuilder(
                new SignatureBuilderHmacSha1Jca()
                .consumerSecret("kAcSOqF21Fu85e7zjz7ZN2U4ZRhfV3WpwPAoE3Z7kBw")
                .tokenSecret("LswwdoUaIvS8ltyTt5jkRh4J50vUPVVHtR2YPi5kE")
                .baseStringBuilder(
                    new BaseStringBuilder()
                    .httpMethod("POST")
                    .baseUri("https://api.twitter.com/1/statuses/update.json")
                    .requestParameter("status", "Hello Ladies + Gentlemen, a signed OAuth request!")
                    .requestParameter("include_entities", "true")
                    .oauthConsumerKey("xvz1evFS4wEEPTGEFPHBog")
                    .oauthNonce("kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg")
                    .oauthSignatureMethod("HMAC-SHA1")
                    .oauthTimestamp("1318622958")
                    .oauthToken("370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb")
                    .oauthVersion("1.0")
                )
            );

        final String actual = builder.build();

        final String documented
            = "OAuth"
              + " oauth_consumer_key=\"xvz1evFS4wEEPTGEFPHBog\""
              + ", oauth_nonce=\"kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg\""
              + ", oauth_signature=\"tnnArxj06cWHq44gCs1OSKk%2FjLY%3D\""
              + ", oauth_signature_method=\"HMAC-SHA1\""
              + ", oauth_timestamp=\"1318622958\""
              + ", oauth_token=\"370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb\""
              + ", oauth_version=\"1.0\"";

        final String expected
            = "OAuth"
              + " oauth_consumer_key=\"xvz1evFS4wEEPTGEFPHBog\""
              + ", oauth_nonce=\"kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg\""
              + ", oauth_signature=\"tnnArxj06cWHq44gCs1OSKk%2FjLY%3D\""
              + ", oauth_signature_method=\"HMAC-SHA1\""
              + ", oauth_timestamp=\"1318622958\""
              + ", oauth_token=\"370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb\""
              + ", oauth_version=\"1.0\"";

        assertEquals(actual, expected);
    }


    /**
     *
     * @throws Exception
     *
     * @see <a href="http://nouncer.com/oauth/authentication.html">OAuth 1.0
     * Authentication Sandbox</a>
     */
    @Test
    public void testNouncerExample() throws Exception {

        final AuthorizationBuilder builder = new AuthorizationBuilder()
            .realm("http://photos.example.net/photos")
            .signatureBuilder(
                new SignatureBuilderHmacSha1Jca()
                .consumerSecret("kd94hf93k423kf44")
                .tokenSecret("pfkkdhi9sl3r4s00")
                .baseStringBuilder(
                    new BaseStringBuilder()
                    .httpMethod("GET")
                    .baseUri("http://photos.example.net/photos")
                    .requestParameter("size", "original")
                    .requestParameter("file", "vacation.jpg")
                    .oauthConsumerKey("dpf43f3p2l4k3l03")
                    .oauthNonce("kllo9940pd9333jh")
                    .oauthSignatureMethod("HMAC-SHA1")
                    .oauthTimestamp("1191242096")
                    .oauthToken("nnch734d00sl2jdk")
                    .oauthVersion("1.0")
                )
            );

        final String actual = builder.build();

        final String documented
            = "OAuth"
              + " realm=\"http://photos.example.net/photos\","
              + " oauth_consumer_key=\"dpf43f3p2l4k3l03\","
              + " oauth_token=\"nnch734d00sl2jdk\","
              + " oauth_nonce=\"kllo9940pd9333jh\","
              + " oauth_timestamp=\"1191242096\","
              + " oauth_signature_method=\"HMAC-SHA1\","
              + " oauth_version=\"1.0\","
              + " oauth_signature=\"tR3%2BTy81lMeYAr%2FFid0kMTYa%2FWM%3D\"";

        final String expected
            = "OAuth"
              + " realm=\"http://photos.example.net/photos\","
              + " oauth_consumer_key=\"dpf43f3p2l4k3l03\","
              + " oauth_nonce=\"kllo9940pd9333jh\","
              + " oauth_signature=\"tR3%2BTy81lMeYAr%2FFid0kMTYa%2FWM%3D\","
              + " oauth_signature_method=\"HMAC-SHA1\","
              + " oauth_timestamp=\"1191242096\","
              + " oauth_token=\"nnch734d00sl2jdk\","
              + " oauth_version=\"1.0\"";

        assertEquals(actual, expected);
    }


}

