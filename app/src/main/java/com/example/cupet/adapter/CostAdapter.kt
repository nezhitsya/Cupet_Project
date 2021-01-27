package com.example.cupet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cupet.R
import com.example.cupet.model.Cost
import java.text.DateFormat
import java.text.SimpleDateFormat

class CostAdapter(val context: Context, val costList: ArrayList<Cost>): RecyclerView.Adapter<CostAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cost_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return costList.size
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var diagnosis = itemView?.findViewById<TextView>(R.id.diagnosis)
        val weight = itemView?.findViewById<TextView>(R.id.weight)
        val species = itemView?.findViewById<TextView>(R.id.species)
        val cost = itemView?.findViewById<TextView>(R.id.cost)
        var time = itemView?.findViewById<TextView>(R.id.time)

        fun bind(mCost: Cost, context: Context) {
            diagnosis?.text = mCost.diagnosis
            weight?.text = mCost.weight + " kg"
            species?.text = mCost.species
            cost?.text = mCost.cost.toString() + " 원"
            var df: DateFormat = SimpleDateFormat("yy.MM.dd")
            time?.text = df.format(mCost.time)
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(costList[position], context)
    }
}
