package com.timeparadox.ravikiranastore.fragment.ui.login

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
import com.timeparadox.ravikiranastore.MainActivity
import com.timeparadox.ravikiranastore.databinding.FragmentAddProductBinding
import com.timeparadox.ravikiranastore.fragment.HomeFragment
import com.timeparadox.ravikiranastore.model.ProductListModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


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
    var uiType = ""
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
//        getFireBaseData()
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)/* when (intent.getStringExtra("UiType")) {
             "addData" -> {
                 uiType = intent.getStringExtra("UiType") as String
             }
             "editData" -> {
                 if (intent.hasExtra("proList")) {
                     uiType = intent.getStringExtra("UiType") as String
                     proList = intent.getSerializable("proList", ProList::class.java)
                     binding.txId.text = "ID: " + proList.id
                     binding.etId.setText(proList.id)
                     binding.etName.setText(proList.name)
                     binding.etPrice.setText(proList.amount)
                     id_code = proList.id
                 }
             }
         }*/

        val aa: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), R.layout.simple_spinner_item, unitList)
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = aa
        productType = binding.spinner.setSelection(0).toString()
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View, position: Int, id: Long
            ) {
                productType = parent.getItemAtPosition(position).toString()
                Log.d("TAG", "type====> " + binding.spinner.selectedItem.toString())
                Log.d("TAG", "productType====> $productType")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
//        spinner_gender.getSelectedItem().toString()
//        spinner_gender.setSelection(2)


        binding.productCode.doAfterTextChanged {
            id_code = it.toString()
        }
        binding.productName.doAfterTextChanged {
            proNameEnter = it.toString()
        }

        binding.productUpdate.setOnClickListener {
            getDate()
            if (uiType == "editData") {
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
                            " Product_Id  is not match+${code_check}",
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
                    document_id = document.getString("productCode") ?: ""
                    code_check = document_id
                    name = document.getString("productName") ?: ""
                    pro_name = name
                    Log.d(TAG, "document_id: " + document_id)
                    Log.d(TAG, "document_id: " + id_code)
                    if (id_code.trim().equals(code_check.trim(), false)) {
                        document_id = document.id
                        checkValue = true
                        break
                    } else {
                        checkValue = false
                    }
                } catch (fireBase: FirebaseFirestoreException) {
                    Log.e(TAG, "error in firebase : ${fireBase.message}")
                    fireBase.printStackTrace()
                }
            }

        } else {
            checkValue = false
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
}