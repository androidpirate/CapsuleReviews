package com.github.androidpirate.capsulereviews.ui.movie.list

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
import com.github.androidpirate.capsulereviews.data.network.response.movies.NetworkMoviesListItem
import com.github.androidpirate.capsulereviews.ui.adapter.PagedMoviesAdapter
import com.github.androidpirate.capsulereviews.util.ContentFormatter
import com.github.androidpirate.capsulereviews.util.GridSpacingItemDecoration
import com.github.androidpirate.capsulereviews.util.ItemClickListener
import com.github.androidpirate.capsulereviews.util.internal.SortType
import com.github.androidpirate.capsulereviews.util.internal.SortType.*
import com.github.androidpirate.capsulereviews.viewmodel.PagedMovieListViewModel
import com.github.androidpirate.capsulereviews.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_paged_movies_list.*
import kotlinx.android.synthetic.main.paged_movie_toolbar.*

class PagedMoviesListFragment : Fragment(), ItemClickListener {
    private val args: PagedMoviesListFragmentArgs by navArgs()
    private lateinit var viewModel: PagedMovieListViewModel
    private lateinit var adapter: PagedMoviesAdapter
    private lateinit var sort: SortType
    private var flagDecoration = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = ViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory).get(PagedMovieListViewModel::class.java)
        sort = args.sortType
        adapter = PagedMoviesAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_paged_movies_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btUp.setOnClickListener {
            findNavController().navigate(R.id.action_paged_movie_list_to_list)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        when(sort) {
            POPULAR -> {
                viewModel.popularMovies.observe(viewLifecycleOwner, Observer {
                    if(it != null) {
                        adapter.submitList(it)
                    }
                })
            }
            TOP_RATED -> {
                viewModel.topRatedMovies.observe(viewLifecycleOwner, Observer {
                    if(it != null) {
                        adapter.submitList(it)
                    }
                })
            }
            NOW_PLAYING -> {
                viewModel.nowPlayingMovies.observe(viewLifecycleOwner, Observer {
                    if(it != null) {
                        adapter.submitList(it)
                    }
                })
            }
            UPCOMING -> {
                viewModel.upcomingMovies.observe(viewLifecycleOwner, Observer {
                    if(it != null) {
                        adapter.submitList(it)
                    }
                })
            }
            TRENDING -> {
                viewModel.trendingMovies.observe(viewLifecycleOwner, Observer {
                    if(it != null) {
                        adapter.submitList(it)
                    }
                })
            }
        }
        setupViews()
    }

    private fun setupViews() {
        setToolbarTitle()
        setRecyclerView()
    }

    private fun setToolbarTitle() {
        when(sort) {
            POPULAR -> movieToolbarTitle.text = POPULAR_TITLE
            TOP_RATED -> movieToolbarTitle.text = TOP_RATED_TITLE
            NOW_PLAYING -> movieToolbarTitle.text = NOW_PLAYING_TITLE
            UPCOMING -> movieToolbarTitle.text = UPCOMING_TITLE
            TRENDING -> movieToolbarTitle.text = TRENDING_TITLE
        }
    }

    private fun setRecyclerView() {
        rvPagedMovies.layoutManager = GridLayoutManager(requireContext(), 3)
        if(!flagDecoration) {
            rvPagedMovies.addItemDecoration(GridSpacingItemDecoration(4, 30, true))
            setFlagDecorationOn()
        }
        rvPagedMovies.adapter = adapter
    }

    private fun setFlagDecorationOn() {
        flagDecoration = true
    }

    override fun <T> onItemClick(item: T, isLast: Boolean, sort: SortType) {
        val action = PagedMoviesListFragmentDirections
            .actionPagedMovieListToDetail((item as NetworkMoviesListItem).id)
        findNavController().navigate(action)
    }

    companion object {
        const val POPULAR_TITLE = "Popular Movies"
        const val TOP_RATED_TITLE = "Top Rated Movies"
        const val NOW_PLAYING_TITLE = "Now Playing Movies"
        const val UPCOMING_TITLE = "Upcoming Movies"
        const val TRENDING_TITLE = "Trending Movies"

    }
}