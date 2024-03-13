package com.sampah.banksampahdigital.user.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sampah.banksampahdigital.R
import com.sampah.banksampahdigital.databinding.FragmentCategoryBinding
import com.sampah.banksampahdigital.databinding.FragmentTrashBinding

class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    private fun setupView(){
        binding.apply {
            binding?.tvSubtitleJemputSampah1?.text = R.string.subtitle_Jenis_sampah1.toString()
            binding?.tvSubtitleJemputSampah2?.text = R.string.subtitle_Jenis_sampah2.toString()

            binding?.tvTitleSampahAnorganik?.text = R.string.title_anorganik.toString()
            binding?.tvSubtitleSampahAnorganik1?.text = R.string.subtitle_anorganik1.toString()
            binding?.tvSubtitleSampahAnorganik2?.text = R.string.subtitle_anorganik2.toString()

            binding?.tvTitleSampahOrganik?.text = R.string.title_organik.toString()
            binding?.tvSybtitleSampahOrganik1?.text = R.string.subtitle_organik1.toString()
            binding?.tvSybtitleSampahOrganik2?.text = R.string.subtitle_organik2.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}