package com.example.storyapplication.view.authentication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.storyapplication.R
import com.example.storyapplication.ViewModelFactory
import com.example.storyapplication.databinding.LoginFragmentBinding
import com.example.storyapplication.view.dashboard.DashboardActivity

class LoginFragment : Fragment() {

    private var _loginBinding : LoginFragmentBinding? = null
    private val binding get() = _loginBinding!!
    private val viewModel: AuthenticationViewModel by activityViewModels{factory}
    private lateinit var factory: ViewModelFactory
    private lateinit var message: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _loginBinding = LoginFragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)
        factory = ViewModelFactory.getInstance(requireActivity())
        setupAction()
        initObserver()
        playAnimation()
    }

    private fun setupAction(){
        binding.loginButton.setOnClickListener{
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()


            if(email.isEmpty() || password.isEmpty()){
                val messageAllert = getString(R.string.message_login_page)
                Toast.makeText(context,messageAllert,Toast.LENGTH_SHORT).show()

            }else{
                viewModel.userLogin(email,password)
                viewModel.error.observe(viewLifecycleOwner){
                    event -> event.getContentIfNotHandled()?.let{ error ->
                    if (!error){
                        initObserver()
                        viewModel.user.observe(viewLifecycleOwner){
                            event->event.getContentIfNotHandled()?.let{
                                viewModel.saveUserToken(it.token)
                                viewModel.saveUserName(it.name)
                                viewModel.saveUserEmail(email)
                            val intent = Intent (activity,DashboardActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        }
                        }
                    }else{
                        val massageAllert = getString(R.string.wrong_credential)
                        Toast.makeText(context,"$message: $massageAllert",Toast.LENGTH_SHORT).show()
                        binding.passwordEditText.apply{
                            text?.clear()
                            setError(null)
                        }
                    }
                }
                }
            }
        }
    }

    private fun initObserver() {
        viewModel.message.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                message = it
            }
        }
        viewModel.loading.observe(viewLifecycleOwner){ event->
            event.getContentIfNotHandled()?.let{
                loading(it)
            }
        }
    }
    private fun loading (isLoading: Boolean){
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.loginButton.isEnabled = false
        } else {
            binding.progressBar.visibility = View.INVISIBLE
            binding.loginButton.isEnabled = true
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            }.start()

            val title =
                ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
            val message =
                ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(500)
            val emailTextView =
                ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
            val emailEditTextLayout =
                ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
            val passwordTextView =
                ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
            val passwordEditTextLayout =
                ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f)
                    .setDuration(500)
            val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)

            AnimatorSet().apply {
                playSequentially(
                    title,
                    message,
                    emailTextView,
                    emailEditTextLayout,
                    passwordTextView,
                    passwordEditTextLayout,
                    login
                )
                startDelay = 500
            }.start()
        }

}