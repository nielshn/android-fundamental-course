package com.dicoding.dicodingevent.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.data.response.DetailEventItem
import com.dicoding.dicodingevent.databinding.ActivityDetailEventBinding

class DetailEventActivity : AppCompatActivity() {
    private lateinit var activityDetailEventBinding: ActivityDetailEventBinding
    private val viewModel: DetailEventViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        activityDetailEventBinding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(activityDetailEventBinding.root)

        setSupportActionBar(activityDetailEventBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detail Event"

        activityDetailEventBinding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val eventId = intent.getIntExtra("EVENT_ID", -1)

        if (eventId != -1) {
            viewModel.fetchEventDetail(eventId.toString())
        }

        viewModel.eventDetail.observe(this) { eventDetail ->
            if (eventDetail != null) {
                updateUI(eventDetail)
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun updateUI(eventDetail: DetailEventItem) {
        activityDetailEventBinding.apply {

            eventName.text = eventDetail.name
            eventOwner.text = eventDetail.ownerName
            eventTime.text = eventDetail.beginTime
            val sisaQuota = eventDetail.quota - eventDetail.registrants
            eventSisaQuota.text = "Sisa Quota: $sisaQuota"
            eventDescription.text = HtmlCompat.fromHtml(
                eventDetail.description,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            Glide.with(this@DetailEventActivity)
                .load(eventDetail.mediaCover)
                .into(eventImage)

            registerButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(eventDetail.link)
                startActivity(intent)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        activityDetailEventBinding.progressBar.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }

}