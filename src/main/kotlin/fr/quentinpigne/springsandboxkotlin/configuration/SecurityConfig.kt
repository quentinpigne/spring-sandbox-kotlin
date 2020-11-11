package fr.quentinpigne.springsandboxkotlin.configuration

import fr.quentinpigne.springsandboxkotlin.utils.security.JwtPreAuthenticatedProcessingFilter
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtPreAuthenticatedProcessingFilter: JwtPreAuthenticatedProcessingFilter
) : WebSecurityConfigurerAdapter() {

    public override fun configure(http: HttpSecurity?) {
        // Disable CSRF (cross site request forgery)
        http?.csrf()?.disable()

        // No session will be created or used by spring security
        http?.sessionManagement()?.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        // Entry points
        http?.authorizeRequests()//
            ?.mvcMatchers(HttpMethod.GET, "/test/**")?.hasRole("ADMIN")
            // Disallow everything else...
            ?.anyRequest()?.authenticated()

        // Apply JWT
        http?.addFilter(jwtPreAuthenticatedProcessingFilter)
    }

    override fun configure(web: WebSecurity?) {
        web?.ignoring()//
            ?.mvcMatchers("/v2/api-docs")//
            ?.mvcMatchers("/swagger-resources/**")//
            ?.mvcMatchers("/swagger-ui/**")
    }
}
