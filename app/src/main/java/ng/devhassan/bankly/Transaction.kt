package ng.devhassan.bankly

import com.squareup.moshi.Json

data class Transaction(
    val creditAccountNo: String?,
    val debitAccountNo: String?,
    val transactionBy: String?,
    val channelName: String?,
    val statusName: String?,
    val userType: Long,

    @Json(name = "userId")
    val userID: Long,

    val initiator: Any?,
    val archived: Boolean,
    val product: Any?,
    val hasProduct: Boolean,
    val senderName: String?,
    val receiverName: String?,
    val balanceBeforeTransaction: Double,
    val id: Long,
    val reference: String,
    val transactionType: Long,
    val transactionTypeName: String?,
    val description: String,
    val narration: String,
    val amount: Double,
    val creditAccountNumber: String,
    val deditAccountNumber: String,
    val parentReference: String,
    val transactionDate: String,
    val credit: Double?,
    val debit: Double?,
    val balanceAfterTransaction: Double,
    val sender: String?,
    val receiver: String?,
    val channel: Long,
    val status: Long,
    val charges: Double?,
    val aggregatorCommission: Double?,
    val hasCharges: Boolean,
    val agentCommission: Long?,
    val debitAccountNumber: String,
    val initiatedBy: Any?,

    @Json(name = "stateId")
    val stateID: Long?,

    @Json(name = "lgaId")
    val lgaID: Long?,

    @Json(name = "regionId")
    val regionID: Long?,

    @Json(name = "aggregatorId")
    val aggregatorID: Long?,

    val isCredit: Boolean
)
