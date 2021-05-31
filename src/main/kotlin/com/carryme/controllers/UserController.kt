package com.carryme.controllers

import com.carryme.dto.requests.LoginRequestDto
import com.carryme.dto.response.BaseResponse
import com.carryme.dto.response.UserResponseDto
import com.carryme.entities.User
import com.carryme.repositories.UserRepository
import com.carryme.services.UserDetailService
import com.carryme.utils.JWTUtil
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping(value = ["/user"])
class UserController(
        @Autowired
        val authenticationManager: AuthenticationManager,
        @Autowired
        val userDetailsService: UserDetailService,
        @Autowired
        val userRepository: UserRepository,
        @Autowired
        val jwtUtil: JWTUtil
) : BaseController(){

    //@PreAuthorize("hasAnyRole('ADMIN','USER')")
    @RequestMapping("/me", method = [RequestMethod.GET])
    fun test(principal: Principal): BaseResponse {
        val u: User = userDetailsService.findByUsername(principal.name)
        var response = UserResponseDto()
        BeanUtils.copyProperties(u,response)
        val roles = mutableListOf<String>()
        u.roles!!.forEach {
            roles.add(it.name!!)
        }
        response.roles = roles
        return successResponse(response)!!
    }

    @RequestMapping("/all-guest", method = [RequestMethod.GET])
    fun allGuest(principal: Principal): BaseResponse {
        val u: User = userDetailsService.findByUsername(principal.name)
        var guestUser = userRepository.findAllByGuestUserId(u.id)
        var response: MutableList<UserResponseDto> = mutableListOf()
        val userDtoM = UserResponseDto()
        BeanUtils.copyProperties(u,userDtoM)
        response.add(userDtoM)
        guestUser.forEach {
            val userDto = UserResponseDto()
            BeanUtils.copyProperties(it,userDto)
            response.add(userDto)
        }

        return successResponse(response)!!
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
}