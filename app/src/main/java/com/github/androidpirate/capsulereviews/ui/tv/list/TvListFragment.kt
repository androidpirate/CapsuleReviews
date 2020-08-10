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
import com.github.androidpirate.capsulereviews.ui.adapter.ListItemAdapter
import com.github.androidpirate.capsulereviews.util.ItemClickListener
import kotlinx.android.synthetic.main.fragment_tv_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TvListFragment : Fragment(), ItemClickListener{
    private lateinit var popularShowsAdapter: ListItemAdapter<TvShowsListItem>
    private lateinit var popularShows: List<TvShowsListItem>
    private lateinit var topRatedShowsAdapter: ListItemAdapter<TvShowsListItem>
    private lateinit var topRatedShows: List<TvShowsListItem>
    private lateinit var trendingShowsAdapter: ListItemAdapter<TvShowsListItem>
    private lateinit var trendingShows: List<TvShowsListItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupAdapters()
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

    private fun setupAdapters() {
        popularShowsAdapter = ListItemAdapter(TvListFragment::class.simpleName, this)
        topRatedShowsAdapter = ListItemAdapter(TvListFragment::class.simpleName, this)
        trendingShowsAdapter = ListItemAdapter(TvListFragment::class.simpleName, this)
    }

    override fun <T> onItemClick(item: T) {
        val action = TvListFragmentDirections.actionTvListToDetail((item as TvShowsListItem).id)
        findNavController().navigate(action)
    }
}