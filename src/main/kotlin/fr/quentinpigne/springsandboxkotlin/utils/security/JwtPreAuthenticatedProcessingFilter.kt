package fr.quentinpigne.springsandboxkotlin.utils.security

import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

@Component
class JwtPreAuthenticatedProcessingFilter(
    jwtAuthenticationUserDetailsService: JwtAuthenticationUserDetailsService
) : AbstractPreAuthenticatedProcessingFilter() {

    init {
        val preAuthenticatedAuthenticationProvider = PreAuthenticatedAuthenticationProvider()
        preAuthenticatedAuthenticationProvider.setPreAuthenticatedUserDetailsService(jwtAuthenticationUserDetailsService)
        this.setAuthenticationManager(ProviderManager(listOf(preAuthenticatedAuthenticationProvider)))
    }

    override fun getPreAuthenticatedPrincipal(httpServletRequest: HttpServletRequest): Any? = httpServletRequest.getHeader(HttpHeaders
        .AUTHORIZATION)

    override fun getPreAuthenticatedCredentials(httpServletRequest: HttpServletRequest): Any = "N/A"
}
