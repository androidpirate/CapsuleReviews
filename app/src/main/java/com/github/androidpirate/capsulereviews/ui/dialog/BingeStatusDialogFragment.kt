package com.github.androidpirate.capsulereviews.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.util.internal.Constants

class BingeStatusDialogFragment(private val listener: BingeStatusDialogListener): DialogFragment() {

    interface BingeStatusDialogListener {
        fun onStatusSelected(status: String, itemId: Int)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val status = Constants.getBingeStatusArray()
        val builder = AlertDialog.Builder(activity, R.style.BingeStatusAlertDialogTheme)
        builder.setTitle(Constants.BINGE_STATUS_ALERT_DIALOG_TITLE)
            .setItems(status) {
                _, which ->
                listener.onStatusSelected(status[which], requireArguments().getInt(Constants.ARG_ITEM_ID))
            }
            .setNegativeButton(Constants.ALERT_DIALOG_CANCEL) {
                dialog, _ ->
                dialog.cancel()
            }
        return builder.create()
    }

    companion object {
        fun newInstance(listener: BingeStatusDialogListener, itemId: Int): BingeStatusDialogFragment {
            val fragment = BingeStatusDialogFragment(listener)
            val args = Bundle()
            args.putInt(Constants.ARG_ITEM_ID, itemId)
            fragment.arguments = args
            return fragment
        }
    }
}