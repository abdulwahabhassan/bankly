package ng.devhassan.bankly.model

import ng.devhassan.bankly.Transactions

data class TFUiState(
    val transactionType: Transactions,
    val transactions: List<Transaction>?
)