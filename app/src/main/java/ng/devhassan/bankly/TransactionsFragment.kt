package ng.devhassan.bankly

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import ng.devhassan.bankly.databinding.FragmentTransactionsBinding

@AndroidEntryPoint
class TransactionsFragment : Fragment() {

    private var _binding: FragmentTransactionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var transactionsAdapter: TransactionsAdapter
    private val viewModel: TransactionsViewModel by activityViewModels()

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

    private fun initSearchViewListener() {
        binding.transactionsSearchViewET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.filterBySearchQuery(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun updateSelectedTab(transactionType: Transactions) {
        when(transactionType) {
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

            }
        )
        binding.transactionsRV.adapter = transactionsAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}