package com.carryme.utils

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import com.carryme.entities.Privileges
import com.carryme.entities.Role
import com.carryme.entities.User
import com.carryme.repositories.PrivilegeRepository
import com.carryme.repositories.RoleRepository
import com.carryme.repositories.UserRepository
import java.util.*
import javax.transaction.Transactional


@Component
class SetupDataLoader : ApplicationListener<ContextRefreshedEvent> {
    var alreadySetup = true

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var  roleRepository: RoleRepository

    @Autowired
    private lateinit var  privilegeRepository: PrivilegeRepository

    @Autowired
    private lateinit var  passwordEncoder: PasswordEncoder

    @Transactional
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        if (alreadySetup) return
        val readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE")
        val writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE")
        val adminPrivileges: Collection<Privileges> = Arrays.asList(
                readPrivilege, writePrivilege)
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges)
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege))
        val adminRole = roleRepository.findByName("ROLE_ADMIN")!!
        val user = User()
        user.fullname = "Test"
        user.password = passwordEncoder.encode("test")
        user.username  = "test@test.com"
        user.roles = Arrays.asList(adminRole)
        user.active = true
        userRepository.save(user)
        alreadySetup = true
    }

    @Transactional
    fun createPrivilegeIfNotFound(name: String?): Privileges {
        var privilege: Privileges? = privilegeRepository.findByName(name!!)
        if (privilege == null) {
            privilege = Privileges().apply {
                this.name = name
            }
            privilegeRepository.save(privilege)
        }
        return privilege
    }

    @Transactional
    fun createRoleIfNotFound(
            name: String?, privileges: Collection<Privileges>): Role {
        var role: Role? = roleRepository.findByName(name!!)
        if (role == null) {
            role = Role().apply { this.name = name }
            role.privileges = privileges
            roleRepository.save(role)
        }
        return role
    }
}