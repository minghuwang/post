package com.example.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.util.*

class PostViewModel : ViewModel() {
    private val liveDataNotes = MutableLiveData<List<String>>()
    private val liveDataComments = MutableLiveData<HashMap<String, List<String>>>()

    fun getData() {
        println("getData")
        viewModelScope.launch {
            var postModel = PostModel()
            var notes: ByteArray = postModel.HttpGetNotes()
            var comments: ByteArray = postModel.HttpGetComments()

            liveDataNotes.value = parseNotes(notes)
            liveDataComments.value = parseComments(comments)
        }
    }

    fun getNotes(): LiveData<List<String>> {
        return liveDataNotes
    }

    fun getComments(): LiveData<HashMap<String, List<String>>> {
        return liveDataComments;
    }

    private fun parseNotes(notesByte: ByteArray): ArrayList<String> {
        val gson = Gson()
        println("notesByte: ")
        println(notesByte)
        var notesList: ArrayList<String> = ArrayList<String>()
        if (notesByte != null) {
            val notesStr = String(notesByte)
            var notes = gson.fromJson(notesStr, Array<Note>::class.java).asList()
            for (i in 0 until notes.size - 1) {

                var id = "id:" + notes[i].id.toString()
                var userId = "userId:" + notes[i].userId.toString()
                var title = "title: " + notes[i].title
                var body = "body" + notes[i].body

                var header = id + "\n" + userId + "\n" + title + "\n" + body
                notesList.add(header)
            }
        } else {
            print("noteBytes == null")
        }
//        println("Last header: ")
//        println(lastHeader)
        return notesList
    }

    private fun parseComments(commentsByte: ByteArray): HashMap<String, List<String>> {
        var commentsMap = hashMapOf<String, List<String>>() //comments
        val gson = Gson()
        val commentsStr = String(commentsByte)
        var comments = gson.fromJson(commentsStr, Array<Comment>::class.java).asList()
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
                commentsMap.put(postId.toString(), commentList)
                postId = currentPostId
                commentList = mutableListOf()
            }
        }
//        println("last commentList: ")
//        println(commentList)
        return commentsMap
    }
}