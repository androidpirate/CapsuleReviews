package com.github.androidpirate.capsulereviews.ui.tv.list

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.androidpirate.capsulereviews.BuildConfig
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.api.MovieDbService
import com.github.androidpirate.capsulereviews.data.response.tvShows.TvShowsListItem
import com.github.androidpirate.capsulereviews.data.response.videos.VideosListItem
import com.github.androidpirate.capsulereviews.ui.adapter.ListItemAdapter
import com.github.androidpirate.capsulereviews.util.ItemClickListener
import kotlinx.android.synthetic.main.fragment_tv_list.*
import kotlinx.android.synthetic.main.tv_showcase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class TvListFragment : Fragment(), ItemClickListener{
    private lateinit var popularShowsAdapter: ListItemAdapter<TvShowsListItem>
    private lateinit var popularShows: List<TvShowsListItem>
    private lateinit var topRatedShowsAdapter: ListItemAdapter<TvShowsListItem>
    private lateinit var topRatedShows: List<TvShowsListItem>
    private lateinit var trendingShowsAdapter: ListItemAdapter<TvShowsListItem>
    private lateinit var trendingShows: List<TvShowsListItem>
    private lateinit var showCaseTvShow: TvShowsListItem
    private lateinit var showCaseVideos: List<VideosListItem>
    private var videoKey: String ?= null

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val apiService = MovieDbService()
        GlobalScope.launch(Dispatchers.Main) {
            container.visibility = View.GONE
            loadingScreen.visibility = View.VISIBLE

            withContext(Dispatchers.IO) {
                popularShows = apiService.getPopularTvShows().tvShowsListItems
            }
            withContext(Dispatchers.IO) {
                topRatedShows = apiService.getTopRatedTvShows().tvShowsListItems
            }
            withContext(Dispatchers.IO) {
               trendingShows = apiService.getTrendingTvShows().tvShowsListItems
                val randomItemNo = Random.nextInt(0 , 19)
                showCaseTvShow = trendingShows[randomItemNo]
            }
            withContext(Dispatchers.IO) {
                showCaseVideos = apiService.getTvShowVideos(showCaseTvShow.id).videosListItems
                for(video in showCaseVideos) {
                    if(video.site == "YouTube" && video.type == "Trailer") {
                        videoKey = video.key
                        break
                    }
                }
            }
            setShowCaseTvShow()
            setupViews()
        }
    }

    private fun setupAdapters() {
        popularShowsAdapter = ListItemAdapter(TvListFragment::class.simpleName, this)
        topRatedShowsAdapter = ListItemAdapter(TvListFragment::class.simpleName, this)
        trendingShowsAdapter = ListItemAdapter(TvListFragment::class.simpleName, this)
    }

    private fun setShowCaseTvShow() {
        Glide.with(this)
            .load(BuildConfig.MOVIE_DB_IMAGE_BASE_URL + "w342/" + showCaseTvShow.posterPath)
            .placeholder(R.drawable.ic_image_placeholder)
            .into(scPoster)
        scTitle.text = showCaseTvShow.name

        scAddFavorite.setOnClickListener {
            // TODO 7: Implement adding to favorites here
        }

        scInfo.setOnClickListener {
            onItemClick(showCaseTvShow)
        }

        scPlay.setOnClickListener {
            val trailerEndpoint = BuildConfig.YOUTUBE_BASE_URL + videoKey
            if(trailerEndpoint != BuildConfig.YOUTUBE_BASE_URL) {
                val uri = Uri.parse(trailerEndpoint)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.video_no_available_toast_content),
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setupViews() {
        popularShowsAdapter.submitList(popularShows)
        topRatedShowsAdapter.submitList(topRatedShows)
        trendingShowsAdapter.submitList(trendingShows)
        rvPopular.adapter = popularShowsAdapter
        rvTopRated.adapter = topRatedShowsAdapter
        rvTrending.adapter = trendingShowsAdapter
        loadingScreen.visibility = View.GONE
        container.visibility = View.VISIBLE
    }

    override fun <T> onItemClick(item: T) {
        val action = TvListFragmentDirections.actionTvListToDetail((item as TvShowsListItem).id)
        findNavController().navigate(action)
    }
}