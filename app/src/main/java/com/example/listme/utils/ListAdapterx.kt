package com.example.listme.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.listme.databinding.EachListItemBinding

class ListAdapterx(private val list:MutableList<ListData>) :
    RecyclerView.Adapter<ListAdapterx.ListViewHolder>() {

    private var listener:ListAdapterClicksInterface? = null
    fun setListener(listener:ListAdapterClicksInterface){
        this.listener = listener
    }
    inner class ListViewHolder(val binding: EachListItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = EachListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        with(holder){
            with(list[position]){
                binding.todoTask.text = this.task

                binding.deleteTask.setOnClickListener {
                    listener?.onDeleteTaskBtnClicked(this)
                }

                binding.editTask.setOnClickListener {
                    listener?.onEditTaskBtnClicked(this)
                }
            }
        }
    }

    interface ListAdapterClicksInterface{
        fun onDeleteTaskBtnClicked(toDoData: ListData)
        fun onEditTaskBtnClicked(toDoData: ListData)
    }
}
