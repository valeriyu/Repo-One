package com.valeriyu.materialdesign.ui.products

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.valeriyu.materialdesign.R
import com.valeriyu.materialdesign.databinding.FragmentProductsListBinding


class ProductsListFragment : Fragment(R.layout.fragment_products_list) {
    private val viewModel: ProductsListViewModel by viewModels()
    private val binding: FragmentProductsListBinding by viewBinding(FragmentProductsListBinding::bind)
    private var listAdapter: ProductsListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        bindViewModel()
        viewModel.getProductsList()
    }

    private fun bindViewModel() {
        viewModel.products.observe(viewLifecycleOwner) {
            listAdapter?.submitList(it)
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            if (it.isEmpty()) return@observe
            val navView: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
            Snackbar.make(binding.root, it, Snackbar.LENGTH_INDEFINITE)
                .setAction("Повторить.") {
                    Snackbar.make(it, "Список обновлён.", Snackbar.LENGTH_LONG)
                        .setAnchorView(navView)
                        .show()
                }
                .setMaxInlineActionWidth(2)
                .setAnchorView(navView)
                .show()
        }
    }

    private fun initList() {
        listAdapter = ProductsListAdapter()

        with(binding.productsList) {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            // itemAnimator = ScaleInAnimator()
            setHasFixedSize(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listAdapter = null
    }
}