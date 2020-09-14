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
import androidx.recyclerview.widget.GridLayoutManager
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.network.response.tvShows.NetworkTvShowsListItem
import com.github.androidpirate.capsulereviews.ui.adapter.paged.PagedItemAdapter
import com.github.androidpirate.capsulereviews.ui.adapter.paged.PagedItemClickListener
import com.github.androidpirate.capsulereviews.util.GridSpacingItemDecoration
import com.github.androidpirate.capsulereviews.util.internal.*
import com.github.androidpirate.capsulereviews.util.internal.GenericSortType.*
import com.github.androidpirate.capsulereviews.util.internal.NetworkType.*
import com.github.androidpirate.capsulereviews.viewmodel.PagedTvShowListViewModel
import com.github.androidpirate.capsulereviews.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_paged_movies_list.*
import kotlinx.android.synthetic.main.fragment_paged_movies_list.container
import kotlinx.android.synthetic.main.fragment_paged_movies_list.loadingScreen
import kotlinx.android.synthetic.main.fragment_paged_tv_list.*
import kotlinx.android.synthetic.main.paged_tv_toolbar.*

class PagedTvListFragment : Fragment(), PagedItemClickListener {
    private val args: PagedTvListFragmentArgs by navArgs()
    private lateinit var viewModel: PagedTvShowListViewModel
    private lateinit var adapter: PagedItemAdapter<NetworkTvShowsListItem>
    private lateinit var genericSort: GenericSortType
    private lateinit var genre: GenreType
    private lateinit var network: NetworkType
    private var flagDecoration = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = ViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory).get(PagedTvShowListViewModel::class.java)
        genericSort = args.genericSortType
        network = args.network
        genre = args.genre
        adapter = PagedItemAdapter(fragment = FragmentType.PAGED_TV_LIST, clickListener = this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_paged_tv_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayLoadingScreen()
        btUp.setOnClickListener {
            findNavController().navigate(R.id.action_paged_tv_list_to_list)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(network != NONE) {
            when(network) {
                NETFLIX -> {
                    viewModel.trendingTvShowsOnNetflix.observe(viewLifecycleOwner, Observer {
                        if(it != null) {
                            adapter.submitList(it)
                        }
                    })
                }
                HULU -> {
                    viewModel.trendingTvShowsOnHulu.observe(viewLifecycleOwner, Observer {
                        if(it != null) {
                            adapter.submitList(it)
                        }
                    })
                }
                DISNEY_PLUS -> {
                    viewModel.trendingTvShowsOnDisney.observe(viewLifecycleOwner, Observer {
                        if(it != null) {
                            adapter.submitList(it)
                        }
                    })
                }
            }
        } else {
            when(genericSort) {
                POPULAR -> {
                    viewModel.popularTvShows.observe(viewLifecycleOwner, Observer {
                        if(it != null) {
                            adapter.submitList(it)
                        }
                    })
                }
                TOP_RATED -> {
                    viewModel.topRatedTvShows.observe(viewLifecycleOwner, Observer {
                        if(it != null) {
                            adapter.submitList(it)
                        }
                    })
                }
                TRENDING -> {
                    viewModel.trendingTvShows.observe(viewLifecycleOwner, Observer {
                        if(it != null) {
                            adapter.submitList(it)
                        }
                    })
                }
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
        setToolbarTitle()
        setRecyclerView()
    }

    private fun setToolbarTitle() {
        if(network != NONE) {
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
        rvPagedTvShows.layoutManager = GridLayoutManager(requireContext(), 3)
        if(!flagDecoration) {
            rvPagedTvShows.addItemDecoration(GridSpacingItemDecoration(4, 30, true))
            setFlagDecorationOn()
        }
        rvPagedTvShows.adapter = adapter
    }

    private fun setFlagDecorationOn() {
        flagDecoration = true
    }

    override fun <T> onPagedItemClick(item: T) {
        val action = PagedTvListFragmentDirections.actionPagedTvListToDetail((item as NetworkTvShowsListItem).id)
        findNavController().navigate(action)
    }
}