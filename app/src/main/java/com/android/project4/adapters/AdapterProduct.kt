package com.android.project4.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.project4.FilteringProducts
import com.android.project4.databinding.ItemViewProductBinding
import com.android.project4.models.Product
import com.denzcoskun.imageslider.models.SlideModel

class AdapterProduct() : RecyclerView.Adapter<AdapterProduct.ProductViewHolder>() ,Filterable {

    class ProductViewHolder (val binding: ItemViewProductBinding) :ViewHolder(binding.root) {


    }

    val diffutil = object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.productRandomId == newItem.productRandomId
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    val differ= AsyncListDiffer(this, diffutil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(ItemViewProductBinding.inflate(LayoutInflater.from(parent.context) , parent , false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
         val product= differ.currentList[position]

        holder.binding.apply {
            val imageList = ArrayList<SlideModel>()

            val productImage = product.productImageUris

            for ( i in 0 until productImage?.size!!){
                imageList.add(SlideModel(product.productImageUris!![i].toString()))
            }
            ivImageSlider.setImageList(imageList)

            tvProductTitle.text=product.productTitle
            etScientificName.text=product.productScientificName
            etSpecies.text=product.productSpecies
            etFamily.text=product.productFamily
            etDescription.text=product.productDescription
            etLight.text=product.productLight
            etWater.text=product.productWater
            etSoil.text=product.productSoil
            etToxicity.text=product.productToxicity
            etPests.text=product.productPests
            etFact1.text=product.productFact1
            etFact2.text=product.productFact2


            //tvProductPrice.text = "₹" + product.productPrice
        }

    }

    private val filter: FilteringProducts? =null
    var originalList =ArrayList<Product>()
    override fun getFilter(): Filter {
        if(filter == null) return FilteringProducts(this,originalList)
        return filter
    }
}