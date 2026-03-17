package com.collins.Skaet_wallet_system.config.SecurityConfig.SecurityServices;

import com.collins.Skaet_wallet_system.dtos.Request.LoginRequestDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final String secretKey = "wtxXrFRTGOy1OSiyshjNNrTvyEq1xttWbXlAcecbqH5";

    public String generateToken(LoginRequestDto loginRequest){
        Map<String,Object> claims = new HashMap<>();
        claims.put("email",loginRequest.getEmail());
        claims.put("role","User");

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(loginRequest.getEmail())
                .setIssuer("Skaet wallet system")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (10 * 60 * 1000))) // 10mins
                .signWith(generateKey())
                .compact();
    }

    private SecretKey generateKey(){
        byte[] decode
                = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(decode);
    }


    public String extractUserName(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    private<T> T extractClaims(String token, Function<Claims,T> claimsResolver){
        Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder().
                setSigningKey(generateKey()).
                build().
                parseClaimsJws(token).
                getBody();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }
}
