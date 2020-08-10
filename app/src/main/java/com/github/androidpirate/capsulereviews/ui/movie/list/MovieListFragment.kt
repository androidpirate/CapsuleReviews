package com.github.androidpirate.capsulereviews.ui.movie.list

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.androidpirate.capsulereviews.BuildConfig
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.api.MovieDbService
import com.github.androidpirate.capsulereviews.data.response.movies.MoviesListItem
import com.github.androidpirate.capsulereviews.data.response.videos.VideosListItem
import com.github.androidpirate.capsulereviews.ui.adapter.ListItemAdapter
import com.github.androidpirate.capsulereviews.util.ItemClickListener
import kotlinx.android.synthetic.main.detail_action_bar.*
import kotlinx.android.synthetic.main.fragment_movie_list.*
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.android.synthetic.main.movie_showcase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class MovieListFragment : Fragment(), ItemClickListener {
    private lateinit var popularMoviesAdapter: ListItemAdapter<MoviesListItem>
    private lateinit var popularMovies: List<MoviesListItem>
    private lateinit var topRatedMoviesAdapter: ListItemAdapter<MoviesListItem>
    private lateinit var topRatedMovies: List<MoviesListItem>
    private lateinit var nowPlayingMoviesAdapter: ListItemAdapter<MoviesListItem>
    private lateinit var nowPlayingMovies: List<MoviesListItem>
    private lateinit var upcomingMoviesAdapter: ListItemAdapter<MoviesListItem>
    private lateinit var upcomingMovies: List<MoviesListItem>
    private lateinit var trendingMoviesAdapter: ListItemAdapter<MoviesListItem>
    private lateinit var trendingMovies: List<MoviesListItem>
    private lateinit var showCaseMovie: MoviesListItem
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
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val apiService = MovieDbService()
        GlobalScope.launch(Dispatchers.Main ) {
            withContext(Dispatchers.IO) {
                popularMovies = apiService.getPopularMovies().moviesListItems
            }
            withContext(Dispatchers.IO) {
                topRatedMovies = apiService.getTopRatedMovies().moviesListItems
            }
            withContext(Dispatchers.IO) {
                nowPlayingMovies = apiService.getNowPlayingMovies().moviesListItems
            }
            withContext(Dispatchers.IO) {
                upcomingMovies = apiService.getUpcomingMovies().moviesListItems
            }
            withContext(Dispatchers.IO) {
                trendingMovies = apiService.getTrendingMovies().moviesListItems
                val randomItemNo = Random.nextInt(0 , 19)
                showCaseMovie = trendingMovies[randomItemNo]
            }
            withContext(Dispatchers.IO) {
                showCaseVideos = apiService.getMovieVideos(showCaseMovie.id).videosListItems
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
        popularMoviesAdapter = ListItemAdapter(
            MovieListFragment::class.simpleName, this)
        topRatedMoviesAdapter = ListItemAdapter(
            MovieListFragment::class.simpleName, this)
        nowPlayingMoviesAdapter = ListItemAdapter(
            MovieListFragment::class.simpleName, this)
        upcomingMoviesAdapter = ListItemAdapter(
            MovieListFragment::class.simpleName, this)
        trendingMoviesAdapter = ListItemAdapter(
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
        popularMoviesAdapter.submitList(popularMovies)
        rvPopular.adapter = popularMoviesAdapter
        topRatedMoviesAdapter.submitList(topRatedMovies)
        rvTopRated.adapter = topRatedMoviesAdapter
        nowPlayingMoviesAdapter.submitList(nowPlayingMovies)
        rvNowPlaying.adapter = nowPlayingMoviesAdapter
        upcomingMoviesAdapter.submitList(upcomingMovies)
        rvUpcoming.adapter = upcomingMoviesAdapter
        trendingMoviesAdapter.submitList(trendingMovies)
        rvTrending.adapter = trendingMoviesAdapter
    }

    override fun <T> onItemClick(item: T) {
        val action = MovieListFragmentDirections
            .actionMovieListToDetail((item as MoviesListItem).id)
        findNavController().navigate(action)
    }
}