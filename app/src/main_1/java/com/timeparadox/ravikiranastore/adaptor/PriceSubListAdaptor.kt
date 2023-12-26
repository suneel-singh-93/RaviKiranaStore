package com.timeparadox.ravikiranastore.adaptor
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timeparadox.ravikiranastore.databinding.ItemPricingByWeightBinding
import com.timeparadox.ravikiranastore.databinding.ItemSubPriceListBinding
import com.timeparadox.ravikiranastore.fragment.data.model.PriceList


class PriceSubListAdaptor(val context: Context) :
    RecyclerView.Adapter<PriceSubListAdaptor.Holder>() {
    var priceList: ArrayList<PriceList> = arrayListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemPricingByWeightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ) }
    override fun getItemCount(): Int {
        return priceList.size
    }
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindView(priceList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAll(priceList: ArrayList<PriceList>) {
        this.priceList.addAll(priceList)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        this.priceList.clear()
        notifyDataSetChanged()
    }

    class Holder(private val binding: ItemPricingByWeightBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(priceList: PriceList) {
            binding.amount.text = priceList.price
            binding.typeAmount.text = priceList.weight
            binding.type.text = priceList.type
        } }

}
