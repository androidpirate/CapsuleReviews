package com.github.androidpirate.capsulereviews.ui.tv.paged

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.network.response.tvShows.NetworkTvShowsListItem
import com.github.androidpirate.capsulereviews.ui.adapter.paged.PagedItemAdapter
import com.github.androidpirate.capsulereviews.ui.adapter.paged.PagedItemClickListener
import com.github.androidpirate.capsulereviews.ui.dialog.TvShowGenresDialogFragment
import com.github.androidpirate.capsulereviews.util.GridSpacingItemDecoration
import com.github.androidpirate.capsulereviews.util.internal.*
import com.github.androidpirate.capsulereviews.util.internal.GenericSortType.*
import com.github.androidpirate.capsulereviews.util.internal.NetworkType.*
import com.github.androidpirate.capsulereviews.viewmodel.PagedTvShowsListViewModel
import com.github.androidpirate.capsulereviews.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_paged_movies_list.container
import kotlinx.android.synthetic.main.fragment_paged_movies_list.loadingScreen
import kotlinx.android.synthetic.main.fragment_paged_tv_shows_list.*
import kotlinx.android.synthetic.main.paged_tv_shows_toolbar.*

class PagedTvShowsListFragment :
    Fragment(),
    PagedItemClickListener,
    TvShowGenresDialogFragment.TvShowGenresDialogListener{

    private val args: PagedTvShowsListFragmentArgs by navArgs()
    private lateinit var viewModel: PagedTvShowsListViewModel
    private lateinit var adapter: PagedItemAdapter<NetworkTvShowsListItem>
    private lateinit var genericSort: GenericSortType
    private lateinit var genre: GenreType
    private lateinit var network: NetworkType
    private var flagDecoration = false
    private var tvShowsByNetwork = false
    private var tvShowsByGenre = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = ViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory).get(PagedTvShowsListViewModel::class.java)
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
                tvShowsByNetwork = true
                getTvShowsByNetwork()
            }
            genre != ALL -> {
                tvShowsByGenre = true
                getTvShowsByGenre()
            }
            else -> {
                getTvShowsByGenericSort()
            }
        }
        displayContainerScreen()
        setupViews()
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
        if(tvShowsByNetwork) {
            displaySpinners()
        }
        if(tvShowsByGenre) {
            displaySpinners()
            tvGenreSpinner.text = Constants.getTvGenresKey(args.genre)
            // TODO: Add a function in Constants to handle networkKey
            tvNetworkSpinner.text = args.network.toString()
            tvNetworkSpinner.setOnClickListener {
                // TODO: Not Implemented Yet
            }
            tvGenreSpinner.setOnClickListener {
                showTvGenresDialog()
            }
        } else {
            displayToolbarTitle()
        }
    }

    private fun displaySpinners() {
        tvToolbarTitle.visibility = View.GONE
        tvNetworkSpinner.visibility = View.VISIBLE
        tvGenreSpinner.visibility = View.VISIBLE
    }

    private fun displayToolbarTitle() {
        tvToolbarTitle.visibility = View.VISIBLE
        tvNetworkSpinner.visibility = View.GONE
        tvGenreSpinner.visibility = View.GONE
        setToolbarTitle()
    }

    private fun setToolbarTitle() {
        if(network != NetworkType.ALL) {
            when(network) {
                NETFLIX -> tvToolbarTitle.text = Constants.TV_TRENDING_NETFLIX_TITLE
                HULU -> tvToolbarTitle.text = Constants.TV_TRENDING_HULU_TITLE
                DISNEY_PLUS -> tvToolbarTitle.text = Constants.TV_TRENDING_DISNEY_TITLE
            }
        } else {
            when(genericSort) {
                POPULAR -> tvToolbarTitle.text = Constants.TV_POPULAR_TITLE
                TOP_RATED -> tvToolbarTitle.text = Constants.TV_TOP_RATED_TITLE
                TRENDING -> tvToolbarTitle.text = Constants.TV_TRENDING_TITLE
            }
        }
    }

    private fun setRecyclerView() {
        if(!flagDecoration) {
            rvPagedTvShows.addItemDecoration(GridSpacingItemDecoration(4, 30, true))
            setFlagDecorationOn()
        }
        rvPagedTvShows.adapter = adapter
    }

    private fun setFlagDecorationOn() {
        flagDecoration = true
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
        val genrePosition = Constants.getTvGenrePosition(args.genre)
        val genresDialog = TvShowGenresDialogFragment.newInstance(this, genrePosition)
        genresDialog.show(requireActivity().supportFragmentManager, Constants.PAGED_TV_SHOWS_LIST_FRAG_TAG)
    }

    override fun <T> onPagedItemClick(item: T) {
        val action = PagedTvShowsListFragmentDirections
            .actionPagedTvShowsListToDetail((item as NetworkTvShowsListItem).id)
        findNavController().navigate(action)
    }

    override fun onGenreSelected(genre: GenreType) {
        val action = PagedTvShowsListFragmentDirections
            .actionPagedTvShowsListToSelf(args.genericSortType,
                NetworkType.ALL, genre)
        findNavController().navigate(action)
    }
}