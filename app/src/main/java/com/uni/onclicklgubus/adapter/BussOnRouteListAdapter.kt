package com.uni.onclicklgubus.adapter

import com.uni.freebie.base.BaseRecyclerViewAdapter
import com.uni.onclicklgubus.R
import com.uni.onclicklgubus.databinding.IvBusBinding
import com.uni.onclicklgubus.databinding.IvDriverBinding
import com.uni.onclicklgubus.model.Bus
import com.uni.onclicklgubus.model.Driver

class BussOnRouteListAdapter : BaseRecyclerViewAdapter<Bus, IvBusBinding>() {

    override fun getLayout() = R.layout.iv_bus

    override fun onBindViewHolder(
        holder: Companion.BaseViewHolder<IvBusBinding>,
        position: Int
    ) {

        holder.binding.bus = items[position]

        items[position].apply {


        }


        holder.binding.root.setOnClickListener {
            listener?.invoke(it, items[position], position)
        }

        holder.binding.cvBus.setOnClickListener {
            listener?.invoke(it, items[position], position)
        }

    }
}