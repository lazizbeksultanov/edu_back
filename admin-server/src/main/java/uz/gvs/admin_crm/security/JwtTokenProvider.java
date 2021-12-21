package uz.gvs.admin_crm.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uz.gvs.admin_crm.entity.User;

import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${app.jwtSecret}")
    private String secretKey;

    @Value("${app.jwtExpirationInMs}")
    private Long expireTime;

    public String generateToken(User user) {
        Date durationLife = new Date(new Date().getTime() + expireTime);
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("roles", user.getRoles())
                .setIssuedAt(new Date())
                .setExpiration(durationLife)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();

    }
}
