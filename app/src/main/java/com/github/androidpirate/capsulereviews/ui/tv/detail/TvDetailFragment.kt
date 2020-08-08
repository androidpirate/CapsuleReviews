package com.github.androidpirate.capsulereviews.ui.tv.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.api.MovieDbService
import com.github.androidpirate.capsulereviews.data.response.tvShow.CreatedBy
import com.github.androidpirate.capsulereviews.data.response.tvShow.Genre
import com.github.androidpirate.capsulereviews.data.response.tvShow.Network

import com.github.androidpirate.capsulereviews.data.response.tvShow.TvShowResponse
import com.github.androidpirate.capsulereviews.data.response.tvShows.TvShowsListItem
import com.github.androidpirate.capsulereviews.ui.movie.detail.MovieDetailFragmentArgs
import com.github.androidpirate.capsulereviews.ui.tv.list.TvListAdapter
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.movie_header.*
import kotlinx.android.synthetic.main.tv_header.*
import kotlinx.android.synthetic.main.tv_header.btUp
import kotlinx.android.synthetic.main.tv_info.*
import kotlinx.android.synthetic.main.tv_summary.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TvDetailFragment : Fragment() {
    private val args: TvDetailFragmentArgs by navArgs()
    private lateinit var tvShow: TvShowResponse
    private lateinit var adapter: TvListAdapter
    private lateinit var similarTvShow: List<TvShowsListItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btUp.setOnClickListener {
            findNavController().navigate(R.id.action_tv_detail_toList)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val apiService = MovieDbService()
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                tvShow = apiService.getTvShowDetails(args.showId)
            }
            withContext(Dispatchers.IO) {
                similarTvShow = apiService.getSimilarTvShows(args.showId).tvShowsListItems
            }
            setupViews()
        }
    }

    private fun setupViews() {
        Glide.with(requireContext())
            .load("https://image.tmdb.org/t/p/w185/" + tvShow.posterPath)
            .into(tvPoster)
        Glide.with(requireContext())
            .load("https://image.tmdb.org/t/p/w185/" + tvShow.posterPath)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(100)))
            .into(tvHeaderBg)
        tvTitle.text = tvShow.name
        // TODO 6: Content rating requires a ReleasesResponse
        releaseDate.text = formatReleaseDate(tvShow.releaseDate)
        genres.text = formatGenres(tvShow.genres)
        status.text = tvShow.status
        createdBy.text = formatCreatedBy(tvShow.createdBy)
        overview.text = tvShow.overview
        seasons.text = tvShow.numberOfSeasons.toString()
        episodes.text = tvShow.numberOfEpisodes.toString()
        runTime.text = formatRunTime(tvShow.episodeRunTime[0])
        type.text = tvShow.type
        network.text = formatNetworks(tvShow.networks)
    }

    private fun formatReleaseDate(releaseDate: String): String {
        val dateArray = releaseDate.split("-")
        val year = dateArray[0]
        val month = dateArray[1]
        val day = dateArray[2]
        return "$day/$month/$year"
    }

    private fun formatGenres(genres: List<Genre>): String {
        var movieGenres: String = ""
        for(i in genres.indices) {
            if(i == genres.size - 1) {
                movieGenres += genres[i].name
                break
            }
            movieGenres += "${genres[i].name}, "
        }
        return movieGenres
    }

    private fun formatCreatedBy(createdBy: List<CreatedBy>): String {
        var creators = ""
        for(i in createdBy.indices) {
            if(i == createdBy.size - 1) {
                creators += createdBy[i].name
                break
            }
            creators += "${createdBy[i].name}, "
        }
        return creators
    }

    private fun formatNetworks(networks: List<Network>): String {
        var network = ""
        for(i in networks.indices) {
            if(i == networks.size - 1) {
                network += networks[i].name
                break
            }
            network += "${networks[i].name}, "
        }
        return network
    }

    private fun formatRunTime(runTime: Int): String {
        return "$runTime mins"
    }
}