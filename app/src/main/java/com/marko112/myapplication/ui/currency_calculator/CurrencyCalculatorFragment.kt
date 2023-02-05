package com.marko112.myapplication.ui.currency_calculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.marko112.myapplication.databinding.FragmentCurrencyCalculatorBinding

class CurrencyCalculatorFragment : Fragment() {

    private var _binding: FragmentCurrencyCalculatorBinding? = null
    private var kunaToEur: Boolean = true
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val currencyCalculatorViewModel =
            ViewModelProvider(this).get(CurrencyCalculatorViewModel::class.java)

        _binding = FragmentCurrencyCalculatorBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val currencyNameToConvert = binding.currencyNameToConvert
        val convertedCurrencyName = binding.currencyNameConverted
        val moneyToConvert = binding.insertAmmountToConvert
        val convertedMoneyAmount = binding.convertedAmmount
        val swapButton = binding.swapButton
        val convertButton = binding.convertButton

        currencyNameToConvert.text = "KUNA"
        convertedCurrencyName.text = "EUR"

        moneyToConvert.setOnClickListener {
            convertedMoneyAmount.text = swap(moneyToConvert.text.toString().toDouble()).toString()
        }

        convertButton.setOnClickListener {
            if(moneyToConvert.text.isNotEmpty()){
                convertedMoneyAmount.text = swap(moneyToConvert.text.toString().toDouble()).toString()
            }
            else{
                Toast.makeText(context,"Enter money amount to convert.",Toast.LENGTH_LONG).show();
            }
        }

        swapButton.setOnClickListener {
            kunaToEur = !kunaToEur
                if(kunaToEur) {
                    currencyNameToConvert.text = "KUNA"
                    convertedCurrencyName.text = "EUR"
                    if (moneyToConvert.text.isNotEmpty()) {
                        convertedMoneyAmount.text = swap(moneyToConvert.text.toString().toDouble()).toString()
                    }
                }
                else{
                    currencyNameToConvert.text = "EUR"
                    convertedCurrencyName.text = "KUNA"
                    if(moneyToConvert.text.isNotEmpty()){
                    convertedMoneyAmount.text = swap(moneyToConvert.text.toString().toDouble()).toString()
                    }
                }
        }

        currencyCalculatorViewModel.text.observe(viewLifecycleOwner) {
            //textView.text = it

        }
        return root
    }

    private fun swap(moneyAmount: Double):Double{
        if(!kunaToEur){
            return moneyAmount*7.53450
        }
        else{
            return moneyAmount/7.53450
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}