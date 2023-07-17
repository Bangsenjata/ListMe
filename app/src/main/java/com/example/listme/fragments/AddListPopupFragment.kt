package com.example.listme.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.listme.R
import com.example.listme.databinding.FragmentAddListPopupBinding
import com.example.listme.databinding.FragmentHomeBinding
import com.google.android.material.textfield.TextInputEditText

class AddListPopupFragment : DialogFragment() {

    private lateinit var binding: FragmentAddListPopupBinding
    private lateinit var listener: DialogNextBtnClickListener

    fun setListener(listener : DialogNextBtnClickListener){
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddListPopupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerEvents()
    }

    private fun registerEvents(){
        binding.todoNextBtn.setOnClickListener {
            val todoTask = binding.todoInput.text.toString()
            if(todoTask.isNotEmpty()){
                listener.onSaveTask(todoTask, binding.todoInput)
            }else{
                Toast.makeText(context, "Please type some task", Toast.LENGTH_SHORT).show()
            }
        }

        binding.todoClose.setOnClickListener {
            dismiss()
        }
    }

    interface DialogNextBtnClickListener{
        fun onSaveTask(todo : String, todoInput : TextInputEditText)
    }

}