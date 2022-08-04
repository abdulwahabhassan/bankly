package ng.devhassan.bankly

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(private val transactionsRepository: TransactionsRepository) :
    ViewModel() {
    private var _transactions:
            MutableLiveData<List<Transaction>> = MutableLiveData(emptyList())
    val transactions: LiveData<List<Transaction>> = _transactions

    private val transactionType = MutableStateFlow(Transactions.ALL)

    init {
        viewModelScope.launch {
            retrieveTransactions()
        }
    }

    private suspend fun retrieveTransactions() {
        combine(
            transactionType,
            flowOf(transactionsRepository.getTransactions())
        ) { transactionType, transactions ->
            when (transactionType) {
                Transactions.ALL -> {
                    transactions
                }
                Transactions.CREDIT -> {
                    transactions?.filter { it.isCredit }
                }
                Transactions.DEBIT -> {
                    transactions?.filter { !it.isCredit }
                }
            }
        }.collect { transactions ->
            _transactions.value = transactions
        }
    }

    fun filterByAll() {
        transactionType.value = Transactions.ALL
    }

    fun filterByCredit() {
        transactionType.value = Transactions.CREDIT
    }

    fun filterByDebit() {
        transactionType.value = Transactions.DEBIT
    }

}