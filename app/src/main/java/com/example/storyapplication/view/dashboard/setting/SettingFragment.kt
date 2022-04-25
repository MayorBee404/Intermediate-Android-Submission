package com.example.storyapplication.view.dashboard.setting

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.storyapplication.ViewModelFactory
import com.example.storyapplication.databinding.SettingFragmentBinding
import com.example.storyapplication.view.authentication.AuthenticationViewModel
import com.example.storyapplication.view.authentication.MainActivity
import java.util.*

class SettingFragment : Fragment() {

    private lateinit var factory: ViewModelFactory
    private val viewModel: SettingViewModel by activityViewModels{factory}
    private val authenticationViewModel : AuthenticationViewModel by activityViewModels{factory}
    private var _settingbinding : SettingFragmentBinding?= null
    private val binding get() = _settingbinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _settingbinding = SettingFragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view : View,savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factory = ViewModelFactory.getInstance(requireActivity())

        binding.btnLogout.setOnClickListener {
            startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
        }

        initObserve()
        initView()
    }
    private fun initObserve(){

        viewModel.getUserName().observe(viewLifecycleOwner){username ->
            binding.tvName.text = username
            binding.textView3.text = username
        }
        viewModel.getUserEmail().observe(viewLifecycleOwner){email ->
            binding.tvEmail.text = email
        }

    }
    private fun initView(){
        binding.btnLogout.setOnClickListener {
            authenticationViewModel.logout()
            startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
        }
        binding.setLanguage.setOnClickListener {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
        binding.localName.setOnClickListener{
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        binding.localName.text = Locale.getDefault().displayName

        }
    override fun onDestroyView() {
        super.onDestroyView()
        _settingbinding = null
    }
}
