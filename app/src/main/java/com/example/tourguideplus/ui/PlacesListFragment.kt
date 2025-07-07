package com.example.tourguideplus.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tourguideplus.R
import com.example.tourguideplus.data.model.Place
import com.example.tourguideplus.navigation.Screen
import com.example.tourguideplus.ui.adapter.PlaceListAdapter
import com.example.tourguideplus.ui.viewmodel.PlaceViewModel
import kotlinx.coroutines.launch

class PlacesListFragment : Fragment(R.layout.fragment_places_list) {
    private lateinit var viewModel: PlaceViewModel
    private lateinit var adapter: PlaceListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PlaceViewModel::class.java)

        adapter = PlaceListAdapter { place ->
            findNavController().navigate(
                PlacesListFragmentDirections
                    .actionToPlaceDetails(place.id)
            )
        }

        val rv = view.findViewById<RecyclerView>(R.id.rv_places).apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(context, VERTICAL))
            adapter = this@PlacesListFragment.adapter
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.places.collect { adapter.submitList(it) }
        }
    }
}
