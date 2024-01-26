package com.example.sharedpreferencesexample

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.sharedpreferencesexample.databinding.MainFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


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

        binding.apply {
            textView.text = spName

            btnSave.setOnClickListener {
                with (sharedPref.edit()) {
                    putString("name", viewModel.name.value)
                    apply()
                }
            }

            btnSeeSP.setOnClickListener {
                Toast.makeText(requireContext(), sharedPref.getString("name", "No hay nada guardado"), Toast.LENGTH_LONG).show()
            }

            btnClear.setOnClickListener {
                with (sharedPref.edit()) {
                    clear()
                    apply()
                }
            }

            etName.onTextChanged {
                viewModel.setName(it)
            }
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