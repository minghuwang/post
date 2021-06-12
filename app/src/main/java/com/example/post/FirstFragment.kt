package com.example.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.beust.klaxon.Parser
import com.example.post.databinding.FragmentFirstBinding

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking



/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var listView: ListView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var bundle = Bundle()

        super.onViewCreated(view, savedInstanceState)
//        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
//        }
        runBlocking {
            println("start: ${Thread.currentThread().name}")
            // Todo: connect to server and get the post/comments
            var notes = GlobalScope.async {
                println("async start: ${Thread.currentThread().name}")
                val httpClient = HttpClient(Android)
                try {
                    var resp =
                        httpClient.get<HttpResponse>("https://jsonplaceholder.typicode.com/posts") {
                            headers {
                                append("Accept", "application/json")
                            }
                        }
                    if (resp.status == HttpStatusCode.OK) {
                        println("resp.status == HttpStatusCode.OK")
//                        val parser: Parser = Parser.default()
                        val bytes: ByteArray = resp.readBytes()
                        bundle.putByteArray("notes", bytes)
                    } else {
                        println("httpStatusCode:")
                        println(resp.status)
                    }
                    httpClient.close()
                } catch (e: java.net.UnknownHostException) {
                    println("exception of http")
                    println(e)
                }

                // Thread.sleep(1000)
                println("async end: ${Thread.currentThread().name}")

            }
            notes.join()
            var comments = GlobalScope.async {
                val httpClient = HttpClient(Android)
                try {
                    var resp =
                        httpClient.get<HttpResponse>("https://jsonplaceholder.typicode.com/comments") {
                            headers {
                                append("Accept", "application/json")
                            }
                        }
                    if (resp.status == HttpStatusCode.OK) {
                        println("comments: resp.status == HttpStatusCode.OK")
                        val parser: Parser = Parser.default()
                        val bytes: ByteArray = resp.readBytes()
                        bundle.putByteArray("comments", bytes)

                    } else {
                        println("httpStatusCode:")
                        println(resp.status)
                    }
                    httpClient.close()
                } catch (e: java.net.UnknownHostException) {
                    println("exception of http")
                    println(e)
                }

                // Thread.sleep(1000)
                println("async end: ${Thread.currentThread().name}")

            }
            comments.join()
            binding.buttonFirst.setOnClickListener {
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
            }
            //Thread.sleep(3000)
            println("end: ${Thread.currentThread().name}")
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}