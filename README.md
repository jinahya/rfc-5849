# rfc5849
implementation of [The OAuth 1.0 Protocol](https://tools.ietf.org/html/rfc5849).

[![Dependency Status](https://www.versioneye.com/user/projects/57cbd063939fc6004abe4ba3/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/57cbd063939fc6004abe4ba3)
[![Build Status](https://travis-ci.org/jinahya/rfc5849.svg?branch=develop)](https://travis-ci.org/jinahya/rfc5849)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.jinahya/rfc5849.svg?maxAge=2592000&style=flat-square)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.jinahya%22%20a%3A%22rfc5849%22)
[![Javadocs](http://www.javadoc.io/badge/com.github.jinahya/rfc5849.svg?style=flat-square)](http://www.javadoc.io/doc/com.github.jinahya/rfc5849)

## Components

### OAuthBaseString

For building a [Signature Base String](https://tools.ietf.org/html/rfc5849#section-3.4.1).
````java
final OAuthBaseString baseString = new OAuthBaseString();
````
You can use following methods to set values.
````java
baseString.httpMethod(httpMethod);
baseString.baseUri(baseUri);
baseString.queryParameter(key, value);
baseString.protocolParameter(key, value); // key must start with 'oauth_'
baseString.entityParameter(key, value);
```

### OAuthSignature

For generating a [Signature](https://tools.ietf.org/html/rfc5849#section-3.4).

|class                   |method     |platform|
|------------------------|-----------|--------|
|`OAuthSignatureHmacSha1Bc` |`HMAC-SHA1`|BC      |
|`OAuthSignatureHmacSha1Jca`|`HMAC-SHA1`|JCA     |
|`OAuthSignatureRsaSha1Bc`  |`RSA-SHA1` |BC      |
|`OAuthSignatureRsaSha1Jca` |`RSA-SHA1` |JCA     |
|`OAuthSignaturePlaintext`  |`PLAINTEXT`|        |

#### HMAC-SHA1

There are two implementations. One is for Java [Cryptograph Architexture](http://docs.oracle.com/javase/8/docs/technotes/guides/security/crypto/CryptoSpec.html) and the other is for [Legion of the Bouncy Castle](http://www.bouncycastle.org/java.html).
```java
final OAuthSignatureHmacSha1 signature = new OAuthSignatureHmacSha1Bc();
final OAuthSignatureHmacSha1 signature = new OAuthSignatureHmacSha1Jca();
```
You can use following methods to set values.
```java
signature.consumerSecret(consumerSecret);
signature.tokenSecret(tokenSecret);
signature.baseString(baseString); // OAuthBaseString
```

#### RSA-SHA1

There are, again, two implementations.  One is for Java [Cryptograph Architexture](http://docs.oracle.com/javase/8/docs/technotes/guides/security/crypto/CryptoSpec.html) and the other is for [Legion of the Bouncy Castle](http://www.bouncycastle.org/java.html).
```java
final OAuthSignatureRsaSha1 signature = new OAuthSignatureRsaSha1Bc(); // BC
final OAuthSignatureRsaSha1 signature = new OAuthSignatureRsaSha1Jca(); // JCA
```
You can use following methods to set values.
```java
signature.initParam(PrivateKey privateKey); // JCA
signature.initParam(CipherParameter cipherParameter); // BC
```

#### PLAINTEXT

```java
new OAuthSignaturePlaintext();
```

### OAuthTransmission

```java
final OAuthAuthentication authentication = new OAuthAuthentication();
authentication.realm(realm);
authentication.signer(signer); // OAuthSigner
```
Three kinds of output defined in [3.5. Parameter Transmission](https://tools.ietf.org/html/rfc5849#section-3.5) are supported.
```
final String header = authentication.toHeader(); // Authorization Header
final String body = authentication.toBody(); // Form-Encoded Body
final String query = authentication.toQuery(); // Request URI Query
```

## Examples

```java
final OAuthAuthentication authentication = new OAuthAuthentication()
    .realm("Example")
    .signer(
        new OAuthSignerHmacSha1Bc()
            .consumerSecret("j49sk3j29djd")
            .tokenSecret("dh893hdasih9")
            .baseString(
                new OAuthBaseString()
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

final String header = authentication.toHeader();
````
