package com.dicoding.mynoteappwithroom.ui.insert

import android.app.Application
import androidx.lifecycle.ViewModel
import com.dicoding.mynoteappwithroom.database.Note
import com.dicoding.mynoteappwithroom.repository.NoteRepository

class NoteAddUpdateViewModel(application: Application): ViewModel() {
    private val mNoteRepository: NoteRepository = NoteRepository(application)
    fun insert(note: Note){
        mNoteRepository.insert(note)
    }

    fun update(note: Note){
        mNoteRepository.update(note)
    }

    fun delete(note: Note){
        mNoteRepository.delete(note)
    }
}