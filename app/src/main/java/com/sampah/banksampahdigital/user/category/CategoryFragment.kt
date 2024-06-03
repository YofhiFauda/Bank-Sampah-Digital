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
            binding?.tvSubtitleJemputSampah1?.text = resources.getString(R.string.subtitle_Jenis_sampah1)
            binding?.tvSubtitleJemputSampah2?.text = resources.getString(R.string.subtitle_Jenis_sampah2)

            binding?.tvTitleSampahAnorganik?.text = resources.getString(R.string.title_anorganik)
            binding?.tvSubtitleSampahAnorganik1?.text = resources.getString(R.string.subtitle_anorganik1)
            binding?.tvSubtitleSampahAnorganik2?.text = resources.getString(R.string.subtitle_anorganik2)

            binding?.tvTitleSampahOrganik?.text = resources.getString(R.string.title_organik)
            binding?.tvSybtitleSampahOrganik1?.text = resources.getString(R.string.subtitle_organik1)
            binding?.tvSybtitleSampahOrganik2?.text = resources.getString(R.string.subtitle_organik2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}