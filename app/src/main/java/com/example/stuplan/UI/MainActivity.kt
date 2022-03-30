package com.example.stuplan.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.stuplan.R
import com.example.stuplan.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val taskFragment = TasksFragment()
        val completedFragment = CompletedFragment()
        val coursesFragment = CoursesFragment()
        val settingsFragment = SettingsFragment()

        changeFragment(taskFragment)

        binding.bottomNav.setOnItemSelectedListener {menuitem->
            when(menuitem.itemId){
                R.id.tasks -> changeFragment(taskFragment)
                R.id.completed -> changeFragment(completedFragment)
                R.id.courses -> changeFragment(coursesFragment)
                R.id.settings -> changeFragment(settingsFragment)
            }
            true
        }




    }

    fun changeFragment(fragment : Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.host_fragment, fragment)
            commit()
        }
    }
}