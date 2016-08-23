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

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public final class TwitterConstants {

    public static final String URL_OAUTH_REQUEST_TOKEN
            = "https://api.twitter.com/oauth/request_token";

    public static final String URL_OAUTH_AUTHORIZE
            = "https://api.twitter.com/oauth/authorize";

    public static final String URL_OAUTH_ACCESS_TOKEN
            = "https://api.twitter.com/oauth/access_token";

    public static final String KEY_X_AUTH_PASSWORD = "x_auth_password";

    public static final String KEY_X_AUTH_USERNAME = "x_auth_username";

    public static final String KEY_X_AUTH_MODE = "x_auth_mode";

    private TwitterConstants() {
        super();
    }
}
