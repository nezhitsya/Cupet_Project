package com.example.cupet.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.cupet.R
import com.example.cupet.adapter.HomeAdapter
import com.example.cupet.model.Hospital
import com.google.firebase.database.*

class SearchFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    var hospitalList = arrayListOf<Hospital>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_search, container, false)

        var search: EditText = view.findViewById(R.id.search_bar)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = linearLayoutManager

        search.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchHospital(s.toString().toLowerCase())
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        return view
    }

    private fun searchHospital(s: String) {
        var query: Query = FirebaseDatabase.getInstance().getReference("Hospital").orderByChild("name").startAt(s).endAt(s + "\uf8ff")

        query.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                hospitalList.clear()
                for(snapshot: DataSnapshot in dataSnapshot.children) {
                    val hospital: Hospital? = snapshot.getValue(Hospital::class.java)
                    if (hospital != null) {
                        hospitalList.add(hospital)
                    }
                }
                val adapter = HomeAdapter(context!!, hospitalList)
                adapter.notifyDataSetChanged()
                recyclerView?.adapter = adapter
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }
}
