package com.carryme.security

import com.carryme.jobs.TimeReportScheduler
import io.jsonwebtoken.io.IOException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import com.carryme.services.UserDetailService
import com.carryme.utils.JWTUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.Throws

@Component
class JwtAuthFilter(
        @Autowired
        val jwtUtil: JWTUtil,
        @Autowired
        val userDetailService: UserDetailService
): OncePerRequestFilter() {

    private val log: Logger = LoggerFactory.getLogger(TimeReportScheduler::class.java)

    @Throws(ServletException::class,IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        var authorizationHeader: String? = request.getHeader("Authorization")

        var username: String? = null
        var jwt: String? = null

        if(request.requestURL.toString().contains("/auth/")){
            authorizationHeader = null
        }
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            jwt = authorizationHeader.substring(7)
            username = jwtUtil.extractUsername(jwt)
        }

        if(username != null && SecurityContextHolder.getContext().authentication == null){
            val userDetails : UserDetails = userDetailService.loadUserByUsername(username)
            if(jwtUtil.validateToken(jwt!!,userDetails)){
                val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
                        userDetails,null,userDetails.authorities
                )

                usernamePasswordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
            }
        }
        filterChain.doFilter(request,response)
    }

}