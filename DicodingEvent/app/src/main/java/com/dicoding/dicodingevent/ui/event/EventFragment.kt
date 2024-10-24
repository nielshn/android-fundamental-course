package com.dicoding.dicodingevent.ui.event

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dicoding.dicodingevent.EventAdapter
import com.dicoding.dicodingevent.databinding.FragmentEventBinding
import com.dicoding.dicodingevent.ui.DetailEventActivity
import com.google.android.material.snackbar.Snackbar

class EventFragment : Fragment() {

    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventAdapter: EventAdapter
    private val eventViewModel: EventViewModel by viewModels {
        EventViewModelFactory(requireArguments().getInt("eventType", 1))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEventBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Get eventType from arguments
        val eventType = arguments?.getInt("eventType", 1) ?: 1

        setupRecyclerView(eventType)

        // Observe the LiveData
        eventViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        eventViewModel.events.observe(viewLifecycleOwner) {
            eventAdapter.submitList(it)
        }

        eventViewModel.snackbarText.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { message ->
                showSnackbar(message)
            }
        }

        // Setup SearchView
        setupSearchView()

        return root
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun setupRecyclerView(eventType: Int) {
        eventAdapter = EventAdapter { event ->
            val intent = Intent(requireContext(), DetailEventActivity::class.java).apply {
                putExtra("EVENT_ID", event.id)
            }
            startActivity(intent)
        }
        binding.rvEvents.apply {
            layoutManager = if (eventType == 0) {
                // Finished events use staggered grid layout
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            } else {
                // Upcoming events use linear layout
                LinearLayoutManager(requireContext())
            }
            adapter = eventAdapter
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()) {
                    eventViewModel.fetchEvents()
                } else {
                    eventViewModel.searchEvents(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    eventViewModel.fetchEvents()
                }
                return false
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
