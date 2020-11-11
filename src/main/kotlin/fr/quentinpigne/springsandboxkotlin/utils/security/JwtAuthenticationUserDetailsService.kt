package fr.quentinpigne.springsandboxkotlin.utils.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Encoders
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class JwtAuthenticationUserDetailsService : AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    private val BEARER_PREFIX = "Bearer "
    private val NAME_CLAIM = "name"
    private val AUTHORITIES_CLAIM = "authorities"

    @Value("\${security.jwt.token.secret-key}")
    private lateinit var secretKey: String

    @PostConstruct
    protected fun init() {
        secretKey = Encoders.BASE64.encode(secretKey.toByteArray())
    }

    override fun loadUserDetails(preAuthenticatedAuthenticationToken: PreAuthenticatedAuthenticationToken): UserDetails {
        val claims = decodeJwt(resolveToken(preAuthenticatedAuthenticationToken))
        return User.withUsername(claims[NAME_CLAIM, String::class.java]).password("").authorities(getAuthorities(claims)).accountExpired(false)
            .accountLocked(false).credentialsExpired(false).disabled(false).build()
    }

    private fun resolveToken(abstractAuthenticationToken: AbstractAuthenticationToken): String? {
        val bearerToken = abstractAuthenticationToken.principal as String?
        return if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) bearerToken.substring(7) else null
    }

    private fun decodeJwt(jwt: String?): Claims {
        val jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build()
        return jwtParser.parseClaimsJws(jwt).body
    }

    private fun getAuthorities(claims: Claims): List<GrantedAuthority> {
        val authorities = claims.get(AUTHORITIES_CLAIM, List::class.java)
        return authorities.map { SimpleGrantedAuthority(it as String) }
    }
}
