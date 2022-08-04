package ng.devhassan.bankly

import android.os.Bundle
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

        initClickListeners()

        initTransactionsRecyclerViewAdapter()

        viewModel.transactions.observe(viewLifecycleOwner) { transactions ->
            transactionsAdapter.submitList(transactions)
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