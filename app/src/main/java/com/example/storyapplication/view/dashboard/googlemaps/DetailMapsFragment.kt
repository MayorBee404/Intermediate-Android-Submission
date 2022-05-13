package com.example.storyapplication.view.dashboard.googlemaps

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.storyapplication.R
import com.example.storyapplication.databinding.FragmentDetailMapsBinding


class DetailMapsFragment : DialogFragment() {
    private var _detailmapsbinding : FragmentDetailMapsBinding?= null
    private val binding get() = _detailmapsbinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _detailmapsbinding = FragmentDetailMapsBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val bundle = DetailMapsFragmentArgs.fromBundle(arguments as Bundle)

        binding.include2.apply {
            nameDetail.text = bundle.mapsName
            Glide.with(requireActivity())
                .load (bundle.mapsImage)
                .error(R.drawable.placeholder_image)
                .into(ivDetailImage)
            descriptionDetail.text = bundle.mapsDescription
        }

    }

}