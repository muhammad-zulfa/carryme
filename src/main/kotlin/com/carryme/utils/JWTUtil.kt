package com.carryme.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import kotlin.reflect.KFunction1


@Component
class JWTUtil{

    private final val SECRET_KEY: String = "J7FdeEeD0r2uhUHkEnHL0NgJpKi9lxmaOK2yntDayvTQuhrO0aCreyPiBhsW1DF6"

    fun generateToken(userDetails: UserDetails) : String {
        val claims: Map<String,Object> = mapOf()
        return createToken(claims, userDetails.username)
    }

    private fun getSigningKey(): Key? {
        val keyBytes = Decoders.BASE64.decode(SECRET_KEY)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    private fun createToken(claims: Map<String, Object>, username: String?): String {

        return Jwts.builder().setClaims(claims)
                .setSubject(username)
                .setIssuedAt(Date(System.currentTimeMillis()))
                .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(getSigningKey()!!, SignatureAlgorithm.HS256).compact()
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean{
        val username: String = extractUsername(token)
        return (username == userDetails.username && !isTokenExpired(token))
    }

    fun extractUsername(token: String): String {
        return extractClaim(token,Claims::getSubject)
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String): Date {
        return extractClaim(token,Claims::getExpiration)
    }

    private fun <T> extractClaim(token: String, claimResolver: KFunction1<Claims, T>): T {
        val claim: Claims = extractAllClaim(token)
        return claimResolver.apply {  }.invoke(claim)
    }

    private fun extractAllClaim(token: String): Claims {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY)
                .build().parse(token).body as Claims
    }
}