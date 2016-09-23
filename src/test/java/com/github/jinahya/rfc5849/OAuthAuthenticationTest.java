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

import static java.lang.invoke.MethodHandles.lookup;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class OAuthAuthenticationTest {

    private static final Logger logger = getLogger(lookup().lookupClass());

    /**
     * Tests against the example from {@code dev.twitter.com/oauth}.
     *
     * @throws Exception if an error occurs.
     * @see
     * <a href="https://dev.twitter.com/oauth/overview/creating-signatures">Creating
     * a signature Overview</a>
     * @see
     * <a href="https://dev.twitter.com/oauth/overview/authorizing-requests">Authorizing
     * a request Overview</a>
     */
    @Test
    public void twitterExample() throws Exception {
        final OAuthAuthentication builder = new OAuthAuthentication()
                .signer(
                        new OAuthSignerHmacSha1Jca()
                        .consumerSecret("kAcSOqF21Fu85e7zjz7ZN2U4ZRhfV3WpwPAoE3Z7kBw")
                        .tokenSecret("LswwdoUaIvS8ltyTt5jkRh4J50vUPVVHtR2YPi5kE")
                        .baseString(
                                new OAuthBaseString()
                                .httpMethod("POST")
                                .baseUri("https://api.twitter.com/1/statuses/update.json")
                                .queryParameter("include_entities", "true")
                                .oauthConsumerKey("xvz1evFS4wEEPTGEFPHBog")
                                .oauthNonce("kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg")
                                .oauthSignatureMethod("HMAC-SHA1")
                                .oauthTimestamp("1318622958")
                                .oauthToken("370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb")
                                .oauthVersion("1.0")
                                .entityParameter("status", "Hello Ladies + Gentlemen, a signed OAuth request!")
                        )
                );
        final String actual = builder.toHeader();
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
     * Tests against the example from {@code http://nouncer.com/oauth}.
     *
     * @throws Exception if an error occurs.
     * @see <a href="http://nouncer.com/oauth/authentication.html">OAuth 1.0
     * Authentication Sandbox</a>
     */
    @Test
    public void nouncerExample() throws Exception {
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
        final OAuthAuthentication builder = new OAuthAuthentication()
                .realm("http://photos.example.net/photos")
                .signer(
                        new OAuthSignerHmacSha1Jca()
                        .consumerSecret("kd94hf93k423kf44")
                        .tokenSecret("pfkkdhi9sl3r4s00")
                        .baseString(
                                new OAuthBaseString()
                                .httpMethod("GET")
                                .baseUri("http://photos.example.net/photos")
                                .queryParameter("size", "original")
                                .queryParameter("file", "vacation.jpg")
                                .oauthConsumerKey("dpf43f3p2l4k3l03")
                                .oauthNonce("kllo9940pd9333jh")
                                .oauthSignatureMethod("HMAC-SHA1")
                                .oauthTimestamp("1191242096")
                                .oauthToken("nnch734d00sl2jdk")
                                .oauthVersion("1.0")
                        )
                );
        final String actual = builder.toHeader();
        assertEquals(actual, expected);
    }

    /**
     * Tests against rfc5849.
     *
     * @throws Exception
     * @see <a href="http://tools.ietf.org/html/rfc5849#section-1.2">1.2.
     * Example</a>
     */
    @Test
    public void rfc5849_1_2() throws Exception {
        final String consumerKey = "dpf43f3p2l4k3l03";
        final String consumerSecret = "kd94hf93k423kf44";
        {
            final String documented
                    = "OAuth"
                      + " realm=\"Photos\","
                      + " oauth_consumer_key=\"dpf43f3p2l4k3l03\","
                      + " oauth_signature_method=\"HMAC-SHA1\","
                      + " oauth_timestamp=\"137131200\","
                      + " oauth_nonce=\"wIjqoS\","
                      + " oauth_callback=\"http%3A%2F%2Fprinter.example.com%2Fready\","
                      + " oauth_signature=\"74KNZJeDHnMBp0EMJ9ZHt%2FXKycU%3D\"";
            final String expected
                    = "OAuth"
                      + " realm=\"Photos\","
                      + " oauth_callback=\"http%3A%2F%2Fprinter.example.com%2Fready\","
                      + " oauth_consumer_key=\"dpf43f3p2l4k3l03\","
                      + " oauth_nonce=\"wIjqoS\","
                      + " oauth_signature=\"74KNZJeDHnMBp0EMJ9ZHt%2FXKycU%3D\","
                      + " oauth_signature_method=\"HMAC-SHA1\","
                      + " oauth_timestamp=\"137131200\"";
            final OAuthAuthentication builder = new OAuthAuthentication()
                    .realm("Photos")
                    .signer(
                            new OAuthSignerHmacSha1Bc()
                            .consumerSecret(consumerSecret)
                            .tokenSecret("")
                            .baseString(
                                    new OAuthBaseString()
                                    .httpMethod("POST")
                                    .baseUri("https://photos.example.net/initiate")
                                    .oauthConsumerKey(consumerKey)
                                    .oauthTimestamp("137131200")
                                    .oauthNonce("wIjqoS")
                                    .oauthCallback("http://printer.example.com/ready")
                            )
                    );
            final String actual = builder.toHeader();
            assertEquals(actual, expected);
        }
        {
            final String oauthToken = "hh5s93j4hdidpola";
            final String oauthTokenSecret = "hdhd0244k9j7ao03";
            final String oauthVerifier = "hfdp7dh39dks9884";
            final String documented
                    = "OAuth"
                      + " realm=\"Photos\","
                      + " oauth_consumer_key=\"dpf43f3p2l4k3l03\","
                      + " oauth_token=\"hh5s93j4hdidpola\","
                      + " oauth_signature_method=\"HMAC-SHA1\","
                      + " oauth_timestamp=\"137131201\","
                      + " oauth_nonce=\"walatlh\","
                      + " oauth_verifier=\"hfdp7dh39dks9884\","
                      + " oauth_signature=\"gKgrFCywp7rO0OXSjdot%2FIHF7IU%3D\"";
            final String expected
                    = "OAuth"
                      + " realm=\"Photos\","
                      + " oauth_consumer_key=\"dpf43f3p2l4k3l03\","
                      + " oauth_nonce=\"walatlh\","
                      + " oauth_signature=\"gKgrFCywp7rO0OXSjdot%2FIHF7IU%3D\","
                      + " oauth_signature_method=\"HMAC-SHA1\","
                      + " oauth_timestamp=\"137131201\","
                      + " oauth_token=\"hh5s93j4hdidpola\","
                      + " oauth_verifier=\"hfdp7dh39dks9884\"";
            final OAuthAuthentication builder = new OAuthAuthentication()
                    .realm("Photos")
                    .signer(
                            new OAuthSignerHmacSha1Bc()
                            .consumerSecret(consumerSecret)
                            .tokenSecret(oauthTokenSecret)
                            .baseString(
                                    new OAuthBaseString()
                                    .httpMethod("POST")
                                    .baseUri("https://photos.example.net/token")
                                    .oauthConsumerKey(consumerKey)
                                    .oauthTimestamp("137131201")
                                    .oauthNonce("walatlh")
                                    .oauthToken(oauthToken)
                                    .oauthVerifier(oauthVerifier)
                            )
                    );
            final String actual = builder.toHeader();
            assertEquals(actual, expected);
        }
        {
            final String oauthToken = "nnch734d00sl2jdk";
            final String oauthTokenSecret = "pfkkdhi9sl3r4s00";
            final String documented
                    = "OAuth"
                      + " realm=\"Photos\","
                      + " oauth_consumer_key=\"dpf43f3p2l4k3l03\","
                      + " oauth_token=\"nnch734d00sl2jdk\","
                      + " oauth_signature_method=\"HMAC-SHA1\","
                      + " oauth_timestamp=\"137131202\","
                      + " oauth_nonce=\"chapoH\","
                      + " oauth_signature=\"MdpQcU8iPSUjWoN%2FUDMsK2sui9I%3D\"";
            final String expected
                    = "OAuth"
                      + " realm=\"Photos\","
                      + " oauth_consumer_key=\"dpf43f3p2l4k3l03\","
                      + " oauth_nonce=\"chapoH\","
                      + " oauth_signature=\"MdpQcU8iPSUjWoN%2FUDMsK2sui9I%3D\","
                      + " oauth_signature_method=\"HMAC-SHA1\","
                      + " oauth_timestamp=\"137131202\","
                      + " oauth_token=\"nnch734d00sl2jdk\"";
            final OAuthAuthentication builder = new OAuthAuthentication()
                    .realm("Photos")
                    .signer(
                            new OAuthSignerHmacSha1Bc()
                            .consumerSecret(consumerSecret)
                            .tokenSecret(oauthTokenSecret)
                            .baseString(
                                    new OAuthBaseString()
                                    .httpMethod("GET")
                                    .baseUri("http://photos.example.net/photos")
                                    .oauthConsumerKey(consumerKey)
                                    .oauthTimestamp("137131202")
                                    .oauthNonce("chapoH")
                                    .oauthToken(oauthToken)
                                    .queryParameter("file", "vacation.jpg")
                                    .queryParameter("size", "original")
                            )
                    );
            final String actual = builder.toHeader();
            assertEquals(actual, expected);
        }
    }

    /**
     * Test against RFC5849.
     *
     * @throws Exception if failed
     * @see <a href="http://tools.ietf.org/html/rfc5849#section-2.1">2.1.
     * Temporary Credentials</a>
     */
    @Test
    public void rfc5849_2_1() throws Exception {
        final String documented
                = "OAuth"
                  + " realm=\"Example\","
                  + " oauth_consumer_key=\"jd83jd92dhsh93js\","
                  + " oauth_signature_method=\"PLAINTEXT\","
                  + " oauth_callback=\"http%3A%2F%2Fclient.example.net%2Fcb%3Fx%3D1\","
                  + " oauth_signature=\"ja893SD9%26\"";
        final String expected
                = "OAuth"
                  + " realm=\"Example\","
                  + " oauth_callback=\"http%3A%2F%2Fclient.example.net%2Fcb%3Fx%3D1\","
                  + " oauth_consumer_key=\"jd83jd92dhsh93js\","
                  + " oauth_signature=\"ja893SD9%26\","
                  + " oauth_signature_method=\"PLAINTEXT\"";
        final OAuthAuthentication builder = new OAuthAuthentication()
                .realm("Example")
                .signer(
                        new OAuthSignerPlaintext()
                        .consumerSecret("ja893SD9")
                        .tokenSecret("")
                        .baseString(
                                new OAuthBaseString()
                                .oauthConsumerKey("jd83jd92dhsh93js")
                                .oauthCallback("http://client.example.net/cb?x=1")
                        )
                );
        final String actual = builder.toHeader();
        assertEquals(actual, expected);
    }

    /**
     * Tests against rfc5849.
     *
     * @throws Exception if failed
     * @see <a href="http://tools.ietf.org/html/rfc5849#section-2.3">2.3. Token
     * Credentials</a>
     */
    @Test
    public void rfc5849_2_3() throws Exception {
        final String documented
                = "OAuth"
                  + " realm=\"Example\","
                  + " oauth_consumer_key=\"jd83jd92dhsh93js\","
                  + " oauth_token=\"hdk48Djdsa\","
                  + " oauth_signature_method=\"PLAINTEXT\","
                  + " oauth_verifier=\"473f82d3\","
                  + " oauth_signature=\"ja893SD9%26xyz4992k83j47x0b\"";
        final String expected
                = "OAuth"
                  + " realm=\"Example\","
                  + " oauth_consumer_key=\"jd83jd92dhsh93js\","
                  + " oauth_signature=\"ja893SD9%26xyz4992k83j47x0b\","
                  + " oauth_signature_method=\"PLAINTEXT\","
                  + " oauth_token=\"hdk48Djdsa\","
                  + " oauth_verifier=\"473f82d3\"";
        final OAuthAuthentication builder = new OAuthAuthentication()
                .realm("Example")
                .signer(
                        new OAuthSignerPlaintext()
                        .consumerSecret("ja893SD9")
                        .tokenSecret("xyz4992k83j47x0b")
                        .baseString(
                                new OAuthBaseString()
                                .oauthConsumerKey("jd83jd92dhsh93js")
                                .oauthToken("hdk48Djdsa")
                                .oauthVerifier("473f82d3")
                        )
                );
        final String actual = builder.toHeader();
        assertEquals(actual, expected);
    }

    /**
     * Tests against the example from {@code 3.1. Making Requests}.
     *
     * @throws Exception if an error occurs.
     * @see <a href="http://tools.ietf.org/html/rfc5849#section-3.1">3.1. Making
     * Requests</a>
     */
    @Test(enabled = true)
    public void rfc5849_3_1() throws Exception {
        final String documented
                = "OAuth"
                  + " realm=\"Example\","
                  + " oauth_consumer_key=\"9djdj82h48djs9d2\","
                  + " oauth_token=\"kkk9d7dh3k39sjv7\","
                  + " oauth_signature_method=\"HMAC-SHA1\","
                  + " oauth_timestamp=\"137131201\","
                  + " oauth_nonce=\"7d8f3e4a\","
                  + " oauth_signature=\"bYT5CMsGcbgUdFHObYMEfcx6bsw%3D\"";
        // with errata
        final String expected
                = "OAuth"
                  + " realm=\"Example\","
                  + " oauth_consumer_key=\"9djdj82h48djs9d2\","
                  + " oauth_nonce=\"7d8f3e4a\","
                  + " oauth_signature=\"r6%2FTJjbCOr97%2F%2BUU0NsvSne7s5g%3D\","
                  + " oauth_signature_method=\"HMAC-SHA1\","
                  + " oauth_timestamp=\"137131201\","
                  + " oauth_token=\"kkk9d7dh3k39sjv7\"";
        final OAuthAuthentication builder = new OAuthAuthentication()
                .realm("Example")
                .signer(
                        new OAuthSignerHmacSha1Bc()
                        .consumerSecret("j49sk3j29djd")
                        .tokenSecret("dh893hdasih9")
                        .baseString(
                                new OAuthBaseString()
                                .httpMethod("POST")
                                .baseUri("http://example.com/request")
                                .oauthConsumerKey("9djdj82h48djs9d2")
                                .oauthNonce("7d8f3e4a")
                                .oauthTimestamp("137131201")
                                .oauthToken("kkk9d7dh3k39sjv7")
                                .queryParameter("b5", "=%3D")
                                .queryParameter("a3", "a")
                                .queryParameter("c@", "")
                                .queryParameter("a2", "r b")
                                .entityParameter("c2", "")
                                .entityParameter("a3", "2 q")
                        )
                );
        final String actual = builder.toHeader();
        assertEquals(actual, expected);
    }
}
