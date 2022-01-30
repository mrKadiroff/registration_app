package com.example.registration.fragments

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.registration.R
import com.example.registration.adapter.Register_adapter
import com.example.registration.databinding.FragmentUserBinding
import com.example.registration.databinding.LayoutBottomSheetBinding
import com.example.registration.db.MyDbHelper
import com.example.registration.models.RegisterModel
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.material.bottomsheet.BottomSheetDialog


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class UserFragment : Fragment() {
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
    lateinit var myDbHelper: MyDbHelper
    lateinit var registerList: ArrayList<RegisterModel>
    lateinit var binding: FragmentUserBinding
    lateinit var sheetText: TextView
    lateinit var rvAdapter: Register_adapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentUserBinding.inflate(layoutInflater)
        myDbHelper = MyDbHelper(binding.root.context)

//        val v: View = inflater.inflate(R.layout.layout_bottom_sheet, container, false)
//// declare it as a field at the top of the class
//// declare it as a field at the top of the class
//        sheetText = v.findViewById<View>(R.id.namer) as TextView




        setRv()

        return binding.root
    }

    private fun setRv() {
        registerList = myDbHelper.getAllCamera()
        rvAdapter = Register_adapter(registerList,object : Register_adapter.OnItemClickListener{
            override fun onItemClick(registerModel: RegisterModel) {


                var bottomsheet = BottomSheetDialog(requireContext())
                val v = layoutInflater.inflate(R.layout.layout_bottom_sheet, null)
                val bottom_sheetbinding =
                    LayoutBottomSheetBinding.bind(v)

                bottom_sheetbinding.namer.setText(registerModel.ism)
                bottom_sheetbinding.phone.setText(registerModel.telefon)
                bottom_sheetbinding.profile.setImageURI(Uri.parse(registerModel.rasm))

                bottom_sheetbinding.message.setOnClickListener {
                    var bundle = Bundle()
                    bundle.putSerializable("kontakt",registerModel)
                    findNavController().navigate(R.id.smsFragment, bundle)
                    bottomsheet.dismiss()
                }

                bottom_sheetbinding.call.setOnClickListener {
                    askPermission(Manifest.permission.CALL_PHONE){
                        //all permissions already granted or just granted
                        val callIntent = Intent(Intent.ACTION_CALL)
                        callIntent.data = Uri.parse("tel:" + registerModel.telefon)

                        startActivity(callIntent)
                        bottomsheet.dismiss()

                    }.onDeclined { e ->
                        if (e.hasDenied()) {


                            android.app.AlertDialog.Builder(binding.root.context)
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

                bottomsheet.setContentView(v)
                bottomsheet.show()

            }

        })
        binding.registerRv.adapter = rvAdapter
        rvAdapter.notifyDataSetChanged()
        rvAdapter.notifyItemInserted(registerList.size)

    }
//    fun onButtonClick(text: String?) {
//        sheetText.setText(text)
//    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}