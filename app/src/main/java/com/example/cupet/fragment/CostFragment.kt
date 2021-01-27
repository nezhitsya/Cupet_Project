package com.example.cupet.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.cupet.R
import com.example.cupet.adapter.CostAdapter
import com.example.cupet.model.Cost
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_cost.*

class CostFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    var costList = arrayListOf<Cost>()

    lateinit var mReference: DatabaseReference
    lateinit var hospitalName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_cost, container, false)

        val preferences: SharedPreferences = context!!.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        hospitalName = preferences.getString("name", "none").toString()

        var cost_txt: EditText = view.findViewById(R.id.cost_txt)
        var send: TextView = view.findViewById(R.id.send)
        var species: Spinner = view.findViewById(R.id.species)
        var weight: Spinner = view.findViewById(R.id.weight)
        var diagnosis: EditText = view.findViewById(R.id.diagnosis)

        ArrayAdapter.createFromResource(
            context!!,
            R.array.species,
            android.R.layout.simple_spinner_item
        ).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            species.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            context!!,
            R.array.weight,
            android.R.layout.simple_spinner_item
        ).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            weight.adapter = adapter
        }

        send.setOnClickListener {
            if(cost_txt.text.toString().equals("")) {
                Toast.makeText(context, "진료 기록을 작성해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                postCost()
            }
        }

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager

        getCost()

        return view
    }

    private fun postCost() {
        var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Cost").child(hospitalName)
        val costid: String = reference.push().key.toString()

        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["cost"] = cost_txt.text
        hashMap["costid"] = costid
        hashMap["weight"] = weight.selectedItem.toString()
        hashMap["time"] = System.currentTimeMillis()
        hashMap["species"] = species.selectedItem.toString()
        hashMap["diagnosis"] = diagnosis.text.toString()

        reference.child(costid).setValue(hashMap)
    }

    private fun getCost() {
        mReference = FirebaseDatabase.getInstance().getReference("Cost").child(hospitalName)

        mReference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                costList.clear()

                for(snapshot: DataSnapshot in dataSnapshot.children) {
                    val cost: Cost? = snapshot.getValue(Cost::class.java)
                    cost?.let {
                        costList.add(cost)
                    }
                }
                val adapter = CostAdapter(context!!, costList)
                adapter.notifyDataSetChanged()
                recyclerView.adapter = adapter
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }
}
