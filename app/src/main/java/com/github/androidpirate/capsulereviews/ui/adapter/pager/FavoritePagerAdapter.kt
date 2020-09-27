package com.github.androidpirate.capsulereviews.ui.adapter.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.androidpirate.capsulereviews.ui.favorites.FavoriteMoviesListFragment
import com.github.androidpirate.capsulereviews.ui.favorites.FavoriteTvShowsListFragment
import com.github.androidpirate.capsulereviews.util.internal.Constants

class FavoritePagerAdapter(
    activity: FragmentActivity): FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return Constants.PAGER_PAGE_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return if(position == 0) {
            FavoriteMoviesListFragment.newInstance()
        } else {
            FavoriteTvShowsListFragment.newInstance()
        }
    }
}