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


import com.github.jinahya.rfc5849.Constants;
import java.awt.Desktop;
import java.io.Console;
import static java.lang.invoke.MethodHandles.lookup;
import java.net.URI;
import static java.util.Optional.ofNullable;
import java.util.function.Supplier;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;


/**
 *
 * {@code mvn -Dexec.classpathScope=test \
 * -Dexec.mainClass=com.github.jinahya.rfc5849.main.TwitterPinBasedOAuth \
 * -Dtwitter.consumerKey=<consumerKey> \
 * -Dtwitter.consumerSecret=<consumerSecret> test-compile exec:java}
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see <a href="https://dev.twitter.com/oauth/pin-based">PIN-based
 * authorization</a>
 */
public class TwitterPinBasedOAuth {


    private static String systemProperty(final String key,
                                         final Supplier<String> def) {

        return ofNullable(System.getProperty(key)).orElseGet(def);
    }


    private static String systemProperty(final String key) {

        return systemProperty(key, () -> {
            throw new RuntimeException("missing property: " + key);
        });
    }


    public static void main(final String[] args) throws Exception {

        final Logger logger = getLogger(lookup().lookupClass());

        final String consumerKey = systemProperty("twitter.consumerKey");
        final String consumerSecret = systemProperty("twitter.consumerSecret");

        final Console console = System.console();
        if (console == null) {
            System.err.printf("No console found.\n");
            System.exit(1);
        }

        final TwitterClient client = new TwitterClient();

        final MultivaluedMap<String, String> requestToken
            = client.oAuthRequestToken(consumerKey, consumerSecret,
                                       Constants.OAUTH_CALLBACK_OUT_OF_BAND);
        console.printf("Request token has been retrived.\n");
        String oauthToken = requestToken.getFirst(Constants.OAUTH_TOKEN);
        console.printf("\toauthToken: %1$s\n", oauthToken);
        String oauthTokenSecret
            = requestToken.getFirst(Constants.OAUTH_TOKEN_SECRET);
        console.printf("\t%1$s\n", oauthTokenSecret);
        final String oauthCallbackConfirmed
            = requestToken.getFirst(Constants.OAUTH_CALLBACK_CONFIRMED);
        console.printf("%1$s\n", oauthCallbackConfirmed);

        final URI authorizationUri
            = UriBuilder.fromUri(TwitterConstants.URL_OAUTH_AUTHORIZE)
            .queryParam(Constants.OAUTH_TOKEN, oauthToken).build();
        console.printf("authorizationUri: %1$s\n", authorizationUri);
        try {
            final Desktop desktop = Desktop.getDesktop();
            console.printf(
                "Now, I'm going to open a browser for your autohrization. [Enter to continue...]");
            console.readLine();
            desktop.browse(authorizationUri);
        } catch (final Exception e) {
            console.printf("Can't open a browser.\n");
            console.printf(
                "Please go to " + authorizationUri + " and pass me the PIN.\n");
        }
        final String pin = console.readLine("PIN: ").trim();
        if (pin.isEmpty()) {
            console.printf("An empty PIN entered. Exiting...");
            System.exit(0);
        }

        final MultivaluedMap<String, String> oauthAccessToken
            = client.oAuthAccessToken(
                consumerKey, consumerSecret, oauthToken, oauthTokenSecret, null,
                null, null, pin);
        console.printf("Access token retrived.\n");
        oauthToken = oauthAccessToken.getFirst(Constants.OAUTH_TOKEN);
        console.printf("\toauthToken: %1$s\n", oauthToken);
        oauthTokenSecret = oauthAccessToken.getFirst(Constants.OAUTH_TOKEN_SECRET);
        console.printf("\toauthTokenSecret: %1$s\n", oauthTokenSecret);
    }


}

