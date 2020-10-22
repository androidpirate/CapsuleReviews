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
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.androidpirate.capsulereviews.BuildConfig
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.network.response.tvShow.NetworkTvShow
import com.github.androidpirate.capsulereviews.data.network.response.tvShows.NetworkTvShowsListItem
import com.github.androidpirate.capsulereviews.util.ContentFormatter
import com.github.androidpirate.capsulereviews.util.GridSpacingItemDecoration
import com.github.androidpirate.capsulereviews.ui.adapter.similar.SimilarContentAdapter
import com.github.androidpirate.capsulereviews.ui.adapter.similar.SimilarContentClickListener
import com.github.androidpirate.capsulereviews.util.internal.Constants
import com.github.androidpirate.capsulereviews.util.internal.FragmentType
import com.github.androidpirate.capsulereviews.util.internal.FragmentType.*
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
import java.lang.IllegalArgumentException

class TvShowDetailFragment : Fragment(), SimilarContentClickListener {
    private val args: TvShowDetailFragmentArgs by navArgs()
    private lateinit var networkTvShow: NetworkTvShow
    private lateinit var adapter: SimilarContentAdapter<NetworkTvShowsListItem>
    private lateinit var similarShows: List<NetworkTvShowsListItem>
    private lateinit var viewModel: TvShowDetailViewModel
    private var videoKey: String = Constants.EMPTY_VIDEO_KEY
    private var imdbId: String = Constants.EMPTY_FIELD_STRING
    private var imdbEndpoint: String = Constants.EMPTY_FIELD_STRING
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = ViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory).get(TvShowDetailViewModel::class.java)
        adapter = SimilarContentAdapter(fragment = TV_DETAIL,clickListener = this)
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
        viewModel.getTvShowDetails(args.showId).observe(viewLifecycleOwner, Observer {
            networkTvShow = it
            setTvShowPoster()
            setTvShowDetails()
            setFavoriteButtonState()
            setIMDBLink()
        })
        viewModel.getIsFavorite().observe(viewLifecycleOwner, Observer {
            if(it != null) {
                isFavorite = it
                setFavoriteButtonState()
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
        viewModel.getImdbEndpoint().observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty() || it.isNotBlank()) {
                imdbEndpoint = it
            }
        })
        setupUpNavigation()
        setFavoriteListener()
        setShareListener()
        displayContainerScreen()

    }

    override fun onResume() {
        super.onResume()
        setFlagDecorationOn()
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
            .load(
                BuildConfig.MOVIE_DB_IMAGE_BASE_URL +
                        Constants.ADAPTER_POSTER_WIDTH +
                        networkTvShow.posterPath)
            .placeholder(R.drawable.ic_image_placeholder_white)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(100)))
            .into(tvHeaderBg)
        Glide.with(requireContext())
            .load(BuildConfig.MOVIE_DB_IMAGE_BASE_URL + Constants.ADAPTER_POSTER_WIDTH + networkTvShow.posterPath)
            .placeholder(R.drawable.ic_image_placeholder_white)
            .into(tvPoster)
    }

    private fun setTvShowDetails() {
        tvTitle.text = networkTvShow.name
        releaseDate.text = ContentFormatter.formatReleaseDate(networkTvShow.releaseDate)
        genres.text = ContentFormatter.formatGenres(networkTvShow.networkGenres)
        userRating.text = networkTvShow.voteAverage.toString()
        status.text = networkTvShow.status
        createdBy.text = ContentFormatter.formatCreatedBy(networkTvShow.createdBy)
        overview.text = networkTvShow.overview
        seasons.text = networkTvShow.numberOfSeasons.toString()
        episodes.text = networkTvShow.numberOfEpisodes.toString()
        if(networkTvShow.episodeRunTime.isNotEmpty()) {
            runTime.text = ContentFormatter.formatTvShowRunTime(networkTvShow.episodeRunTime[0])
        }
        type.text = networkTvShow.type
        network.text = ContentFormatter.formatNetworks(networkTvShow.networkNetworkInfos)
    }

    private fun setFavoriteButtonState() {
        btFavorite.isActivated = isFavorite
    }

    private fun setIMDBLink() {
        val imdbEndpoint = BuildConfig.IMDB_BASE_URL + imdbId
        viewModel.setImdbEndpoint(imdbEndpoint)
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

    private fun setupUpNavigation() {
        btUp.setOnClickListener {
            when(args.baseDestination) {
                FragmentType.TV_LIST, FragmentType.TV_DETAIL ->
                    findNavController().navigate(R.id.action_tv_detail_to_list)
                FragmentType.FAVORITE_TV_DETAIL ->
                    findNavController().navigate(R.id.action_tv_detail_to_favorites)
                FragmentType.SEARCH_RESULTS ->
                    findNavController().navigate(R.id.action_tv_detail_to_search)
                else ->
                    throw IllegalArgumentException(Constants.ILLEGAL_FRAGMENT_TYPE_EXCEPTION)
            }
        }
    }

    private fun setFavoriteListener() {
        btFavorite.setOnClickListener {
            if(!isFavorite) {
                viewModel.insertFavoriteTvShow(networkTvShow)
                setFavoriteButtonState()
            } else {
                viewModel.deleteFavoriteTvShow(networkTvShow)
                setFavoriteButtonState()
            }
        }
    }

    private fun setShareListener() {
        btShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_SUBJECT, Constants.INTENT_TV_SUBJECT)
                putExtra(Intent.EXTRA_TEXT, imdbEndpoint)
                type = Constants.INTENT_TYPE_TEXT
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    private fun setTrailerLink() {
        val trailerEndpoint = BuildConfig.YOUTUBE_BASE_URL + videoKey
        btPlay.setOnClickListener {
            if(videoKey != Constants.EMPTY_VIDEO_KEY) {
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
            rvSimilar.adapter = adapter
        }
    }

    private fun setFlagDecorationOn() {
        if(!viewModel.getFlagDecoration()) {
            rvSimilar.addItemDecoration(
                GridSpacingItemDecoration(
                    resources.getInteger(R.integer.grid_span_count),
                    resources.getDimensionPixelSize(R.dimen.list_item_spacing),
                    true))
        }
        viewModel.setFlagDecorationOn()
    }

    private fun navigateToSelf(action: NavDirections) {
        findNavController().navigate(action)
    }

    override fun <T> onItemClick(item: T) {
        viewModel.setFlagDecorationOff()
        val action = TvShowDetailFragmentDirections
            .actionTvDetailToSelf((item as NetworkTvShowsListItem).id, FragmentType.TV_DETAIL)
        navigateToSelf(action)
    }
}