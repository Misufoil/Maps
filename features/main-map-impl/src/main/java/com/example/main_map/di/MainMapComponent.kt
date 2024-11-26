package com.example.main_map.di

import com.example.core_utils.MainMap
import com.example.main_map_api.MainMapComponentCreator
import com.example.main_map_api.RegistrationFragment
import dagger.Binds
import dagger.Component
import dagger.Module

@[MainMap Component(modules = [MainMapModule::class])]
interface MainMapComponent : MainMapComponentCreator {

    @Component.Builder
    interface Builder {
        fun build(): MainMapComponent
    }
}

@Module
interface MainMapModule {

    @Binds
    fun bindRegFragmentImpl_to_RegFragment(registerFragmentProvider: RegistrationFragmentImpl)
            : RegistrationFragment
}

