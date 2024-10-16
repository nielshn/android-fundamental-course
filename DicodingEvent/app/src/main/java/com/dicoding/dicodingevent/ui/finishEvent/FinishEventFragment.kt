package com.dicoding.dicodingevent.ui.finishEvent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dicoding.dicodingevent.databinding.FragmentDashboardBinding
import com.dicoding.dicodingevent.ui.home.EventAdapter

class FinishEventFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val dashboardViewModel by viewModels<FinishEventViewModel>()
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()

        dashboardViewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }

        dashboardViewModel.events.observe(viewLifecycleOwner){
            eventAdapter.submitList(it)
        }

        dashboardViewModel.fetchFinishedEvents()
        return root
    }



    private fun setupRecyclerView() {
        eventAdapter = EventAdapter()
        binding.rvFinishEvents.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = eventAdapter
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
//
//    private fun showErrorToast(message: String){
//        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
//    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}