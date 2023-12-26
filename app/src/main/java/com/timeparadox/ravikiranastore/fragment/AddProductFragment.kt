package com.timeparadox.ravikiranastore.fragment

import android.R
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.firestore.ktx.memoryCacheSettings
import com.google.firebase.firestore.ktx.persistentCacheSettings
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import com.timeparadox.ravikiranastore.MainActivity
import com.timeparadox.ravikiranastore.databinding.FragmentAddProductBinding
import com.timeparadox.ravikiranastore.model.ProductListModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class AddProductFragment : Fragment() {

    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!
    var openingTime = ""
    var closeTime = ""
    var currentPosition = -1
    lateinit var proList: ProductListModel
    var db = Firebase.firestore // Firebase init required
    private val TAG = "Firebase"
    var document_id = ""
    var id_code = ""
    var name: String = ""
    var uiType = "addType"
    var code_check = ""
    var pro_name = ""
    var proNameEnter = ""
    private var productType = ""
    lateinit var documentRef: DocumentReference
    lateinit var resultAll: QuerySnapshot
    val unitList = arrayOf("Kg", "Ltr", "Pkt")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        val settings = firestoreSettings {
            setLocalCacheSettings(memoryCacheSettings {})
            setLocalCacheSettings(persistentCacheSettings {})
        }
        db.firestoreSettings = settings

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val aa: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), R.layout.simple_spinner_item, unitList)
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = aa

        when (arguments?.getString("UiType")) {
            "addType" -> {
                productType = binding.spinner.setSelection(0).toString()
            }

            "editType" -> {
                uiType = arguments?.getString("UiType")!!
                binding.productName.setText(arguments?.getString("name"))
                binding.productPrice.setText(arguments?.getString("price"))
                if (arguments?.getString("type")!!.lowercase().equals("kg", false)) {
                    binding.spinner.setSelection(0).toString()
                } else if (arguments?.getString("type")!!.lowercase().equals("ltr", false)) {
                    binding.spinner.setSelection(1).toString()
                } else {
                    binding.spinner.setSelection(2).toString()
                }

                productType = arguments?.getString("type")!!
                binding.dateAndTime.text = arguments?.getString("lateDate")
                binding.whereIsPlace.setText(arguments?.getString("place"))
                binding.productQuantity.setText(arguments?.getString("quantity"))
                binding.productCode.setText(arguments?.getString("code"))
                id_code = arguments?.getString("code")!!.trim()
                binding.productCode.isEnabled = false
            }
        }


        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View, position: Int, id: Long
            ) {
                productType = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.productCode.doAfterTextChanged {
            id_code = it.toString()
        }
        binding.productName.doAfterTextChanged {
            proNameEnter = it.toString()
//            productUpdate()
        }

        binding.productUpdate.setOnClickListener {
            getDate()
            if (uiType == "editType") {
                if (document_id != "") {
                    if (productUpdate()) {
                        if (isEmptyValidation()) {
                            updateProduct()
                        } else {
                            Toast.makeText(
                                requireContext(), "in update true condition", Toast.LENGTH_SHORT
                            ).show()
                        }

                    } else {
                        Toast.makeText(
                            requireContext(),
                            " Product_Id  is match+${code_check}",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                } else {
                    Toast.makeText(requireContext(), "Document Id missing ", Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                if (productNotAvailable()) {
                    Toast.makeText(
                        requireContext(),
                        "This product Id and Name is already use  product +${code_check} ${pro_name}",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (isEmptyValidation()) {
                        addProduct()
                    }

                }
            }
        }
    }


    private fun addProduct() {
        val productDetails = hashMapOf(
            "productCode" to id_code,
            "productName" to binding.productName.text.toString(),
            "productPrice" to binding.productPrice.text.toString(),
            "productType" to productType,
            "whereIsPlace" to binding.whereIsPlace.text.toString(),
            "productUnit" to binding.productQuantity.text.toString(),
            "newUpdateDate" to binding.dateAndTime.text.toString(),
            "lastUpdateDate" to binding.dateAndTime.text.toString(),
        )
        db.collection("productDetails").add(productDetails).addOnSuccessListener {
            ((context) as MainActivity).loadCurrentFragment(HomeFragment(), "addProduct")
        }.addOnFailureListener { e ->
            Toast.makeText(
                requireContext(), "Product not added in db  " + e.message, Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun getFireBaseData() {
        db.collection("productDetails").get().addOnSuccessListener { result ->
            resultAll = result
            for (document in resultAll) {
                try {
                    document_id = document.getString("productCode") ?: ""
                    code_check = document_id
                    name = document.getString("productName") ?: ""
                    pro_name = name
                    Log.d(TAG, "document_id: " + document_id)
                    if (id_code.trim().equals(code_check.trim(), false)) {
                        document_id = document.id
                        break
                    }
                } catch (fireBase: FirebaseFirestoreException) {
                    Log.e(TAG, "Error in firebase  : ${fireBase.message}")
                    fireBase.printStackTrace()
                }
            }
        }.addOnFailureListener {
            Log.w(TAG, "Error getting documents. " + it.message)
        }
    }

    private fun updateProduct() {
        Log.d(TAG, "updateUser: ${document_id}")
        documentRef = db.collection("productDetails").document(document_id)
        val updates = hashMapOf<String, Any>(
            "productName" to binding.productName.text.toString(),
            "productPrice" to binding.productPrice.text.toString(),
            "productType" to productType,
            "whereIsPlace" to binding.whereIsPlace.text.toString(),
            "productUnit" to binding.productQuantity.text.toString(),
            "newUpdateDate" to binding.dateAndTime.text.toString(),
            "lastUpdateDate" to binding.dateAndTime.text.toString(),
        )
        // Update the document
        documentRef.update(updates).addOnSuccessListener {
            // Update successful
            ((context) as MainActivity).loadCurrentFragment(HomeFragment(), "editProduct")
        }.addOnFailureListener { fireBase ->
            Log.e(TAG, "getFireBaseData: ${fireBase.message}")
            fireBase.printStackTrace()
        }
    }

    private fun productUpdate(): Boolean {
        var checkValue = true
        if (!resultAll.isEmpty) {
            for (document in resultAll) {
                try {
                    code_check = document.getString("productCode") ?: ""
//                    code_check = document_id
                    name = document.getString("productName") ?: ""
                    pro_name = name
                    if (id_code.trim()!=code_check.trim()) {
                        if ( proNameEnter.lowercase().trim()==pro_name.lowercase().trim())
                        {
                            document_id = document.id
                            checkValue = false
                            break
                        }
                    }
                    else{
                        if (id_code==code_check)
                        {
                            document_id = document.id
                            Log.d(TAG, "document_id: "+document_id)
                            Log.d(TAG, "productName: "+binding.productName.text)
                            checkValue = true
                        }
                    }
                } catch (fireBase: FirebaseFirestoreException) {
                    Log.e(TAG, "error in firebase : ${fireBase.message}")
                    fireBase.printStackTrace()
                }
            }
        }
        return checkValue
    }


    private fun productNotAvailable(): Boolean {
        var checkValue = true
        if (!resultAll.isEmpty) {
            for (document in resultAll) {
                try {
                    document_id = document.getString("productCode") ?: ""
                    code_check = document_id
                    name = document.getString("productName") ?: ""
                    pro_name = name
                    Log.d(TAG, "document_id: " + document_id)
                    if (id_code.trim().equals(code_check.trim(), false)) {
                        checkValue = true
                        break
                    } else if (proNameEnter.lowercase().trim()
                            .equals(pro_name.lowercase().trim(), false)
                    ) {
                        checkValue = true
                        break
                    } else {
                        checkValue = false
                    }
                } catch (fireBase: FirebaseFirestoreException) {
                    Log.e(TAG, "Error in firebase : ${fireBase.message}")
                    fireBase.printStackTrace()
                }
            }

        } else {
            checkValue = false
        }
        return checkValue
    }


    private fun getDate() {
        val currentTime = Calendar.getInstance().time
        val dateFormat: DateFormat = SimpleDateFormat("EEEE yyyy-MM-dd h:mm a", Locale.getDefault())
        val strDate = dateFormat.format(currentTime)
        val date = getDateTimeFormat(strDate)
        binding.dateAndTime.text = date
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDateTimeFormat(date: String?): String? {
        val sdf = SimpleDateFormat("EEEE yyyy-MM-dd h:mm a", Locale.getDefault())
        var testDate: Date? = null
        try {
            testDate = sdf.parse(date)
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
        val formatter = SimpleDateFormat("EEEE yyyy-MM-dd h:mm a", Locale.getDefault())
        return formatter.format(testDate)
    }

    private fun isEmptyValidation(): Boolean {
        if (binding.productCode.text.toString().trim() == "") {
            binding.productCode.error = "Product code missing"
            return false
        } else if (binding.productName.text.toString().trim() == "") {
            binding.productName.error = "Product name missing"
            return false
        } else if (binding.productPrice.text.toString().trim() == "") {
            binding.productPrice.error = "Price missing"
            return false
        } else if (productType.trim() == "") {
            binding.productType.error = "Product type missing"
            return false
        } else if (binding.whereIsPlace.text.toString().trim() == "") {
            binding.whereIsPlace.error = "Help to find the product"
            return false
        } else if (binding.productQuantity.text.toString().trim() == "") {
            binding.productQuantity.error = ""
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        getFireBaseData()
    }


    private fun printResponse(logKeyword: String?, response: Any?) {
        Log.d(
            logKeyword,
            "Response:- ${GsonBuilder().setPrettyPrinting().create().toJson(response).trimIndent()}"
        )
    }
}