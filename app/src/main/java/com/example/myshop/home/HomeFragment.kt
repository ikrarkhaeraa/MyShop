package com.example.myshop.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.SealedClass
import com.example.core.retrofit.response.Products
import com.example.core.retrofit.response.SearchProducts
import com.example.myshop.MainActivity
import com.example.myshop.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var listProduct: PagingData<Products>
    private lateinit var searchProducts: List<SearchProducts>
    private var searchQuery = MutableStateFlow("")

    private val pagingProductAdapter = HomePagingAdapter { product ->
        (requireActivity() as MainActivity).goToDetail(product.id)
    }

    private val searchProductAdapter = HomeSearchAdapter { product ->
        (requireActivity() as MainActivity).goToDetail(product.id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.searchTextField.doOnTextChanged { text, _, _, _->
            searchQuery.value = binding.searchTextField.text.toString()
            Log.d("cekQuery", searchQuery.value)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            requestAndGetData()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            pagingProductAdapter.loadStateFlow.collectLatest { pagingState ->
                val isLoading = pagingState.refresh is LoadState.Loading
                val isError = pagingState.refresh is LoadState.Error
                val isSuccess = pagingState.refresh is LoadState.NotLoading

                if (isLoading) {
                    showLoading(true)
                    hideErrorState()
                } else if (isSuccess) {
                    showLoading(false)
                    hideErrorState()
                } else if (isError) {
                    showLoading(false)
                    showErrorState()
                }
            }
        }
    }

    private suspend fun requestAndGetData() {
        val filterData: StateFlow<String> = searchQuery
        filterData.collectLatest {
            if (searchQuery.value == "") {
                Log.d("cekHomeFlow", "masuk paging")
                binding.recyclerView.adapter = pagingProductAdapter
                binding.recyclerView.adapter = pagingProductAdapter.withLoadStateFooter(
                    footer = LoadingStateAdapter { pagingProductAdapter.retry() }
                )
                homeViewModel.getPagingProduct().collectLatest{
                    listProduct = it
                    pagingProductAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                }
            }
            else {
                Log.d("cekHomeFlow", "masuk search")
                binding.recyclerView.adapter = searchProductAdapter
                homeViewModel.getSearchProductData(searchQuery.value)
                homeViewModel.productSearchData.collectLatest {
                    when (it) {
                        is SealedClass.Init -> {

                        }
                        is SealedClass.Loading -> {
                            hideErrorState()
                            showLoading(true)
                        }
                        is SealedClass.Success -> {
                            hideErrorState()
                            showLoading(false)
                            searchProducts = it.data.products
                            searchProductAdapter.submitList(searchProducts)
                        }
                        is SealedClass.Error -> {
                            showLoading(false)
                            showErrorState()
                        }
                        else -> {}
                    }
                }
            }
        }
    }


    private fun hideErrorState() {
        binding.errorDesc.visibility = GONE
        binding.errorTitle.visibility = GONE
        binding.resetButton.visibility = GONE
    }

    private fun showErrorState() {
        binding.errorDesc.visibility = VISIBLE
        binding.errorTitle.visibility = VISIBLE
        binding.resetButton.visibility = VISIBLE
        binding.resetButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                requestAndGetData()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}