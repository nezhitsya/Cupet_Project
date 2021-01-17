package com.example.cupet.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.cupet.R
import com.example.cupet.model.Post
import kotlinx.android.synthetic.main.fragment_community.*

class CommunityFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    var postList = arrayListOf<Post>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view: View = inflater.inflate(R.layout.fragment_community, container, false)

        var write: ImageView = view.findViewById(R.id.write)
        var write_txt: TextView = view.findViewById(R.id.write_txt)

        write.setOnClickListener{
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, WriteFragment()).addToBackStack(null).commit()
        }

        write_txt.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, WriteFragment()).addToBackStack(null).commit()
        }

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager

        return view
    }
}