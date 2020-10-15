package com.github.androidpirate.capsulereviews.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.ui.adapter.dialog.NetworkAdapter
import com.github.androidpirate.capsulereviews.ui.adapter.dialog.NetworkClickListener
import com.github.androidpirate.capsulereviews.util.internal.Constants
import com.github.androidpirate.capsulereviews.util.internal.NetworkType
import kotlinx.android.synthetic.main.tv_networks_dialog.*

class TvNetworksDialogFragment(
    private val listener: NetworksDialogListener,
    private val selectedPosition: Int):
    DialogFragment(),
    NetworkClickListener {

    private lateinit var adapter: NetworkAdapter

    interface NetworksDialogListener {
        fun onNetworkSelected(network: NetworkType)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = NetworkAdapter(this, selectedPosition)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tv_networks_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cancelButton.setOnClickListener {
            dialog?.cancel()
        }
        tvNetworks.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun getSelectedNetwork(itemPosition: Int): NetworkType {
        val map = Constants.getTvNetworksMap()
        return map[Constants.getTvNetworksArray()[itemPosition]]  ?: error(Constants.NETWORK_TYPE_ERROR_MESSAGE)
    }

    override fun onNetworkClick(itemPosition: Int) {
        listener.onNetworkSelected(getSelectedNetwork(itemPosition))
        dialog?.cancel()
    }

    companion object {
        fun newInstance(listener: NetworksDialogListener, selectedPosition: Int): TvNetworksDialogFragment {
            return TvNetworksDialogFragment(listener, selectedPosition)
        }
    }
}