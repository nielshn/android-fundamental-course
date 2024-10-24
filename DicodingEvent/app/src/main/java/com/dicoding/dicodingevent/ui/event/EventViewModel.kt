package com.dicoding.dicodingevent.ui.event

import android.util.Log
import android.widget.Toast
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

class EventViewModel(private val eventType: Int) : ViewModel() {

    private val _events =
        MutableLiveData<List<ListEventsItem>>(emptyList())
    val events: LiveData<List<ListEventsItem>> = _events

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    init {
        fetchEvents()
    }

    fun fetchEvents() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvents(eventType)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    // Handle the nullable listEvents safely
                    _events.value = response.body()?.listEvents ?: emptyList()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _snackbarText.value = Event("Failure to load events: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                _snackbarText.value = Event("Failed to load events: ${t.message}")
            }
        })
    }

    fun searchEvents(keyword: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvents(eventType)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val filteredEvents = response.body()?.listEvents?.filter {
                        it.name.contains(keyword, ignoreCase = true)
                    } ?: emptyList()
                    _events.value = filteredEvents
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "EventViewModel"
    }
}
