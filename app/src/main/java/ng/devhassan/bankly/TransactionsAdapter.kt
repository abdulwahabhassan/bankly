package ng.devhassan.bankly

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ng.devhassan.bankly.databinding.ItemLayoutTransactionBinding
import timber.log.Timber

class TransactionsAdapter(
    private val context: Context,
    private val onItemClicked: (position: Int, itemAtPosition: Transaction) -> Unit
) : ListAdapter<Transaction, TransactionsAdapter.TransactionHistoryVH>(object :
    DiffUtil.ItemCallback<Transaction>() {

    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem == newItem
    }

}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHistoryVH {
        //inflate the view to be used by the payment option view holder
        val binding = ItemLayoutTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionHistoryVH(binding, onItemClick = { position ->
            val itemAtPosition = currentList[position]
            this.onItemClicked(position, itemAtPosition)
        })

    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: TransactionHistoryVH, position: Int) {
        val itemAtPosition = currentList[position]
        holder.bind(itemAtPosition)
    }


    inner class TransactionHistoryVH(private val binding: ItemLayoutTransactionBinding, onItemClick: (position: Int) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick(adapterPosition)
            }
        }


        fun bind(transaction: Transaction) {
            Timber.d("$transaction")
            with(binding) {
                transactionTitleTV.text = transaction.transactionTypeName
                transactionAmountTV.text = StringBuilder("₦").append(
                    Utils.formatAmount(
                        transaction.amount
                    )
                )
                transactionDateTV.text = Utils.formatTransactionDate(transaction.transactionDate)
                transactionStatusTV.text = transaction.statusName
                if (transaction.statusName.equals("Successful", true)) {
                    transactionStatusTV.setTextColor(context.resources.getColor(R.color.green))
                } else {
                    transactionStatusTV.setTextColor(context.resources.getColor(R.color.red))
                }
                if (transaction.isCredit) {
                    transactionTypeIV.setImageResource(R.drawable.ic_arrow_credit)
                    transactionTypeIV.setBackgroundResource(R.drawable.bg_rounded_square_credit)
                } else {
                    transactionTypeIV.setImageResource(R.drawable.ic_arrow_debit)
                    transactionTypeIV.setBackgroundResource(R.drawable.bg_rounded_square_debit)
                }
            }
        }

    }
}