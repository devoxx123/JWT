package com.jwt.demo.config;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.jwt.demo.model.NetworkResponseDTO;
import com.jwt.demo.model.User;
import com.jwt.demo.utils.NetworkUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import static com.jwt.demo.model.Constants.SIGNING_KEY;
import static com.jwt.demo.model.Constants.ACCESS_TOKEN_VALIDITY_SECONDS;



@Component
public class JwtTokenUtil implements Serializable {
	
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    
    public String generateToken(String username, HttpServletRequest request) {
    	   NetworkResponseDTO network = NetworkUtils.getDeviceAddresses.apply(request);

        return Jwts.builder()
                .setSubject(username)
                .claim("mac", network.getMacAddress())
                .claim("ip", network.getIpAddress())
                .claim("scopes", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .setIssuer("http://github.com")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS*1000))
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
                .compact();
    }
    

    /*public String generateToken(User user, HttpServletRequest request) {
        return doGenerateToken(user.getUsername());
    }
  
    private String doGenerateToken(String username) {
    	   NetworkResponseDTO network = NetworkUtils.getDeviceAddresses.apply(request);
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("scopes", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        claims.put("mac", network.getMacAddress());
        claims.put("ip", network.getIpAddress());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("http://greenlearner.com")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS*1000))
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
                .compact();
    }*/

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (
              username.equals(userDetails.getUsername())
                    && !isTokenExpired(token));
    }

}
