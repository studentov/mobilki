package com.example.spineguard

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.spineguard.ui.exercises.ExercisesFragment
import com.example.spineguard.ui.home.HomeFragment
import com.example.spineguard.ui.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavView = findViewById(R.id.bottomNavView)

        // Открываем главную только при первом запуске Activity
        if (savedInstanceState == null) {
            openFragment(HomeFragment())
            bottomNavView.selectedItemId = R.id.menu_home
        }

        bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    openFragment(HomeFragment())
                    true
                }
                R.id.menu_exercises -> {
                    openFragment(ExercisesFragment())
                    true
                }
                R.id.menu_profile -> {
                    openFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }

        // Новый обработчик кнопки "Назад" (вместо onBackPressed)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fm = supportFragmentManager
                if (fm.backStackEntryCount > 0) {
                    fm.popBackStack()
                } else {
                    // стандартное поведение назад (закрыть Activity)
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, fragment)
            .commit()
    }
}


