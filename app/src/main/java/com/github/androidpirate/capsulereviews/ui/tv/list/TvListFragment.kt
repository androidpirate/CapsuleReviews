package com.github.androidpirate.capsulereviews.ui.tv.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.api.MovieDbService
import com.github.androidpirate.capsulereviews.data.response.tvShows.TvShowsListItem
import com.github.androidpirate.capsulereviews.util.ItemClickListener
import kotlinx.android.synthetic.main.fragment_tv_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TvListFragment : Fragment(), ItemClickListener{
    private lateinit var popularShowsAdapter: TvListAdapter
    private lateinit var topRatedShowsAdapter: TvListAdapter
    private lateinit var trendingShowsAdapter: TvListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        popularShowsAdapter = TvListAdapter(this)
        topRatedShowsAdapter = TvListAdapter(this)
        trendingShowsAdapter = TvListAdapter(this)
        val apiService = MovieDbService()
        GlobalScope.launch(Dispatchers.Main) {
            popularShowsAdapter.submitList(
                withContext(Dispatchers.IO) { apiService.getPopularTvShows().tvShowsListItems }
            )
            topRatedShowsAdapter.submitList(
                withContext(Dispatchers.IO) {apiService.getTopRatedTvShows().tvShowsListItems}
            )
            trendingShowsAdapter.submitList(
                withContext(Dispatchers.IO) {apiService.getTrendingTvShows().tvShowsListItems}
            )
        }
        rvPopular.adapter = popularShowsAdapter
        rvTopRated.adapter = topRatedShowsAdapter
        rvTrending.adapter = trendingShowsAdapter
    }

    override fun <T> onItemClick(item: T) {
        val action = TvListFragmentDirections.actionTvListToDetail((item as TvShowsListItem).id)
        findNavController().navigate(action)
    }
}