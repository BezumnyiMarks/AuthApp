package com.example.authapp.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.authapp.Data.LoadingState
import com.example.authapp.Data.Payment
import com.example.authapp.MainViewModel
import com.example.authapp.Data.PaymentsAdapter
import com.example.authapp.R
import com.example.authapp.Repository
import com.example.authapp.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch
import java.util.Calendar

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private val rep = Repository()
    private val paymentsAdapter = PaymentsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.payments.adapter = paymentsAdapter
        rep.getAccessToken(requireActivity())?.let { viewModel.loadPayments(it) }
        lifecycleScope.launch {
            viewModel.loading.collect{ loadState ->
                if (loadState == LoadingState.Success)
                    viewModel.payments.collect{
                        paymentsAdapter.submitList(getPayments(it))
                    }
            }
        }

        binding.buttonBack.setOnClickListener {
            rep.saveAccessToken(requireActivity(), "")
            rep.savePassword(requireActivity(), "")
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getPayments(payments: List<Payment>): List<Payment>{
        val paymentsList = payments.toMutableList()
        val calendar = Calendar.getInstance()
        var dateTimeText = ""
        var amount = ""

        for (i in paymentsList.indices){
            if (paymentsList[i].created != "" && paymentsList[i].created != "null") {
                calendar.timeInMillis = paymentsList[i].created.toLong()
                val month = getTime(calendar.get(Calendar.MONTH) + 1)
                dateTimeText = "${calendar.get(Calendar.DAY_OF_MONTH)}.$month.${calendar.get(Calendar.YEAR)}" + "\n" +
                        "${getTime(calendar.get(Calendar.HOUR_OF_DAY))}:" +
                        "${getTime(calendar.get(Calendar.MINUTE))}:" +
                        getTime(calendar.get(Calendar.SECOND))
            }
            else dateTimeText = resources.getString(R.string.no_info)

            amount = if (paymentsList[i].amount != "" && paymentsList[i].amount != "null"){
                paymentsList[i].amount.toBigDecimal().toString()
            } else resources.getString(R.string.no_info)

            paymentsList[i] = Payment(
                paymentsList[i].id,
                paymentsList[i].title,
                amount,
                dateTimeText
            )
        }

        return paymentsList
    }

    private fun getTime(time: Int): String{
        return if(time < 10) "0$time"
        else time.toString()
    }
}