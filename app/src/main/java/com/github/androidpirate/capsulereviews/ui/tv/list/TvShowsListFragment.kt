package com.github.androidpirate.capsulereviews.ui.tv.list

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.androidpirate.capsulereviews.BuildConfig
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.network.response.tvShow.NetworkTvShow
import com.github.androidpirate.capsulereviews.data.network.response.tvShows.NetworkTvShowsListItem
import com.github.androidpirate.capsulereviews.ui.adapter.list.ListItemAdapter
import com.github.androidpirate.capsulereviews.ui.adapter.list.ItemClickListener
import com.github.androidpirate.capsulereviews.ui.dialog.TvNetworksDialogFragment
import com.github.androidpirate.capsulereviews.ui.dialog.TvShowGenresDialogFragment
import com.github.androidpirate.capsulereviews.util.internal.*
import com.github.androidpirate.capsulereviews.util.internal.FragmentType
import com.github.androidpirate.capsulereviews.util.internal.GenericSortType
import com.github.androidpirate.capsulereviews.viewmodel.TvShowsListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_tv_shows_list.*
import kotlinx.android.synthetic.main.fragment_tv_shows_list.container
import kotlinx.android.synthetic.main.fragment_tv_shows_list.loadingScreen
import kotlinx.android.synthetic.main.fragment_tv_shows_list.noConnectionScreen
import kotlinx.android.synthetic.main.fragment_tv_shows_list.rvPopular
import kotlinx.android.synthetic.main.fragment_tv_shows_list.rvTopRated
import kotlinx.android.synthetic.main.fragment_tv_shows_list.rvTrending
import kotlinx.android.synthetic.main.no_connection_screen.*
import kotlinx.android.synthetic.main.tv_showcase.scAddFavorite
import kotlinx.android.synthetic.main.tv_showcase.scInfo
import kotlinx.android.synthetic.main.tv_showcase.scPlay
import kotlinx.android.synthetic.main.tv_showcase.scPoster
import kotlinx.android.synthetic.main.tv_showcase.scTitle
import kotlinx.android.synthetic.main.tv_toolbar.*

@AndroidEntryPoint
class TvShowsListFragment :
    Fragment(),
    ItemClickListener,
    TvShowGenresDialogFragment.TvShowGenresDialogListener,
    TvNetworksDialogFragment.NetworksDialogListener {

    private val viewModel: TvShowsListViewModel by viewModels()
    private lateinit var popularShowsAdapter: ListItemAdapter<NetworkTvShowsListItem>
    private lateinit var topRatedShowsAdapter: ListItemAdapter<NetworkTvShowsListItem>
    private lateinit var trendingShowsAdapter: ListItemAdapter<NetworkTvShowsListItem>
    private lateinit var popularNetflixAdapter: ListItemAdapter<NetworkTvShowsListItem>
    private lateinit var popularHuluAdapter: ListItemAdapter<NetworkTvShowsListItem>
    private lateinit var popularDisneyPlusAdapter: ListItemAdapter<NetworkTvShowsListItem>
    private var showcaseVideoKey = Constants.EMPTY_VIDEO_KEY
    private var isShowcaseFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupAdapters()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_shows_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayLoadingScreen()
        viewModel.isOnline.observe(viewLifecycleOwner, Observer { isOnline ->
            if(isOnline) {
                setupViews()
                viewModel.popularTvShows.observe(viewLifecycleOwner, Observer(popularShowsAdapter::submitList))
                viewModel.topRatedTvShows.observe(viewLifecycleOwner, Observer(topRatedShowsAdapter::submitList))
                viewModel.trendingTvShows.observe(viewLifecycleOwner, Observer(trendingShowsAdapter::submitList))
                viewModel.popularShowsOnNetflix.observe(viewLifecycleOwner, Observer (popularNetflixAdapter::submitList))
                viewModel.popularShowsOnHulu.observe(viewLifecycleOwner, Observer(popularHuluAdapter::submitList))
                viewModel.popularShowsOnDisneyPlus.observe(viewLifecycleOwner, Observer(popularDisneyPlusAdapter::submitList))
                viewModel.showcaseTvShow.observe(viewLifecycleOwner, Observer { showcaseTvShow ->
                    if(showcaseTvShow != null) {
                        setShowCaseTvShow(showcaseTvShow)
                        viewModel.setIsShowcaseFavorite(showcaseTvShow.id)
                    }
                })
                viewModel.showcaseVideoKey.observe(viewLifecycleOwner, Observer {
                    if(it != null) {
                        showcaseVideoKey = it
                    }
                })
                viewModel.getIsShowcaseFavorite().observe(viewLifecycleOwner, Observer {
                    if(it != null) {
                        this.isShowcaseFavorite = it
                        setFavoriteButtonState()
                    }
                })
                displayContainerScreen()
            } else {
                displayNoConnectionScreen()
            }
        })
    }

    private fun displayLoadingScreen() {
        container.visibility = View.GONE
        noConnectionScreen.visibility = View.GONE
        loadingScreen.visibility = View.VISIBLE
    }

    private fun displayNoConnectionScreen() {
        container.visibility = View.GONE
        loadingScreen.visibility = View.GONE
        noConnectionScreen.visibility = View.VISIBLE
        setRefreshListener()
    }

    private fun displayContainerScreen() {
        loadingScreen.visibility = View.GONE
        noConnectionScreen.visibility = View.GONE
        container.visibility = View.VISIBLE
    }

    private fun setupAdapters() {
        popularShowsAdapter = ListItemAdapter(
            fragment = FragmentType.TV_LIST,
            clickListener = this,
            genericSort = GenericSortType.POPULAR,
            network = NetworkType.ALL,
            genre = GenreType.ALL
        )
        topRatedShowsAdapter = ListItemAdapter(
            fragment = FragmentType.TV_LIST,
            clickListener = this,
            genericSort = GenericSortType.TOP_RATED,
            network = NetworkType.ALL,
            genre = GenreType.ALL
        )
        trendingShowsAdapter = ListItemAdapter(
            fragment = FragmentType.TV_LIST,
            clickListener = this,
            genericSort = GenericSortType.TRENDING,
            network = NetworkType.ALL,
            genre = GenreType.ALL
        )
        popularNetflixAdapter = ListItemAdapter(
            fragment = FragmentType.TV_LIST,
            clickListener = this,
            genericSort = GenericSortType.POPULAR,
            network = NetworkType.NETFLIX,
            genre = GenreType.ALL
        )
        popularHuluAdapter = ListItemAdapter(
            fragment = FragmentType.TV_LIST,
            clickListener = this,
            genericSort = GenericSortType.POPULAR,
            network = NetworkType.HULU,
            genre = GenreType.ALL
        )
        popularDisneyPlusAdapter = ListItemAdapter(
            fragment = FragmentType.TV_LIST,
            clickListener = this,
            genericSort = GenericSortType.POPULAR,
            network = NetworkType.DISNEY_PLUS,
            genre = GenreType.ALL
        )
    }

    private fun setupViews() {
        rvPopular.adapter = popularShowsAdapter
        rvTopRated.adapter = topRatedShowsAdapter
        rvTrending.adapter = trendingShowsAdapter
        rvPopularOnNetflix.adapter = popularNetflixAdapter
        rvPopularOnHulu.adapter = popularHuluAdapter
        rvPopularOnDisneyPlus.adapter = popularDisneyPlusAdapter
        tvGenreSpinner.setOnClickListener {
            showTvGenresDialog()
        }
        tvNetworkSpinner.setOnClickListener {
            showTvNetworksDialog()
        }
    }

    private fun setShowCaseTvShow(showcaseTvShow: NetworkTvShow) {
        setShowcaseTvShowPoster(showcaseTvShow.posterPath)
        setShowCaseTvShowTitle(showcaseTvShow.name)
        setShowCaseTvShowClickListeners(showcaseTvShow)
    }

    private fun setShowcaseTvShowPoster(showcaseTvShowPosterPath: String) {
        Glide.with(this)
            .load(
                BuildConfig.MOVIE_DB_IMAGE_BASE_URL +
                        Constants.SHOWCASE_POSTER_WIDTH +
                        showcaseTvShowPosterPath)
            .placeholder(R.drawable.ic_image_placeholder_white)
            .into(scPoster)
    }

    private fun setShowCaseTvShowTitle(showcaseTvShowTitle: String) {
        scTitle.text = showcaseTvShowTitle
    }

    private fun setShowCaseTvShowClickListeners(showcaseTvShow: NetworkTvShow) {
        setFavoriteListener(showcaseTvShow)
        setPlayTrailerListener()
        setShowcaseDetailListener(showcaseTvShow)
    }

    private fun setFavoriteListener(showcaseTvShow: NetworkTvShow) {
        scAddFavorite.setOnClickListener {
            if(!isShowcaseFavorite) {
                viewModel.insertShowcaseMovieToFavorites(showcaseTvShow)
                setFavoriteButtonState()
            } else {
                viewModel.deleteShowcaseMovieFromFavorites(showcaseTvShow)
                setFavoriteButtonState()
            }
        }
    }

    private fun setPlayTrailerListener() {
        val trailerEndpoint = BuildConfig.YOUTUBE_BASE_URL + showcaseVideoKey
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

    private fun setShowcaseDetailListener(showcaseTvShow: NetworkTvShow) {
        scInfo.setOnClickListener {
            val action = TvShowsListFragmentDirections
                .actionTvShowsListToDetail(showcaseTvShow.id)
            findNavController().navigate(action)
        }
    }

    private fun setRefreshListener() {
        refresh.setOnClickListener {
            val action = TvShowsListFragmentDirections.actionTvShowsListPopToSelf()
            findNavController().navigate(action)
        }
    }

    private fun setFavoriteButtonState() {
        scAddFavorite.isActivated = isShowcaseFavorite
    }

    private fun showTvGenresDialog() {
        val genresDialog = TvShowGenresDialogFragment
            .newInstance(this, Constants.DEFAULT_SELECTED_POSITION)
        genresDialog.show(requireActivity().supportFragmentManager, Constants.TV_SHOWS_LIST_FRAG_TAG)
    }

    private fun showTvNetworksDialog() {
        val networksDialog = TvNetworksDialogFragment.newInstance(this, Constants.DEFAULT_SELECTED_POSITION)
        networksDialog.show(requireActivity().supportFragmentManager,   Constants.TV_SHOWS_LIST_FRAG_TAG)
    }

    private fun navigateToPagedTvShowsList(action: NavDirections) {
        findNavController().navigate(action)
    }

    private fun navigateToDetails(action: NavDirections) {
        findNavController().navigate(action)
    }

    override fun <T> onItemClick(
        item: T,
        isLast: Boolean,
        genericSort: GenericSortType,
        network: NetworkType,
        genre: GenreType) {
            if(isLast) {
                if(network != NetworkType.ALL) {
                    when(network) {
                        NetworkType.NETFLIX -> {
                            val action = TvShowsListFragmentDirections
                                .actionTvShowsListToPagedTvShows(GenericSortType.POPULAR, NetworkType.NETFLIX, GenreType.ALL)
                            navigateToPagedTvShowsList(action)
                        }
                        NetworkType.HULU -> {
                            val action = TvShowsListFragmentDirections
                                .actionTvShowsListToPagedTvShows(GenericSortType.POPULAR, NetworkType.HULU,  GenreType.ALL)
                            navigateToPagedTvShowsList(action)
                        }
                        NetworkType.DISNEY_PLUS -> {
                            val action = TvShowsListFragmentDirections
                                .actionTvShowsListToPagedTvShows(GenericSortType.POPULAR, NetworkType.DISNEY_PLUS, GenreType.ALL)
                            navigateToPagedTvShowsList(action)
                        }
                    }
                } else {
                    when(genericSort) {
                        GenericSortType.POPULAR -> {
                            val action = TvShowsListFragmentDirections
                                .actionTvShowsListToPagedTvShows(GenericSortType.POPULAR,
                                    NetworkType.ALL, GenreType.ALL)
                            navigateToPagedTvShowsList(action)
                        }
                        GenericSortType.TOP_RATED -> {
                            val action = TvShowsListFragmentDirections
                                .actionTvShowsListToPagedTvShows(GenericSortType.TOP_RATED,
                                    NetworkType.ALL, GenreType.ALL)
                            navigateToPagedTvShowsList(action)
                        }
                        GenericSortType.TRENDING -> {
                            val action = TvShowsListFragmentDirections
                                .actionTvShowsListToPagedTvShows(GenericSortType.TRENDING,
                                    NetworkType.ALL, GenreType.ALL)
                            navigateToPagedTvShowsList(action)
                        }
                    }
                }
            } else {
                val action = TvShowsListFragmentDirections
                    .actionTvShowsListToDetail((item as NetworkTvShowsListItem).id, FragmentType.TV_LIST)
                navigateToDetails(action)
            }
    }

    override fun onGenreSelected(genre: GenreType) {
        val action = TvShowsListFragmentDirections
            .actionTvShowsListToPagedTvShows(
                GenericSortType.POPULAR,
                NetworkType.ALL,
                genre)
        navigateToPagedTvShowsList(action)
    }

    override fun onNetworkSelected(network: NetworkType) {
        val action = TvShowsListFragmentDirections
            .actionTvShowsListToPagedTvShows(
                GenericSortType.POPULAR,
                network,
                GenreType.ALL
            )
        navigateToPagedTvShowsList(action)
    }
}