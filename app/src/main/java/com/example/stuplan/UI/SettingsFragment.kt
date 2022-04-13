package com.example.stuplan.UI

import android.content.Intent
import android.net.Uri
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

        binding.switchSettingsNotification.setOnCheckedChangeListener { switchView, isChecked ->
            if (isChecked) {
                // The switch enabled
                Toast.makeText(requireContext(), "Notification turned on",Toast.LENGTH_SHORT).show()

            } else {
                // The switch disabled
                Toast.makeText(requireContext(), "Notification turned off",Toast.LENGTH_SHORT).show()

            }
        }



        binding.btnClearSettings.setOnClickListener {
            dbHelper.clearAllData()
            Toast.makeText(requireContext(), "All data cleared.",Toast.LENGTH_SHORT).show()
        }

        binding.tvAboutStuplanSettings.setOnClickListener {
            val infoFragmentBottomSheet = AboutBottomSheet()
            infoFragmentBottomSheet.show(childFragmentManager,"")
        }

        binding.tvContactStuplanSettings.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:stuplanappmdev@gmail.com")
            }
            startActivity(Intent.createChooser(emailIntent, "Send feedback to us"))
        }
    }





}