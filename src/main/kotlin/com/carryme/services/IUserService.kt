package com.carryme.services

import com.carryme.entities.User

interface IUserService: IBaseServices<User,Long> {
    fun findByUsername(username: String): User
}