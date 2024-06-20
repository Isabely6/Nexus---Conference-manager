package com.example.damproject20

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class CurrentActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_layout)

        val idUser = intent.getIntExtra("idUser", 0)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Set default fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, FragmentConference()).commit()
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null

            when (item.itemId) {
                R.id.navigation_conference -> selectedFragment = FragmentConference()
                R.id.navigation_list -> {
                    val fragment = FragmentList()
                    val bundle = Bundle()
                    bundle.putString("idUser", idUser.toString())
                    fragment.arguments = bundle
                    selectedFragment = fragment
                }
                R.id.navigation_schedule -> {
                    val fragment = FragmentSchedule()
                    val bundle = Bundle()
                    bundle.putString("idUser", idUser.toString())
                    fragment.arguments = bundle
                    selectedFragment = fragment
                }
                R.id.navigation_track -> {
                    val fragment = FragmentTracksActivity()
                    val bundle = Bundle()
                    bundle.putString("idUser", idUser.toString())
                    fragment.arguments = bundle
                    selectedFragment = fragment
                }
                R.id.navigation_user -> {
                    val fragment = FragmentUserProfile()
                    val bundle = Bundle()
                    bundle.putString("idUser", idUser.toString())
                    fragment.arguments = bundle
                    selectedFragment = fragment
                }
            }

            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit()
            }

            true
        }
    }
}

