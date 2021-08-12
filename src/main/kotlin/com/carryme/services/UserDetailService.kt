package com.carryme.services

import com.carryme.entities.Privileges
import com.carryme.entities.Role
import com.carryme.entities.User
import com.carryme.repositories.RoleRepository
import com.carryme.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.*
import java.util.stream.Collectors


@Service
class UserDetailService: UserDetailsService,IUserService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var roleRepository: RoleRepository

    override fun loadUserByUsername(username: String?): UserDetails {
        val user: User = userRepository.findByUsernameLogin(username!!)
                ?: return org.springframework.security.core.userdetails.User(
                        " ", " ", true, true, true, true,
                        getAuthorities(Arrays.asList(
                                roleRepository.findByName("ROLE_USER")!!)))

        return org.springframework.security.core.userdetails.User(
                user.username, user.password, user.active, true, true,
                true, getAuthorities(user.roles!!))
        //return User("foo","foo", arrayListOf())
    }

    private fun getAuthorities(
            roles: Collection<Role>): Collection<GrantedAuthority?>? {
        val authorities: MutableList<GrantedAuthority> = ArrayList()
        for (role in roles) {
            authorities.add(SimpleGrantedAuthority(role.name))
            authorities.addAll(role.privileges.stream()
                    .map { p -> SimpleGrantedAuthority(p.name) }
                    .collect(Collectors.toList())
            )
        }

        return authorities
        //return getGrantedAuthorities(getPrivileges(roles))
    }

    private fun getPrivileges(roles: Collection<Role>): List<String> {
        val privileges: MutableList<String> = ArrayList()
        val collection: MutableList<Privileges> = ArrayList()
        for (role in roles) {
            collection.addAll(role.privileges)
        }
        for (item in collection) {
            privileges.add(item.name!!)
        }
        return privileges
    }

    private fun getGrantedAuthorities(privileges: List<String>): List<GrantedAuthority>? {
        val authorities: MutableList<GrantedAuthority> = ArrayList()
        for (privilege in privileges) {
            authorities.add(SimpleGrantedAuthority(privilege))
        }
        return authorities
    }


    override fun findAll(pageable: Pageable?): Page<User>? {
        TODO("Not yet implemented")
    }

    override fun findAll(): MutableIterable<User>? {
        TODO("Not yet implemented")
    }

    override fun findById(id: Long): User {
        TODO("Not yet implemented")
    }

    override fun save(entity: User): User {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }

    override fun findByUsername(username: String): User {
        return userRepository.findByUsername(username)!!
    }
}