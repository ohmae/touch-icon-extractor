/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.sample

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import net.mm2d.touchicon.LinkIcon
import okhttp3.Request
import java.io.IOException

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
class IconDialog : DialogFragment() {
    private val extractor = TouchIconExtractorHolder.extractor
    private val compositeDisposable = CompositeDisposable()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val act = activity!!
        val arg = arguments!!
        val title = arg.getString(KEY_TITLE)!!
        val siteUrl = arg.getString(KEY_SITE_URL)!!
        val view = act.layoutInflater.inflate(R.layout.dialog_icon, act.window.decorView as ViewGroup, false)
        view.findViewById<TextView>(R.id.site_url).text = siteUrl
        val progressBar: ProgressBar = view.findViewById(R.id.progress_bar)
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(act)
        recyclerView.addItemDecoration(DividerItemDecoration(act, DividerItemDecoration.VERTICAL))
        Single.fromCallable { extractor.extract(siteUrl) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { progressBar.visibility = View.GONE }
                .subscribe({ recyclerView.adapter = IconListAdapter(act, it) }, {})
                .addTo(compositeDisposable)
        return AlertDialog.Builder(act)
                .setTitle(title)
                .setView(view)
                .create()
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        compositeDisposable.dispose()
    }

    private inner class IconListAdapter(context: Context, private val list: List<LinkIcon>) : RecyclerView.Adapter<IconViewHolder>() {
        private val inflater = LayoutInflater.from(context)
        override fun onCreateViewHolder(parent: ViewGroup, type: Int): IconViewHolder {
            return IconViewHolder(inflater.inflate(R.layout.li_icon, parent, false))
        }

        override fun getItemCount(): Int = list.size

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
            val iconInfo = list[position]
            holder.itemView.tag = iconInfo
            holder.sizes.text = iconInfo.sizes
            holder.rel.text = iconInfo.rel.value
            holder.type.text = iconInfo.type
            holder.url.text = iconInfo.url
            val size = iconInfo.inferSize()
            holder.imageSizes.text = "(${size.x}x${size.y})"
            Single.fromCallable { downloadIcon(iconInfo.url) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (holder.itemView.tag == iconInfo) {
                            holder.imageSizes.text = "${it.width}x${it.height} (${size.x}x${size.y})"
                            holder.icon.setImageBitmap(it)
                        }
                    }, {})
                    .addTo(compositeDisposable)
        }

        private fun downloadIcon(url: String): Bitmap {
            val request = Request.Builder().get().url(url).build()
            val response = OkHttpClientHolder.client.newCall(request).execute()
            if (!response.isSuccessful) {
                throw IOException()
            }
            val bin = response.body()?.bytes() ?: throw IOException()
            return BitmapFactory.decodeByteArray(bin, 0, bin.size) ?: throw IOException()
        }
    }

    private class IconViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.icon)
        val imageSizes: TextView = view.findViewById(R.id.image_size)
        val sizes: TextView = view.findViewById(R.id.sizes)
        val rel: TextView = view.findViewById(R.id.rel)
        val type: TextView = view.findViewById(R.id.type)
        val url: TextView = view.findViewById(R.id.url)
    }

    companion object {
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SITE_URL = "KEY_SITE_URL"

        fun show(activity: FragmentActivity, title: String, siteUrl: String) {
            IconDialog().also {
                it.arguments = makeArgument(title, siteUrl)
            }.show(activity.supportFragmentManager, "")
        }

        private fun makeArgument(title: String, siteUrl: String): Bundle {
            return Bundle().also {
                it.putString(KEY_TITLE, title)
                it.putString(KEY_SITE_URL, siteUrl)
            }
        }
    }
}
