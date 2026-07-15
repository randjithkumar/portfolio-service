package com.peplatform.portfolioservice.common.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;

/**
 * Secret generator utility object for encoding and decoding sensitive data, such as JWT tokens.
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @date 2026.07.11
 * @since 0.0.1-SNAPSHOT
 */
public class SecretGenerator {

    public static void main(String[] args) {
        String secret = Encoders.BASE64.encode(
                Jwts.SIG.HS256.key().build().getEncoded()
        );

        System.out.println(secret);
    }
}