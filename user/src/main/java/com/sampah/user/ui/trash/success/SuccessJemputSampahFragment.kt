package com.sampah.user.ui.trash.success

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.sampah.user.R
import com.sampah.user.databinding.FragmentSuccessJemputSampahBinding
import com.sampah.user.ui.profile.dataPribadi.DataPribadiActivity
import com.sampah.user.ui.trash.TrashFragment
import com.sampah.user.ui.trash.TrashFragmentDirections
import com.sampah.user.utils.Utils.showToast
class SuccessJemputSampahFragment : Fragment() {
    private var _binding: FragmentSuccessJemputSampahBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSuccessJemputSampahBinding.inflate(inflater, container, false)
        return binding?.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Lottie animation
        binding?.imageSuccessAnimation?.apply {
            setAnimation(R.raw.success_animation)
            repeatCount = 0 // Play once
            playAnimation()
        }

        binding?.btnKembali?.setOnClickListener {
            try {
                val action = SuccessJemputSampahFragmentDirections.actionSuccessJemputSampahFragmentToTrashFragment()
                findNavController().navigate(action)
            } catch (e: IllegalArgumentException) {
                Log.e("Navigation", "Navigation action/destination not found: ${e.message}")
            }
        }

        binding?.btnTrackSampah?.setOnClickListener {
            showToast(requireActivity(),"This Feature is Coming Soon")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}