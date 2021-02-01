package com.example.cupet.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
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
import java.lang.Integer.parseInt

class EstimateFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    var estimateList = arrayListOf<Estimate>()
    var scoreList = arrayListOf<String>()
    var num: Int = 0
    var result: Int = 0
    var average: Int = 0

    lateinit var mReference: DatabaseReference
    lateinit var hospitalName: String
    var likes: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_estimate, container, false)

        val preferences: SharedPreferences = context!!.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        hospitalName = preferences.getString("name", "none").toString()

        var estimate_txt: EditText = view.findViewById(R.id.estimate_txt)
        var send: TextView = view.findViewById(R.id.send)
        var likes1: ImageView = view.findViewById(R.id.likes1)
        var likes2: ImageView = view.findViewById(R.id.likes2)
        var likes3: ImageView = view.findViewById(R.id.likes3)

        likes1.setOnClickListener {
            likes1?.setImageResource(R.drawable.ic_like)
            likes = 1
        }
        likes2.setOnClickListener {
            likes1?.setImageResource(R.drawable.ic_like)
            likes2?.setImageResource(R.drawable.ic_like)
            likes = 2
        }
        likes3.setOnClickListener {
            likes1?.setImageResource(R.drawable.ic_like)
            likes2?.setImageResource(R.drawable.ic_like)
            likes3?.setImageResource(R.drawable.ic_like)
            likes = 3
        }

        send.setOnClickListener {
            if(estimate_txt.text.toString().equals("")) {
                Toast.makeText(context, "후기를 작성해주세요.", Toast.LENGTH_SHORT).show()
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
        getScore()

        return view
    }

    private fun postEstimate() {
        var estimate = estimate_txt.text.toString()
        var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Estimate").child(hospitalName)
        val estimateid: String = reference.push().key.toString()

        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["estimate"] = estimate
        hashMap["estimateid"] = estimateid
        hashMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid
        hashMap["time"] = System.currentTimeMillis()
        hashMap["likes"] = likes

        reference.child(estimateid).setValue(hashMap)
        estimate_txt.setText("")
    }

    private fun getEstimate() {
        mReference = FirebaseDatabase.getInstance().getReference("Estimate").child(hospitalName)

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
                adapter.notifyDataSetChanged()
                recyclerView.adapter = adapter
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }

    private fun getScore() {
        mReference = FirebaseDatabase.getInstance().getReference("Estimate").child(hospitalName)

        mReference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                scoreList.clear()

                for(snapshot: DataSnapshot in dataSnapshot.children) {
                    val estimate: Estimate? = snapshot.getValue(Estimate::class.java)
                    estimate?.let {
                        scoreList.add(estimate.likes.toString())
                    }
                }
                num = scoreList.size
                scoreList?.let {
                    for(i in it) {
                        result += parseInt(i)
                        average = result / num
                    }
                }
                var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Hospital").child(hospitalName)
                val hashMap: HashMap<String, Any> = HashMap()
                hashMap["likes"] = average

                reference.updateChildren(hashMap)
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }
}
