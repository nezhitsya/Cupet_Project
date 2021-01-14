package com.example.cupet.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cupet.R
import com.example.cupet.adapter.HomeAdapter
import com.example.cupet.model.Hospital

class homeFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var hospitalList: List<Hospital>
    lateinit var homeAdapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager

        hospitalList = ArrayList<Hospital>()
        homeAdapter = HomeAdapter(context, hospitalList)
        recyclerView.adapter = homeAdapter

        return view
    }
}
