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


import java.util.Objects;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @param <T>
 */
public abstract class SignatureBuilderTest<T extends SignatureBuilder> {


    public SignatureBuilderTest(final Class<T> builderType) {

        super();

        this.builderType
            = Objects.requireNonNull(builderType, "null builderType");
    }


    /**
     * Creates a new instance of {@link #builderType}. The {@code newInstance()}
     * method of {@code SignatureBuidlerTest} class returns the result of
     * {@code builderType#newInstance()}.
     *
     * @return a new instance of {@link #builderType}.
     */
    protected T newInstance() {

        try {
            return builderType.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    protected final Class<T> builderType;


}

