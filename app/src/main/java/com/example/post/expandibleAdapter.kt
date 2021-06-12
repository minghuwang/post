package com.example.post

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import java.util.HashMap
class CustomExpandableListAdapter internal constructor(
    private val context: Context,
    private val titleList: List<String>,
    private val dataList: HashMap<String, List<String>>
) : BaseExpandableListAdapter() {
    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        println("getChild: listPosition = $listPosition, expandedListPosition = $expandedListPosition")
        //postId starts from 1 and this only affect child
        return this.dataList[(listPosition+1).toString()]!![expandedListPosition]
    }
    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        println("getChildId")
        return expandedListPosition.toLong()
    }
    override fun getChildView(
        listPosition: Int,
        expandedListPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        println("getChildView listPosition = $listPosition +1, expandedListPosition = $expandedListPosition")
        var convertView = convertView
        val expandedListText = getChild(listPosition, expandedListPosition) as String
        if (convertView == null) {
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_comments, null)
        }
        val expandedListTextView = convertView!!.findViewById<TextView>(
            R.id.comment)
        expandedListTextView.text = expandedListText
        return convertView
    }
    override fun getChildrenCount(listPosition: Int): Int {
        println("getChildrenCount listPosition =$listPosition")
        return this.dataList[(listPosition+1).toString()]!!.size
    }
    override fun getGroup(listPosition: Int): Any {
        println("getGroup")
        return this.titleList[listPosition]
    }
    override fun getGroupCount(): Int {
        println("getGroupCount")
        return this.titleList.size
    }
    override fun getGroupView(
        listPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        println("getGroupView listPosition = $listPosition, isExpanded = $isExpanded")
        var convertView = convertView
        val listTitle = getGroup(listPosition) as String
        if (convertView == null) {
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_notes, null)
        }
        val listTitleTextViewNodeTitle = convertView!!.findViewById<TextView>(R.id.note_title)
        listTitleTextViewNodeTitle.setTypeface(null, Typeface.BOLD)
        listTitleTextViewNodeTitle.text = listTitle
        return convertView
    }
    override fun getGroupId(listPosition: Int): Long {
        println("getGroupId listPosition = $listPosition")
        return listPosition.toLong()
    }
    override fun hasStableIds(): Boolean {
        println("hasStableIds")
        return false
    }
    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        println("isChildSelectable")
        return true
    }
}