# rfc-5849
## BaseStringBuilder
````java
final BaseStringBuilder builder = new BaseStringBuilder();
````
You can use following methods to set values.
````java
baseStringBuilder.httpMethod(httpMethod);
baseStringBuilder.baseUri(baseUri);
baseStringBuilder.queryParameter(key, value);
baseStringBuilder.protocolParameter(key, value); // key must start with 'oauth_'
baseStringBuilder.entityParameter(key, value);
````
## SignatureBuilder
### HMAC-SHA1
There are two implementations. One for Java Cryptograph Architexture/Extention and one for Bouncy Castle.
````java
new SignatureBuilderHmacSha1Bc();
new SignatureBuilderHmacSha1Jc();
````
When you create a builder you can use following methods to set values.
````java
signatureBuilder.consumerSecret(consumerSecret);
signatureBuilder.tokenSecret(tokenSecret);
signatureBuilder.baseStringBuilder(baseStringBuilder);
````
### RSA-SHA1
````java
new SignatureBuilderRsaSha1Bc();
new SignatureBuilderRsaSha1Jc();
```
You can use following methods to set values.
````java
signatureBuilder.privateKey(privateKey);
signatureBuidler.baseStringBuilder(baseStringBuilder);
````
### PLAINTEXT
````java
new SignaatureBuilderPlaintext();
````
## AuthorizationBuilder
````java
final AuthorizationBuilder authorizationBuilder = new AuthorizationBuilder();
authorizationBuilder.realm(realm);
authorizationBuilder.signatureBuilder(signatureBuilder);
````
## Examples
````java
final AuthorizationBuilder builder = new AuthorizationBuilder()
    .realm("Example")
    .signatureBuilder(new SignatureBuilderHmacSha1Bc()
        .consumerSecret("j49sk3j29djd")
        .tokenSecret("dh893hdasih9")
        .baseStringBuilder(new BaseStringBuilder()
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
