package com.github.androidpirate.capsulereviews.ui.adapter.dialog

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.util.internal.Constants
import kotlinx.android.synthetic.main.networks_list_item.view.*

class NetworkAdapter(
    private val listener: NetworkClickListener,
    selectedNetwork: Int):
RecyclerView.Adapter<NetworkAdapter.NetworkHolder>() {

    private val networks = Constants.getNetworksArray()
    private var index = selectedNetwork

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NetworkHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.networks_list_item, parent, false)
        return NetworkHolder(view)
    }

    override fun onBindViewHolder(holder: NetworkHolder, position: Int) {
        holder.onBindNetwork(networks[position])
        holder.itemView.networkTitle.setOnClickListener {
            listener.onNetworkClick(position)
        }
        val networkTitle = holder.itemView.networkTitle
        if(index == position) {
            networkTitle.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.colorAccent))
            networkTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
        } else {
            networkTitle.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.colorWhite))
        }
    }

    override fun getItemCount(): Int {
        return networks.size
    }

    inner class NetworkHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun onBindNetwork(network: String) {
            itemView.networkTitle.text = network
        }
    }
}