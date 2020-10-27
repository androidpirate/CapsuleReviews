package com.github.androidpirate.capsulereviews.ui.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.db.entity.DBFavorite
import com.github.androidpirate.capsulereviews.util.internal.Constants
import com.github.androidpirate.capsulereviews.util.internal.FragmentType
import com.github.androidpirate.capsulereviews.viewmodel.FavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_favorite_movie_detail.*
import kotlinx.android.synthetic.main.movie_summary.*

@AndroidEntryPoint
class FavoriteMovieDetailFragment : Fragment() {

    private val args: FavoriteMovieDetailFragmentArgs by navArgs()
    private val viewModel: FavoritesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_movie_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getFavoriteMovie(args.movieId).observe(viewLifecycleOwner, Observer {
            if(it != null) {
                setupViews(it)
            }
        })
    }

    private fun setupViews(movie: DBFavorite) {
        movieTitle.text = movie.title
        genres.text = movie.genres
        runTime.text = movie.runtime
        userRating.text = movie.voteAverage.toString()
        overview.text = movie.overview
        setBingeStatus(movie.bingeStatus)
        setMovieDetailLink(movie.id)
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

    private fun setMovieDetailLink(movieId: Int) {
        movieDetailLink.setOnClickListener {
            val action = FavoriteMovieDetailFragmentDirections
                .actionFavoriteToMovieDetail(movieId, FragmentType.FAVORITE_MOVIE_DETAIL)
            findNavController().navigate(action)
        }
    }
}