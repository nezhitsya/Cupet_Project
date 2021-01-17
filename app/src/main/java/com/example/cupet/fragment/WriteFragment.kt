package com.example.cupet.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

import com.example.cupet.R

class WriteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View =  inflater.inflate(R.layout.fragment_write, container, false)

        var write: ImageView = view.findViewById(R.id.finish)
        var write_txt: TextView = view.findViewById(R.id.finish_txt)
        var title: EditText = view.findViewById(R.id.title)
        var description: EditText = view.findViewById(R.id.description)

        write.setOnClickListener {
            post()
        }

        write_txt.setOnClickListener {
            post()
        }

        return view
    }

    private fun post() {

    }
}
