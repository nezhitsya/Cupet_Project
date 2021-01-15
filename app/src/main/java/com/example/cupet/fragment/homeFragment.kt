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
import com.google.firebase.database.*

class homeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    var hospitalList = arrayListOf<Hospital>()
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var mReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view: View = inflater.inflate(R.layout.fragment_home, container, false)

        firebaseDatabase = FirebaseDatabase.getInstance()

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager

        mReference = FirebaseDatabase.getInstance().getReference("Hospital")
        mReference.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(dataSnapshot: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (child in dataSnapshot.children) {
                    val hospital: Hospital? = child.getValue(Hospital::class.java)
                    hospitalList?.add(hospital!!)
                }
                val adapter = HomeAdapter(context!!, hospitalList)
                recyclerView?.adapter = adapter
            }
        })

        return view
    }
}
