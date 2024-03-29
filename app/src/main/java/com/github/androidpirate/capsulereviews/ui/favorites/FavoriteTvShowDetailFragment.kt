package com.github.androidpirate.capsulereviews.ui.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.db.entity.DBFavorite
import com.github.androidpirate.capsulereviews.util.internal.Constants
import com.github.androidpirate.capsulereviews.util.internal.FragmentType
import com.github.androidpirate.capsulereviews.viewmodel.FavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_favorite_tv_show_detail.*
import kotlinx.android.synthetic.main.fragment_favorite_tv_show_detail.bingeStatus
import kotlinx.android.synthetic.main.fragment_favorite_tv_show_detail.overview
import kotlinx.android.synthetic.main.fragment_favorite_tv_show_detail.userRating
import kotlinx.android.synthetic.main.tv_summary.*

@AndroidEntryPoint
class FavoriteTvShowDetailFragment : Fragment() {

    private val args: FavoriteTvShowDetailFragmentArgs by navArgs()
    private val viewModel: FavoritesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_tv_show_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getFavoriteTvShow(args.tvShowId).observe(viewLifecycleOwner, Observer {
            if(it != null) {
                setupViews(it)
            }
        })
    }

    private fun setupViews(tvShow: DBFavorite) {
        tvShowTitle.text = tvShow.title
        genres.text = tvShow.genres
        userRating.text = tvShow.voteAverage.toString()
        status.text = tvShow.status
        overview.text = tvShow.overview
        network.text = tvShow.networks
        setBingeStatus(tvShow.bingeStatus)
        setTvShowDetailLink(tvShow.id)
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

    private fun setTvShowDetailLink(showId: Int) {
        tvDetailLink.setOnClickListener {
            val action = FavoriteTvShowDetailFragmentDirections
                .actionFavoriteToTvShowDetail(showId, FragmentType.FAVORITE_TV_DETAIL)
            navigateToDetails(action)
        }
    }

    private fun navigateToDetails(action: NavDirections) {
        findNavController().navigate(action)
    }
}