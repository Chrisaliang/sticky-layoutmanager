package com.jay.widget.layoutmanager.demo

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.jay.widget.StickyHeaders
import com.jay.widget.StickyHeaders.ViewSetup
import com.jay.widget.StickyHeadersGridLayoutManager
import com.jay.widget.StickyHeadersLinearLayoutManager
import com.jay.widget.StickyHeadersStaggeredGridLayoutManager
import java.util.*

class MainActivity : AppCompatActivity() {
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: MyAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mRecyclerView = findViewById(R.id.recycler_view)
        mRecyclerView?.setHasFixedSize(true)
        setLinearLayoutManager()
        mAdapter = MyAdapter()
        mRecyclerView?.setAdapter(mAdapter)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu1 -> setLinearLayoutManager()
            R.id.menu2 -> setGridLayoutManager()
            R.id.menu3 -> setStaggeredGridLayoutManager()
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setLinearLayoutManager() {
        val layoutManager = StickyHeadersLinearLayoutManager<MyAdapter>(this)
        mRecyclerView?.layoutManager = layoutManager
    }

    private fun setGridLayoutManager() {
        val layoutManager = StickyHeadersGridLayoutManager<MyAdapter>(this, 3)
        layoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (mAdapter?.isStickyHeader(position)!!) {
                    3
                } else 1
            }
        }
        mRecyclerView?.layoutManager = layoutManager
    }

    private fun setStaggeredGridLayoutManager() {
        val layoutManager = StickyHeadersStaggeredGridLayoutManager<MyAdapter>(3, StaggeredGridLayoutManager.VERTICAL)
        mRecyclerView!!.layoutManager = layoutManager
    }

    internal class MyAdapter : RecyclerView.Adapter<MyViewHolder>(), StickyHeaders, ViewSetup {
        var datas: MutableList<String> = ArrayList()

        init {
            for (i in 65 until 26 + 65) {
                datas.add(i.toChar().toString())
                for (j in 0..9) {
                    val itemText = getItemText(i.toChar())
                    datas.add(itemText)
                }
            }
        }

        private fun getItemText(prefix: Char): String {
            val length = createRandom(0, 10)
            val builder = StringBuilder()
            builder.append(prefix)
            for (i in 0 until length) {
                val random = createRandom(0, 51)
                builder.append(DICT[random])
            }
            return builder.toString()
        }

        private fun createRandom(min: Int, max: Int): Int {
            val random = Random()
            return random.nextInt(max) % (max - min + 1) + min
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            return if (viewType == HEADER_ITEM) {
                val inflate = LayoutInflater.from(parent.context).inflate(R.layout.header_item, parent, false)
                MyViewHolder(inflate)
            } else {
                val inflate = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
                MyViewHolder(inflate)
            }
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val item = datas[position]
            val textView = holder.itemView.findViewById<TextView>(android.R.id.text1)
            textView.text = item
        }

        override fun getItemCount(): Int {
            return datas.size
        }

        override fun getItemViewType(position: Int): Int {
            return if (position % 11 == 0) HEADER_ITEM else super.getItemViewType(position)
        }

        override fun isStickyHeader(position: Int): Boolean {
            return getItemViewType(position) == HEADER_ITEM
        }

        override fun setupStickyHeaderView(stickyHeader: View?) {
            stickyHeader?.let { ViewCompat.setElevation(it, 10f) }
        }

        override fun teardownStickyHeaderView(stickyHeader: View?) {
            stickyHeader?.let { ViewCompat.setElevation(it, 0f) }
        }

        override fun onViewAttachedToWindow(holder: MyViewHolder) {
            super.onViewAttachedToWindow(holder)
            val lp = holder.itemView.layoutParams
            if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams) {
                if (isStickyHeader(holder.layoutPosition)) {
                    lp.isFullSpan = true
                }
            }
        }

        companion object {
            private val DICT = arrayOf(
                    "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
                    "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"
            )
            private const val HEADER_ITEM = 123
        }
    }

    internal class MyViewHolder(itemView: View?) : ViewHolder(itemView!!)
}