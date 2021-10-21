package com.example.itsapp.view.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.itsapp.R
import com.example.itsapp.model.vo.device.Device
import com.example.itsapp.model.vo.spec.Spec
import kotlinx.android.synthetic.main.favorites_device_item.view.*
import java.text.DecimalFormat

class FavoritesDeviceAdapter(var deviceList:ArrayList<Device>) : RecyclerView.Adapter<FavoritesDeviceAdapter.ViewHolder>() {

    private lateinit var itemClickListenerDelete : OnItemClickListenerDelete

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favorites_device_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.deviceImg.setImageResource(R.drawable.ic_baseline_laptop_24)
//        holder.deviceName.text = deviceList.get(position).deviceName
//        holder.deviceBrand.text = deviceList.get(position).deviceBrand
//        holder.devicePrice.text = deviceList.get(position).devicePrice
//        holder.deviceOs.text = deviceList.get(position).deviceOs
//        holder.deviceCpu.text = deviceList.get(position).deviceCpu
//        holder.deviceMemory.text = deviceList.get(position).deviceMemory
//        holder.deviceSsd.text = deviceList.get(position).deviceSsd

        holder.bindItems(deviceList.get(position))

        holder.deleteBtn.setOnClickListener(View.OnClickListener {
            itemClickListenerDelete.onClick(it,position)
        })
    }

    fun updateItem2(position: Int){
        deviceList.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    fun updateItem(item: List<Device>){
        deviceList = item as ArrayList<Device>
        notifyDataSetChanged()
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
//        val deviceImg = itemView.findViewById<ImageView>(R.id.device_img)
//        val deviceName = itemView.findViewById<TextView>(R.id.device_name)
//        val deviceBrand = itemView.findViewById<TextView>(R.id.device_brand)
//        val devicePrice = itemView.findViewById<TextView>(R.id.device_price)
//        val deviceOs = itemView.findViewById<TextView>(R.id.device_os)
//        val deviceCpu = itemView.findViewById<TextView>(R.id.device_cpu)
//        val deviceMemory = itemView.findViewById<TextView>(R.id.device_memory)
//        val deviceSsd = itemView.findViewById<TextView>(R.id.device_ssd)

        fun bindItems(data : Device){
            itemView.device_name.text = data.deviceName
            itemView.device_brand.text = data.deviceBrand
            itemView.device_price.text = data.devicePrice
            itemView.device_os.text = data.deviceOs
            itemView.device_cpu.text = data.deviceCpu
            itemView.device_memory.text = data.deviceMemory
            itemView.device_ssd.text = data.deviceSsd
            Glide.with(itemView.context).load(data.imgurl).into(itemView.device_img)
        }

        val deleteBtn = itemView.findViewById<ImageButton>(R.id.favorites_delete_btn)
    }

    interface OnItemClickListenerDelete{
        fun onClick(v: View, position: Int)
    }
    fun setItemClickListenerDelete(onItemClickListenerDelete: OnItemClickListenerDelete){
        this.itemClickListenerDelete = onItemClickListenerDelete
    }

    // 리사이클러뷰 아이템간 간격
    inner class VerticalItemDecorator(private val divHeight : Int) : RecyclerView.ItemDecoration() {
        @Override
        override fun getItemOffsets(outRect: Rect, view: View, parent : RecyclerView, state : RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.top = divHeight
            outRect.bottom = divHeight
        }
    }
}