package ru.tvhelp.akruglov.photogallery

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment

class PhotoGalleryActivity : SingleFragmentActivity() {

    override fun createFragment() = PhotoGalleryFragment.newInstance()
}
