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
import com.example.listme.utils.ListData
import com.google.android.material.textfield.TextInputEditText

class AddListPopupFragment : DialogFragment() {

    private lateinit var binding: FragmentAddListPopupBinding
    private lateinit var listener: DialogNextBtnClickListener
    private var listData: ListData? = null

    fun setListener(listener : DialogNextBtnClickListener){
        this.listener = listener
    }

    companion object{
        const val TAG = "AddListPopupFragment"

        @JvmStatic
        fun newInstance(taskId:String, task:String) = AddListPopupFragment().apply {
            arguments = Bundle().apply {
                putString("taskId", taskId)
                putString("task", task)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddListPopupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(arguments != null){
            listData = ListData(
                arguments?.getString("taskId").toString(),
                arguments?.getString("task").toString()
            )
            binding.todoInput.setText(listData?.task)
        }
        registerEvents()
    }

    private fun registerEvents(){
        binding.todoNextBtn.setOnClickListener {
            val todoTask = binding.todoInput.text.toString()
            if(todoTask.isNotEmpty()){
                if(listData == null){
                    listener.onSaveTask(todoTask, binding.todoInput)
                }else{
                    listData?.task = todoTask
                    listener.onUpdateTask(listData!!, binding.todoInput)
                }
            }else{
                Toast.makeText(context, "Please type some task", Toast.LENGTH_SHORT).show()
            }
        }

        binding.todoClose.setOnClickListener {
            dismiss()
        }
    }

    interface DialogNextBtnClickListener{
        fun onSaveTask(todo : String, todoInput: TextInputEditText)
        fun onUpdateTask(listData: ListData, todoInput: TextInputEditText)
    }

}