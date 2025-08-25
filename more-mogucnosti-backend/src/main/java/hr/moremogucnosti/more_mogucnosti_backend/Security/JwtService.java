package hr.moremogucnosti.more_mogucnosti_backend.Security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtService {
    @Value("${app.jwt.secret}") private String secret;
    @Value("${app.jwt.expiration}") private long expirationMs;

    public String generate(String username){
        var now = new Date();
        return Jwts.builder()
                .setSubject(username) //postavlja subject claim, email korisnika
                .setIssuedAt(now) //kad je izdan token
                .setExpiration(new Date(now.getTime() + expirationMs)) //kad istječe
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256) //digitalno potpisuje token, koristi tajni ključ iz env varijable, i HS256 algoritam
                .compact();
    }

    public String validateAndGetSubject(String token){
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    } //ako potpis ne paše - SignatureException, ako je istekel ExpiredJwtException
}
