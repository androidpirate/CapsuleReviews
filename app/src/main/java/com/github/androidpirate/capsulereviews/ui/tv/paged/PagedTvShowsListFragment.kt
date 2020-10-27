package com.github.androidpirate.capsulereviews.ui.tv.paged

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.network.response.tvShows.NetworkTvShowsListItem
import com.github.androidpirate.capsulereviews.ui.adapter.paged.PagedItemAdapter
import com.github.androidpirate.capsulereviews.ui.adapter.paged.PagedItemClickListener
import com.github.androidpirate.capsulereviews.ui.dialog.TvNetworksDialogFragment
import com.github.androidpirate.capsulereviews.ui.dialog.TvShowGenresDialogFragment
import com.github.androidpirate.capsulereviews.util.GridSpacingItemDecoration
import com.github.androidpirate.capsulereviews.util.internal.*
import com.github.androidpirate.capsulereviews.util.internal.GenericSortType.*
import com.github.androidpirate.capsulereviews.util.internal.NetworkType
import com.github.androidpirate.capsulereviews.viewmodel.PagedTvShowsListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_paged_movies_list.container
import kotlinx.android.synthetic.main.fragment_paged_movies_list.loadingScreen
import kotlinx.android.synthetic.main.fragment_paged_tv_shows_list.*
import kotlinx.android.synthetic.main.paged_tv_shows_toolbar.*

@AndroidEntryPoint
class PagedTvShowsListFragment :
    Fragment(),
    PagedItemClickListener,
    TvShowGenresDialogFragment.TvShowGenresDialogListener,
    TvNetworksDialogFragment.NetworksDialogListener {

    private val args: PagedTvShowsListFragmentArgs by navArgs()
    private val viewModel: PagedTvShowsListViewModel by viewModels()
    private lateinit var adapter: PagedItemAdapter<NetworkTvShowsListItem>
    private lateinit var genericSort: GenericSortType
    private lateinit var genre: GenreType
    private lateinit var network: NetworkType
    private var tvShowsByGenericSort = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        genericSort = args.genericSortType
        viewModel.setGenericSort(genericSort)
        network = args.network
        viewModel.setNetwork(network)
        genre = args.genre
        viewModel.setGenre(genre)
        adapter = PagedItemAdapter(fragment = FragmentType.PAGED_TV_LIST, clickListener = this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_paged_tv_shows_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayLoadingScreen()
        btUp.setOnClickListener {
            findNavController().navigate(R.id.action_paged_tv_shows_list_to_list)
        }
        when {
            network != NetworkType.ALL -> {
                tvShowsByGenericSort = false
                getTvShowsByNetwork()
            }
            genre != GenreType.ALL -> {
                tvShowsByGenericSort = false
                getTvShowsByGenre()
            }
            else -> {
                getTvShowsByGenericSort()
            }
        }
        displayContainerScreen()
        setupViews()
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

    private fun setupViews() {
        setupToolbar()
        setRecyclerView()
    }

    private fun setupToolbar() {
        when {
            tvShowsByGenericSort -> {
                displayToolbarTitle()
            } else -> {
                displaySpinners()
            }
        }
    }

    private fun displaySpinners() {
        tvNetworkSpinner.visibility = View.VISIBLE
        tvGenreSpinner.visibility = View.VISIBLE
        tvGenreSpinner.text = Constants.getTvGenresKey(genre)
        tvNetworkSpinner.text = Constants.getTvNetworksKey(network)
        tvNetworkSpinner.setOnClickListener {
            showTvNetworksDialog()
        }
        tvGenreSpinner.setOnClickListener {
            showTvGenresDialog()
        }
    }

    private fun displayToolbarTitle() {
        tvNetworkSpinner.visibility = View.GONE
        tvGenreSpinner.visibility = View.GONE
        setToolbarTitle()
    }

    private fun setToolbarTitle() {
        when(genericSort) {
            POPULAR -> tvToolbarTitle.text = Constants.TV_POPULAR_TITLE
            TOP_RATED -> tvToolbarTitle.text = Constants.TV_TOP_RATED_TITLE
            TRENDING -> tvToolbarTitle.text = Constants.TV_TRENDING_TITLE
            else -> Constants.TV_POPULAR_TITLE
        }
    }

    private fun setRecyclerView() {
        rvPagedTvShows.adapter = adapter
    }

    private fun setFlagDecorationOn() {
        if(!viewModel.getFlagDecoration()) {
            rvPagedTvShows.addItemDecoration(
                GridSpacingItemDecoration(
                    resources.getInteger(R.integer.grid_span_count),
                    resources.getDimensionPixelSize(R.dimen.list_item_spacing),
                    true))
        }
        viewModel.setFlagDecorationOn()
    }

    private fun getTvShowsByNetwork() {
        viewModel.tvShowsByNetwork.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                adapter.submitList(it)
            }
        })
    }

    private fun getTvShowsByGenre() {
        viewModel.tvShowsByGenre.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                adapter.submitList(it)
            }
        })
    }

    private fun getTvShowsByGenericSort() {
        viewModel.tvShowsByGenericSortType.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                adapter.submitList(it)
            }
        })
    }

    private fun showTvGenresDialog() {
        val genrePosition = Constants.getTvGenrePosition(genre)
        val genresDialog = TvShowGenresDialogFragment.newInstance(this, genrePosition)
        genresDialog.show(requireActivity().supportFragmentManager, Constants.PAGED_TV_SHOWS_LIST_FRAG_TAG)
    }

    private fun showTvNetworksDialog() {
        val networkPosition = Constants.getTvNetworkPosition(network)
        val networksDialog = TvNetworksDialogFragment.newInstance(this, networkPosition)
        networksDialog.show(requireActivity().supportFragmentManager,   Constants.PAGED_TV_SHOWS_LIST_FRAG_TAG)
    }

    override fun <T> onPagedItemClick(item: T) {
        viewModel.setFlagDecorationOff()
        val action = PagedTvShowsListFragmentDirections
            .actionPagedTvShowsListToDetail((item as NetworkTvShowsListItem).id)
        findNavController().navigate(action)
    }

    override fun onGenreSelected(genre: GenreType) {
        viewModel.setGenre(genre)
        network = viewModel.getNetwork()
        val action = PagedTvShowsListFragmentDirections
            .actionPagedTvShowsListToSelf(
                genericSort,
                network,
                genre)
        findNavController().navigate(action)
    }

    override fun onNetworkSelected(network: NetworkType) {
        viewModel.setNetwork(network)
        genre = viewModel.getGenre()
        val action = PagedTvShowsListFragmentDirections
            .actionPagedTvShowsListToSelf(
                genericSort,
                network,
                genre)
        findNavController().navigate(action)
    }
}