package com.example.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.post.databinding.FragmentSecondBinding
import com.google.gson.Gson


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
data class UserInfo(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    var listAdapter: ExpandableListAdapter? = null
    var expListView: ExpandableListView? = null
    var listDataHeader: List<String>? = null
    var listDataChild: HashMap<String, List<String>>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
       var bundle = getArguments()
        var byteArray = bundle!!.getByteArray("byteArray")
        val str = String(byteArray!!)
        val gson = Gson()

        var userInfos = gson.fromJson(str, Array<UserInfo>::class.java).asList()

        var listDataHeader = arrayListOf<String>()
        var listDataChild = hashMapOf<String, List<String>>()
        for (i in 0 until userInfos.size-1) {
            //println("i = $i")
            var title = userInfos[i].title
            //println(title)
            listDataHeader.add(title)
            var info: MutableList<String> = mutableListOf()
            info.add(userInfos[i].id.toString())
            info.add(userInfos[i].userId.toString())
            info.add(userInfos[i].title)
            info.add(userInfos[i].body)
            listDataChild.put(title, info)
        }

        val adapter = CustomExpandableListAdapter(requireContext(), listDataHeader, listDataChild)
        //ListAdapter(requireContext(), android.R.layout.simple_expandable_list_item_1, listItems)
        binding.myListView.setAdapter(adapter)
        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}