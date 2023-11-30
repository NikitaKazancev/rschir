package ru.nk.rschir.authentication.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.nk.rschir.users.User;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${secret_key}")
    private String SECRET_KEY;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(SECRET_KEY)
        );
    }

    public String createToken(User user) {
        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 86400))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims getAllClaims(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    private <T> T getClaim(String jwt, Function<Claims, T> claimsGetFunction) {
        return claimsGetFunction.apply(getAllClaims(jwt));
    }

    public String getEmail(String jwt) {
        return getClaim(jwt, Claims::getSubject);
    }

    private Date getExpiration(String jwt) {
        return getClaim(jwt, Claims::getExpiration);
    }

    public boolean isExpired(String jwt) {
        return getExpiration(jwt).before(new Date());
    }

    public boolean isValid(String jwt, User user) {
        return getEmail(jwt).equals(user.getEmail()) && !isExpired(jwt);
    }

//    public String getFromHeader(HttpServletRequest request) {
//        String authHeader = request.getHeader("Authorization");
//
//    }
}
