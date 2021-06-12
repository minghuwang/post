package com.example.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.post.databinding.FragmentSecondBinding
import com.google.gson.Gson


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
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

    var listDataHeader: ArrayList<String> = arrayListOf()
    //var listDataNotes: HashMap<String, List<String>>? = hashMapOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        val gson = Gson()
        var bundle = getArguments()
        var notesByte = bundle!!.getByteArray("notes")
        val notesStr = String(notesByte!!)
        var notes = gson.fromJson(notesStr, Array<Note>::class.java).asList()

        for (i in 0 until notes.size - 1) {

            var id = "id:" + notes[i].id.toString()
            var userId = "userId:" + notes[i].userId.toString()
            var title = "title: " + notes[i].title
            var body = "body" + notes[i].body

            var header = id + "\n" + userId + "\n" + title + "\n" + body
            listDataHeader.add(header)
            //listDataNotes!!.put(notes[i].id.toString(), listDataHeader) //for future use
        }

        var commentsByte = bundle!!.getByteArray("comments")
        val commentsStr = String(commentsByte!!)
        var comments = gson.fromJson(commentsStr, Array<Comment>::class.java).asList()
        var listDataChild = hashMapOf<String, List<String>>() //comments
        //postId start from 1
        var postId = comments[0].postId
        var commentList: MutableList<String> = mutableListOf()
        for (i in 0 until comments.size - 1) {
            //Each time get post id, if it is the same as previous one, add into one commentList
            var currentPostId = comments[i].postId

            if (postId == currentPostId) {
                //comment includes postId, id, name, email, body
                var comment =
                    "postId: " + currentPostId.toString() + "\n" + "id: " + comments[i].id.toString() + "\n" + "name: " + comments[i].name + "\n" + "email: " + comments[i].email + "\n" + "body: " + comments[i].body
                commentList.add(comment)
            } else {
                listDataChild.put(postId.toString(), commentList)
                postId = currentPostId
                commentList = mutableListOf()
            }
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