package com.example.stuplan.UI

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.stuplan.databinding.FragmentSettingsBinding
import com.example.stuplan.sqlite.DatabaseHelper

class SettingsFragment : Fragment() {

    private lateinit var binding : FragmentSettingsBinding
    private lateinit var dbHelper : DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())

        if (binding.switchSettingsNotification.isEnabled){

        }else{

        }

        binding.btnClearSettings.setOnClickListener {
            dbHelper.clearAllData()
            Toast.makeText(requireContext(), "All data cleared.",Toast.LENGTH_SHORT).show()
        }
    }





}