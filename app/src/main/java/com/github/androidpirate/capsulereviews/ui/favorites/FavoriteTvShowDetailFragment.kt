package com.github.androidpirate.capsulereviews.ui.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.db.entity.DBFavorite
import com.github.androidpirate.capsulereviews.util.internal.Constants
import com.github.androidpirate.capsulereviews.viewmodel.FavoritesViewModel
import com.github.androidpirate.capsulereviews.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_favorite_movie_detail.*
import kotlinx.android.synthetic.main.fragment_favorite_tv_show_detail.*
import kotlinx.android.synthetic.main.fragment_favorite_tv_show_detail.bingeStatus
import kotlinx.android.synthetic.main.fragment_favorite_tv_show_detail.overview
import kotlinx.android.synthetic.main.fragment_favorite_tv_show_detail.userRating
import kotlinx.android.synthetic.main.tv_summary.*

class FavoriteTvShowDetailFragment : Fragment() {
    private val args: FavoriteTvShowDetailFragmentArgs by navArgs()
    private lateinit var viewModel: FavoritesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_tv_show_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = ViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory).get(FavoritesViewModel::class.java)
        viewModel.getFavoriteTvShow(args.tvShowId).observe(viewLifecycleOwner, Observer {
            if(it != null) {
                setupViews(it)
            }
        })
    }

    private fun setupViews(tvShow: DBFavorite) {
        tvShowTitle.text = tvShow.title
        releaseDate.text = tvShow.releaseDate
        genres.text = tvShow.genres
        status.text = tvShow.status
        userRating.text = tvShow.voteAverage.toString()
        setBingeStatus(tvShow.bingeStatus)
        overview.text = tvShow.overview
        network.text = tvShow.networks
        createdBy.text = tvShow.createdBy
        numberOfSeasons.text = tvShow.numberOfSeasons.toString()
        numberOfEpisodes.text = tvShow.numberOfEpisodes.toString()
    }

    private fun setBingeStatus(bingeStatusString: String) {
        bingeStatus.text = bingeStatusString
        when(bingeStatusString) {
            Constants.DEFAULT_BINGE_STATUS -> {
                bingeStatus
                    .setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.colorDefaultBingeStatus))
            }
            Constants.BINGING_BINGE_STATUS -> {
                bingeStatus
                    .setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.colorBingingBingeStatus))
            }
            Constants.SEEN_BINGE_STATUS -> {
                bingeStatus
                    .setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.colorSeenBingeStatus))
            }
        }
    }
}