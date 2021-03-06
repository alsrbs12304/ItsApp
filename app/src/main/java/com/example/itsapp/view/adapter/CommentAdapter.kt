package com.example.itsapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.itsapp.R
import com.example.itsapp.model.vo.comment.Comment
import kotlinx.android.synthetic.main.comment_item.view.*
import kotlinx.android.synthetic.main.comment_item.view.profile_img
import kotlinx.android.synthetic.main.my_review_item.view.*


class CommentAdapter(var commentList:ArrayList<Comment>) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    private lateinit var itemClickListener : OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentAdapter.ViewHolder, position: Int) {
//        holder.writer.text = commentList.get(position).writer
//        holder.comment.text = commentList.get(position).comment
//        holder.writeTime.text = commentList.get(position).writeTime
        holder.bindItems(commentList.get(position))
        holder.delete_comment_btn.setOnClickListener{
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return commentList.size
    }
    fun updateItem2(position: Int){
        commentList.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    fun updateItem(item: List<Comment>){
        commentList = item as ArrayList<Comment>
        notifyDataSetChanged()
    }
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
//        val writer = itemView.findViewById<TextView>(R.id.comment_writer)
//        val comment = itemView.findViewById<TextView>(R.id.comment_content)
//        val writeTime = itemView.findViewById<TextView>(R.id.comment_write_time)
        val delete_comment_btn = itemView.findViewById<ImageButton>(R.id.delete_comment_btn)

        fun bindItems(data : Comment){
            itemView.comment_writer.text = data.writer
            itemView.comment_content.text = data.comment
            itemView.comment_write_time.text = data.writeTime
            Glide.with(itemView.context).load(data.profileUrl).fallback(R.drawable.profile_img).circleCrop().into(itemView.profile_img)
        }
    }
    interface OnItemClickListener{
        fun onClick(v: View, position: Int)
    }
    fun setItemClickListener(onItemClickListener: CommentAdapter.OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }
}