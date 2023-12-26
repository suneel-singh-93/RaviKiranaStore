package com.timeparadox.ravikiranastore

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
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
    var doubleBackToExitPressedOnce = false
    lateinit var db:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val currentFragment: Fragment =
            this.supportFragmentManager.findFragmentById(R.id.container)!!
        if (currentFragment.javaClass.simpleName.equals("HomeFragment", ignoreCase = true)) {
            if (supportFragmentManager.backStackEntryCount == 0) {
                onBackpressed(this)
            } else {
                super.onBackPressed()
            }
        } else {
            loadCurrentFragment(HomeFragment(), "")
        }
    }

    fun onBackpressed(activity: Activity) {
        if (doubleBackToExitPressedOnce) {
            doubleBackToExitPressedOnce = false
            activity.finish()
            return
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(activity, "Press again to exit", Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)
    }
}