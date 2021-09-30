package com.example.cupet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cupet.R
import com.example.cupet.model.Cost

class CalculateAdapter(val context: Context, val calcList: ArrayList<Cost>): RecyclerView.Adapter<CalculateAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalculateAdapter.Holder {
        val view = LayoutInflater.from(parent.context).inflate(com.example.cupet.R.layout.calculate_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return calcList.size
    }

    inner class Holder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        var hospital = itemView?.findViewById<TextView>(R.id.hospital_name)
        var diagnosis = itemView?.findViewById<TextView>(R.id.diagnosis)
        var cost_txt = itemView?.findViewById<TextView>(R.id.cost)

        fun bind(mCost: Cost, context: Context) {
            hospital?.text = mCost.hospital
            diagnosis?.text = mCost.diagnosis
            cost_txt?.text = mCost.cost.toString() + " 원"
        }
    }

    override fun onBindViewHolder(holder: CalculateAdapter.Holder, position: Int) {
        holder?.bind(calcList[position], context)
    }

}