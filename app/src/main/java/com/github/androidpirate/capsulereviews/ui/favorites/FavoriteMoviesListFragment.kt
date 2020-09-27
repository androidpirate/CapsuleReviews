package com.github.androidpirate.capsulereviews.ui.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.ui.adapter.pager.PagerListItemAdapter
import com.github.androidpirate.capsulereviews.ui.adapter.pager.PagerListItemClickListener
import com.github.androidpirate.capsulereviews.util.internal.Constants
import com.github.androidpirate.capsulereviews.viewmodel.FavoritesViewModel
import com.github.androidpirate.capsulereviews.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_favorite_movies_list.*

class FavoriteMoviesListFragment : Fragment(), PagerListItemClickListener {
    private lateinit var viewModel: FavoritesViewModel
    private lateinit var adapter: PagerListItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = PagerListItemAdapter(this)
        val factory = ViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory).get(FavoritesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_movies_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.favoriteMovies.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                adapter.submitList(it)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        setupViews()
    }

    private fun setupViews() {
        rvMovies.adapter = adapter
    }

    private fun showListItemAlertDialog(itemId: Int) {
        val status = arrayOf(
            Constants.DEFAULT_BINGE_STATUS,
            Constants.BINGING_BINGE_STATUS,
            Constants.SEEN_BINGE_STATUS)
        val builder: AlertDialog.Builder = AlertDialog
            .Builder(requireContext(), R.style.BingeStatusAlertDialogTheme)
        builder.setTitle(Constants.BINGE_STATUS_ALERT_DIALOG_TITLE)
            .setItems(status) {
                    _, which ->
                viewModel.updateFavoriteStatus(status[which], itemId)
            }
            .setNegativeButton(Constants.ALERT_DIALOG_CANCEL) {
                    dialog, _ ->
                dialog.cancel()
            }
        builder.show()
    }

    override fun onPagerItemClick(itemId: Int) {
        val action = FavoritesFragmentDirections.actionFavoriteMoviesListToDetail(itemId)
        findNavController().navigate(action)
    }

    override fun onUnFavoriteClick(itemId: Int) {
        viewModel.deleteFavoriteWithId(itemId)
    }

    override fun onChangeStatusClick(itemId: Int) {
        showListItemAlertDialog(itemId)
    }

    companion object {
        fun newInstance(): FavoriteMoviesListFragment {
            return FavoriteMoviesListFragment()
        }
    }
}