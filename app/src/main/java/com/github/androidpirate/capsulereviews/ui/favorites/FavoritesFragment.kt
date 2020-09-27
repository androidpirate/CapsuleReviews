package com.github.androidpirate.capsulereviews.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.ui.adapter.pager.FavoritePagerAdapter
import com.github.androidpirate.capsulereviews.util.internal.Constants
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_favorites.*

class FavoritesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewpager.adapter = FavoritePagerAdapter(requireActivity())
        TabLayoutMediator(tabLayout, viewpager) {
                tab,
                position ->
                    tab.text = tabTitles[position]
        }.attach()
    }

    companion object {
        private val tabTitles = arrayOf(Constants.MOVIE_TAB_TITLE, Constants.TV_SHOWS_TAB_TITLE)
    }
}