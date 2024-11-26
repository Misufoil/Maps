package com.example.search_bar.di

import com.example.core_utils.MainMap
import com.example.search_bar_api.RegistrationFragment
import com.example.search_bar_api.SearchComponentCreator
import dagger.Binds
import dagger.Component
import dagger.Module

@[MainMap Component(modules = [SearchModule::class])]
interface SearchComponent: SearchComponentCreator {
    @Component.Builder
    interface Builder {
        fun build(): SearchComponent
    }
}

@Module
interface SearchModule {
    @Binds
    fun bindRegFragmentImpl_to_RegFragment(registrationFragmentImpl: RegistrationFragmentImpl)
            : RegistrationFragment
}
