package com.example.listme.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.example.listme.R
import com.example.listme.databinding.FragmentHomeBinding
import com.example.listme.utils.ListAdapterx
import com.example.listme.utils.ListData
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment(), AddListPopupFragment.DialogNextBtnClickListener,
    ListAdapterx.ListAdapterClicksInterface {

    private lateinit var auth : FirebaseAuth
    private lateinit var databaseRef : DatabaseReference
    private lateinit var navController: NavController
    private lateinit var binding: FragmentHomeBinding
    private var popUpFragment: AddListPopupFragment? = null
    private lateinit var adapter: ListAdapterx
    private lateinit var mList: MutableList<ListData>
    private lateinit var user : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        getDataFromFirebase()
        registerEvents()

        user = FirebaseAuth.getInstance()
        binding.logoutHome.setOnClickListener {
            user.signOut()
            navController.navigate(R.id.action_homeFragment_to_loginFragment)
        }
    }

    private fun registerEvents(){
        binding.addBtnHome.setOnClickListener {
            if(popUpFragment != null)
                childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()
            popUpFragment = AddListPopupFragment()
            popUpFragment!!.setListener(this)
            popUpFragment!!.show(
                childFragmentManager,
                AddListPopupFragment.TAG
            )
        }
    }

    private fun init(view: View){
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference
            .child("Tasks").child(auth.currentUser?.uid.toString())

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        mList = mutableListOf()
        adapter = ListAdapterx(mList)
        adapter.setListener(this)
        binding.recyclerView.adapter = adapter
    }

    private fun getDataFromFirebase(){
        databaseRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
                for (taskSnapshot in snapshot.children){
                    val listTask = taskSnapshot.key?.let{
                        ListData(it, taskSnapshot.value.toString())
                    }

                    if(listTask != null){
                        mList.add(listTask)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message , Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onSaveTask(todo: String, todoInput: TextInputEditText) {

        databaseRef.push().setValue(todo).addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(context, "List saved successfully!" , Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, it.exception?.message , Toast.LENGTH_SHORT).show()
            }
            todoInput.text = null
            popUpFragment!!.dismiss()
        }
    }

    override fun onUpdateTask(listData: ListData, todoInput: TextInputEditText) {
        val map = HashMap<String, Any>()
        map[listData.taskId] = listData.task
        databaseRef.updateChildren(map).addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(context, "Updated Successfully!" , Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, it.exception?.message , Toast.LENGTH_SHORT).show()
            }
            todoInput.text=null
            popUpFragment!!.dismiss()
        }
    }

    override fun onDeleteTaskBtnClicked(toDoData: ListData) {
        databaseRef.child(toDoData.taskId).removeValue().addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(context, "Deleted Successfully!" , Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, it.exception?.message , Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onEditTaskBtnClicked(toDoData: ListData) {
        if(popUpFragment != null)
            childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()

        popUpFragment = AddListPopupFragment.newInstance(toDoData.taskId , toDoData.task)
        popUpFragment!!.setListener(this)
        popUpFragment!!.show(childFragmentManager , AddListPopupFragment.TAG)
    }

}