package com.example.maps

import com.example.core_utils.AppScope
import com.example.main_map.di.DaggerMainMapComponent
import com.example.main_map.di.MainMapComponent
import com.example.main_map_api.MainMapComponentCreator
import com.example.search_bar.di.DaggerSearchComponent
import com.example.search_bar.di.SearchComponent
import com.example.search_bar_api.SearchComponentCreator
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@[AppScope Component(modules = [AppModule::class])]
interface AppComponent {
    fun inject(mainActivity: MainActivity)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
    }
}

@Module
class AppModule {
    @Provides
    @AppScope
    fun provideMainMapComponentCreator(): MainMapComponentCreator {
        return DaggerMainMapComponent.builder().build()
    }

    @Provides
    @AppScope
    fun provideSearchComponentCreator(): SearchComponentCreator {
        return DaggerSearchComponent.builder().build()
    }
}




