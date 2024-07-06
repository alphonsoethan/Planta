package com.android.project4.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.project4.Constants
import com.android.project4.R
import com.android.project4.adapters.AdapterCategory
import com.android.project4.databinding.FragmentHomeBinding
import com.android.project4.ml.Model
import com.android.project4.models.Category
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val imageSize = 224

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        setStatusBarColor()
        setAllCategories()
        setupImageClassification()
        navigatingToSearchFragment()

        return binding.root
    }

    private fun setupImageClassification() {

        binding.button.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, 1)
            } else {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            var image = data?.extras?.get("data") as? Bitmap
            if (image != null) {
                val dimension = minOf(image.width, image.height)
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension)
                binding.imageView.setImageBitmap(image)

                val scaledImage = Bitmap.createScaledBitmap(image, imageSize, imageSize, false)
                classifyImage(scaledImage)
            }
        }
    }

    private fun classifyImage(image: Bitmap) {
        try {
            val model = Model.newInstance(requireContext())

            val inputFeature0 =
                TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
            val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
            byteBuffer.order(ByteOrder.nativeOrder())

            val intValues = IntArray(imageSize * imageSize)
            image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)

            var pixel = 0
            for (i in 0 until imageSize) {
                for (j in 0 until imageSize) {
                    val value = intValues[pixel++]
                    byteBuffer.putFloat(((value shr 16) and 0xFF) * (1f / 255f))
                    byteBuffer.putFloat(((value shr 8) and 0xFF) * (1f / 255f))
                    byteBuffer.putFloat((value and 0xFF) * (1f / 255f))
                }
            }

            inputFeature0.loadBuffer(byteBuffer)

            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            val confidences = outputFeature0.floatArray
            var maxPos = 0
            var maxConfidence = 0f
            for (i in confidences.indices) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i]
                    maxPos = i
                }
            }

            val classes = arrayOf(
                "Rose Plant", "Peace Lily", "Periwinkle", "Monstera",
                "Areca Palm", "Zebra Haworthia", "Barrel Cactus"
            )
//            binding.searchEt.hint = classes[maxPos]

            var s = ""
            for (i in classes.indices) {
                s += String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100)
            }
            binding.classified.setText("classified as : \n" + classes[maxPos])
            binding.confidencesText.setText("Confidence :\n $s")

            model.close()
        } catch (e: IOException) {
            // Handle the exception
        }
    }

    private fun setAllCategories() {
        val categoryList = ArrayList<Category>()

        for (i in 0 until Constants.allProductsCategoryIcon.size) {
            categoryList.add(Category(Constants.allProductsCategory[i], Constants.allProductsCategoryIcon[i]))
        }
        binding.rvCategories.adapter = AdapterCategory(categoryList) { category ->
            val bundle = Bundle().apply {
                putString("category", category.title)
            }
            findNavController().navigate(R.id.action_homeFragment_to_categoryFragment, bundle)
        }
    }

    private fun navigatingToSearchFragment() {
        binding.searchCv.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun setStatusBarColor(){
        activity?.window?.apply {
            val statusBarColors = ContextCompat.getColor(requireContext(), R.color.green)
            statusBarColor = statusBarColors

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

}


//package com.android.project4.fragments
//package app.ij.mlwithtensorflowlite
//
//import android.widget.LinearLayout
//import com.google.android.material.card.MaterialCardView
//
//
//
//import android.os.Build
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.core.content.ContextCompat
//import androidx.navigation.fragment.findNavController
//import com.android.project4.Constants
//import com.android.project4.R
//import com.android.project4.adapters.AdapterCategory
//import com.android.project4.databinding.FragmentHomeBinding
//import com.android.project4.models.Category
//import android.Manifest
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.graphics.Bitmap
//import android.media.ThumbnailUtils
//import android.provider.MediaStore
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import android.widget.Button
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.google.android.material.appbar.AppBarLayout
//import com.google.android.material.appbar.CollapsingToolbarLayout
//import org.tensorflow.lite.DataType
//import org.tensorflow.lite.schema.Model
//import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
//import java.io.IOException
//import java.nio.ByteBuffer
//import java.nio.ByteOrder
//
//
//class HomeFragment : Fragment() {
//
//    private lateinit var binding: FragmentHomeBinding
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentHomeBinding.inflate(layoutInflater)
//        setStatusBarColor()
//        setAllCategories()
//        navigatingToSearchFragment()
//
//        return binding.root
//    }
//
//
//
//
//    fun onCategoryIconClicked(category: Category) {
//        val bundle = Bundle()
//        bundle.putString("category", category.title)
//        findNavController().navigate(R.id.action_homeFragment_to_categoryFragment , bundle)
//    }
//
//    private fun navigatingToSearchFragment() {
//        binding.searchCv.setOnClickListener {
//            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
//        }
//    }
//
//    private fun setAllCategories() {
//        val categoryList = ArrayList<Category>()
//
//        for(i in 0 until Constants.allProductsCategoryIcon.size){
//            categoryList.add(Category(Constants.allProductsCategory[i] , Constants.allProductsCategoryIcon[i]))
//        }
//        binding.rvCategories.adapter = AdapterCategory(categoryList, ::onCategoryIconClicked)
//    }
//
//    private fun setStatusBarColor(){
//        activity?.window?.apply {
//            val statusBarColors = ContextCompat.getColor(requireContext(), R.color.green)
//            statusBarColor = statusBarColors
//
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//            }
//        }
//    }
//}