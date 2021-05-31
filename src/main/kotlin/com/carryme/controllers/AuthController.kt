package com.carryme.controllers

import com.carryme.dto.requests.LoginRequestDto
import com.carryme.dto.requests.UserData
import com.carryme.dto.response.BaseResponse
import com.carryme.entities.User
import com.carryme.repositories.RoleRepository
import com.carryme.repositories.UserRepository
import com.carryme.services.UserDetailService
import com.carryme.utils.JWTUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.*

@CrossOrigin(origins = ["*","localhost:3001"], maxAge = 3600)
@RestController
@RequestMapping(value = ["/auth"])
class AuthController(
        @Autowired
        val authenticationManager: AuthenticationManager,
        @Autowired
        val userDetailsService: UserDetailService,
        @Autowired
        val jwtUtil: JWTUtil,
        @Autowired
        val userRepository: UserRepository,
        @Autowired
        val roleRepository: RoleRepository,
        @Autowired
        val passwordEncoder: PasswordEncoder
) : BaseController(){

    @RequestMapping("", method = [RequestMethod.GET])
    fun test(): String{
        return "test"
    }

    @RequestMapping(value = ["/login"], method = [RequestMethod.POST])
    fun signIn(@RequestBody loginForm: LoginRequestDto): BaseResponse {
        try {
            authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(loginForm.username,loginForm.password)
            )
        }catch (er: BadCredentialsException){
            val res = "Invalid Credential"
            return errorUnauthorizedResponse(res,null)!!
        }

        val userDetails: UserDetails = userDetailsService.loadUserByUsername(loginForm.username)

        val jwt: String = jwtUtil.generateToken(userDetails)
        return successResponse(jwt)!!
    }

    @RequestMapping(value = ["/register"], method = [RequestMethod.POST])
    fun signUp(@RequestBody registerForm: UserData): BaseResponse {
        try {
            val adminRole = roleRepository.findByName("ROLE_USER")!!
            val user = User()
            val date = Date()
            user.fullname = registerForm.fullname
            user.password = passwordEncoder.encode(registerForm.password)
            user.username  = registerForm.username
            user.phone = registerForm.phone
            user.roles = Arrays.asList(adminRole)
            user.active = true
            user.createdAt = date
            user.updatedAt = date
            userRepository.save(user)
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(registerForm.username,registerForm.password)
            )
        }catch (er: BadCredentialsException){
            val res = "Register Failed"
            return errorUnauthorizedResponse(res,null)!!
        }

        val userDetails: UserDetails = userDetailsService.loadUserByUsername(registerForm.username)

        val jwt: String = jwtUtil.generateToken(userDetails)
        return successResponse(jwt)!!
    }
}