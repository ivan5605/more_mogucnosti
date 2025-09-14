package hr.moremogucnosti.more_mogucnosti_backend.security;

import hr.moremogucnosti.more_mogucnosti_backend.entity.Korisnik;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    } //makni ne treba

    public String generateToken(Korisnik k) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("uloga", k.getUloga().getNazivUloga());
        var now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(k.getId()))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationMs))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String validateAndGetSubject(String token){
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    } //ako potpis ne paše - SignatureException, ako je istekel ExpiredJwtException makni ne treba

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public long getExpirationMs(String token) {
        if (token==null) {
            return 0L;
        }

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration().toInstant().toEpochMilli();
    } //maknut cu dok sve ostale file-ove prilagodim

    public Long extractUserId(String token) {
        return Long.valueOf(extractAllClaims(token).getSubject());
    } //ne treba validate jer imam ovo pa kao sub=id?


    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Long extractExpirationAt(String token) {
        return extractAllClaims(token).get("exp", Long.class);
    }
}
