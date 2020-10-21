package com.github.androidpirate.capsulereviews.ui.search

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.network.response.multiSearch.NetworkMultiSearchListItem
import com.github.androidpirate.capsulereviews.ui.adapter.paged.PagedItemAdapter
import com.github.androidpirate.capsulereviews.ui.adapter.paged.PagedItemClickListener
import com.github.androidpirate.capsulereviews.util.GridSpacingItemDecoration
import com.github.androidpirate.capsulereviews.util.internal.Constants
import com.github.androidpirate.capsulereviews.util.internal.FragmentType
import com.github.androidpirate.capsulereviews.viewmodel.PagedSearchResultsViewModel
import com.github.androidpirate.capsulereviews.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.search_bar.*

class SearchFragment : Fragment(), PagedItemClickListener {

    private lateinit var viewModel: PagedSearchResultsViewModel
    private lateinit var adapter: PagedItemAdapter<NetworkMultiSearchListItem>
    private var isKeyboardOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = ViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory).get(PagedSearchResultsViewModel::class.java)
        adapter = PagedItemAdapter(FragmentType.SEARCH_RESULTS, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSoftKeyboardListener(view)
        setupViews()
        etSearch.setOnEditorActionListener { textView, actionId, keyEvent ->
            return@setOnEditorActionListener when(actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    if(etSearch.text.isNotBlank() || etSearch.text.isNotEmpty()) {
                        viewModel.setQueryString(etSearch.text.toString())
                        displaySearchResults()
                        if(isKeyboardOn) {
                            hideSoftKeyboard()
                        }
                    } else {
                        hideSoftKeyboard()
                    }
                    true
                }
                else -> false
            }
        }
        etSearch.setOnFocusChangeListener { view, b ->
            if(etSearch.hasFocus()) {
                displayClearButton()
            }
        }
        clearButton.setOnClickListener {
            clearQueryString()
        }
    }

    override fun onResume() {
        super.onResume()
        if(etSearch.text.isNotBlank() || etSearch.text.isNotEmpty()) {
            displaySearchResults()
        }
        setFlagDecorationOn()
    }

    private fun displaySearchResults() {
        viewModel.searchResults.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    private fun displayClearButton() {
        clearButton.visibility = View.VISIBLE
    }

    private fun hideClearButton() {
        clearButton.visibility = View.GONE
    }

    private fun clearQueryString() {
        etSearch.text.clear()
    }

    private fun setupViews() {
        rvSearch.adapter = adapter
    }

    private fun setFlagDecorationOn() {
        if(!viewModel.getFlagDecoration()) {
            rvSearch.addItemDecoration(
                GridSpacingItemDecoration(
                    resources.getInteger(R.integer.grid_span_count),
                    resources.getDimensionPixelSize(R.dimen.list_item_spacing),
                    true))
        }
        viewModel.setFlagDecorationOn()
    }

    private fun setSoftKeyboardListener(view: View) {
        view.viewTreeObserver
            .addOnGlobalLayoutListener {
                val rect = Rect()
                view.getWindowVisibleDisplayFrame(rect)
                val heightDiff = view.rootView.height - (rect.bottom - rect.top)
                if(heightDiff > 500) {
                    isKeyboardOn = true
                } else if(heightDiff < 500) {
                    isKeyboardOn = false
                }
            }
    }

    private fun hideSoftKeyboard() {
        val inputManager: InputMethodManager = activity?.
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focusedView = activity?.currentFocus
        if(focusedView != null) {
            inputManager
                .hideSoftInputFromWindow(
                    focusedView.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS)
        }
        etSearch.clearFocus()
        hideClearButton()
    }

    override fun <T> onPagedItemClick(item: T) {
        viewModel.setFlagDecorationOff()
        val searchItem = item as NetworkMultiSearchListItem
        when(searchItem.mediaType) {
            Constants.MEDIA_TYPE_MOVIE -> {
                val action = SearchFragmentDirections.searchToMovieDetail(searchItem.id)
                findNavController().navigate(action)
            }
             Constants.MEDIA_TYPE_TV -> {
                 val action = SearchFragmentDirections.searchToTvShowDetail(searchItem.id)
                 findNavController().navigate(action)
             }
        }
    }
}