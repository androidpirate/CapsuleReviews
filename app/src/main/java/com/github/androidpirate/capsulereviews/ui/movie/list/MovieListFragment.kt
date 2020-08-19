package com.github.androidpirate.capsulereviews.ui.movie.list

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.androidpirate.capsulereviews.BuildConfig
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.network.api.MovieDbService
import com.github.androidpirate.capsulereviews.data.network.response.movies.NetworkMoviesListItem
import com.github.androidpirate.capsulereviews.data.network.response.videos.NetworkVideosListItem
import com.github.androidpirate.capsulereviews.ui.adapter.ListItemAdapter
import com.github.androidpirate.capsulereviews.util.ItemClickListener
import kotlinx.android.synthetic.main.fragment_movie_list.*
import kotlinx.android.synthetic.main.movie_showcase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class MovieListFragment : Fragment(), ItemClickListener {
    private lateinit var popularNetworkMoviesAdapter: ListItemAdapter<NetworkMoviesListItem>
    private lateinit var popularMovies: List<NetworkMoviesListItem>
    private lateinit var topRatedNetworkMoviesAdapter: ListItemAdapter<NetworkMoviesListItem>
    private lateinit var topRatedMovies: List<NetworkMoviesListItem>
    private lateinit var nowPlayingNetworkMoviesAdapter: ListItemAdapter<NetworkMoviesListItem>
    private lateinit var nowPlayingMovies: List<NetworkMoviesListItem>
    private lateinit var upcomingNetworkMoviesAdapter: ListItemAdapter<NetworkMoviesListItem>
    private lateinit var upcomingMovies: List<NetworkMoviesListItem>
    private lateinit var trendingNetworkMoviesAdapter: ListItemAdapter<NetworkMoviesListItem>
    private lateinit var trendingMovies: List<NetworkMoviesListItem>
    private lateinit var showCaseMovie: NetworkMoviesListItem
    private lateinit var showCaseVideos: List<NetworkVideosListItem>
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
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val apiService = MovieDbService()
        GlobalScope.launch(Dispatchers.Main ) {
            container.visibility = View.GONE
            loadingScreen.visibility = View.VISIBLE

            withContext(Dispatchers.IO) {
                popularMovies = apiService.getPopularMovies().networkMoviesListItems
            }
            withContext(Dispatchers.IO) {
                topRatedMovies = apiService.getTopRatedMovies().networkMoviesListItems
            }
            withContext(Dispatchers.IO) {
                nowPlayingMovies = apiService.getNowPlayingMovies().networkMoviesListItems
            }
            withContext(Dispatchers.IO) {
                upcomingMovies = apiService.getUpcomingMovies().networkMoviesListItems
            }
            withContext(Dispatchers.IO) {
                trendingMovies = apiService.getTrendingMovies().networkMoviesListItems
                val randomItemNo = Random.nextInt(0 , 19)
                showCaseMovie = trendingMovies[randomItemNo]
            }
            withContext(Dispatchers.IO) {
                showCaseVideos = apiService.getMovieVideos(showCaseMovie.id).networkVideosListItems
                for(video in showCaseVideos) {
                    if(video.site == "YouTube" && video.type == "Trailer") {
                        videoKey = video.key
                        break
                    }
                }
            }
            setShowCaseMovie()
            setupViews()
        }
    }

    private fun setupAdapters() {
        popularNetworkMoviesAdapter = ListItemAdapter(
            MovieListFragment::class.simpleName, this)
        topRatedNetworkMoviesAdapter = ListItemAdapter(
            MovieListFragment::class.simpleName, this)
        nowPlayingNetworkMoviesAdapter = ListItemAdapter(
            MovieListFragment::class.simpleName, this)
        upcomingNetworkMoviesAdapter = ListItemAdapter(
            MovieListFragment::class.simpleName, this)
        trendingNetworkMoviesAdapter = ListItemAdapter(
            MovieListFragment::class.simpleName, this)
    }

    private fun setShowCaseMovie() {
        Glide.with(requireContext())
            .load(BuildConfig.MOVIE_DB_IMAGE_BASE_URL + "w342/" + showCaseMovie.posterPath)
            .placeholder(R.drawable.ic_image_placeholder)
            .into(scPoster)
        scTitle.text = showCaseMovie.title

        scAddFavorite.setOnClickListener {
            // TODO 7: Implement adding to favorites here
        }

        scInfo.setOnClickListener {
            onItemClick(showCaseMovie)
        }

        for(video in showCaseVideos) {
            val trailerEndpoint = BuildConfig.YOUTUBE_BASE_URL + videoKey
            scPlay.setOnClickListener {
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
    }

    private fun setupViews() {
        popularNetworkMoviesAdapter.submitList(popularMovies)
        rvPopular.adapter = popularNetworkMoviesAdapter
        topRatedNetworkMoviesAdapter.submitList(topRatedMovies)
        rvTopRated.adapter = topRatedNetworkMoviesAdapter
        nowPlayingNetworkMoviesAdapter.submitList(nowPlayingMovies)
        rvNowPlaying.adapter = nowPlayingNetworkMoviesAdapter
        upcomingNetworkMoviesAdapter.submitList(upcomingMovies)
        rvUpcoming.adapter = upcomingNetworkMoviesAdapter
        trendingNetworkMoviesAdapter.submitList(trendingMovies)
        rvTrending.adapter = trendingNetworkMoviesAdapter
        loadingScreen.visibility = View.GONE
        container.visibility = View.VISIBLE
    }

    override fun <T> onItemClick(item: T) {
        val action = MovieListFragmentDirections
            .actionMovieListToDetail((item as NetworkMoviesListItem).id)
        findNavController().navigate(action)
    }
}