package com.example.post

import com.beust.klaxon.Parser
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class PostModel {

    suspend fun HttpGetComments(): ByteArray {
        var comments: ByteArray = ByteArray(0)
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
                //val parser: Parser = Parser.default()
                comments = resp.readBytes()
                println("comments content:")
                println(comments)
            } else {
                println("httpStatusCode:")
                println(resp.status)
            }
            httpClient.close()
        } catch (e: java.net.UnknownHostException) {
            println("exception of http")
            println(e)
        }
        println("async end: ${Thread.currentThread().name}")
        return comments
    }

    suspend fun HttpGetNotes(): ByteArray{
        var notes: ByteArray = ByteArray(0)
        println("start: ${Thread.currentThread().name}")
        // Connect to server and get the post
        try {
            val httpClient = HttpClient(Android)
            var resp =
                httpClient.get<HttpResponse>("https://jsonplaceholder.typicode.com/posts") {
                    headers {
                        append("Accept", "application/json")
                    }
                }
            if (resp.status == HttpStatusCode.OK) {
                println("resp.status == HttpStatusCode.OK")
                //println(resp.body)
                notes = resp.readBytes()
                println("notes:")
                println(notes)
               // return notes
            } else {
                println("httpStatusCode:")
                println(resp.status)
            }
            httpClient.close()
        } catch (e: java.net.UnknownHostException) {
            println("exception of http")
            println(e)
        }
        println("async end: ${Thread.currentThread().name}")
        return notes
    }

}