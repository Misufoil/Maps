package com.example.search_bar.di

import androidx.fragment.app.Fragment
import com.example.search_bar.SearchFragment
import com.example.search_bar_api.RegistrationFragment
import javax.inject.Inject

class RegistrationFragmentImpl @Inject constructor(): RegistrationFragment {
    override fun provideFragment(): Fragment {
        return SearchFragment.newInstance()
    }
}