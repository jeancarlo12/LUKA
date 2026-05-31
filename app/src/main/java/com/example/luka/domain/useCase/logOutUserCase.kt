package com.example.luka.domain.useCase

import com.example.luka.domain.repository.AuthRepository

class logOutUserCase (private val repository: AuthRepository) {
    operator fun invoke(){
        repository.logout()
    }
}