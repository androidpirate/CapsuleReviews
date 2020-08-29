package com.github.androidpirate.capsulereviews.ui.tv.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.androidpirate.capsulereviews.BuildConfig
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.network.response.genre.NetworkGenre
import com.github.androidpirate.capsulereviews.data.network.response.tvShow.NetworkCreatedBy
import com.github.androidpirate.capsulereviews.data.network.response.tvShow.NetworkNetworkInfo
import com.github.androidpirate.capsulereviews.data.network.response.tvShow.NetworkTvShow
import com.github.androidpirate.capsulereviews.data.network.response.tvShows.NetworkTvShowsListItem
import com.github.androidpirate.capsulereviews.ui.adapter.ListItemAdapter
import com.github.androidpirate.capsulereviews.util.GridSpacingItemDecoration
import com.github.androidpirate.capsulereviews.util.ItemClickListener
import com.github.androidpirate.capsulereviews.viewmodel.TvShowDetailViewModel
import com.github.androidpirate.capsulereviews.viewmodel.ViewModelFactory
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.detail_action_bar.*
import kotlinx.android.synthetic.main.detail_similar.*
import kotlinx.android.synthetic.main.fragment_tv_detail.container
import kotlinx.android.synthetic.main.fragment_tv_detail.loadingScreen
import kotlinx.android.synthetic.main.tv_header.*
import kotlinx.android.synthetic.main.tv_header.btUp
import kotlinx.android.synthetic.main.tv_info.*
import kotlinx.android.synthetic.main.tv_info.imdbLink
import kotlinx.android.synthetic.main.tv_info.overview
import kotlinx.android.synthetic.main.tv_summary.*

class TvDetailFragment : Fragment(), ItemClickListener {
    private val args: TvDetailFragmentArgs by navArgs()
    private lateinit var networkTvShow: NetworkTvShow
    private var videoKey: String = ""
    private lateinit var adapter: ListItemAdapter<NetworkTvShowsListItem>
    private lateinit var similarShows: List<NetworkTvShowsListItem>
    private var imdbId: String = ""
    private lateinit var viewModel: TvShowDetailViewModel
    private var flagDecoration = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ListItemAdapter(TvDetailFragment::class.simpleName, this)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        )
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
        displayLoadingScreen()
        btUp.setOnClickListener {
            findNavController().navigate(R.id.action_tv_detail_toList)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = ViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory).get(TvShowDetailViewModel::class.java)
        viewModel.getTvShowDetails(args.showId).observe(viewLifecycleOwner, Observer {
            if(it != null) {
                networkTvShow = it
                setTvShowPoster()
                setTvShowDetails()
                setIMDBLink()
            }
        })
        viewModel.getShowKey(args.showId).observe(viewLifecycleOwner, Observer {
            if(it != null) {
                videoKey = it
                setTrailerLink()
            }
        })
        viewModel.getSimilarTvShows(args.showId).observe(viewLifecycleOwner, Observer {
            if(it != null) {
                similarShows = it
                setSimilarShows()
            }
        })
        viewModel.getIMDBId(args.showId).observe(viewLifecycleOwner, Observer {
            if(it != null) {
                imdbId = it
                setIMDBLink()
            }
        })
        displayContainerScreen()
    }

    private fun displayLoadingScreen() {
        loadingScreen.visibility = View.VISIBLE
        container.visibility = View.GONE
    }

    private fun displayContainerScreen() {
        loadingScreen.visibility = View.GONE
        container.visibility = View.VISIBLE
    }

    private fun setTvShowPoster() {
        Glide.with(requireContext())
            .load(BuildConfig.MOVIE_DB_IMAGE_BASE_URL + "w185/" + networkTvShow.posterPath)
            .placeholder(R.drawable.ic_image_placeholder)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(100)))
            .into(tvHeaderBg)
        Glide.with(requireContext())
            .load(BuildConfig.MOVIE_DB_IMAGE_BASE_URL + "w185/" + networkTvShow.posterPath)
            .placeholder(R.drawable.ic_image_placeholder)
            .into(tvPoster)
    }

    private fun setTvShowDetails() {
        tvTitle.text = networkTvShow.name
        releaseDate.text = formatReleaseDate(networkTvShow.releaseDate)
        genres.text = formatGenres(networkTvShow.genres)
        userRating.text = networkTvShow.voteAverage.toString()
        status.text = networkTvShow.status
        createdBy.text = formatCreatedBy(networkTvShow.networkCreatedBy)
        overview.text = networkTvShow.overview
        seasons.text = networkTvShow.numberOfSeasons.toString()
        episodes.text = networkTvShow.numberOfEpisodes.toString()
        if(networkTvShow.episodeRunTime.isNotEmpty()) {
            runTime.text = formatRunTime(networkTvShow.episodeRunTime[0])
        }
        type.text = networkTvShow.type
        network.text = formatNetworks(networkTvShow.networkNetworkInfos)
    }

    private fun setIMDBLink() {
        val imdbEndpoint = BuildConfig.IMDB_BASE_URL + imdbId
        imdbLink.setOnClickListener {
            if(imdbEndpoint != BuildConfig.IMDB_BASE_URL) {
                val uri = Uri.parse(imdbEndpoint);
                val intent = Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.link_not_available_toast_content),
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setTrailerLink() {
        val trailerEndpoint = BuildConfig.YOUTUBE_BASE_URL + videoKey
        btPlay.setOnClickListener {
            if(videoKey != "") {
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

    private fun setSimilarShows() {
        if(similarShows.isEmpty()) {
            emptyListMessage.visibility = View.VISIBLE
            rvSimilar.visibility = View.INVISIBLE
        } else {
            adapter.submitList(similarShows)
            rvSimilar.layoutManager = GridLayoutManager(requireContext(), 3)
            if(!flagDecoration) {
                rvSimilar.addItemDecoration(GridSpacingItemDecoration(4, 30, true))
                setFlagDecorationOn()
            }
            rvSimilar.adapter = adapter
        }
    }

    private fun setFlagDecorationOn() {
        flagDecoration = true
    }

    private fun formatReleaseDate(releaseDate: String): String {
        val dateArray = releaseDate.split("-")
        val year = dateArray[0]
        val month = dateArray[1]
        val day = dateArray[2]
        return "$day/$month/$year"
    }

    private fun formatGenres(genres: List<NetworkGenre>): String {
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

    private fun formatCreatedBy(networkCreatedBy: List<NetworkCreatedBy>): String {
        var creators = ""
        for(i in networkCreatedBy.indices) {
            if(i == networkCreatedBy.size - 1) {
                creators += networkCreatedBy[i].name
                break
            }
            creators += "${networkCreatedBy[i].name}, "
        }
        return creators
    }

    private fun formatNetworks(networkNetworkInfos: List<NetworkNetworkInfo>): String {
        var network = ""
        for(i in networkNetworkInfos.indices) {
            if(i == networkNetworkInfos.size - 1) {
                network += networkNetworkInfos[i].name
                break
            }
            network += "${networkNetworkInfos[i].name}, "
        }
        return network
    }

    private fun formatRunTime(runTime: Int): String {
        return "$runTime mins"
    }

    override fun <T> onItemClick(item: T) {
        val action = TvDetailFragmentDirections
            .actionTvDetailFragmentSelf((item as NetworkTvShowsListItem).id)
        findNavController().navigate(action)
    }
}