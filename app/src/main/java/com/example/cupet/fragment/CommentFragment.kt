package com.example.cupet.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView

import com.example.cupet.R
import com.example.cupet.model.Comment
import com.google.firebase.database.DatabaseReference

class CommentFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    var commentList = arrayListOf<Comment>()

    lateinit var mReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_comment, container, false)

        var comment: EditText = view.findViewById(R.id.comment)
        var send: TextView = view.findViewById(R.id.send)

        var toolbar: Toolbar = activity!!.findViewById(R.id.toolbar)
        var toolbar_txt: TextView = toolbar.findViewById(R.id.toolbar_title)
        var search: ImageView = toolbar.findViewById(R.id.search)
        var spinner: ImageView = toolbar.findViewById(R.id.spinner)
        var bookmark: ImageView = toolbar.findViewById(R.id.bookmark)
        var trash: ImageView = toolbar.findViewById(R.id.trash)
        toolbar_txt.text = "댓글"
        search.visibility = View.GONE
        spinner.visibility = View.GONE
        bookmark.visibility = View.GONE
        trash.visibility = View.GONE

        return view
    }
}
