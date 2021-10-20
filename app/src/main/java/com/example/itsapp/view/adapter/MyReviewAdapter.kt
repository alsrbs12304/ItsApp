package com.example.itsapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.itsapp.R
import com.example.itsapp.model.vo.review.Review
import kotlinx.android.synthetic.main.my_review_item.view.*
import kotlinx.android.synthetic.main.my_review_item.view.device_img

class MyReviewAdapter (var reviewList:ArrayList<Review>) : RecyclerView.Adapter<MyReviewAdapter.ViewHolder>(){
    private lateinit var itemClickListener : OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_review_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.writer.text = reviewList.get(position).writer
//        holder.riviewPoint.rating = reviewList.get(position).reviewPoint.toFloat()
//        holder.writeTime.text = reviewList.get(position).writeTime
//        holder.contentPros.text = reviewList.get(position).contentPros
//        holder.contentCons.text = reviewList.get(position).contentCons
//        holder.commnetCount.text = reviewList.get(position).commentCount.toString()
        holder.bindItems(reviewList.get(position))
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }
    fun updateItem2(position: Int){
        reviewList.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    fun updateItem(item: List<Review>){
        reviewList = item as ArrayList<Review>
        notifyDataSetChanged()
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
//        val writer = itemView.findViewById<TextView>(R.id.writer)
//        val riviewPoint = itemView.findViewById<RatingBar>(R.id.review_point)
//        val writeTime = itemView.findViewById<TextView>(R.id.write_time)
//        val contentPros = itemView.findViewById<TextView>(R.id.content_pros)
//        val contentCons = itemView.findViewById<TextView>(R.id.content_cons)
//        val commnetCount = itemView.findViewById<TextView>(R.id.comment_count)

        fun bindItems(data : Review){
            itemView.writer.text = data.writer
            itemView.review_point.rating = data.reviewPoint.toFloat()
            itemView.write_time.text = data.writeTime
            itemView.content_pros.text = data.contentPros
            itemView.content_cons.text = data.contentCons
            itemView.comment_count.text = data.commentCount.toString()
            itemView.device_brand.text = data.deviceBrand
            itemView.device_name.text = data.deviceName
            Glide.with(itemView.context).load(data.imgurl).into(itemView.device_img)
        }
    }

    interface OnItemClickListener{
        fun onClick(v: View, position: Int)
    }
    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }
}