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
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
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
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import net.mm2d.touchicon.Icon
import net.mm2d.touchicon.PageIcon

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
        val view = act.layoutInflater.inflate(
            R.layout.dialog_icon,
            act.window.decorView as ViewGroup,
            false
        )
        view.findViewById<TextView>(R.id.site_url).text = siteUrl
        val progressBar: ProgressBar = view.findViewById(R.id.progress_bar)
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(act)
        recyclerView.addItemDecoration(DividerItemDecoration(act, DividerItemDecoration.VERTICAL))
        val adapter = IconListAdapter(act)
        recyclerView.adapter = adapter
        Single.fromCallable { extractor.fromPage(siteUrl) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progressBar.visibility = View.GONE }
            .subscribe({ adapter.add(it) }, {})
            .addTo(compositeDisposable)
        Single.fromCallable { extractor.listFromDomain(siteUrl, true, listOf("120x120")) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progressBar.visibility = View.GONE }
            .subscribe({ adapter.add(it) }, {})
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

    private inner class IconListAdapter(context: Context) : RecyclerView.Adapter<IconViewHolder>() {
        private val list: MutableList<Icon> = mutableListOf()
        private val inflater = LayoutInflater.from(context)
        override fun onCreateViewHolder(parent: ViewGroup, type: Int): IconViewHolder {
            return if (type == 0) {
                LinkIconViewHolder(inflater.inflate(R.layout.li_link_icon, parent, false))
            } else {
                RootIconViewHolder(inflater.inflate(R.layout.li_root_icon, parent, false))
            }
        }

        fun add(icons: List<Icon>) {
            val positionStart = list.size
            list.addAll(icons)
            notifyItemRangeInserted(positionStart, icons.size)
        }

        override fun getItemViewType(position: Int): Int = when (list[position]) {
            is PageIcon -> 0
            else -> 1
        }

        override fun getItemCount(): Int = list.size

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
            holder.apply(list[position])
        }
    }

    private abstract class IconViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun apply(iconInfo: Icon)
    }

    @SuppressLint("SetTextI18n")
    private class LinkIconViewHolder(view: View) : IconViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.icon)
        val imageSizes: TextView = view.findViewById(R.id.image_size)
        val sizes: TextView = view.findViewById(R.id.sizes)
        val rel: TextView = view.findViewById(R.id.rel)
        val type: TextView = view.findViewById(R.id.type)
        val url: TextView = view.findViewById(R.id.url)
        override fun apply(iconInfo: Icon) {
            itemView.tag = iconInfo
            sizes.text = iconInfo.sizes
            rel.text = iconInfo.rel.value
            type.text = iconInfo.mimeType
            url.text = iconInfo.url
            val size = iconInfo.inferSize()
            val inferSize = if (size.x > 0 && size.y > 0) "(${size.x}x${size.y})" else "(uncertain)"
            imageSizes.text = inferSize
            GlideApp.with(itemView)
                .load(iconInfo.url)
                .override(Target.SIZE_ORIGINAL)
                .addListener(bitmapHook {
                    imageSizes.text = "${it.width}x${it.height} $inferSize"
                })
                .into(icon)
        }
    }

    @SuppressLint("SetTextI18n")
    private class RootIconViewHolder(view: View) : IconViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.icon)
        val imageSizes: TextView = view.findViewById(R.id.image_size)
        val sizes: TextView = view.findViewById(R.id.sizes)
        val length: TextView = view.findViewById(R.id.length)
        val type: TextView = view.findViewById(R.id.type)
        val url: TextView = view.findViewById(R.id.url)
        override fun apply(iconInfo: Icon) {
            itemView.tag = iconInfo
            sizes.text = iconInfo.sizes
            length.text = iconInfo.length.toString()
            type.text = iconInfo.mimeType
            url.text = iconInfo.url
            GlideApp.with(itemView)
                .load(iconInfo.url)
                .override(Target.SIZE_ORIGINAL)
                .addListener(bitmapHook { imageSizes.text = "${it.width}x${it.height}" })
                .into(icon)
        }
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

        private fun bitmapHook(callback: ((bitmap: Bitmap) -> Unit)): RequestListener<Drawable?> {
            return object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    if (resource is BitmapDrawable) {
                        callback(resource.bitmap)
                    }
                    return false
                }
            }
        }
    }
}
