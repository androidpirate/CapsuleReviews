package com.github.androidpirate.capsulereviews.ui.more

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.androidpirate.capsulereviews.BuildConfig
import com.github.androidpirate.capsulereviews.R
import kotlinx.android.synthetic.main.fragment_more.*

class MoreFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tmdbLink.setOnClickListener {
            val uri = Uri.parse(BuildConfig.MOVIE_DB_HOME_URL)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }
}