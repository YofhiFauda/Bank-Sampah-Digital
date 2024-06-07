package com.sampah.banksampahdigital.user.profile

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sampah.banksampahdigital.R
import com.sampah.banksampahdigital.databinding.FragmentProfileBinding
import com.sampah.banksampahdigital.utils.Media
import com.yalantis.ucrop.UCrop
import java.io.File
import java.io.FileOutputStream

@Suppress("DEPRECATION")
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var currentPhotoPath: String
    private var getFile: File? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding?.fabCamera?.setOnClickListener {
            Log.d(ContentValues.TAG, "FabCamera clicked")
            showBottomSheetDialog()
        }

        binding?.profileImage?.setOnClickListener {
            showImageDialog()
        }

        setupView()
        setupExit()
        loadProfileImage()
        setupAction()
    }

    private fun setupAction() {
        binding?.layoutDataPribadi?.setOnClickListener {
            Toast.makeText(this.context, "This feature is coming soon", Toast.LENGTH_SHORT).show()
        }
        binding?.layoutInformasiPenukaran?.setOnClickListener {
            Toast.makeText(this.context, "This feature is coming soon", Toast.LENGTH_SHORT).show()
        }
        binding?.layoutPenukaranPoint?.setOnClickListener {
            Toast.makeText(this.context, "This feature is coming soon", Toast.LENGTH_SHORT).show()
        }
        binding?.layoutRiwayatPenukaran?.setOnClickListener {
            Toast.makeText(this.context, "This feature is coming soon", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_dialog, null)
        bottomSheetDialog.setContentView(bottomSheetView)

        bottomSheetView.findViewById<TextView>(R.id.tv_take_photo).setOnClickListener {
            startTakePhoto()
            bottomSheetDialog.dismiss()
        }
        bottomSheetView.findViewById<TextView>(R.id.tv_choose_from_gallery).setOnClickListener {
            startGallery()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun setupView() {
        val userId = firebaseAuth.currentUser

        if (userId != null) {
            // Mengambil data pengguna dari Firestore berdasarkan ID pengguna
            firestore.collection("users").document(userId.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        // Dokumen ditemukan, ambil data pengguna
                        val username = document.getString("username")
                        val email = document.getString("email")

                        binding?.tvTitlePengguna?.text = username
                        binding?.tvEmail?.text = email

                        // Lakukan apa pun dengan data pengguna yang diperoleh
                        Log.d(ContentValues.TAG, "Username: $username, Email: $email")
                    } else {
                        Log.d(ContentValues.TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    // Gagal mengambil data pengguna
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, resources.getString(R.string.ambil_dari_galeri))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            startCrop(selectedImg)
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireContext().packageManager)
        Media.createTempFile(requireActivity().application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.fileprovider",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            val uri = Uri.fromFile(myFile)
            startCrop(uri)
        }
    }

    private fun startCrop(uri: Uri) {
        val destinationUri = Uri.fromFile(File(requireContext().cacheDir, "cropped_image.jpg"))
        val options = UCrop.Options().apply {
            withAspectRatio(1f, 1f)
            setFreeStyleCropEnabled(false)
        }
        UCrop.of(uri, destinationUri)
            .withAspectRatio(1.0f, 1.0f) // Force 1:1 aspect ratio
            .withOptions(options)
            .start(requireContext(), this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)
            resultUri?.let {
                binding?.profileImage?.setImageURI(it)
                saveImageToInternalStorage(BitmapFactory.decodeFile(File(it.path).absolutePath))
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            // Tangani error cropping
            Log.e("ProfileFragment", "Crop error: $cropError")
        }
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): String {
        val filename = "profile_image.png"
        val fos: FileOutputStream = requireContext().openFileOutput(filename, Context.MODE_PRIVATE)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.close()
        saveImagePathToPreferences(filename)
        return filename
    }

    private fun saveImagePathToPreferences(path: String) {
        val sharedPreferences = requireContext().getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("profile_image_path", path)
        editor.apply()
    }
//FUll IMAGE PREVIEW
//    private fun showImageDialog() {
//        val dialog = Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen)
//        dialog.setContentView(R.layout.profil_image_dialog)
//        val imageView = dialog.findViewById<ImageView>(R.id.imageView)
//
//        val sharedPreferences = requireContext().getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
//        val path = sharedPreferences.getString("profile_image_path", null)
//        if (!path.isNullOrEmpty()) {
//            val file = File(requireContext().filesDir, path)
//            if (file.exists()) {
//                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
//                imageView.setImageBitmap(bitmap)
//            }
//        }
//
//        dialog.show()
//    }


    private fun showImageDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.profil_image_dialog)
        val imageView = dialog.findViewById<ImageView>(R.id.imageView)

        val sharedPreferences = requireContext().getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val path = sharedPreferences.getString("profile_image_path", null)
        if (!path.isNullOrEmpty()) {
            val file = File(requireContext().filesDir, path)
            if (file.exists()) {
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                imageView.setImageBitmap(bitmap)
            }
        }

        dialog.show()
    }

    private fun loadProfileImage() {
        val sharedPreferences = requireContext().getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val path = sharedPreferences.getString("profile_image_path", null)
        if (!path.isNullOrEmpty()) {
            val file = File(requireContext().filesDir, path)
            if (file.exists()) {
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                binding?.profileImage?.setImageBitmap(bitmap)
            }
        }
    }

    private fun setupExit() {
        binding?.layoutKeluar?.setOnClickListener {
            AlertDialog.Builder(requireActivity()).apply {
                setTitle("Peringatan!")
                setMessage("Apakah anda yakin ingin keluar?")
                setPositiveButton("Ya") { _, _ ->
                    firebaseAuth.signOut()
                    activity?.finish()
                }
                setNegativeButton("Tidak") { dialog, _ ->
                    dialog.dismiss()
                }
                create()
                show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
                requireActivity().finish()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        private const val REQUEST_CODE_PERMISSIONS = 10

        const val CAMERA_X_RESULT = 200
    }
}

