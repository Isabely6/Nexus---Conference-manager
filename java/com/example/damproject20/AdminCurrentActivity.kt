package com.example.damproject20

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminCurrentActivity : AppCompatActivity() {

    private lateinit var topAdminNavMenu: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_current)

        topAdminNavMenu = findViewById(R.id.topAdminNavMenu)

        // Set default fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, FragmentAdminSessions()).commit()
        }

        topAdminNavMenu.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null

            when (item.itemId) {
                R.id.navigation_session -> selectedFragment = FragmentAdminSessions()
                R.id.navigation_comments -> selectedFragment = FragmentAdminComments()
                R.id.navigation_editsession -> selectedFragment = FragmentAdminEditSession()
                R.id.navigation_logout -> finish()
            }

            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit()
            }

            true
        }
    }
}
