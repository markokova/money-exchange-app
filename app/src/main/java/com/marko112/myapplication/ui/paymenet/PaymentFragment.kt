package com.marko112.myapplication.ui.paymenet

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.marko112.myapplication.*
import com.marko112.myapplication.databinding.FragmentPaymentBinding
import com.marko112.myapplication.ui.currency_calculator.home.HomeFragment
import java.time.LocalDate

//import com.marko112.myapplication.MainActivity.addItem

class PaymentFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    private var _binding: FragmentPaymentBinding? = null
    private val db = Firebase.firestore
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val users = Register.UsersSingleton.users

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val paymentViewModel =
            ViewModelProvider(this).get(PaymentViewModel::class.java)

        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        auth = FirebaseAuth.getInstance()

        val recipientEmail = binding.fragmentPaymentUserNameToPay
        val transactionAmount = binding.fragmentPaymentAmmountToPay
        val description = binding.fragmentPaymentPaymentDescription
        val payButton = binding.fragmentPaymentPayButton
        val payerEmail = auth.currentUser?.email
        val balance = binding.fragmentPaymentMoneyAmmount
        val name = binding.fragmentPaymentName
        val email = binding.fragmentPaymentEmail


        var recipient: User? = null
        var payer: User? = null
        //find payer among all users
        for(user in users){
            if(user.email == payerEmail){
                payer = user
                if(payer == null){
                    Log.d(TAG,"Payer not found.")
                }
            }
        }

        payButton.setOnClickListener {
            //find recipient and payer in all users
            for(user in users){
                if(user.email == recipientEmail.text.toString()){
                    recipient = user
                    break
                }
            }
            if(recipient == null){
                Toast.makeText(this@PaymentFragment.context, "There is no such account. Check the recipient email you entered.", Toast.LENGTH_LONG).show()
            }
            else if (payer != null) {
                if(payer!!.isAbleToPay(transactionAmount.text.toString().toDouble()) == 2){
                    val transaction = Transaction(
                        date = Timestamp.now(),
                        description = description.text.toString(),
                        amount = transactionAmount.text.toString().toDouble(),
                        recipientEmail = recipientEmail.text.toString(),
                        payerEmail = payerEmail
                    )
                    //add transaction to specific users and manage balances
                    recipient?.transactions?.add(transaction)
                    transaction.amount?.let { it1 -> recipient?.receive(it1) }
                    payer?.transactions?.add(transaction)
                    transaction.amount?.let { it1 -> payer?.pay(it1) }
                    //add transaction to database
                    db.collection("transactions").add(transaction).addOnSuccessListener {
                        transaction.id = it.id
                        Toast.makeText(
                            this@PaymentFragment.context,
                            "Transaction is successful",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                else if(payer!!.isAbleToPay(transactionAmount.text.toString().toDouble()) == 1){
                    Toast.makeText(
                        this@PaymentFragment.context,"Not enough money on account to make this transaction.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else if(payer!!.isAbleToPay(transactionAmount.text.toString().toDouble()) == 0){
                    Toast.makeText(
                        this@PaymentFragment.context,"It is not possible to make a transaction worth 0 EUR",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            else if(payer == null){
                Toast.makeText(
                    this@PaymentFragment.context, "payer = null.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        paymentViewModel.text.observe(viewLifecycleOwner) {
            balance.text = payer?.balance.toString()
            name.text = payer?.name
            email.text = auth.currentUser?.email

        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}