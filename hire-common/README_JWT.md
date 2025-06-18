jwt 由3段组成
`Header`+ `Payload`+ `Verify Signature`
第三部分: `Verify Signature` 用的算法是：HMACSHA256(base4URLEncode(Header)+'.'+base4URLEncode(Payload)+自定义签名)
所以自定义签名不同，得到的第三部分也不同，需要把自定义签名需要好好保存。


引入：

```XML
            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt-api</artifactId>
                <version>0.12.3</version>
            </dependency>
            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt-impl</artifactId>
                <version>0.12.3</version>
                <scope>runtime</scope>
            </dependency>
            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt-jackson</artifactId>
                <version>0.12.3</version>
                <scope>runtime</scope>
            </dependency>

```

根据上面的规则，代码应该如下：
```java
    // userKey就是自定义的密钥，先转化为base64

  String base64 = Base64.getEncoder().encodeToString(userKey.getBytes());

  // 2. 对base64生成一个秘钥的对象
  SecretKey secretKey = Keys.hmacShaKeyFor(base64.getBytes());
  
  // 然后把secretKey 连同body整体给Jwt，注意可以设置过期时间

  Date expireDate = new Date(System.currentTimeMillis() + expireTimes);
  Jwts.builder().subject(body).expiration(expireDate)
                .signWith(secretKey)
                .compact();
  
  // 如果需要验证jwt，需要传入前端的此jwt，通过secretKey生成jwtParser，然后调用parseSignedClaims方法解析此jwt
  // 3. 校验jwt
  JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build(); // 构造解析器
  // 解析成功，可以获得Claims，从而去get相关的数据，如果此处抛出异常，则说明解析不通过，也就是token失效或者被篡改
  Jws<Claims> jws = jwtParser.parseSignedClaims(pendingJWT);      // 解析jwt
    
```