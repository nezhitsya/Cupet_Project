package com.example.cupet.adapter

import android.content.Context
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cupet.model.Comment
import com.example.cupet.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.DateFormat
import java.text.SimpleDateFormat

class CommentAdapter(val context: Context, val commentList: ArrayList<Comment>): RecyclerView.Adapter<CommentAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(com.example.cupet.R.layout.comment_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    inner class Holder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        var profile = itemView?.findViewById<ImageView>(com.example.cupet.R.id.profile)
        var nickname = itemView?.findViewById<TextView>(com.example.cupet.R.id.nickname)
        val comment = itemView?.findViewById<TextView>(com.example.cupet.R.id.comment)
        var time = itemView?.findViewById<TextView>(com.example.cupet.R.id.time)
        var option = itemView?.findViewById<Button>(com.example.cupet.R.id.option)

        fun bind(mComment: Comment, context: Context) {
            comment?.text = mComment.comment
            var df: DateFormat = SimpleDateFormat("yy.MM.dd  hh:mm")
            time?.text = df.format(mComment.time)
            publisherInfo(profile, nickname, mComment.publisher)

            if(mComment.publisher!! != FirebaseAuth.getInstance().currentUser!!.toString()) {
                option!!.visibility = View.GONE
            }

            option?.setOnClickListener{
                val popupMenu = PopupMenu(context, option, Gravity.END)
                popupMenu.menuInflater.inflate(com.example.cupet.R.menu.post_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                    when(item.itemId) {
                        com.example.cupet.R.id.edit -> {
                            Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                        }
                        com.example.cupet.R.id.delete -> {
                            FirebaseDatabase.getInstance().getReference("Comment").child(mComment.commentid!!).removeValue().addOnCompleteListener { task ->
                                if(task.isSuccessful) {
                                    Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        else -> {
                            false
                        }
                    } as Boolean
                }
                popupMenu.show()
            }
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(commentList[position], context)
    }

    private fun publisherInfo(profile: ImageView?, nickname: TextView?, userid: String?) {
        var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid.toString())

        reference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user: User? = dataSnapshot.getValue(User::class.java)
                if (user != null) {
                    if (nickname != null) {
                        nickname.text = user.nickname
                    }
                    if (profile != null) {
                        Glide.with(context).load(user.profile).into(profile)
                    }
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }
}