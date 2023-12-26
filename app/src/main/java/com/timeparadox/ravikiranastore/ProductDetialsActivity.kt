package com.timeparadox.ravikiranastore

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.google.gson.GsonBuilder
import com.timeparadox.ravikiranastore.adaptor.PriceSubListAdaptor
import com.timeparadox.ravikiranastore.databinding.ActivityProductDetialsBinding
import com.timeparadox.ravikiranastore.model.PriceList
import com.timeparadox.ravikiranastore.model.ProductListModel
import java.io.Serializable
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class ProductDetialsActivity : AppCompatActivity() {
    lateinit var productDetails :ProductListModel
    lateinit var binding:ActivityProductDetialsBinding
    lateinit var priceSubListAdaptor: PriceSubListAdaptor
    var tempAmount: ArrayList<PriceList> = arrayListOf()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetialsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("productDetails"))
        {
            productDetails = intent.getSerializable_("productDetails",ProductListModel::class.java) as ProductListModel

        }
        if (productDetails.productType.equals("Kg",false))
        {
            binding.pktView.visibility = View.GONE
            binding.kgView.visibility = View.VISIBLE
            binding.baseType.text = productDetails.productType+"/Gm"
        }else{
            binding.pktView.visibility = View.VISIBLE
            binding.kgView.visibility = View.GONE
            binding.baseType.text = productDetails.productType
            binding.productBasePrice.text = productDetails.productPrice+" Rs"
            binding.productName.text = productDetails.productName

            binding.baseType1.text = productDetails.productType
            binding.productBasePrice1.text = productDetails.productPrice+" Rs"
            binding.productName1.text = productDetails.productName
        }
        binding.productBasePrice.text = productDetails.productPrice+" Rs"
        binding.productName.text = productDetails.productName


        priceSubListAdaptor = PriceSubListAdaptor(this)
        binding.priceListRCV.adapter = priceSubListAdaptor
        getTemp(productDetails.productPrice.toDouble())
        if (tempAmount.size > 7) {
            priceSubListAdaptor.addAll(tempAmount)
        }else
        {
            priceSubListAdaptor.addAll(tempAmount)
        }
        binding.etPrice.doAfterTextChanged {
            if (it.toString() != "") {
                if (it.toString().toDouble()>productDetails.productPrice.toDouble())
                {
                    binding.weightResult.text = "${it.toString()} Rs. ka "+getWeight(it.toString().toDouble()).toString()+" Kg"
                }else{
                    binding.weightResult.text = "${it.toString()} Rs. ka "+getWeight(it.toString().toDouble()).toString()+" grm"
                }
            } else {
//                binding.weightResult.text = getWeight(1.0).toString()+" Rs. ka ${it.toString()} gm"
                binding.weightResult.text =  "${it.toString()} Rs. ka "+ getWeight(1.0).toString()+" grm"
            }
        }
        binding.etWeightEnter.doAfterTextChanged {
            if (it.toString() != "") {
                if (it.toString().contains(".")) {
                    var newWeight = (it.toString().toDouble() * 1000)
                    binding.priceResult.text = "${it.toString()} Kg "+getPrice(newWeight.toLong()).toString()+" Rs. ka "
                    Log.d("TAG", "if  " + newWeight.toLong())
                } else {
                    if (it.toString().toLong()>1000)
                    {
                        binding.priceResult.text = "${it.toString()} Kg "+getPrice(it.toString().toLong()).toString()+" Rs. ka"
                        Log.d("TAG", "Else: " + it.toString().toLong())
                    }else
                    {
                        binding.priceResult.text = "${it.toString()} gram "+getPrice(it.toString().toLong()).toString()+" Rs. ka"
                        Log.d("TAG", "Else: " + it.toString().toLong())
                    }
                }
            } else {
                binding.priceResult.text = "${it.toString()} gram "+getPrice(1).toString()+" Rs. ka"
            }
        }
    }

   private fun getTemp(baseAmount: Double) {
       var amt: Double = 1.0
       Log.d("TAG", "getTemp: "+baseAmount)
       var weight: Int = 0
       var type = "gram"
       (0 until 8).forEach {
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
       weight_ = round((priceEnter * 1000) / productDetails.productPrice.toDouble())
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
       price_ = (weightEnter * productDetails.productPrice.toDouble() / 1000)
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
   }
    private fun <T : Serializable?> Intent.getSerializable_(key: String, m_class: Class<T>): T {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            this.getSerializableExtra(key, m_class)!!
        else
            this.getSerializableExtra(key) as T
    }

    private fun printResponse(logKeyword: String?, response: Any?) {
        Log.d(
            logKeyword,
            "Response:- ${GsonBuilder().setPrettyPrinting().create().toJson(response).trimIndent()}"
        )
    }
}