package com.uni.onclicklgubus.adapter

import com.uni.freebie.base.BaseRecyclerViewAdapter
import com.uni.onclicklgubus.R
import com.uni.onclicklgubus.databinding.IvDriverBinding
import com.uni.onclicklgubus.databinding.IvStudentBinding
import com.uni.onclicklgubus.model.Driver
import com.uni.onclicklgubus.model.Student

class StudentsListAdapter : BaseRecyclerViewAdapter<Student, IvStudentBinding>() {

    override fun getLayout() = R.layout.iv_student

    override fun onBindViewHolder(
        holder: Companion.BaseViewHolder<IvStudentBinding>,
        position: Int
    ) {

        holder.binding.studnet = items[position]

        items[position].apply {


        }


        holder.binding.root.setOnClickListener {
            listener?.invoke(it, items[position], position)
        }

    }
}