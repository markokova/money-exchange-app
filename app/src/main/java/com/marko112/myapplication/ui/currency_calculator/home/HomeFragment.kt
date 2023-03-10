package com.marko112.myapplication.ui.currency_calculator.home

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.api.Distribution.BucketOptions.Linear
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.marko112.myapplication.*
import com.marko112.myapplication.databinding.FragmentHomeBinding
import java.util.ArrayList

class HomeFragment : Fragment() {

//    private late init var recyclerAdapter: TransactionRecyclerAdapter
    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    private val users = Register.UsersSingleton.users

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        auth = FirebaseAuth.getInstance()

        val transactions: ArrayList<Transaction> = ArrayList()
        val recyclerAdapter = TransactionRecyclerAdapter(transactions)

        val email: TextView = binding.fragmentHomeEmail
        val moneyAmount: TextView = binding.fragmentHomeMoneyAmmount
        val recyclerView = binding.fragmentHomeTransactions
        val name = binding.fragmentHomeName


        val currentUser = auth?.currentUser?.email

        db.collection("transactions")
            .get()
            .addOnSuccessListener {
                for(data in it.documents){
                    val transaction = data.toObject(Transaction::class.java)
                    if(transaction != null){
                        if(transaction.payerEmail.equals(currentUser) || transaction.recipientEmail.equals(currentUser)){
                            transaction.id = data.id
                            //transactions.add(transaction)
                            recyclerAdapter.add(transaction)
                        }
                    }
                }
            }



        homeViewModel.text.observe(viewLifecycleOwner) {
            email.text = auth.currentUser?.email
            var currentUser: User? = null
            for(user in users){
                if(user.email == auth.currentUser?.email){
                    currentUser = user
                    break
                }
            }
            if (currentUser != null) {
                moneyAmount.text = currentUser.balance.toString()
                name.text = currentUser.name
            }
            else{
                Log.d(TAG,"currentUser in HomeFragment is null.")
            }
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@HomeFragment.activity)
                adapter = recyclerAdapter
            }
            recyclerAdapter.setOnItemClickListener(listener = object: TransactionRecyclerAdapter.ItemListener{
                @SuppressLint("ResourceType")
                override fun onItemClick(index: Int) {
                    val transactionFragment = TransactionFragment()
                    val bundle = Bundle()
                    bundle.putString("payerEmail",recyclerAdapter.items[index].payerEmail.toString())
                    bundle.putString("amount",recyclerAdapter.items[index].amount.toString())
                    bundle.putString("recipientEmail",recyclerAdapter.items[index].recipientEmail.toString())
                    bundle.putString("description",recyclerAdapter.items[index].description.toString())
                    val dateLength = recyclerAdapter.items[index].date?.toDate().toString().length
                    val date = recyclerAdapter.items[index].date?.toDate().toString().substring(0, dateLength - 15)
                    bundle.putString("date",date)

                    transactionFragment.arguments = bundle

                    val fragmentTransaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.nav_host_fragment_content_main,transactionFragment).addToBackStack(null).commit()
                }
            })
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}