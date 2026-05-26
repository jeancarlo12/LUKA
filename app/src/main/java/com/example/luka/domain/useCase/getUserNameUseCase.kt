package com.example.luka.domain.useCase

import com.example.luka.data.reposioRy.AuthRepositoryImpl

class getUserNameUseCase(private val repository: AuthRepositoryImpl) {
    operator fun invoke(onResult:(String)->Unit){
        repository.getUserName(onResult)
    }
}