package com.example.myshop.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshop.MainActivity
import com.example.myshop.databinding.FragmentFavoriteBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val favoriteViewModel: FavoriteViewModel by activityViewModels()
    private lateinit var adapter: FavoriteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = FavoriteAdapter(
            deleteItem = { favoriteEntity -> favoriteViewModel.deleteFavorite(favoriteEntity.id) },
            onButtonClick = { product ->
                (requireActivity() as MainActivity).goToDetail(product.id)
            },
        )

        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            favoriteViewModel.getFavorite().collectLatest {
                if (it.isNullOrEmpty()) {
                    showErrorState()
                } else {
                    hideErrorState()
                    adapter.submitList(it)
                }
            }
        }

        binding.deleteAll.setOnClickListener {
            favoriteViewModel.deleteAllFavorite()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun hideErrorState() {
        binding.errorDesc.visibility = GONE
        binding.errorTitle.visibility = GONE
        binding.recyclerView.visibility = VISIBLE
    }

    private fun showErrorState() {
        binding.errorDesc.visibility = VISIBLE
        binding.errorTitle.visibility = VISIBLE
        binding.recyclerView.visibility = GONE
    }

}