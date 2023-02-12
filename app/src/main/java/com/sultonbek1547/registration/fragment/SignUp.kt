package com.sultonbek1547.registration.fragment

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.model.User
import com.sultonbek1547.registration.BuildConfig
import com.sultonbek1547.registration.R
import com.sultonbek1547.registration.database.MyDbHelper
import com.sultonbek1547.registration.databinding.BottomSheetCamGalBinding
import com.sultonbek1547.registration.databinding.FragmentSignUpBinding
import com.sultonbek1547.registration.utils.Constraints.COUNTRY_NAMES
import com.sultonbek1547.registration.utils.Constraints.UZB_PHONE_NUM_PREFIX
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class SignUp : Fragment() {

    private val binding by lazy { FragmentSignUpBinding.inflate(layoutInflater) }
    private lateinit var myDbHelper: MyDbHelper
    private var countryState = false
    private var countryPosition = 0
    var photoSelectedState = false
    private lateinit var photoUri: Uri
    private lateinit var photoPath: String
    private var cameraUsedState = false
    var databaseState = false
    private lateinit var lastUserInDb: User
    private var imageFileName = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        var fullName: String
        var number: String
        var address: String
        var password: String

        myDbHelper = MyDbHelper(requireContext())
        if (myDbHelper.getAllUser().isEmpty()) {
            databaseState = true
        } else {
            lastUserInDb = myDbHelper.getAllUser().last()
        }

        binding.apply {

            btnAdd.setOnClickListener {
                val bottomSheetDialog =
                    BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
                val bottomSheetBinding = BottomSheetCamGalBinding.inflate(layoutInflater)
                bottomSheetDialog.setContentView(bottomSheetBinding.root)
                bottomSheetBinding.btnGallery.setOnClickListener {
                    bottomSheetDialog.dismiss()
                    getImageContent.launch("image/*")
                }
                bottomSheetBinding.btnCamera.setOnClickListener {
                    bottomSheetDialog.dismiss()
                    val imageFile = createFile()
                    photoUri = FileProvider.getUriForFile(
                        requireContext(), BuildConfig.APPLICATION_ID, imageFile!!
                    )
                    getImageContentCamera.launch(photoUri)
                }
                bottomSheetDialog.show()

            }

            btnSignUp.setOnClickListener {
                fullName = edtName.text.toString().trim()
                number = edtNumber.text.toString().trim()
                address = edtAddress.text.toString().trim()
                password = edtPassword.text.toString().trim()

                if (number.length > 2) {
                    if (!UZB_PHONE_NUM_PREFIX.contains(number.substring(0, 2).toInt())) {
                        edtNumber.error = "Raqam xato!"
                    }
                }

                if (photoSelectedState&&countryState && fullName.isNotEmpty() && number.isNotEmpty() && UZB_PHONE_NUM_PREFIX.contains(
                        number.substring(0, 2).toInt()
                    ) && address.isNotEmpty() && password.isNotEmpty()
                ) {
                    if (!cameraUsedState) {
                        savePhotoToDir()
                    }

                    /** saving to database */
                    myDbHelper.addUser(
                        User(
                            fullName,
                            number,
                            countryPosition,
                            address,
                            password,
                            photoPath
                        )
                    )
                    Toast.makeText(context, "Saqlandi", Toast.LENGTH_SHORT).show()

                    val showUsers = ShowUsers()
                    parentFragmentManager.beginTransaction().replace(R.id.my_container, showUsers)
                        .commit()
                } else {
                    Toast.makeText(context, "To'ldiring", Toast.LENGTH_SHORT).show()
                }

            }


            /** countries spinner*/
            binding.apply {
                spinnerCountry.setAdapter(
                    ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        COUNTRY_NAMES
                    )
                )

                spinnerCountry.setOnItemClickListener { _, _, i, _ ->
                    countryState = true
                    countryPosition = i
                }


            }

        }


        return binding.root
    }

    private val getImageContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it ?: return@registerForActivityResult
        binding.image.setImageURI(it)
        photoUri = it
        photoSelectedState = true
    }

    private fun savePhotoToDir() {
        imageFileName = if (databaseState) {
            1
        } else {
            lastUserInDb.id!! + 1
        }
        val inputStream = requireActivity().contentResolver?.openInputStream(photoUri)
        val file = File(requireActivity().filesDir, "$imageFileName.jpg")
        photoPath = file.absolutePath
        val fileOutputStream = FileOutputStream(file)
        inputStream?.copyTo(fileOutputStream)
        inputStream?.close()
        fileOutputStream.close()
    }


    private var getImageContentCamera =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                binding.image.setImageURI(photoUri)
                imageFileName = if (databaseState) {
                    1
                } else {
                    lastUserInDb.id!! + 1
                }
                val inputStream = requireActivity().contentResolver?.openInputStream(photoUri)
                val file = File(requireActivity().filesDir, "$imageFileName.jpg")
                val fileOutputStream = FileOutputStream(file)
                inputStream?.copyTo(fileOutputStream)
                inputStream?.close()
                fileOutputStream.close()
                photoPath = file.absolutePath
                photoSelectedState = true
                cameraUsedState = true
            } else {
                cameraUsedState = false
            }
        }


    private fun createFile(): File? {
        val format = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val externalFilesDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_$format", ".jpg", externalFilesDir).apply {
            photoPath = absolutePath
        }
    }


}