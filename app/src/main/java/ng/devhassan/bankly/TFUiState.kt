package ng.devhassan.bankly

data class TFUiState(
    val transactionType: Transactions,
    val transactions: List<Transaction>?
)