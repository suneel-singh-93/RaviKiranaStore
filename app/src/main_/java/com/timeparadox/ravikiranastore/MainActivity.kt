package com.timeparadox.ravikiranastore

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.firestore.ktx.memoryCacheSettings
import com.google.firebase.firestore.ktx.persistentCacheSettings
import com.google.firebase.ktx.Firebase
import com.timeparadox.ravikiranastore.databinding.ActivityMainBinding
import com.timeparadox.ravikiranastore.fragment.HomeFragment
import com.timeparadox.ravikiranastore.model.ProductListModel

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    lateinit var db:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db=Firebase.firestore
        if (savedInstanceState == null) {
            loadCurrentFragment(HomeFragment(), "")
        }
        val settings = firestoreSettings {
            setLocalCacheSettings(memoryCacheSettings {})
            setLocalCacheSettings(persistentCacheSettings {})
        }
        // Firebase init required
        db.firestoreSettings = settings

      /*  if (intent.hasExtra("UIType")) {
            uiType = intent.getStringExtra("UIType").toString()
            when (uiType) {
                "paymentMethod" -> {
                    loadCurrentFragment(PaymentMethodFragment(), "")
                }
                "helpRevalue" -> {
                    loadCurrentFragment(HelpResolutionFragment(), "")
                }
                "miscellaneous" -> {
                    loadCurrentFragment(MiscellaneousFragment(), "")
                }
                "job_darft" -> {
                    loadCurrentFragment(JobDraftFragment(), "")
                }
                "bookingEdit" -> {
                    loadCurrentFragment(BookingFragment(), "")
                }

            }
        } else {
            if (savedInstanceState == null) {
                loadCurrentFragment(HomeFragment(), "")
            }
        }*/



    }



    open fun loadCurrentFragment(fragment: Fragment, src: String) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }
}