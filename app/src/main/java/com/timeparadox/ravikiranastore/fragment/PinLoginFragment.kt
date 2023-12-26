package com.timeparadox.ravikiranastore.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.goodiebag.pinview.Pinview
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.timeparadox.ravikiranastore.MainActivity
import com.timeparadox.ravikiranastore.R
import com.timeparadox.ravikiranastore.databinding.FragmentPinLoginBinding


class PinLoginFragment : BottomSheetDialogFragment() {
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentPinLoginBinding
    var api_date = ""
    var dateAndTime: DateAndTime? = null
    lateinit var readPin:String
//    private val dateTimeFragment: SwitchDateTimeDialogFragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       /* arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }

        bundle = arguments
        if (bundle != null) {
            userPostBean = bundle.getSerializable("postData") as UserPostBean
            isUserPost = bundle.getBoolean("isUserPost")
        }
        val gson = Gson()
        val json: String = Prefs.getString(Constants.SP_USER_DETALIS, "")
        obj = gson.fromJson(json, UserDetail::class.java)*/

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)


/*
        arguments?.getString("param1")?.let {
            param1 = it
            try {
                dateAndTime = (context as DateAndTime)
            } catch (e: ClassCastException) {
                throw ClassCastException(activity.toString() + " must implement onSomeEventListener")
            }

        }
*/

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPinLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

  /*  companion object {
        fun newInstance(param1: String, param2: String) = TimeScheduleFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }
*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pin.setPinViewEventListener(object : Pinview.PinViewEventListener {
            override fun onDataEntered(pinview: Pinview?, fromUser: Boolean) {
                Toast.makeText(requireContext(), pinview!!.value, Toast.LENGTH_SHORT).show()
                readPin=pinview!!.value
            }
        })
        binding.submit.setOnClickListener {
            if (readPin.equals("9321"))
            {
                binding.wrongPin.visibility = View.GONE
                val bundle = Bundle()
                val editFragment = AddProductFragment()
                bundle.putString("UiType","addType")
                editFragment.arguments = bundle
                ((context as MainActivity)).loadCurrentFragment(AddProductFragment(), "addProduct")
                binding.pin.clearValue()
                dialog!!.setCancelable(false)
                dialog!!.dismiss()

            }else{
             binding.wrongPin.visibility = View.VISIBLE
             binding.pin.clearValue()
             dialog!!.setCancelable(true)
            }
        }
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme) //set your created theme here
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val binding = FragmentPinLoginBinding.inflate(LayoutInflater.from(context), null, false)
        dialog.setContentView(binding.root)
        val v: View = binding.root

        (v.parent as View).setBackgroundColor(resources.getColor(android.R.color.transparent))
    }

    override fun onDestroy() {
        dateAndTime = null
        super.onDestroy()
        (this as Fragment?)?.let {
            requireFragmentManager().beginTransaction().remove(it).commitAllowingStateLoss()
        }
    }

    open interface DateAndTime {
        fun getDataAndTime(
            startTime: String,
            endTime: String,
            date: String,
            api_date: String,
            apiStartTime: String,
            apiEndTime: String
        )
    }
}
