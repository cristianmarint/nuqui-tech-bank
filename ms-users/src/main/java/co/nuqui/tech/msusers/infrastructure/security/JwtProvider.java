package co.nuqui.tech.msusers.infrastructure.security;

import co.nuqui.tech.msusers.domain.dto.User;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;

import static java.util.Date.from;

@Service
public class JwtProvider {

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusMillis(30000)))
                .compact();
    }
}