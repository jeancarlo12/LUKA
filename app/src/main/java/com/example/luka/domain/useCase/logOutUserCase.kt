package com.example.luka.domain.useCase

import com.example.luka.data.reposioRy.AuthRepositoryImpl


class logOutUserCase (private val repository: AuthRepositoryImpl) {
    operator fun invoke(){
        repository.logout()
    }

}