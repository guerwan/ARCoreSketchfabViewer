package io.immersiv.arcoresketchfabviewer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import io.immersiv.arcoresketchfabviewer.models.SketchfabModel

class ModelAdapter(private val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<ModelAdapter.MyViewHolder>(),
    View.OnClickListener {
    private var modelsList: ArrayList<SketchfabModel>? = null
    private val picasso = Picasso.get()

    fun setData(models: ArrayList<SketchfabModel>?) {
        modelsList = models
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_model, parent, false))


    override fun getItemCount() = modelsList?.size ?: 0


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val imageUrl = modelsList?.get(position)?.getBiggestThumbnailUrl()
        if (imageUrl != null) {
            picasso.load(imageUrl).into(holder.image)
        }
        holder.name.text = modelsList?.get(position)?.name
        holder.description.text = modelsList?.get(position)?.name
        holder.itemView.tag = position
        holder.itemView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val position = v?.tag as Int
        onItemClickListener.onItemClicked(position, modelsList?.get(position))
    }

    class MyViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView) {
        val image: ImageView = containerView.findViewById(R.id.image)
        val name: TextView = containerView.findViewById(R.id.name)
        val description: TextView = containerView.findViewById(R.id.description)
    }

    interface OnItemClickListener {
        fun onItemClicked(position: Int, model: SketchfabModel?)
    }
}