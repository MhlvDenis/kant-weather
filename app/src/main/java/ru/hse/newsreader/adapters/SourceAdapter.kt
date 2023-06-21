package ru.hse.newsreader.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.hse.newsreader.R
import ru.hse.newsreader.databinding.SourceItemBinding
import ru.hse.newsreader.entities.Source

class SourceAdapter : RecyclerView.Adapter<SourceAdapter.SourceViewHolder>(){

    var sources: List<Source> = emptyList()

    private var onItemClickListener: ((Source) -> Unit)? = null

    fun setItemClickListener(listener: (Source) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceViewHolder {
        return SourceViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.source_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SourceViewHolder, position: Int) {
        val binding =SourceItemBinding.bind(holder.itemView)
        val source = sources[position]

        binding.apply {
            tvLocation.text = source.location
            tvTemperature.text = source.temperature
            tvPressure.text = source.pressure
            tvWeather.text = source.weather
            root.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(source)
                }
            }
        }
    }

    override fun getItemCount() = sources.size

    class SourceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}