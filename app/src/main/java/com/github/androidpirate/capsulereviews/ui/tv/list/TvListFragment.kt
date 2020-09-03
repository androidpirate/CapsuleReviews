package com.github.androidpirate.capsulereviews.ui.tv.list

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.androidpirate.capsulereviews.BuildConfig
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.db.entity.DBTvShow
import com.github.androidpirate.capsulereviews.data.db.entity.DbTvShowShowcase
import com.github.androidpirate.capsulereviews.ui.adapter.list.ListItemAdapter
import com.github.androidpirate.capsulereviews.ui.adapter.list.ItemClickListener
import com.github.androidpirate.capsulereviews.util.internal.Constants
import com.github.androidpirate.capsulereviews.util.internal.FragmentType.*
import com.github.androidpirate.capsulereviews.util.internal.SortType
import com.github.androidpirate.capsulereviews.util.internal.SortType.*
import com.github.androidpirate.capsulereviews.viewmodel.TvShowListViewModel
import com.github.androidpirate.capsulereviews.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_tv_list.*
import kotlinx.android.synthetic.main.tv_showcase.*

class TvListFragment : Fragment(), ItemClickListener {
    private lateinit var popularShowsAdapter: ListItemAdapter<DBTvShow>
    private lateinit var topRatedShowsAdapter: ListItemAdapter<DBTvShow>
    private lateinit var trendingShowsAdapter: ListItemAdapter<DBTvShow>
    private lateinit var popularNetflixAdapter: ListItemAdapter<DBTvShow>
    private lateinit var popularHuluAdapter: ListItemAdapter<DBTvShow>
    private lateinit var popularDisneyPlusAdapter: ListItemAdapter<DBTvShow>
    private lateinit var viewModel: TvShowListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupAdapters()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        displayLoadingScreen()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = ViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory).get(TvShowListViewModel::class.java)
        viewModel.popularTvShows.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                popularShowsAdapter.submitList(it)
            }
        })
        viewModel.topRatedTvShows.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                topRatedShowsAdapter.submitList(it)
            }
        })
        viewModel.trendingTvShows.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                trendingShowsAdapter.submitList(it)
            }
        })
        viewModel.showcaseTvShow.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                setShowCaseTvShow(it)
            }
        })
        viewModel.popularOnNetflix.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                popularNetflixAdapter.submitList(it)
            }
        })
        viewModel.popularOnHulu.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                popularHuluAdapter.submitList(it)
            }
        })
        viewModel.popularOnDisneyPlus.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                popularDisneyPlusAdapter.submitList(it)
            }
        })
    }

    private fun displayLoadingScreen() {
        container.visibility = View.GONE
        loadingScreen.visibility = View.VISIBLE
    }

    private fun displayContainerScreen() {
        loadingScreen.visibility = View.GONE
        container.visibility = View.VISIBLE
    }

    private fun setupAdapters() {
        popularShowsAdapter = ListItemAdapter(fragment = TV_LIST, sort = POPULAR, clickListener = this)
        topRatedShowsAdapter = ListItemAdapter(fragment = TV_LIST, sort = TOP_RATED, clickListener = this)
        trendingShowsAdapter = ListItemAdapter(fragment = TV_LIST, sort = TRENDING,clickListener = this)
        popularNetflixAdapter = ListItemAdapter(fragment = TV_LIST, sort = POPULAR, clickListener = this)
        popularHuluAdapter = ListItemAdapter(fragment = TV_LIST, sort = POPULAR, clickListener = this)
        popularDisneyPlusAdapter = ListItemAdapter(fragment = TV_LIST, sort = POPULAR, clickListener = this)
    }

    private fun setupViews() {
        rvPopular.adapter = popularShowsAdapter
        rvTopRated.adapter = topRatedShowsAdapter
        rvTrending.adapter = trendingShowsAdapter
        rvPopularOnNetflix.adapter = popularNetflixAdapter
        rvPopularOnHulu.adapter = popularHuluAdapter
        rvPopularOnDisneyPlus.adapter = popularDisneyPlusAdapter
    }

    private fun setShowCaseTvShow(showcaseTvShow: DbTvShowShowcase) {
        setShowcaseTvShowPoster(showcaseTvShow.posterPath)
        setShowCaseTvShowTitle(showcaseTvShow.title)
        setShowCaseTvShowClickListeners(showcaseTvShow)
    }

    private fun setShowcaseTvShowPoster(showcaseTvShowPosterPath: String) {
        Glide.with(this)
            .load(
                BuildConfig.MOVIE_DB_IMAGE_BASE_URL +
                        Constants.SHOWCASE_POSTER_WIDTH +
                        showcaseTvShowPosterPath)
            .placeholder(R.drawable.ic_image_placeholder)
            .into(scPoster)
    }

    private fun setShowCaseTvShowTitle(showcaseTvShowTitle: String) {
        scTitle.text = showcaseTvShowTitle
    }

    private fun setShowCaseTvShowClickListeners(showcaseTvShow: DbTvShowShowcase) {
        scAddFavorite.setOnClickListener {
            // TODO 7: Implement adding to favorites here
        }

        scInfo.setOnClickListener {
            onShowcaseTvShowClick(showcaseTvShow.tvShowId)
        }

        val trailerEndpoint = BuildConfig.YOUTUBE_BASE_URL + showcaseTvShow.videoKey
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
        displayContainerScreen()
    }

    private fun onShowcaseTvShowClick(showcaseTvShowId: Int) {
        val action = TvListFragmentDirections
            .actionTvListToDetail(showcaseTvShowId)
        findNavController().navigate(action)
    }

    override fun <T> onItemClick(item: T, isLast: Boolean, sort: SortType) {
        val action = TvListFragmentDirections.actionTvListToDetail((item as DBTvShow).id)
        findNavController().navigate(action)
    }

}