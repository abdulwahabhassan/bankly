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

    private val _searchQuery = MutableStateFlow("")

    init {
        viewModelScope.launch {
            retrieveTransactions()
        }
    }

    private suspend fun retrieveTransactions() {
        combine(
            _transactionType,
            flowOf(transactionsRepository.getTransactions()),
            _searchQuery
        ) { transactionType, transactions, searchQuery ->
            when (transactionType) {
                Transactions.ALL -> {
                    TFUiState(
                        transactionType,
                        transactions?.filter {
                            searchFilterPredicate(searchQuery, it)
                        }
                    )
                }
                Transactions.CREDIT -> {
                    TFUiState(
                        transactionType,
                        transactions?.filter { it.isCredit }?.filter {
                            searchFilterPredicate(searchQuery, it)
                        }
                    )

                }
                Transactions.DEBIT -> {
                    TFUiState(
                        transactionType,
                        transactions?.filter { !it.isCredit }?.filter {
                            searchFilterPredicate(searchQuery, it)
                        }
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

    fun filterBySearchQuery(searchQuery: String) {
        _searchQuery.value = searchQuery
    }

    private fun searchFilterPredicate(searchQuery: String, transaction: Transaction): Boolean {
        return transaction.transactionTypeName?.contains(searchQuery, true) == true ||
                transaction.transactionDate.contains(searchQuery, true) ||
                transaction.amount.toString().contains(searchQuery, true) ||
                transaction.statusName?.contains(searchQuery, true) == true
    }

}