# rfc5849
implementation of [The OAuth 1.0 Protocol](https://tools.ietf.org/html/rfc5849).

[![Dependency Status](https://www.versioneye.com/user/projects/57cbd063939fc6004abe4ba3/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/57cbd063939fc6004abe4ba3)
[![Build Status](https://travis-ci.org/jinahya/rfc5849.svg?branch=develop)](https://travis-ci.org/jinahya/rfc5849)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.jinahya/rfc5849.svg?maxAge=2592000&style=flat-square)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.jinahya%22%20a%3A%22rfc5849%22)
[![Javadocs](http://www.javadoc.io/badge/com.github.jinahya/rfc5849.svg?style=flat-square)](http://www.javadoc.io/doc/com.github.jinahya/rfc5849)

## buiders

### BaseStringBuilder
````java
final BaseStringBuilder builder = new BaseStringBuilder();
````
You can use following methods to set values.
````java
builder.httpMethod(httpMethod);
builder.baseUri(baseUri);
builder.queryParameter(key, value);
builder.protocolParameter(key, value); // key must start with 'oauth_'
builder.entityParameter(key, value);
````

### SignatureBuilder

|class                        |method   |platform|
|-----------------------------|---------|--------|
|`SignatureBuilderHmacSha1Bc` |HMAC-SHA1|BC      |
|`SignatureBuilderHmacSha1Jca`|HMAC-SHA1|JCA     |
|`SignatureBuilderRsaSha1Bc`  |RSA-SHA1 |BC      |
|`SignatureBuilderRsaSha1Jca` |RSA-SHA1 |JCA     |
|`SignatureBuilderPlaintext`  |PLAINTEXT|        |

#### HMAC-SHA1

There are two implementations. One for Java Cryptograph Architexture/Extention and one for Bouncy Castle.
````java
new SignatureBuilderHmacSha1Bc();
new SignatureBuilderHmacSha1Jca();
````
When you create a builder you can use following methods to set values.
````java
signatureBuilder.consumerSecret(consumerSecret);
signatureBuilder.tokenSecret(tokenSecret);
signatureBuilder.baseStringBuilder(baseStringBuilder);
````

#### RSA-SHA1

````java
new SignatureBuilderRsaSha1Bc();
new SignatureBuilderRsaSha1Jca();
```
You can use following methods to set values.
````java
signatureBuilder.privateKey(privateKey);
signatureBuidler.baseStringBuilder(baseStringBuilder);
````

#### PLAINTEXT

````java
new SignaatureBuilderPlaintext();
````

### AuthorizationBuilder

````java
final AuthorizationBuilder authorizationBuilder = new AuthorizationBuilder();
authorizationBuilder.realm(realm);
authorizationBuilder.signatureBuilder(signatureBuilder);
````

## examples

````java
final AuthorizationBuilder builder = new AuthorizationBuilder()
    .realm("Example")
    .signatureBuilder(
        new SignatureBuilderHmacSha1Bc()
            .consumerSecret("j49sk3j29djd")
            .tokenSecret("dh893hdasih9")
            .baseStringBuilder(
                new BaseStringBuilder()
                    .httpMethod("POST")
                    .baseUri("http://example.com/request")
                    .queryParameter("b5", "=%3D")
                    .queryParameter("a3", "a")
                    .queryParameter("c@", "")
                    .queryParameter("a2", "r b")
                    .oauthConsumerKey("9djdj82h48djs9d2")
                    .oauthNonce("7d8f3e4a")
                    .oauthTimestamp("137131201")
                    .oauthToken("kkk9d7dh3k39sjv7")
                    .entityParameter("c2", "")
                    .entityParameter("a3", "2 q")
        )
    );

final String authorization = builder.build();
````
