package com.github.androidpirate.capsulereviews.ui.adapter.pager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.db.entity.DBFavorite
import com.github.androidpirate.capsulereviews.util.internal.Constants
import kotlinx.android.synthetic.main.pager_list_item.view.*

class PagerListItemAdapter(
    val clickListener: PagerListItemClickListener):
    ListAdapter<DBFavorite, PagerListItemAdapter.PagerItemHolder>(PagerItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerItemHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.pager_list_item, parent, false)
        return PagerItemHolder(itemView)
    }

    override fun onBindViewHolder(holder: PagerItemHolder, position: Int) {
        holder.onBindItem(getItem(position))
    }

    inner class PagerItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBindItem(favoriteItem: DBFavorite) {
            itemView.setOnClickListener {
                clickListener.onPagerItemClick(favoriteItem.id)
            }
            itemView.btMore.setOnClickListener {
                inflatePopupMenu(favoriteItem.id)
            }
            itemView.title.text = favoriteItem.title
            setStatus(favoriteItem)
        }

        private fun setStatus(favoriteItem: DBFavorite) {
            itemView.status.text = favoriteItem.bingeStatus
            when(favoriteItem.bingeStatus) {
                Constants.DEFAULT_BINGE_STATUS ->
                {
                    itemView.status
                        .setTextColor(
                            ContextCompat
                                .getColor(itemView.context, R.color.colorDefaultBingeStatus))
                }
                Constants.BINGING_BINGE_STATUS ->
                {
                    itemView.status
                        .setTextColor(
                            ContextCompat
                                .getColor(itemView.context, R.color.colorBingingBingeStatus))
                }
                Constants.SEEN_BINGE_STATUS ->
                {
                    itemView.status
                        .setTextColor(
                            ContextCompat
                                .getColor(itemView.context, R.color.colorSeenBingeStatus))
                }
            }
        }

        private fun inflatePopupMenu(itemId: Int) {
            val popupMenu = PopupMenu(itemView.context, itemView.btMore)
            popupMenu.inflate(R.menu.favorites_popup_menu)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.unfavorite ->
                    {
                        clickListener.onUnFavoriteClick(itemId)
                        true
                    }
                    R.id.changeStatus ->
                    {
                        clickListener.onChangeStatusClick(itemId)
                        true
                    }
                    else ->
                    {
                        false
                    }
                }
            }
            popupMenu.show()
        }
    }
}