package com.example.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.post.adapters.CustomExpandableListAdapter
import com.example.post.databinding.FragmentSecondBinding

data class Note(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

data class Comment(
    val postId: Int,
    val id: Int,
    val name: String,
    val email: String,
    val body: String
)

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    var adapter: BaseExpandableListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        // Create a ViewModel the first time the system calls an activity's onCreate() method.
        // Re-created activities receive the same MyViewModel instance created by the first activity.

        // Use the 'by viewModels()' Kotlin property delegate
        // from the activity-ktx artifact
        val postViewModel: PostViewModel by viewModels()
        //Or through this way to get postViewModel
        // var postViewModel = ViewModelProvider(this).get(PostViewModel::class.java)

        var listDataHeader = listOf("")
        var listDataChild = hashMapOf("" to listOf(""))
        var adapter =  CustomExpandableListAdapter(requireContext(), listDataHeader, listDataChild)

//        postViewModel.getNotes().observe(viewLifecycleOwner, { liveDataNotes ->
//            print("Find notes change")
//            listDataHeader = liveDataNotes
//            println("listDataHeader begin")
//            println(listDataHeader[0])
//            println("listDataHeader end")
//            adapter =
//                CustomExpandableListAdapter(requireContext(), listDataHeader, listDataChild)
//            if (binding.myListView.getExpandableListAdapter() == null) {
//                binding.myListView.setAdapter(adapter)
//            }
//            adapter.notifyDataSetChanged()
//        })
        postViewModel.getComments()
            .observe(viewLifecycleOwner, { map ->
                print("Find comments change")
                listDataChild = map

                adapter =
                    CustomExpandableListAdapter(requireContext(), postViewModel.getNotes().value!!, listDataChild)
                if (binding.myListView.getExpandableListAdapter() == null) {
                    binding.myListView.setAdapter(adapter)
                }
                adapter.notifyDataSetChanged()
            })
        binding.buttonSecond.setOnClickListener {
            postViewModel.getData()
            // findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}