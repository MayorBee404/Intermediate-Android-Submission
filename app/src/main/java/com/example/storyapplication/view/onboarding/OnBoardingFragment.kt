package com.example.storyapplication.view.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.storyapplication.R
import com.example.storyapplication.databinding.OnBoardingFragmentBinding

class OnBoardingFragment : Fragment() {

   private var _onboardingbinding : OnBoardingFragmentBinding? = null
    private val binding = _onboardingbinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _onboardingbinding = OnBoardingFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view : View,savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)

        setupAction()
        playAnimation()
    }

    private fun setupAction(){
        binding.visitNow.setOnClickListener(){
            findNavController().navigate(R.id.action_onBoardingFragment2_to_welcomeFragment)
        }

    }
    private fun playAnimation(){

    }

}