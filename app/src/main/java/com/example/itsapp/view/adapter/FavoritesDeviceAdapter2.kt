package com.example.itsapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.itsapp.R
import com.example.itsapp.model.vo.device.Device

class FavoritesDeviceAdapter2(var deviceList:ArrayList<Device>) : RecyclerView.Adapter<FavoritesDeviceAdapter2.ViewHolder>() {

    private lateinit var itemClickListenerDelete : OnItemClickListenerDelete
    private lateinit var itemClickListenerCompare : OnItemClickListenerCompare

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favorites_device_item2,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.deviceImg.setImageResource(R.drawable.ic_baseline_laptop_24)
        holder.deviceName.text = deviceList.get(position).deviceName
        holder.deviceBrand.text = deviceList.get(position).deviceBrand
        holder.reviewPoint.text = deviceList.get(position).reviewPoint.toString()
        holder.reviewCount.text = deviceList.get(position).reviewCount.toString()

        holder.deleteBtn.setOnClickListener(View.OnClickListener {
            itemClickListenerDelete.onClick(it,position)
        })
        holder.compareBtn.setOnClickListener{
            itemClickListenerCompare.onClick(it,position)
        }
    }

    fun updateItem(item: List<Device>){
        deviceList = item as ArrayList<Device>
        notifyDataSetChanged()
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val deviceImg = itemView.findViewById<ImageView>(R.id.device_img)
        val deviceName = itemView.findViewById<TextView>(R.id.device_name)
        val deviceBrand = itemView.findViewById<TextView>(R.id.device_brand)
        val reviewPoint = itemView.findViewById<TextView>(R.id.review_point)
        val reviewCount = itemView.findViewById<TextView>(R.id.review_count)

        val deleteBtn = itemView.findViewById<Button>(R.id.favorites_delete_btn)
        val compareBtn = itemView.findViewById<Button>(R.id.favorites_compare_btn)
    }

    interface OnItemClickListenerDelete{
        fun onClick(v: View, position: Int)
    }
    fun setItemClickListenerDelete(onItemClickListenerDelete: OnItemClickListenerDelete){
        this.itemClickListenerDelete = onItemClickListenerDelete
    }
    interface OnItemClickListenerCompare{
        fun onClick(v: View, position: Int)
    }
    fun setItemClickListenerCompare(onItemClickListenerCompare: OnItemClickListenerCompare){
        this.itemClickListenerCompare = onItemClickListenerCompare
    }
}