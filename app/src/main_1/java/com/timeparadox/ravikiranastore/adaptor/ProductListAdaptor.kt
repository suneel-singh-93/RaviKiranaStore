package com.timeparadox.ravikiranastore.adaptor

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.timeparadox.ravikiranastore.MainActivity
import com.timeparadox.ravikiranastore.ProductDetialsActivity
import com.timeparadox.ravikiranastore.databinding.ItemProductListSearchBinding
import com.timeparadox.ravikiranastore.fragment.ProductDetailsFragment
import com.timeparadox.ravikiranastore.model.ProductListModel
import java.util.Locale


class ProductListAdaptor(val context: Context) :
    RecyclerView.Adapter<ProductListAdaptor.Holder>(), Filterable {
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
        holder.bindView(filteredItems[position])
    }

    override fun getItemCount(): Int {
        return filteredItems.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAllProduct(productCodeList: ArrayList<ProductListModel>) {
        this.productList.addAll(productCodeList)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
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
                                .contains(charSearch.lowercase(Locale.ROOT)) ||
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
                filteredItems = results.values as ArrayList<ProductListModel>
                notifyDataSetChanged()
            }
        }
    }

    inner class Holder(private val binding: ItemProductListSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindView(plist: ProductListModel) {
            binding.productCode.text = plist.productCode
            binding.productName.text = plist.productName
            binding.productPrice.text = "${plist.productPrice}Rs/${plist.productType}".trim()
            binding.productUnit.text = "${plist.productType} ="
            Log.d("TAG", "bindView: "+plist.newUpdateDate)
            itemView.setOnClickListener {
                context.startActivity(Intent(context,ProductDetialsActivity::class.java)
                    .putExtra("productDetails",plist))
            }
            val separated = separateDayAndTime(plist.newUpdateDate)
            if (separated != null) {
                binding.proUpdateDate.text = separated.first // date
                binding.proUpdateTime.text = separated.second // dayTime
                Log.d("Soni", "bindView: "+separated.second)
            } else {
                println("Invalid input format")
            }

        }
    }
  /*  fun timeSchedule() {
        val bundle = Bundle()
        bundle.putString("param1", "bookDetails")
        val timeSheduleBinding = TimeScheduleFragment.newInstance("bookDetails", "1")
        timeSheduleBinding.arguments = bundle
        timeSheduleBinding.show(supportFragmentManager, timeSheduleBinding.tag)

    }*/
    fun separateDayAndTime(dateTimeString: String): Pair<String, String>? {
        // Split the string by space
        Log.d("Soni", "separateDayAndTime: $dateTimeString")
        val parts = dateTimeString.split(" ")
        // Ensure that there are enough parts
        if (parts.size != 4) {
            return null
        }
        val date= parts[1].replace(":","")

        val day ="${parts[0]} ${parts[2]} "+parts[3].uppercase(Locale.ENGLISH).replace(":","")  // Combine the date and time
        return Pair(date, day)
    }
    fun removeLastColon(input: String): String {
        val lastIndex = input.lastIndexOf(":")

        if (lastIndex != -1) {
            // Check if the last character is a colon
            if (input[lastIndex] == ':') {
                // Remove the last colon
                return input.substring(0, lastIndex) + input.substring(lastIndex + 1)
            }
        }
        Log.d("Soni", "removeLastColon: "+input)
        return input
    }
}

