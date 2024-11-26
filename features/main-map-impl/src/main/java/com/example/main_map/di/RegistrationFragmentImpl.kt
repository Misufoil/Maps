package com.example.main_map.di

import androidx.fragment.app.Fragment
import com.example.main_map.presentation.MainMapFragment
import com.example.main_map_api.RegistrationFragment
import javax.inject.Inject

class RegistrationFragmentImpl @Inject constructor(): RegistrationFragment {
    override fun provideFragment(): Fragment {
        return MainMapFragment.newInstance()
    }
}