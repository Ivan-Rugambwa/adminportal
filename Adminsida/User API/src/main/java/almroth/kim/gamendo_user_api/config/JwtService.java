package almroth.kim.gamendo_user_api.config;

import almroth.kim.gamendo_user_api.account.model.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private final NotionConfigProperties env;

    public JwtService(NotionConfigProperties env) {
        this.env = env;
    }


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRoles(String token) {
        return extractClaim(token, (Claims c) -> c.get("role").toString());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(Account userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public boolean isValidToken(String token, UserDetails userDetails) {
        final String userEmail = extractUsername(token);
        return (userEmail.equals((userDetails.getUsername()))) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(Map<String, Object> extraClaims, Account userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .claim("role", userDetails.getRoles())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60)))
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        var secretKey = env.secret();
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
