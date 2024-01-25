package com.example.sharedpreferencesexample

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.sharedpreferencesexample.databinding.MainFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class classFragment : Fragment() {

    private lateinit var viewModel: classViewmodel
    private lateinit var binding: MainFragmentBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[classViewmodel::class.java]
        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return

        lifecycleScope.launch {
            autoCompleteText()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spName = sharedPref.getString("name", "")
        binding.textView.text = spName

        if (spName?.isBlank() == false) {
            binding.etName.isEnabled = false
        }

        binding.btnSave.setOnClickListener {
            with (sharedPref.edit()) {
                putString("name", viewModel.name.value)
                apply()
            }
        }

        binding.btnSeeSP.setOnClickListener {
            Toast.makeText(requireContext(), sharedPref.getString("name", "No hay nada guardado"), Toast.LENGTH_LONG).show()
        }
        
        binding.btnClear.setOnClickListener {
            with (sharedPref.edit()) {
                clear()
                apply()
            }
        }


        binding.etName.onTextChanged {
            viewModel.setName(it)
        }
    }

    private suspend fun autoCompleteText() {
        viewModel.name.flowWithLifecycle(lifecycle).collectLatest { name ->
            binding.textView.text = name
        }
    }
}

inline fun TextView.onTextChanged(
    crossinline action: (String) -> Unit,
) {
    doAfterTextChanged {
        if (hasFocus()) {
            action(it.toString())
        }
    }
}

fun TextView.setTextIfDistinct(text: CharSequence?) {
    val textAsString = text.toString()

    if (textAsString == this.text.toString()) {
        return
    }

    val hasFocus = hasFocus()

    if (hasFocus) {
        clearFocus()
    }

    if (this.text.toString() != textAsString) {
        this.text = textAsString
    }

    if (hasFocus) {
        requestFocus()
    }
}