package com.example.storyapplication.view.authentication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import com.example.storyapplication.R
import com.example.storyapplication.ViewModelFactory
import com.example.storyapplication.databinding.RegisterFragmentBinding

class RegisterFragment : Fragment() {

    private var _registerBinding : RegisterFragmentBinding? = null
    private val binding get() = _registerBinding!!
    private lateinit var factory: ViewModelFactory
    private val authenticationViewModel:AuthenticationViewModel by activityViewModels{factory}
    private lateinit var  message :String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _registerBinding = RegisterFragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)

        factory = ViewModelFactory.getInstance(requireActivity())
        initObserver()
        setupAction()
        playAnimation()
    }

    private fun setupAction(){

        binding.signupButton.setOnClickListener(){
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if(name.isEmpty() || email.isEmpty() || password.isEmpty()){
                val messageAllert = getString(R.string.message_login_page)
                Toast.makeText(requireContext(),messageAllert,Toast.LENGTH_SHORT).show()
            }else{
                if(password.length<6) {
                    val messageAllert = getString(R.string.length_character)
                    Toast.makeText(requireContext(), messageAllert, Toast.LENGTH_SHORT).show()

                }else{
                    authenticationViewModel.userRegister(name, email, password)
                    authenticationViewModel.error.observe(viewLifecycleOwner){ event->
                        event.getContentIfNotHandled()?.let{
                            error->
                        if(!error) {
                            activity?.supportFragmentManager?.commit {
                                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                                val messageAllert = getString(R.string.register_success)
                                Toast.makeText(activity, messageAllert, Toast.LENGTH_SHORT).show()

                            }
                        }else{
                            val messageAllert = getString(R.string.register_field)
                            Toast.makeText(requireContext(),"$message: $messageAllert",
                            Toast.LENGTH_SHORT).show()
                        }
                    }
                    }
                }
            }
        }

    }
    private fun initObserver(){
        authenticationViewModel.message.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                message = it
            }
        }

        authenticationViewModel.loading.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                loading(it)
            }
        }
    }

    private fun loading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.signupButton.isEnabled = false
        } else {
            binding.progressBar.visibility = View.INVISIBLE
            binding.signupButton.isEnabled = true
        }
    }

    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val nameTextView = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(500)
        val nameEditTextLayout = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)


        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
            )
            startDelay = 500
        }.start()
    }

}