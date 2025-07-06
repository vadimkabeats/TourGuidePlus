package com.example.tourguideplus.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tourguideplus.R
import com.example.tourguideplus.ui.adapter.PlaceListAdapter
import com.example.tourguideplus.ui.viewmodel.PlaceViewModel
import kotlinx.coroutines.launch

class PlacesListFragment : Fragment() {
    private lateinit var viewModel: PlaceViewModel
    private lateinit var adapter: PlaceListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_places_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1) Настраиваем ViewModel и Adapter
        viewModel = ViewModelProvider(requireActivity())[PlaceViewModel::class.java]
        adapter = PlaceListAdapter { place ->
            // TODO: обработка клика
        }

        // 2) Ищем RecyclerView по id
        val rvPlaces = view.findViewById<RecyclerView>(R.id.rv_places)
        rvPlaces.layoutManager = LinearLayoutManager(requireContext())
        rvPlaces.adapter = adapter
        rvPlaces.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )

        // 3) Подписываемся на поток из ViewModel
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.places.collect { list ->
                adapter.submitList(list)
            }
        }
    }
}

