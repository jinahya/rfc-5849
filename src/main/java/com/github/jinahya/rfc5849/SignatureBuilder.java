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

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public abstract class SignatureBuilder implements Builder<String> {

    // ------------------------------------------------------------ constructors
    SignatureBuilder(final String signatureMethod) {
        super();
        this.signatureMethod = signatureMethod;
    }

//    public SignatureBuilder(final SignatureMethod signatureMethod) {
//        super();
//        this.signatureMethod = signatureMethod.signatureMethod();
//    }
//    SignatureBuilder() {
//        this(null);
//    }
    // --------------------------------------------------------- signatureMethod
    /**
     * Returns signature method.
     *
     * @return signature method.
     */
    String signatureMethod() {
        return signatureMethod;
    }

//    public SignatureBuilder signatureMethod(final String signatureMethod) {
//        this.signatureMethod = signatureMethod;
//        return this;
//    }
    // ------------------------------------------------------- baseStringBuilder
    BaseStringBuilder baseStringBuilder() {
        return baseStringBuilder;
    }

    public SignatureBuilder baseStringBuilder(
            final BaseStringBuilder baseStringBuilder) {
        if (baseStringBuilder == null) {
            throw new NullPointerException("null baseStringBuilder");
        }
        this.baseStringBuilder = baseStringBuilder;
        this.baseStringBuilder.oauthSignatureMethod(signatureMethod);
        return this;
    }

    // -------------------------------------------------------------- baseString
    @Deprecated
    SignatureBuilder baseString(final String baseString) {
        if (baseString == null) {
            throw new NullPointerException("null baseString");
        }
        return baseStringBuilder(new BaseStringBuilder() {
            @Override
            public String build() {
                return baseString;
            }
        });
    }

    // -------------------------------------------------------------------------
//    private String prebuilt;
    private String signatureMethod;

    private BaseStringBuilder baseStringBuilder;
}
