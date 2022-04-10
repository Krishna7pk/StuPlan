package com.example.stuplan.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.stuplan.databinding.AddCourseUiBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddCourseFragment : BottomSheetDialogFragment() {
    private  lateinit var binding: AddCourseUiBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddCourseUiBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




    }
}