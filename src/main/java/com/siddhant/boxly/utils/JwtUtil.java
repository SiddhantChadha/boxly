package com.siddhant.boxly.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secretKey}")
    private String jwtSecret;

    public String generateToken(String userName){
        return Jwts.builder()
                .subject(userName)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + Constants.ACCESS_TOKEN_EXPIRY ))
                .signWith(getSignInKey())
                .compact();
    }

    public String resolveToken(HttpServletRequest request){

        String bearerToken = request.getHeader(Constants.TOKEN_HEADER);

        if(bearerToken!=null && bearerToken.startsWith(Constants.TOKEN_PREFIX)){
            return bearerToken.substring(Constants.TOKEN_PREFIX.length());
        }

        return null;
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        String username = extractUsername(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date(System.currentTimeMillis()));
    }

    private Jws<Claims> extractAllClaims(String token) {

        return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token);

    }

    public String extractUsername(String token){
        return extractAllClaims(token).getPayload().getSubject();
    }

    private Date extractExpiration(String token){
        return extractAllClaims(token).getPayload().getExpiration();
    }

    private SecretKey getSignInKey(){
        return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtSecret));
    }


}
