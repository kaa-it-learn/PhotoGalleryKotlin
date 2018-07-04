package ru.tvhelp.akruglov.photogallery

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_photo_gallery.*
import org.jetbrains.anko.doAsync
import java.io.IOException

class PhotoGalleryFragment(): Fragment() {

    companion object {
        const val TAG = "PhotoGalleryFragment"
        fun newInstance() = PhotoGalleryFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        doAsync {
            try {
                val result = FlickrFetchr().getUrlString("https://www.bignerdranch.com")
                Log.i(TAG, "Fetched contents of URL: " + result)
            } catch(ioe: IOException) {
                Log.e(TAG, "Failed to fetch URL: ", ioe)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photo_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photoRecyclerView.layoutManager = GridLayoutManager(activity!!, 3)
    }
}