package com.example.maps

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.main_map.di.MainMapComponent
import com.example.main_map.presentation.MainMapFragment
import com.example.main_map_api.MainMapComponentCreator
import com.example.main_map_api.RegistrationFragment
import com.example.search_bar.di.SearchComponent
import com.example.search_bar_api.SearchComponentCreator
import dagger.Lazy
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var mainMapComponent: Lazy<MainMapComponentCreator>

    @Inject
    lateinit var searchComponent: Lazy<SearchComponentCreator>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        appComponent.inject(this)

        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        createFragment()
    }

     private fun createFragment() {
         val fragmentSearch = searchComponent.get().createInstance().provideFragment()
         val fragmentMainMap = mainMapComponent.get().createInstance().provideFragment()
         val transaction =  supportFragmentManager.beginTransaction()
         transaction.add(R.id.search_fragment_container, fragmentSearch)
         transaction.add(R.id.mainMap_fragment_container, fragmentMainMap)
         transaction.addToBackStack(null)
         transaction.commit()
    }
}

