package ru.tvhelp.akruglov.photogallery

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_photo_gallery.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.IOException

class PhotoGalleryFragment(): Fragment() {

    companion object {
        const val TAG = "PhotoGalleryFragment"
        fun newInstance() = PhotoGalleryFragment()
    }

    private var items: List<Photo>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        doAsync {
            val photos = FlickrFetchr().fetchItems()
            uiThread {
                items = photos
                setupAdapter()
            }
        }

    }

    private fun setupAdapter() {
        if (isAdded && items != null && photoRecyclerView.adapter == null) {
            photoRecyclerView.adapter = PhotoAdapter(items!!)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photo_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photoRecyclerView.layoutManager = GridLayoutManager(activity!!, 3)
        setupAdapter()
    }

    private inner class PhotoHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bindPhoto(photo: Photo) {
            (itemView as TextView).text = photo.toString()
        }
    }

    private inner class PhotoAdapter(private val photos: List<Photo>): RecyclerView.Adapter<PhotoHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
            return PhotoHolder(TextView(activity!!))
        }

        override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
            holder.bindPhoto(photos[position])
        }

        override fun getItemCount(): Int {
            return photos.size
        }
    }
}