package com.jay.widget

import android.view.View

/**
 * Created by jay on 2017/12/4 上午10:52
 *
 * Adds sticky headers capabilities to the [androidx.recyclerview.widget.RecyclerView.Adapter]. Should return `true` for all
 * positions that represent sticky headers.
 *
 * @link https://github.com/Doist/RecyclerViewExtensions/blob/master/StickyHeaders
 */
interface StickyHeaders {
    fun isStickyHeader(position: Int): Boolean
    interface ViewSetup {
        /**
         * Adjusts any necessary properties of the `holder` that is being used as a sticky header.
         *
         * [.teardownStickyHeaderView] will be called sometime after this method
         * and before any other calls to this method go through.
         */
        fun setupStickyHeaderView(stickyHeader: View?)

        /**
         * Reverts any properties changed in [.setupStickyHeaderView].
         *
         * Called after [.setupStickyHeaderView].
         */
        fun teardownStickyHeaderView(stickyHeader: View?)
    }
}