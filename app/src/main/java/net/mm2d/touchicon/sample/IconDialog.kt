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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import net.mm2d.touchicon.IconInfo
import okhttp3.Request
import java.io.IOException

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
class IconDialog : DialogFragment() {
    val compositeDisposable = CompositeDisposable()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val act = activity!!
        val arg = arguments!!
        val siteUrl = arg.getString(KEY_SITE_URL)!!
        val icons: List<IconInfo> = arg.getParcelableArrayList(KEY_ICON_LIST)!!
        return AlertDialog.Builder(act)
                .setTitle(siteUrl)
                .setAdapter(IconListAdapter(act, icons)) { _, _ -> }
                .create()
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        compositeDisposable.dispose()
    }

    private inner class IconListAdapter(private val context: Context, private val list: List<IconInfo>) : BaseAdapter() {
        private val inflater = LayoutInflater.from(context)
        @SuppressLint("SetTextI18n")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val iconInfo = getItem(position)
            val view = inflateView(R.layout.li_icon, convertView, parent)
            val imageView: ImageView = view.findViewById(R.id.icon)
            Single.fromCallable { downloadIcon(iconInfo.url) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess { view.findViewById<TextView>(R.id.realSizes).text = "image size: ${it.width}x${it.height}" }
                    .subscribe({ imageView.setImageBitmap(it) }, {})
                    .addTo(compositeDisposable)
            view.findViewById<TextView>(R.id.sizes).text = "sizes: ${iconInfo.sizes}"
            view.findViewById<TextView>(R.id.rel).text = "rel: ${iconInfo.rel.value}"
            view.findViewById<TextView>(R.id.type).text = "type: ${iconInfo.type}"
            view.findViewById<TextView>(R.id.url).text = "url: ${iconInfo.url}"
            return view
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

        private fun inflateView(layout: Int, convertView: View?, parent: ViewGroup): View =
                convertView ?: inflater.inflate(layout, parent, false)

        override fun getCount(): Int = list.size

        override fun getItem(position: Int): IconInfo = list[position]

        override fun getItemId(position: Int): Long = position.toLong()
    }

    companion object {
        private const val KEY_SITE_URL = "KEY_SITE_URL"
        private const val KEY_ICON_LIST = "KEY_ICON_LIST"

        fun show(activity: FragmentActivity, title: String, icons: List<IconInfo>) {
            newInstance(title, icons).show(activity.supportFragmentManager, "")
        }

        private fun newInstance(siteUrl: String, icons: List<IconInfo>): IconDialog {
            return IconDialog().also {
                it.arguments = makeArgument(siteUrl, icons)
            }
        }

        private fun makeArgument(siteUrl: String, icons: List<IconInfo>): Bundle {
            return Bundle().also {
                it.putString(KEY_SITE_URL, siteUrl)
                it.putParcelableArrayList(KEY_ICON_LIST, ArrayList(icons))
            }
        }
    }
}
