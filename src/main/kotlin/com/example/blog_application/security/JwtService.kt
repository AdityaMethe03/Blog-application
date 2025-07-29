package com.example.blog_application.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*

@Service
class JwtService {
    @Value("\${application.security.jwt.secret-key}")
    private lateinit var secretKey: String // Use lateinit for injected properties

    fun extractUsername(token: String): String {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).body.subject
    }

    fun generateToken(userDetails: UserDetails): String {
        return Jwts.builder()
            .setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 hours
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        val isTokenExpired = Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).body.expiration.before(Date())
        return (username == userDetails.username) && !isTokenExpired
    }

    private fun getSignInKey(): Key {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}