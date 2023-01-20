package com.marko112.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class TransactionRecyclerAdapter(
    val items: ArrayList<Transaction>
    ): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TransactionViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is TransactionViewHolder ->{
                holder.bind(position, items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun add(item: Transaction){
        items.add(item)
        notifyDataSetChanged()
    }

    class TransactionViewHolder(private val view: View) : RecyclerView.ViewHolder(view){
        private val payerEmail = view.findViewById<TextView>(R.id.recycler_item_payerEmail)
        private val recipientEmail = view.findViewById<TextView>(R.id.recycler_item_recipientEmail)
        //private val description = view.findViewById<TextView>(R.id.recycler_item_paymentDescription)
        private val amount = view.findViewById<TextView>(R.id.recycler_view_moneyTransfered)
        private val date = view.findViewById<TextView>(R.id.recycler_item_transactionDate)

        fun bind(
            index: Int,
            transaction: Transaction
        ){
            recipientEmail.text = transaction.recipientEmail
            payerEmail.text = transaction.payerEmail
            //description.text = transaction.description
            amount.text = transaction.amount.toString()
            val length = transaction.date?.toDate().toString().length
            date.text = transaction.date?.toDate().toString().substring(0, length - 15)
//            transaction.userName = userName.text.toString()
//            transaction.description = description.text.toString()

            //val timestamp = Timestamp(Date(date.text.toString()))
            //transaction.date = timestamp

//            transaction.amount = amount.text.toString().toDouble()
        }
    }
}