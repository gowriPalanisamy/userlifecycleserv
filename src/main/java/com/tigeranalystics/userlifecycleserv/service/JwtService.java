package com.tigeranalystics.userlifecycleserv.service;

import com.tigeranalystics.userlifecycleserv.entity.UserInfo;
import com.tigeranalystics.userlifecycleserv.model.UserInfoDetails;
import com.tigeranalystics.userlifecycleserv.repository.UserInfoRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    @Autowired
    private UserInfoRepository repository;

    // Generate token with given user name
    public String generateToken(Authentication authentication) {
        logger.info("Generating token for the User");
        Map<String, Object> claims = new HashMap<>();
        UserInfoDetails userInfoDetails = (UserInfoDetails) authentication.getPrincipal();
        Optional<UserInfo> userInfo = repository.findByEmail(userInfoDetails.getEmail());
        List<String> roles = userInfoDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        claims.put("roles",roles);
        return createToken(claims,userInfo.get().getId());
    }

    // Create a JWT token with specified claims and subject (user account number)
    private String createToken(Map<String, Object> claims, Long id) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(id))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // Token valid for 30 minutes
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Get the signing key for JWT token
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extract the username from the token
    public String extractUserAccountNumber(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract the expiration date from the token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract a claim from the token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Check if the token is expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Validate the token against user details and expiration
    public Boolean validateToken(String token, UserInfoDetails userDetails) {
        final String userAccNumber = extractUserAccountNumber(token);
        return (userAccNumber.equals(String.valueOf(userDetails.getUserAccountNumber())) && !isTokenExpired(token));
    }
}
