package com.github.androidpirate.capsulereviews.ui.movie.paged

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
import com.github.androidpirate.capsulereviews.data.network.response.movies.NetworkMoviesListItem
import com.github.androidpirate.capsulereviews.ui.adapter.paged.PagedItemAdapter
import com.github.androidpirate.capsulereviews.util.GridSpacingItemDecoration
import com.github.androidpirate.capsulereviews.ui.adapter.paged.PagedItemClickListener
import com.github.androidpirate.capsulereviews.ui.dialog.MovieGenresDialogFragment
import com.github.androidpirate.capsulereviews.util.internal.Constants
import com.github.androidpirate.capsulereviews.util.internal.FragmentType.*
import com.github.androidpirate.capsulereviews.util.internal.GenreType
import com.github.androidpirate.capsulereviews.viewmodel.PagedMoviesListViewModel
import com.github.androidpirate.capsulereviews.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_paged_movies_list.*
import kotlinx.android.synthetic.main.paged_movies_toolbar.*

class PagedMoviesListFragment :
    Fragment(),
    PagedItemClickListener,
    MovieGenresDialogFragment.MovieGenresDialogListener {

    private val args: PagedMoviesListFragmentArgs by navArgs()
    private lateinit var viewModel: PagedMoviesListViewModel
    private lateinit var adapter: PagedItemAdapter<NetworkMoviesListItem>
    private var flagDecoration = false
    private var moviesByGenre = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = ViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory).get(PagedMoviesListViewModel::class.java)
        viewModel.setGenericSort(args.genericSortType)
        viewModel.setGenre(args.genreType)
        adapter = PagedItemAdapter(fragment = PAGED_MOVIE_LIST, clickListener = this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_paged_movies_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayLoadingScreen()
        btUp.setOnClickListener {
            findNavController().navigate(R.id.action_paged_movies_list_to_list)
        }
        if(args.genreType == GenreType.ALL) {
            getMoviesByGenericSort()
        } else {
            getMoviesByGenre()
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
        setupRecyclerView()
    }

    private fun setupToolbar() {
        if(moviesByGenre) {
            displaySpinner()
            movieGenreSpinner.text = Constants.getMovieGenresKey(args.genreType)
            movieGenreSpinner.setOnClickListener {
                showMovieGenreDialog()
            }
        } else {
            displayToolbarTitle()
            movieToolbarTitle.text = Constants.getGenericSortKey(args.genericSortType)
        }
    }

    private fun displaySpinner() {
        movieToolbarTitle.visibility = View.GONE
        movieGenreSpinner.visibility = View.VISIBLE
    }

    private fun displayToolbarTitle() {
        movieToolbarTitle.visibility = View.VISIBLE
        movieGenreSpinner.visibility = View.GONE
    }

    private fun setupRecyclerView() {
        if(!flagDecoration) {
            rvPagedMovies.addItemDecoration(GridSpacingItemDecoration(4, 30, true))
            setFlagDecorationOn()
        }
        rvPagedMovies.adapter = adapter
    }

    private fun setFlagDecorationOn() {
        flagDecoration = true
    }

    private fun getMoviesByGenericSort() {
        viewModel.moviesByGenericSortType.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                adapter.submitList(it)
            }
        })
    }

    private fun getMoviesByGenre() {
        moviesByGenre = true
        viewModel.moviesByGenre.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                adapter.submitList(it)
            }
        })
    }

    private fun showMovieGenreDialog() {
        val genrePosition = Constants.getMovieGenrePosition(args.genreType)
        val genresDialog = MovieGenresDialogFragment.newInstance(this, genrePosition)
        genresDialog.show(requireActivity().supportFragmentManager, Constants.PAGED_MOVIES_LIST_FRAG_TAG)
    }

    override fun <T> onPagedItemClick(item: T) {
        val action = PagedMoviesListFragmentDirections
            .actionPagedMoviesListToDetail((item as NetworkMoviesListItem).id)
        findNavController().navigate(action)
    }

    override fun onGenreSelected(genre: GenreType) {
        val action = PagedMoviesListFragmentDirections
            .actionPagedMoviesListToSelf(args.genericSortType, genre)
        findNavController().navigate(action)
    }
}