package ng.devhassan.bankly.repo

import ng.devhassan.bankly.datasource.Datasource
import ng.devhassan.bankly.model.Transaction
import javax.inject.Inject

class TransactionsRepository @Inject constructor(private val datasource: Datasource) {
    fun getTransactions(): List<Transaction>? {
        return datasource.fetchTransactions()
    }
}