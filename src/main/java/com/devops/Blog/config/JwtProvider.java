package com.devops.Blog.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class JwtProvider {

    private SecretKey key= Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

    public String generateToken(Authentication auth){

        Collection<? extends GrantedAuthority> authorities= auth.getAuthorities();
        String role = populateAuthorities(authorities);

        String jwt= Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+86400000))
                .claim("email",auth.getName())
                .claim("authorities",role)
                .signWith(key)
                .compact();
        return jwt;

    }

    public String populateAuthorities(Collection<? extends  GrantedAuthority> collection){
        Set<String> auth = new HashSet<>();
        for (GrantedAuthority authority:collection){
            auth.add(authority.getAuthority());
        }
        return String.join(",",auth);
    }
}


