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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * A class for encoding/decoding form-urlencoded.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class _Formurl {

    static String encodeFormurl(final String decoded) {
        if (decoded == null) {
            throw new NullPointerException("null decoded");
        }
        try {
            return URLEncoder.encode(decoded, "UTF-8");
        } catch (final UnsupportedEncodingException uee) {
            throw new RuntimeException(uee.getMessage());
        }
    }

    static String decodeFormurl(final String encoded) {
        if (encoded == null) {
            throw new NullPointerException("null encoded");
        }
        try {
            return URLDecoder.decode(encoded, "UTF-8");
        } catch (final UnsupportedEncodingException uee) {
            throw new RuntimeException(uee.getMessage());
        }
    }

    private _Formurl() {
        super();
    }
}
