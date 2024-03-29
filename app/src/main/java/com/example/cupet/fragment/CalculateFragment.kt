package com.example.cupet.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cupet.R
import com.example.cupet.adapter.CalculateAdapter
import com.example.cupet.model.Cost
import com.google.firebase.database.*

class CalculateFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    var calcList = arrayListOf<Cost>()

    lateinit var species: Spinner
    lateinit var weight: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_calculate, container, false)

        species = view.findViewById(R.id.species)
        weight = view.findViewById(R.id.weight)
        var search: EditText = view.findViewById(R.id.search_bar)

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

        search.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchDiagnosis(s.toString().toLowerCase())
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager

        return view
    }

    private fun searchDiagnosis(s: String) {
        var query: Query = FirebaseDatabase.getInstance().getReference("Cost").orderByChild("diagnosis").startAt(s).endAt(s + "\uf8ff")

        query.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                calcList.clear()
                for(snapshot: DataSnapshot in dataSnapshot.children) {
                    val cost: Cost? = snapshot.getValue(Cost::class.java)

                    var weight_txt = weight.selectedItem.toString()
                    var species_txt = species.selectedItem.toString()

                    cost?.let {
                        if (cost.species == species_txt && cost.weight == weight_txt) {
                            calcList.add(cost)
                        }
                    }
                }
                val adapter = CalculateAdapter(context!!, calcList)
                adapter.notifyDataSetChanged()
                recyclerView?.adapter = adapter
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }

}