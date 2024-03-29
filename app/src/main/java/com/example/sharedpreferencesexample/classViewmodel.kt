package com.example.sharedpreferencesexample

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


class classViewmodel: ViewModel() {

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()
    fun setName(newName: String) {
        _name.value = newName
    }
}