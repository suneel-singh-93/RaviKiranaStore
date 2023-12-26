package com.timeparadox.ravikiranastore.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.firestore.ktx.memoryCacheSettings
import com.google.firebase.firestore.ktx.persistentCacheSettings
import com.google.firebase.ktx.Firebase
import com.timeparadox.ravikiranastore.MainActivity
import com.timeparadox.ravikiranastore.adaptor.ProductListAdaptor
import com.timeparadox.ravikiranastore.databinding.FragmentHomeBinding
import com.timeparadox.ravikiranastore.fragment.ui.login.AddProductFragment
import com.timeparadox.ravikiranastore.model.ProductListModel
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    var productList: ArrayList<ProductListModel> = arrayListOf()
    lateinit var db: FirebaseFirestore // Firebase init required
    private val TAG = "Firebase"
    lateinit var productListAdaptor: ProductListAdaptor
    private var _binding: FragmentHomeBinding? =null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        FirebaseApp.initializeApp(requireContext())
        db = Firebase.firestore
        val settings = firestoreSettings {
            setLocalCacheSettings(memoryCacheSettings {})
            setLocalCacheSettings(persistentCacheSettings {})
        }
        // Firebase init required
        db.firestoreSettings = settings
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productListAdaptor = ProductListAdaptor(requireContext())
        binding.productListRCV.adapter = productListAdaptor
        binding.searchBox.doAfterTextChanged { productListAdaptor.filter.filter(it.toString()) }
        binding.addProduct.setOnClickListener {
            ((context as MainActivity)).loadCurrentFragment(AddProductFragment(), "addProduct")
        }

    }

    override fun onResume() {
        super.onResume()
        getFireBaseData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding =null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = HomeFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }

    fun upperCaseWords(sentence: String?): String {
        val matcher: Matcher = Pattern.compile("(?:^| )[^a-z]*[a-z]").matcher(sentence!!)
        val result = StringBuffer()
        while (matcher.find()) {
            matcher.appendReplacement(result, matcher.group().uppercase(Locale.getDefault()))
        }
        return matcher.appendTail(result).toString()
    }

    private fun getFireBaseData() {
        productListAdaptor.productList.clear()
        productList.clear()
        db.collection("productDetails").get().addOnSuccessListener { result ->
            for (document in result) {
                Log.d(TAG, "getFireBaseData:${document.id} \n  ${document.data}")
                try {
                    val productId: String = document.getString("productId") ?: ""
                    val productCode = document.getString("productCode") ?: ""
                    val productName: String = document.getString("productName") ?: ""
                    val productPrice = document.getString("productPrice") ?: ""
                    val whereIdPlace: String = document.getString("whereIdPlace") ?: ""
                    val productQuantity: String = document.getString("productQuantity") ?: ""
                    val lastUpdateDate: String = document.getString("lastUpdateDate") ?: ""
                    val newUpdateDate: String = document.getString("newUpdateDate") ?: ""
                    val productType: String = document.getString("productType") ?: ""
                    val productImage: String = document.getString("productImage") ?: ""
                    val proList = ProductListModel(
                        productId,
                        productCode,
                        productName,
                        productPrice,
                        productType,
                        whereIdPlace,
                        productQuantity,
                        lastUpdateDate,
                        newUpdateDate,
                        productImage
                    )
                    productList.add(proList)
                    productListAdaptor.clear()
                    productListAdaptor.addAllProduct(productList)

                } catch (fireBase: FirebaseFirestoreException) {
                    Log.e(TAG, "getFireBaseData: ${fireBase.message}")
                    fireBase.printStackTrace()
                }
            }
        }.addOnFailureListener {
                Log.w(TAG, "Error getting documents. " + it.message)
            }
    }

}