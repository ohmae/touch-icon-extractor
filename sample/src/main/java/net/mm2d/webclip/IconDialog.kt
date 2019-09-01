/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.webclip

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.mm2d.touchicon.Icon
import net.mm2d.touchicon.PageIcon
import net.mm2d.touchicon.WebAppIcon

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
class IconDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = activity!!
        val arguments = arguments!!
        val title = arguments.getString(KEY_TITLE)!!
        val siteUrl = arguments.getString(KEY_SITE_URL)!!
        val useExtension = arguments.getBoolean(KEY_USE_EXTENSION)
        val view = activity.layoutInflater.inflate(
            R.layout.dialog_icon,
            activity.window.decorView as ViewGroup,
            false
        )
        val extractor =
            if (useExtension) ExtractorHolder.library
            else ExtractorHolder.local
        view.findViewById<TextView>(R.id.site_url).text = siteUrl
        val progressBar: ProgressBar = view.findViewById(R.id.progress_bar)
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        val adapter = IconListAdapter(activity, view.findViewById(R.id.transparent_switch))
        recyclerView.adapter = adapter
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                extractor.fromPage(siteUrl, true)
            }.let { adapter.add(it) }
            progressBar.visibility = View.GONE
        }
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                extractor.listFromDomain(siteUrl, true, listOf("120x120"))
            }.let { adapter.add(it) }
            progressBar.visibility = View.GONE
        }
        return AlertDialog.Builder(activity)
            .setTitle(title)
            .setView(view)
            .create()
    }

    private inner class IconListAdapter(
        context: Context,
        private val transparentSwitch: CompoundButton
    ) : RecyclerView.Adapter<IconViewHolder>() {
        private val list: MutableList<Icon> = mutableListOf()
        private val inflater = LayoutInflater.from(context)

        init {
            transparentSwitch.setOnCheckedChangeListener { _, _ ->
                notifyDataSetChanged()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, type: Int): IconViewHolder {
            return when (type) {
                0 -> PageIconViewHolder(
                    inflater.inflate(
                        R.layout.li_page_icon,
                        parent,
                        false
                    )
                )
                1 -> WebAppIconViewHolder(
                    inflater.inflate(
                        R.layout.li_web_app_icon,
                        parent,
                        false
                    )
                )
                else -> DomainIconViewHolder(
                    inflater.inflate(
                        R.layout.li_domain_icon,
                        parent,
                        false
                    )
                )
            }
        }

        fun add(icons: List<Icon>) {
            val positionStart = list.size
            list.addAll(icons)
            notifyItemRangeInserted(positionStart, icons.size)
        }

        override fun getItemViewType(position: Int): Int = when (list[position]) {
            is PageIcon -> 0
            is WebAppIcon -> 1
            else -> 2
        }

        override fun getItemCount(): Int = list.size

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
            holder.apply(list[position], transparentSwitch.isChecked)
        }
    }

    private abstract class IconViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun apply(icon: Icon, transparent: Boolean)
    }

    @SuppressLint("SetTextI18n")
    private class PageIconViewHolder(view: View) : IconViewHolder(view) {
        val iconImage: ImageView = view.findViewById(R.id.icon)
        val imageSizes: TextView = view.findViewById(R.id.image_size)
        val sizes: TextView = view.findViewById(R.id.sizes)
        val rel: TextView = view.findViewById(R.id.rel)
        val type: TextView = view.findViewById(R.id.type)
        val url: TextView = view.findViewById(R.id.url)
        override fun apply(icon: Icon, transparent: Boolean) {
            iconImage.setBackgroundResource(
                selectBackground(
                    transparent
                )
            )
            itemView.tag = icon
            sizes.text = icon.sizes
            rel.text = icon.rel.value
            type.text = icon.mimeType
            url.text = icon.url
            val size = icon.inferSize()
            val inferSize = if (size.isValid()) {
                "(${size.width}x${size.height})"
            } else {
                "(uncertain)"
            }
            imageSizes.text = inferSize
            GlideApp.with(itemView)
                .load(icon.url)
                .override(Target.SIZE_ORIGINAL)
                .addListener(bitmapHook {
                    imageSizes.text = "${it.width}x${it.height} $inferSize"
                })
                .into(iconImage)
        }
    }

    @SuppressLint("SetTextI18n")
    private class WebAppIconViewHolder(view: View) : IconViewHolder(view) {
        val iconImage: ImageView = view.findViewById(R.id.icon)
        val imageSizes: TextView = view.findViewById(R.id.image_size)
        val sizes: TextView = view.findViewById(R.id.sizes)
        val type: TextView = view.findViewById(R.id.type)
        val density: TextView = view.findViewById(R.id.density)
        val url: TextView = view.findViewById(R.id.url)
        override fun apply(icon: Icon, transparent: Boolean) {
            iconImage.setBackgroundResource(
                selectBackground(
                    transparent
                )
            )
            itemView.tag = icon
            sizes.text = icon.sizes
            type.text = icon.mimeType
            url.text = icon.url
            if (icon is WebAppIcon) {
                density.text = icon.density
            }
            val size = icon.inferSize()
            val inferSize = if (size.width > 0 && size.height > 0) {
                "(${size.width}x${size.height})"
            } else {
                "(uncertain)"
            }
            imageSizes.text = inferSize
            GlideApp.with(itemView)
                .load(icon.url)
                .override(Target.SIZE_ORIGINAL)
                .addListener(bitmapHook {
                    imageSizes.text = "${it.width}x${it.height} $inferSize"
                })
                .into(iconImage)
        }
    }

    @SuppressLint("SetTextI18n")
    private class DomainIconViewHolder(view: View) : IconViewHolder(view) {
        val iconImage: ImageView = view.findViewById(R.id.icon)
        val imageSizes: TextView = view.findViewById(R.id.image_size)
        val sizes: TextView = view.findViewById(R.id.sizes)
        val length: TextView = view.findViewById(R.id.length)
        val type: TextView = view.findViewById(R.id.type)
        val url: TextView = view.findViewById(R.id.url)
        override fun apply(icon: Icon, transparent: Boolean) {
            iconImage.setBackgroundResource(
                selectBackground(
                    transparent
                )
            )
            itemView.tag = icon
            sizes.text = icon.sizes
            length.text = icon.length.toString()
            type.text = icon.mimeType
            url.text = icon.url
            GlideApp.with(itemView)
                .load(icon.url)
                .override(Target.SIZE_ORIGINAL)
                .addListener(bitmapHook {
                    imageSizes.text = "${it.width}x${it.height}"
                })
                .into(iconImage)
        }
    }

    companion object {
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SITE_URL = "KEY_SITE_URL"
        private const val KEY_USE_EXTENSION = "KEY_USE_EXTENSION"

        fun show(
            activity: FragmentActivity,
            title: String,
            siteUrl: String,
            useExtension: Boolean
        ) {
            IconDialog().also {
                it.arguments =
                    makeArgument(title, siteUrl, useExtension)
            }.show(activity.supportFragmentManager, "")
        }

        private fun makeArgument(title: String, siteUrl: String, useExtension: Boolean): Bundle {
            return Bundle().also {
                it.putString(KEY_TITLE, title)
                it.putString(KEY_SITE_URL, siteUrl)
                it.putBoolean(KEY_USE_EXTENSION, useExtension)
            }
        }

        private fun bitmapHook(callback: ((bitmap: Bitmap) -> Unit)): RequestListener<Drawable?> {
            return object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean = false

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

        private fun selectBackground(transparent: Boolean): Int =
            if (transparent) R.drawable.bg_icon else 0
    }
}
