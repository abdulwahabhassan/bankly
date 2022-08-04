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
            MutableLiveData<TFUiState> = MutableLiveData(TFUiState(Transactions.ALL, emptyList()))
    val transactions: LiveData<TFUiState> = _transactions

    private val _transactionType = MutableStateFlow(Transactions.ALL)

    init {
        viewModelScope.launch {
            retrieveTransactions()
        }
    }

    private suspend fun retrieveTransactions() {
        combine(
            _transactionType,
            flowOf(transactionsRepository.getTransactions())
        ) { transactionType, transactions ->
            when (transactionType) {
                Transactions.ALL -> {
                    TFUiState(
                        Transactions.ALL,
                        transactions
                    )
                }
                Transactions.CREDIT -> {
                    TFUiState(
                        Transactions.CREDIT,
                        transactions?.filter { it.isCredit }
                    )

                }
                Transactions.DEBIT -> {
                    TFUiState(
                        Transactions.DEBIT,
                        transactions?.filter { !it.isCredit }
                    )

                }
            }
        }.collect { transactions ->
            _transactions.value = transactions
        }
    }

    fun filterByAll() {
        _transactionType.value = Transactions.ALL
    }

    fun filterByCredit() {
        _transactionType.value = Transactions.CREDIT
    }

    fun filterByDebit() {
        _transactionType.value = Transactions.DEBIT
    }

}