package com.imcalculator.calculator.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.imcalculator.calculator.R

class MathRecyclerAdapter(
    private val context: Context,
    private var mathList: MutableList<String>,
    private val listener: (Boolean, Int) -> Unit
) : RecyclerView.Adapter<MathRecyclerAdapter.MathViewHolder>() {

    private var updatePosition = -1
    private var isEnable = false

    init {
        setHasStableIds(false)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MathViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.calculator_text_recycler_design, parent, false)
        return MathViewHolder(view)
    }

    override fun onBindViewHolder(holder: MathViewHolder, position: Int) {
        val mathText = mathList[position]
        holder.bind(mathText, position)
    }

    override fun getItemCount(): Int = mathList.size

    override fun getItemId(position: Int): Long {
        return mathList[position].hashCode().toLong()
    }

    fun submitList(list: MutableList<String>) {
        updatePosition = -1
        mathList = list
        notifyDataSetChanged()
    }

    fun updateListByPosition(position: Int, text: String) {
        mathList[position] = text
        updatePosition = position
        notifyItemChanged(position, Any())
    }

    inner class MathViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mathText = itemView.findViewById<TextView>(R.id.mathText)
        private val layout = itemView.findViewById<View>(R.id.mathDesignLayout)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION && !isEnable) {
                    isEnable = true
                    layout.setBackgroundResource(R.drawable.selected_item_backround)
                    listener(mathText.text.lastOrNull()?.isDigit() == true, position)
                }
            }
        }

        fun bind(text: String, position: Int) {
            if (text.last().equals('.')) {
                mathText.text = ".${text.substring(0, text.length-1)}"
            } else mathText.text = text

            if (updatePosition == position) {
                layout.setBackgroundResource(R.drawable.selected_item_backround)
                isEnable = true
            } else {
                layout.setBackgroundResource(0)
                isEnable = false
            }
        }
    }
}
