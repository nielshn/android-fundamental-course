package com.dicoding.dicodingevent.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.data.response.EventResponse
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.data.retrofit.ApiConfig
import com.dicoding.dicodingevent.util.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvents: LiveData<List<ListEventsItem>> = _upcomingEvents

    private val _finishedEvents = MutableLiveData<List<ListEventsItem>>()
    val finishedEvents: LiveData<List<ListEventsItem>> = _finishedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    fun fetchAllEvents() {
        _isLoading.value = true

        // Fetch Upcoming Events
        ApiConfig.getApiService().getEvents(active = 1).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    val events = response.body()?.listEvents ?: emptyList()
                    _upcomingEvents.value = events.take(5) // Get up to 5 upcoming events
                } else {
                    Log.e("HomeViewModel", "Failed to fetch upcoming events: ${response.message()}")
                    _snackbarText.value = Event("Failed to fetch upcoming events: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                Log.e("HomeViewModel", "Failed to fetch upcoming events: ${t.message}")
                _snackbarText.value = Event("Failed to fetch finished events: ${t.message}")
            }
        })

        // Fetch Finished Events
        ApiConfig.getApiService().getEvents(active = 0).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    val events = response.body()?.listEvents ?: emptyList()
                    _finishedEvents.value = events.take(5) // Get up to 5 finished events
                } else {
                    Log.e("HomeViewModel", "Failed to fetch finished events: ${response.message()}")
                    _snackbarText.value = Event("Failed to fetch finished events: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                Log.e("HomeViewModel", "Failed to fetch finished events: ${t.message}")
                _snackbarText.value = Event("Failed to fetch finished events: ${t.message}")
            }
        })

        // Set loading to false after both requests are made
        _isLoading.value = false
    }

}
