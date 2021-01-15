package com.example.cupet.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.cupet.R
import kotlinx.android.synthetic.main.fragment_community.*

class CommunityFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_community, container, false)

        write.setOnClickListener{
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, WriteFragment()).commit()
        }

        write_txt.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, WriteFragment()).commit()
        }

        return view
    }
}
