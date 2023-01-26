package com.marko112.myapplication

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class TransactionRecyclerAdapter(
    val items: ArrayList<Transaction>
    ): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private lateinit var mListener: ItemListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TransactionViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false),mListener
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

    class TransactionViewHolder(private val view: View, clickListener: ItemListener) : RecyclerView.ViewHolder(view){

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }

        private val payerEmail = view.findViewById<TextView>(R.id.recycler_item_payerEmail)
        private val recipientEmail = view.findViewById<TextView>(R.id.recycler_item_recipientEmail)
        private val amount = view.findViewById<TextView>(R.id.recycler_view_moneyTransfered)
        private val date = view.findViewById<TextView>(R.id.recycler_item_transactionDate)

        fun bind(
            index: Int,
            transaction: Transaction
        ){
            recipientEmail.text = transaction.recipientEmail
            payerEmail.text = transaction.payerEmail
            amount.text = transaction.amount.toString()
            val length = transaction.date?.toDate().toString().length
            date.text = transaction.date?.toDate().toString().substring(0, length - 15)
        }
    }


    interface ItemListener{
        fun onItemClick(index: Int)
    }

    fun setOnItemClickListener(listener: ItemListener){
        mListener = listener
    }

}