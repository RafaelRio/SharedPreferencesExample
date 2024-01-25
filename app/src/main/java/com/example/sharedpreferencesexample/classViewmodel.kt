package com.example.sharedpreferencesexample

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class classViewmodel @Inject constructor(): ViewModel() {

    private var _name = MutableStateFlow(String())
    var name = _name.asStateFlow()

    fun setName(newName: String) {
        _name.value = newName
    }
}