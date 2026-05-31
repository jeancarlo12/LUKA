package com.example.luka.domain.useCase

import com.example.luka.domain.repository.AuthRepository

class getUserNameUseCase(private val repository: AuthRepository) {
    operator fun invoke(onResult:(String)->Unit){
        repository.getUserName(onResult)
    }
}