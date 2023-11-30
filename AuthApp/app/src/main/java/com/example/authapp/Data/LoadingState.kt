package com.example.authapp.Data

sealed class LoadingState{
    object Loading: LoadingState()
    object Success: LoadingState()
    object Failure: LoadingState()
}


