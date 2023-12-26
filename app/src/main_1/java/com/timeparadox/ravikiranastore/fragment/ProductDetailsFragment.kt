package com.timeparadox.ravikiranastore.fragment

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.google.gson.GsonBuilder
import com.timeparadox.ravikiranastore.R
import com.timeparadox.ravikiranastore.databinding.FragmentProductDetailsBinding
import com.timeparadox.ravikiranastore.model.ProductListModel
import java.io.Serializable
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.math.roundToLong
private const val ARG_PARAM1 = "param1"
class ProductDetailsFragment : Fragment() {
    private var price: String? = null
    private var name: String? = null
    private var unit: String? = null
    var productDetails: ProductListModel? = null
    lateinit var binding:FragmentProductDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            productDetails = it.getSerializable_("productDetails",ProductListModel::class.java) as ProductListModel
            price = it.getString("price")
            name = it.getString("name")
            unit = it.getString("unit")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        printResponse("Neha",productDetails!!.productName)
     /*   val bundle = arguments
        if (bundle != null && bundle.containsKey("productDetails")) {
            // Get the value from the Bundle
            val myValue = bundle.getString("productDetails")
            Log.d("TAG", "onCreateView: "+myValue)
        }else{
            Log.d("TAG", "else conditioin ")
        }
*/
     /*   arguments?.getSerializable_("productDetails",ProductListModel::class.java)?.let {
            productDetails = it
            printResponse("FinalData  ",productDetails)
        }*/
        return binding.root
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: android.os.Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      /*  arguments?.let {
            productDetails=  it.getSerializable_("productDetails",ProductListModel::class.java)!!
            Log.d("soni", " product "+productDetails!!.productName)
        }*/


      /*
        priceSubListAdaptor = PriceSubListAdaptor(this)
        binding.priceRCV.adapter = priceSubListAdaptor
        getTemp(proList.amount.toDouble())
        printResponse("priceList :  ", tempAmount)
        if (tempAmount.size > 7) {
            priceSubListAdaptor.addAll(tempAmount)
        }
        binding.etPriceEnter.doAfterTextChanged {
            if (it.toString() != "") {
                binding.gResult.text = getWeight(it.toString().toDouble()).toString()
            } else {
                binding.gResult.text = getWeight(1.0).toString()
            }

        }
        binding.etWeightEnter.doAfterTextChanged {
            if (it.toString() != "") {
                if (it.toString().contains(".")) {
                    var newWeight = (it.toString().toDouble() * 1000)
                    print("newWight:=== " + newWeight)
                    binding.priceResult.text = getPrice(newWeight.toLong()).toString()
                    Log.d("TAG", "if  " + newWeight.toLong())
                } else {
                    binding.priceResult.text = getPrice(it.toString().toLong()).toString()
                    Log.d("TAG", "Else: " + it.toString().toLong())
                }
            } else {
                binding.priceResult.text = getPrice(1).toString()
            }
        }
        
        */
    }
    companion object {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        fun newInstance(productDetails: ProductListModel): ProductDetailsFragment {
            val fragment = ProductDetailsFragment()
            val args = Bundle()
            args.putSerializable("productDetails", productDetails)
            fragment.arguments = args
            Log.d("TAG", "newInstance: "+args.getSerializable("productDetails")as ProductListModel)
            return fragment
        }
    }

    fun newInstance(productDetails: ProductListModel): ProductDetailsFragment {
        val fragment = ProductDetailsFragment()
        val args = Bundle()
        args.putSerializable("productDetails", productDetails)
        fragment.arguments = args
        Log.d("TAG", "newInstance: "+args.getSerializable("productDetails")as ProductListModel)
        this.productDetails = args.getSerializable_("productDetails",ProductListModel::class.java)
        return fragment
    }

    /*private fun printResponse(logKeyword: String?, response: Any?) {
        Log.d(
            logKeyword,
            "Response:- ${GsonBuilder().setPrettyPrinting().create().toJson(response).trimIndent()}"
        )
    }

    private fun getTemp(baseAmount: Double) {
        var amt: Double = 1.0
        var weight: Int = 0
        var type = "g"
        (0 until 8).forEach {
            it
            when (it) {
                0 -> {
                    weight = 1000
                    amt = ((weight * baseAmount) / 1000).roundToInt().toDouble()
                    tempAmount.add(PriceList(amt.toString(), weight.toString(), type))
                }

                1 -> {
                    weight = 750
                    amt = ((weight * baseAmount) / 1000).roundToLong().toDouble()
                    tempAmount.add(PriceList(amt.toString(), weight.toString(), type))
                }

                2 -> {
                    weight = 500
                    amt = ((weight * baseAmount) / 1000).roundToLong().toDouble()
                    tempAmount.add(PriceList(amt.toString(), weight.toString(), type))
                }

                3 -> {
                    weight = 250
                    amt = ((weight * baseAmount) / 1000).roundToLong().toDouble()
                    tempAmount.add(PriceList(amt.toString(), weight.toString(), type))
                }

                4 -> {
                    weight = 200
                    amt = ((weight * baseAmount) / 1000).roundToLong().toDouble()
                    tempAmount.add(PriceList(amt.toString(), weight.toString(), type))
                }

                5 -> {
                    weight = 150
                    amt = ((weight * baseAmount) / 1000).roundToLong().toDouble()
                    tempAmount.add(PriceList(amt.toString(), weight.toString(), type))
                }

                6 -> {
                    weight = 100
                    amt = ((weight * baseAmount) / 1000).roundToLong().toDouble()
                    tempAmount.add(PriceList(amt.toString(), weight.toString(), type))
                }

                7 -> {
                    weight = 50
                    amt = ((weight * baseAmount) / 1000).roundToLong().toDouble()
                    tempAmount.add(PriceList(amt.toString(), weight.toString(), type))
                }
            }
        }
    }

    private fun getWeight(priceEnter: Double): Double {
        var weight_ = 1.0
        weight_ = round((priceEnter * 1000) / proList.amount.toDouble())
        if (weight_ > 1000.00) {
            weight_ = (weight_ / 1000)
            return weight_
        } else {
            return weight_
        }
    }

    private fun getPrice(weightEnter: Long): Double {
        var price_: Double = 1.0
        var temp_wright = 1.0
        Log.d("temp_price", "getPrice: " + weightEnter)
        price_ = (weightEnter * proList.amount.toDouble() / 1000)
        Log.d("TAG", "getPrice: " + price_)
        var newPrice = price_.toString().split(".").toTypedArray()
        Log.d("TAG", "NewPrice: " + newPrice[1].toString().first().toString())
        if (newPrice[1].toString().first().toString().toInt() >5) {
            Log.d("TAG", "v  " + price_)
//            price_ = price_ + 1.0
            Log.d("TAG", "v  " + price_)
            return round(price_)
        } else {
            return Math.rint(price_)
        }
//        return rint(price_)
    }*/
    private fun printResponse(logKeyword: String?, response: Any?) {
        Log.d(
            logKeyword,
            "Response:- ${GsonBuilder().setPrettyPrinting().create().toJson(response).trimIndent()}"
        )
    }


    private fun <T : Serializable?> Bundle.getSerializable_(key: String, m_class: Class<T>): T {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            this.getSerializable(key, m_class)!!
        else
            this.getSerializable(key) as T
    }
}