package ng.devhassan.bankly.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import ng.devhassan.bankly.*
import ng.devhassan.bankly.adapter.TransactionsAdapter
import ng.devhassan.bankly.databinding.BottomSheetTransactionDetailsBinding
import ng.devhassan.bankly.databinding.FragmentTransactionsBinding
import ng.devhassan.bankly.helper.Utils
import ng.devhassan.bankly.model.Transaction
import ng.devhassan.bankly.vm.TransactionsViewModel

@AndroidEntryPoint
class TransactionsFragment : Fragment() {

    private var _binding: FragmentTransactionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var transactionsAdapter: TransactionsAdapter
    private val viewModel: TransactionsViewModel by activityViewModels()
    private var bottomSheetDialog: BottomSheetDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransactionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTransactionsRecyclerViewAdapter()

        initClickListeners()

        initSearchViewListener()

        viewModel.transactions.observe(viewLifecycleOwner) { uiState ->
            transactionsAdapter.submitList(uiState.transactions)
            updateSelectedTab(uiState.transactionType)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initSearchViewListener() {

        binding.transactionsSearchViewET.setOnKeyListener { searchView, i, event ->
            if ((event.action == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                Utils.hideKeyboard(requireContext(), searchView)
            }
            return@setOnKeyListener false
        }

        binding.transactionsSearchViewET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.filterBySearchQuery(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun updateSelectedTab(transactionType: Transactions) {
        when (transactionType) {
            Transactions.ALL -> {
                binding.allLiner.setBackgroundResource(R.color.blue_purple)
                binding.creditLiner.setBackgroundResource(R.color.white)
                binding.debitLiner.setBackgroundResource(R.color.white)
            }
            Transactions.CREDIT -> {
                binding.creditLiner.setBackgroundResource(R.color.blue_purple)
                binding.allLiner.setBackgroundResource(R.color.white)
                binding.debitLiner.setBackgroundResource(R.color.white)
            }
            Transactions.DEBIT -> {
                binding.debitLiner.setBackgroundResource(R.color.blue_purple)
                binding.allLiner.setBackgroundResource(R.color.white)
                binding.creditLiner.setBackgroundResource(R.color.white)
            }
        }
    }

    private fun initClickListeners() {
        binding.backArrowIB.setOnClickListener {
            activity?.finish()
        }

        binding.allTV.setOnClickListener {
            viewModel.filterByAll()
        }

        binding.creditTV.setOnClickListener {
            viewModel.filterByCredit()
        }

        binding.debitTV.setOnClickListener {
            viewModel.filterByDebit()
        }
    }

    private fun initTransactionsRecyclerViewAdapter() {
        transactionsAdapter = TransactionsAdapter(
            requireContext(),
            onItemClicked = { position: Int, itemAtPosition: Transaction ->
                if (bottomSheetDialog == null) {
                    showTransactionDetails(itemAtPosition)
                }
            }
        )
        binding.transactionsRV.adapter = transactionsAdapter
    }

    private fun showTransactionDetails(transaction: Transaction) {
        val dialogBinding = BottomSheetTransactionDetailsBinding.inflate(
            LayoutInflater.from(requireContext()),
            this.binding.root,
            false
        )

        bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog?.setContentView(dialogBinding.root)
        bottomSheetDialog?.dismissWithAnimation = true
        bottomSheetDialog?.behavior?.isDraggable = true
        bottomSheetDialog?.behavior?.isHideable = true
        bottomSheetDialog?.setOnDismissListener {
            bottomSheetDialog = null
        }
        bottomSheetDialog?.setOnCancelListener {
            bottomSheetDialog = null
        }

        dialogBinding.dateTV.text = Utils.formatTransactionDate(transaction.transactionDate)
        dialogBinding.receiverNameTV.text = transaction.receiver
        dialogBinding.senderNameTV.text = transaction.sender
        dialogBinding.referenceTV.text = transaction.reference
        dialogBinding.transactionTypeNameTV.text = transaction.transactionTypeName
        dialogBinding.amountTV.text = StringBuilder("₦").append(
            Utils.formatAmount(
                transaction.amount
            )
        )
        bottomSheetDialog?.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}