package com.marko112.myapplication.ui.currency_calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.marko112.myapplication.databinding.FragmentCurrencyCalculatorBinding

class CurrencyCalculatorFragment : Fragment() {

    private var _binding: FragmentCurrencyCalculatorBinding? = null
    private var kunaToEur: Boolean = true
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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

        currencyNameToConvert.text = "KUNA"
        convertedCurrencyName.text = "EUR"

        moneyToConvert.setOnClickListener {
            convertedMoneyAmount.text = swap(moneyToConvert.text.toString().toDouble()).toString()
        }

        swapButton.setOnClickListener {
            kunaToEur = !kunaToEur
            if(kunaToEur == true){
                currencyNameToConvert.text = "KUNA"
                convertedCurrencyName.text = "EUR"
                convertedMoneyAmount.text = swap(moneyToConvert.text.toString().toDouble()).toString()
            }
            else{
                currencyNameToConvert.text = "EUR"
                convertedCurrencyName.text = "KUNA"
                convertedMoneyAmount.text = swap(moneyToConvert.text.toString().toDouble()).toString()
            }
        }

        currencyCalculatorViewModel.text.observe(viewLifecycleOwner) {
            //textView.text = it

        }
        return root
    }

    fun swap(moneyAmmount: Double):Double{
        if(kunaToEur == true){
            return moneyAmmount*7.53
        }
        else{
            return moneyAmmount/7.53
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}