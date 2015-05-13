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


package com.github.jinahya.rfc5849.main;


import com.github.jinahya.rfc5849.AuthorizationBuilder;
import com.github.jinahya.rfc5849.BaseStringBuilder;
import com.github.jinahya.rfc5849.NonceBuilder;
import com.github.jinahya.rfc5849.SignatureBuilderHmacSha1Bc;
import com.github.jinahya.rfc5849.TimestampBuilder;
import com.github.jinahya.rfc5849.util.Params;
import static java.lang.invoke.MethodHandles.lookup;
import java.util.List;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class TwitterClient {


    public TwitterClient() {

        super();
    }


    public MultivaluedMap<String, String> oAuthRequestToken(
        final String consumerKey, final String consumerSecret,
        final String oauthCallback)
        throws Exception {

        final String httpMethod = HttpMethod.POST;
        final String baseUri = TwitterConstants.URL_OAUTH_REQUEST_TOKEN;

        final AuthorizationBuilder builder = new AuthorizationBuilder()
            .signatureBuilder(
                new SignatureBuilderHmacSha1Bc()
                .consumerSecret(consumerSecret)
                .tokenSecret("")
                .baseStringBuilder(
                    new BaseStringBuilder()
                    .httpMethod(httpMethod)
                    .baseUri(baseUri)
                    .oauthCallback(oauthCallback)
                    .oauthConsumerKey(consumerKey)
                    .oauthVersion("1.0")
                    .oauthNonce(new NonceBuilder())
                    .oauthTimestamp(new TimestampBuilder())
                )
            );
        final String authorization = builder.build();
        //logger.debug("authorization: {}", authorization);

        final Response response
            = ClientBuilder.newClient()
            .target(baseUri)
            .request()
            .header("Authorization", authorization)
            .method(httpMethod);
        final Response.StatusType status = response.getStatusInfo();
        if (Family.familyOf(response.getStatus()) != Family.SUCCESSFUL) {
            final int statusCode = status.getStatusCode();
            final String reasonPhrase = status.getReasonPhrase();
            //logger.debug("status: {} {}", statusCode, reasonPhrase);
            throw new RuntimeException(
                "Failed to get request token. Status: " + statusCode + " "
                + reasonPhrase);
        }

        return response.readEntity(new GenericType<Form>() {
        }).asMap();
    }


    public MultivaluedMap<String, String> oAuthAccessToken(
        final String consumerKey, final String consumerSecret,
        final String oauthToken, final String oauthTokenSecret,
        final String xAuthPassword, final String xAuthUsername,
        final String xAuthMode, final String oauthVerifier)
        throws Exception {

        final String httpMethod = HttpMethod.POST;
        final String baseUri = TwitterConstants.URL_OAUTH_ACCESS_TOKEN;

        final Params entityParameters = new Params();
        if (xAuthPassword != null) {
            entityParameters.putSingle(TwitterConstants.KEY_X_AUTH_PASSWORD, xAuthPassword);
        }
        if (xAuthUsername != null) {
            entityParameters.putSingle(TwitterConstants.KEY_X_AUTH_USERNAME, xAuthUsername);
        }
        if (xAuthMode != null) {
            entityParameters.putSingle(TwitterConstants.KEY_X_AUTH_MODE, xAuthMode);
        }

        final AuthorizationBuilder builder = new AuthorizationBuilder()
            .signatureBuilder(
                new SignatureBuilderHmacSha1Bc()
                .consumerSecret(consumerSecret)
                .tokenSecret(oauthTokenSecret)
                .baseStringBuilder(
                    new BaseStringBuilder()
                    .httpMethod(httpMethod)
                    .baseUri(baseUri)
                    .oauthConsumerKey(consumerKey)
                    .oauthToken(oauthToken)
                    .oauthNonce(new NonceBuilder())
                    .oauthTimestamp(new TimestampBuilder())
                    .oauthVerifier(oauthVerifier)
                    .oauthVersion("1.0")
                    .entityParameters(entityParameters)
                )
            );
        final String authorization = builder.build();
        //logger.debug("authorization: {}", authorization);

        final Response response
            = ClientBuilder.newClient()
            .target(baseUri)
            .request()
            .header("Authorization", authorization)
            .post(Entity.entity(entityParameters.printFormurlEncoded(),
                                MediaType.APPLICATION_FORM_URLENCODED_TYPE));
        final Response.StatusType status = response.getStatusInfo();
        if (Family.familyOf(response.getStatus()) != Family.SUCCESSFUL) {
            final int statusCode = status.getStatusCode();
            final String reasonPhrase = status.getReasonPhrase();
            //logger.debug("status: {} {}", statusCode, reasonPhrase);
            throw new RuntimeException(
                "Failed to get access token. Status: " + statusCode + " "
                + reasonPhrase);
        }

        return response.readEntity(new GenericType<Form>() {
        }).asMap();
    }


    private final Logger logger = getLogger(lookup().lookupClass());


}

