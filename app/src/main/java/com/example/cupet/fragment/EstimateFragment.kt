package com.example.cupet.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.cupet.R
import com.example.cupet.adapter.EstimateAdapter
import com.example.cupet.model.Estimate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_estimate.*

class EstimateFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    var estimateList = arrayListOf<Estimate>()

    lateinit var mReference: DatabaseReference
    lateinit var postid: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_estimate, container, false)

        val preferences: SharedPreferences = context!!.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        postid = preferences.getString("postid", "none").toString()

        var estimate: EditText = view.findViewById(R.id.estimate)
        var send: TextView = view.findViewById(R.id.send)

        send.setOnClickListener {
            if(estimate.text.toString().equals("")) {
                Toast.makeText(context, "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                postEstimate()
            }
        }

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager

        getEstimate()

        return view
    }

    private fun postEstimate() {
        var estimate = estimate.text.toString()
        var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Estimate").child(postid)
        val estimateid: String = reference.push().key.toString()

        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["estimate"] = estimate
        hashMap["estimateid"] = estimateid
        hashMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid
        hashMap["time"] = System.currentTimeMillis()

        reference.child(estimateid).setValue(hashMap)
    }

    private fun getEstimate() {
        mReference = FirebaseDatabase.getInstance().getReference("Estimate").child(postid)

        mReference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                estimateList.clear()

                for(snapshot: DataSnapshot in dataSnapshot.children) {
                    val estimate: Estimate? = snapshot.getValue(Estimate::class.java)
                    estimate?.let {
                        estimateList.add(estimate)
                    }
                }
                val adapter = EstimateAdapter(context!!, estimateList)
                recyclerView.adapter = adapter
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }
}
