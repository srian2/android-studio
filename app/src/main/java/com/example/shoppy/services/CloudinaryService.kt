package com.example.shoppy.services

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback

class CloudinaryService private constructor(private val context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: CloudinaryService? = null

        private var isInitialized = false  // Flag to track initialization state

        // Thread-safe method to get the instance of CloudinaryService
        fun getInstance(context: Context): CloudinaryService {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: CloudinaryService(context.applicationContext).also {
                    INSTANCE = it
                    // Initialize Cloudinary only once
                    if (!isInitialized) {
                        it.init()
                        isInitialized = true
                    }
                }
            }
        }
    }

   fun init() {
        // Initialize Cloudinary with your configuration
        val config = HashMap<String, String>()
        config["cloud_name"] = "dz62zxsy7" // Replace with your Cloudinary cloud name
        config["api_key"] = "239274435527312" // Replace with your Cloudinary API key
        config["api_secret"] = "M2AP5X70Kbg5VYQKZ99BvVmIpak" // Replace with your Cloudinary API secret

        // Initialize Cloudinary MediaManager
        MediaManager.init(context, config)
    }

    // Upload image to Cloudinary
    fun uploadImage(imageUri: Uri, callback: ImageUploadCallback) {
        val uploadRequest = MediaManager.get().upload(imageUri).callback(object : UploadCallback {

            override fun onStart(requestId: String) {
                callback.onUploadStart()
            }

            override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                callback.onUploadProgress(bytes, totalBytes)
            }

            override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                val imageUrl = resultData["url"] as String
                callback.onUploadSuccess(imageUrl)
            }

            override fun onReschedule(requestId: String, error: ErrorInfo) {
                callback.onUploadRescheduled(error.description)
            }

            override fun onError(requestId: String, error: ErrorInfo) {
                callback.onUploadError(error.description)
            }
        })

        // Dispatch the upload request
        uploadRequest.dispatch()
    }

    // Define the interface for the callback
    interface ImageUploadCallback {
        fun onUploadStart()
        fun onUploadProgress(bytes: Long, totalBytes: Long)
        fun onUploadSuccess(imageUrl: String)
        fun onUploadRescheduled(errorDescription: String)
        fun onUploadError(errorDescription: String)
    }
}
