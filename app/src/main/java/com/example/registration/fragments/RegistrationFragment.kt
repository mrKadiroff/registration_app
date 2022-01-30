package com.example.registration.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.example.registration.BuildConfig
import com.example.registration.R
import com.example.registration.databinding.FragmentAccessBinding
import com.example.registration.databinding.FragmentRegistrationBinding
import com.example.registration.db.MyDbHelper
import com.example.registration.models.RegisterModel
import com.github.florent37.runtimepermission.kotlin.askPermission
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegistrationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistrationFragment : Fragment() {
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

    lateinit var binding: FragmentRegistrationBinding
    lateinit var photoURI: Uri
    lateinit var currentImagePath: String
    val category = arrayOf("Davlat","Uzbekistan","Kazakistan","Russia","the USA")
    lateinit var myDbHelper: MyDbHelper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegistrationBinding.inflate(layoutInflater)
        myDbHelper = MyDbHelper(binding.root.context)

        setSpinner()
        binding.addd.setOnClickListener {
            dialog()
        }

        return binding.root
    }

    private fun setSpinner() {
        val arrayAdapter = ArrayAdapter(binding.root.context,android.R.layout.simple_spinner_dropdown_item,category)
        binding.davlat.adapter = arrayAdapter
    }

    private fun dialog() {
        val picturesDialog = AlertDialog.Builder(binding.root.context)
        val dialog = picturesDialog.create()
        picturesDialog.setNegativeButton("Bekor qilish",{ dialogInterFace: DialogInterface, i: Int ->
            dialog.dismiss()
        })
        picturesDialog.setTitle("Kamera yoki Gallerreyani tanlang")
        val DialogItems = arrayOf("Galereyadan rasm tanlash", "Kamera orqali rasmga olish")
        picturesDialog.setItems(DialogItems){
                dialog, which ->
            when(which){
                0 -> permission_gallery()
                1 -> permission_camera()
            }
        }
        picturesDialog.show()
    }


    private fun permission_gallery() {
        askPermission(Manifest.permission.READ_EXTERNAL_STORAGE) {
            //all permissions already granted or just granted
            //your action
            Toast.makeText(binding.root.context, "Granted", Toast.LENGTH_SHORT).show()
            pickImageFromGallery()

        }
            .onDeclined { e ->
                if (e.hasDenied()) {
                    android.app.AlertDialog.Builder(binding.root.context)
                        .setMessage("Please accept our permissions")
                        .setPositiveButton("yes") { dialog, which ->
                            e.askAgain();
                        } //ask again
                        .setNegativeButton("no") { dialog, which ->
                            Toast.makeText(binding.root.context, "Rad etildi", Toast.LENGTH_SHORT)
                                .show()
                            dialog.dismiss();
                        }
                        .show();
                }
                if (e.hasForeverDenied()) {

                    // you need to open setting manually if you really need it
                    e.goToSettings();
                }

            }}

    private fun pickImageFromGallery() {
        getImageContent.launch("image/*")
    }
    private val getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri ?: return@registerForActivityResult
            binding.profilePic.setImageURI(uri)
            val format = SimpleDateFormat("yyyyMM_HHmmss", Locale.getDefault()).format(Date())

            val filesDir = binding.root.context.filesDir
            val contentResolver = activity?.contentResolver
            val openInputStream = contentResolver?.openInputStream(uri)
            val file = File(filesDir, "image.jp$format")
            val fileOutputStream = FileOutputStream(file)
            openInputStream?.copyTo(fileOutputStream)
            openInputStream?.close()
            fileOutputStream.close()




            val fileAbsolutePath = file.absolutePath
            val fileInputStream = FileInputStream(file)

            binding.regOtish.setOnClickListener {

                val name = binding.name.text.toString().trim()
                val raqam = binding.telRaqam.text.toString().trim()
                val davlat = binding.davlat.selectedItem.toString()
                val manzil = binding.address.text.toString().trim()
                val parol = binding.parol.text.toString().trim()

                if (name.isNotEmpty() && raqam.isNotEmpty() && davlat != "Davlat" && manzil.isNotEmpty() && parol.isNotEmpty()){
                    val registerModel = RegisterModel(fileAbsolutePath,name,raqam,davlat,manzil,parol)
                    myDbHelper.insertRegister(registerModel)
                    findNavController().navigate(R.id.userFragment)
                }else{
                    Toast.makeText(binding.root.context, "Iltimos ma'lumotlarni to'liq va aniq kiritganingizga ishonch hosil qiling", Toast.LENGTH_SHORT).show()
                }


            }



        }



    private fun permission_camera() {
        askPermission(Manifest.permission.CAMERA) {
            //all permissions already granted or just granted
            //your action
            Toast.makeText(binding.root.context, "Granted", Toast.LENGTH_SHORT).show()
            val imageFile = createImageFile()
            photoURI = FileProvider.getUriForFile(binding.root.context, BuildConfig.APPLICATION_ID,imageFile)
            getTakeImageContent.launch(photoURI)

        }
            .onDeclined { e ->
                if (e.hasDenied()) {
                    android.app.AlertDialog.Builder(binding.root.context)
                        .setMessage("Please accept our permissions")
                        .setPositiveButton("yes") { dialog, which ->
                            e.askAgain();
                        } //ask again
                        .setNegativeButton("no") { dialog, which ->
                            Toast.makeText(binding.root.context, "Rad etildi", Toast.LENGTH_SHORT).show()
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
    private val getTakeImageContent =
        registerForActivityResult(ActivityResultContracts.TakePicture()){

            if (it) {
                binding.profilePic.setImageURI(photoURI)
                val format = SimpleDateFormat("yyyyMM_HHmmss", Locale.getDefault()).format(Date())
                val filesDir = binding.root.context.filesDir
                val contentResolver = activity?.contentResolver
                val openInputStream = contentResolver?.openInputStream(photoURI)
                val file = File(filesDir,"image.jpg$format")
                val fileOutputStream = FileOutputStream(file)
                openInputStream?.copyTo(fileOutputStream)
                openInputStream?.close()
                fileOutputStream.close()
                val fileAbsolutePath = file.absolutePath

                binding.regOtish.setOnClickListener {

                    val name = binding.name.text.toString().trim()
                    val raqam = binding.telRaqam.text.toString().trim()
                    val davlat = binding.davlat.selectedItem.toString()
                    val manzil = binding.address.text.toString().trim()
                    val parol = binding.parol.text.toString().trim()

                    if (name.isNotEmpty() && raqam.isNotEmpty() && davlat != "Davlat" && manzil.isNotEmpty() && parol.isNotEmpty()){
                        val registerModel = RegisterModel(fileAbsolutePath,name,raqam,davlat,manzil,parol)
                        myDbHelper.insertRegister(registerModel)
                        findNavController().navigate(R.id.userFragment)
                    }else{
                        Toast.makeText(binding.root.context, "Iltimos ma'lumotlarni to'liq va aniq kiritganingizga ishonch hosil qiling", Toast.LENGTH_SHORT).show()
                    }


                }


            }
        }
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val format = SimpleDateFormat("yyyyMM_HHmmss", Locale.getDefault()).format(Date())
        val externalFilesDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile("JPEG_$format",".jpg",externalFilesDir).apply {
            currentImagePath = absolutePath
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegistrationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegistrationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}