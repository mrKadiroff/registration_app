package com.example.registration.fragments

import android.Manifest
import android.app.AlertDialog
import android.os.Bundle
import android.telephony.SmsManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.registration.R
import com.example.registration.databinding.FragmentSmsBinding
import com.example.registration.models.RegisterModel
import com.github.florent37.runtimepermission.kotlin.askPermission

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SmsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SmsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    lateinit var binding: FragmentSmsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSmsBinding.inflate(layoutInflater,container,false)

        val kontaktt = arguments?.getSerializable("kontakt") as RegisterModel

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.namee.text = ("${kontaktt.ism}  ${kontaktt.telefon}")

        binding.send.setOnClickListener {
            askPermission(Manifest.permission.SEND_SMS){
                //all permissions already granted or just granted
                var sms =binding.smss.text.toString().trim()
                var obj= SmsManager.getDefault()
                obj.sendTextMessage(kontaktt.telefon,null,sms,null,null)
                findNavController().popBackStack()
                Toast.makeText(binding.root.context, "Sms Muvaffaqiyatli jo'natildi!!!", Toast.LENGTH_SHORT).show()

            }.onDeclined { e ->
                if (e.hasDenied()) {


                    AlertDialog.Builder(binding.root.context)
                        .setMessage("Please accept our permissions")
                        .setPositiveButton("yes") { dialog, which ->
                            e.askAgain();
                        } //ask again
                        .setNegativeButton("no") { dialog, which ->
                            dialog.dismiss();
                        }
                        .show();
                }

                if(e.hasForeverDenied()) {

                    // you need to open setting manually if you really need it
                    e.goToSettings();
                }
            }





        }



        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SmsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SmsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}