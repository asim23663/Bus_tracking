package com.uni.onclicklgubus.adapter

import com.uni.freebie.base.BaseRecyclerViewAdapter
import com.uni.onclicklgubus.R
import com.uni.onclicklgubus.databinding.IvDriverBinding
import com.uni.onclicklgubus.model.Driver

class DriversListAdapter : BaseRecyclerViewAdapter<Driver, IvDriverBinding>() {

    override fun getLayout() = R.layout.iv_driver

    override fun onBindViewHolder(
        holder: Companion.BaseViewHolder<IvDriverBinding>,
        position: Int
    ) {

        holder.binding.driver = items[position]

        items[position].apply {


        }


        holder.binding.root.setOnClickListener {
            listener?.invoke(it, items[position], position)
        }

        holder.binding.cvDriver.setOnClickListener {
            listener?.invoke(it, items[position], position)
        }

    }
}