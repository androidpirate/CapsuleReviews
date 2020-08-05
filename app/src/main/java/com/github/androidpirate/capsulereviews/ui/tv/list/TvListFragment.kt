package com.github.androidpirate.capsulereviews.ui.tv.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.util.FakeData
import com.github.androidpirate.capsulereviews.util.ItemClickListener
import kotlinx.android.synthetic.main.fragment_movie_list.rvPopular
import kotlinx.android.synthetic.main.fragment_tv_list.*

class TvListFragment : Fragment(), ItemClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        val adapter: TvListAdapter = TvListAdapter(this)
        adapter.submitList(FakeData.getTvShows())
        rvPopular.adapter = adapter
        rvTopRated.adapter = adapter
        rvTrending.adapter = adapter
    }

    override fun <T> onItemClick(item: T) {
        findNavController().navigate(R.id.action_tv_list_toDetail)
    }
}