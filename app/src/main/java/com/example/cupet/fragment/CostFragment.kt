package com.example.cupet.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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
import java.lang.Integer.parseInt

class CostFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    var costList = arrayListOf<Cost>()

    lateinit var mReference: DatabaseReference
    lateinit var hospitalName: String
    lateinit var weight_txt: String
    lateinit var species_txt: String
    lateinit var disgnosis_txt: String

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

        weight_txt = weight.selectedItem.toString()
        species_txt = species.selectedItem.toString()
        disgnosis_txt = diagnosis.text.toString()

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

        val cost_int: Int = parseInt(cost_txt.text.toString())

        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["cost"] = cost_int
        hashMap["costid"] = costid
        hashMap["weight"] = weight_txt
        hashMap["time"] = System.currentTimeMillis()
        hashMap["species"] = species_txt
        hashMap["diagnosis"] = disgnosis_txt

        reference.child(costid).setValue(hashMap)
        cost_txt.setText("")
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
                recyclerView.adapter = adapter
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }
}
