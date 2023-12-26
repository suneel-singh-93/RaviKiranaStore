package com.timeparadox.ravikiranastore.adaptor

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.timeparadox.ravikiranastore.MainActivity
import com.timeparadox.ravikiranastore.databinding.ItemProductListSearchBinding
import com.timeparadox.ravikiranastore.fragment.HomeFragment
import com.timeparadox.ravikiranastore.fragment.ProductDetailsFragment
import com.timeparadox.ravikiranastore.model.ProductListModel
import java.util.Locale

class ProductListAdaptor(val context: Context) :
    RecyclerView.Adapter<ProductListAdaptor.Holder>() , Filterable {
    val productList: ArrayList<ProductListModel> = arrayListOf()
    var filteredItems = ArrayList<ProductListModel>()
    init {
        filteredItems = productList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemProductListSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindView(productList[position])
    }

    override fun getItemCount(): Int {
        return filteredItems.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAllProduct(productCodeList:ArrayList<ProductListModel>) {
        this.productList.addAll(productCodeList)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear()
    {
        this.productList.clear()
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filteredItems = productList
                } else {
                    val resultList = ArrayList<ProductListModel>()
                    for (row in productList) {
                        if (row.productCode.lowercase(Locale.ENGLISH)
                                .contains(charSearch.lowercase(Locale.ROOT)) ||
                            row.productName.lowercase(Locale.ENGLISH)
                                .contains(charSearch.lowercase(Locale.ROOT))||
                            row.productPrice.lowercase(Locale.ENGLISH)
                                .contains(charSearch.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    filteredItems = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredItems
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                Log.d("TAG", "publishResults: " + results!!.values.toString())
                filteredItems = results?.values as ArrayList<ProductListModel>
                notifyDataSetChanged()
            }

        }
    }

    inner class Holder(private val binding: ItemProductListSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(productListModel: ProductListModel) {
            binding.productCode.text = productListModel.productCode
            binding.productMoreDetails.setOnClickListener {
                (context as MainActivity).loadCurrentFragment(ProductDetailsFragment(),"")
            }
        }
    }
}
