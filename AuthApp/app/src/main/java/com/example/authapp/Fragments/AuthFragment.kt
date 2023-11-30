package com.example.authapp.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.authapp.Data.LoadingState
import com.example.authapp.MainViewModel
import com.example.authapp.R
import com.example.authapp.Repository
import com.example.authapp.databinding.FragmentAuthBinding
import kotlinx.coroutines.launch

class AuthFragment : Fragment() {

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private val rep = Repository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButtonListener()
        addTextChangedListeners()

        lifecycleScope.launch {
            viewModel.loading.collect{ loadState ->
                if (loadState == LoadingState.Success)
                    viewModel.token.collect{
                        if (it != ""){
                            rep.saveAccessToken(requireActivity(), it)
                            viewModel.emptyTokenValue()
                            findNavController().navigate(R.id.action_authFragment_to_profileFragment)
                        }
                    }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        val login = rep.getLogin(requireActivity())
        val password = rep.getPassword(requireActivity())

        binding.editLogin.setText(login, TextView.BufferType.EDITABLE)
        binding.editPassword.setText(password, TextView.BufferType.EDITABLE)
    }

    private fun setButtonListener(){
        val token = rep.getAccessToken(requireActivity())
        binding.buttonAccess.setOnClickListener {
            val login = binding.editLogin.text.toString()
            val password = binding.editPassword.text.toString()
            if (login == "demo" && password == "12345"){
                rep.saveLogin(requireActivity(), login)
                rep.savePassword(requireActivity(), password)
                if (token == "")
                    viewModel.getToken(login, password)
                else token?.let { token ->
                    viewModel.setTokenValue(token)
                }
            }
            else Toast.makeText(requireActivity(), resources.getString(R.string.wrong_cred) , Toast.LENGTH_LONG).show()

            if (login != "demo"){
                binding.editLoginLayout.boxStrokeColor = resources.getColor(R.color.red)
            }
            if (password != "12345"){
                binding.editPasswordLayout.boxStrokeColor = resources.getColor(R.color.red)
            }
        }
    }

    private fun addTextChangedListeners(){
        binding.editLogin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() == "demo"){
                    binding.editLoginLayout.boxStrokeColor = resources.getColor(R.color.text_blue)
                }
            }
        })

        binding.editPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() == "12345"){
                    binding.editPasswordLayout.boxStrokeColor = resources.getColor(R.color.text_blue)
                }
            }
        })
    }
}