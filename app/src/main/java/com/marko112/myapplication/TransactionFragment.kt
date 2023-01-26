package com.marko112.myapplication

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.setFragmentResultListener
import com.google.firebase.auth.FirebaseAuth
import com.marko112.myapplication.databinding.FragmentTransactionBinding

class TransactionFragment : Fragment(){

    private val users = Register.UsersSingleton.users
    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransactionBinding.inflate(inflater,container,false)
        val root: View = binding.root

        val payerName = binding.fragmentTransactionPayerName
        val payerEmail = binding.fragmentTransactionPayerEmail
        val recipientName = binding.fragmentPaymentRecipientName
        val recipientEmail = binding.fragmentTransactionRecipientEmail
        val amount = binding.fragmentTransactionAmount
        val date = binding.fragmentTransactionDate
        val description = binding.fragmentTransactionDescription

        auth = FirebaseAuth.getInstance()

        val transaction: Transaction

//        setFragmentResultListener("getTransaction"){ requestKey, bundle ->
//            val result = bundle.getString("transactionKey")
//        }


        payerEmail.text = arguments?.get("payerEmail").toString()
        recipientEmail.text = arguments?.get("recipientEmail").toString()
        amount.text = arguments?.get("amount").toString()
        description.text = arguments?.get("description").toString()
        date.text = arguments?.get("date").toString()

        //ovo ne bi moralo biti tu kad bi imao u transaction klasi umjesto emaila korisnika instancu klase korisnika, pa bi onda
        //mogao pristupiti imenu korisnika preko instance user koja se nalazi u transakciji
        var recipientNameString: String? = null
        var payer: String? = null
        for(user in users){
            if(user.email == payerEmail.text){
                payer = user.name
            }
            if(user.email == recipientEmail.text){
                recipientNameString = user.name
            }
        }

        payerName.text = payer
        recipientName.text = recipientNameString

        return root
//        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}