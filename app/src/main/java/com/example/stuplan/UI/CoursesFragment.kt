package com.example.stuplan.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.stuplan.R
import com.example.stuplan.databinding.FragmentCoursesBinding


class CoursesFragment : Fragment() {

    private lateinit var binding: FragmentCoursesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCoursesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarCourses.inflateMenu(R.menu.course_frag_menu)

        binding.toolbarCourses.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add_course_menu_item -> {
                    val addCourseBottmSheet = AddCourseFragment()
                    addCourseBottmSheet.show(childFragmentManager,"")
                    true
                }
                R.id.search_course_menu_item -> {
                    true
                }
                else -> false
            }
        }


    }
}