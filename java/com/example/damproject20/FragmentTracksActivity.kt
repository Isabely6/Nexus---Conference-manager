package com.example.damproject20

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException

class FragmentTracksActivity : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TracksAdapter
    private lateinit var requestQueue: RequestQueue
    private val trackList = ArrayList<Track>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_fragment_tracks, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewTracks)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = TracksAdapter(trackList)
        recyclerView.adapter = adapter

        requestQueue = Volley.newRequestQueue(requireContext())

        loadTracks()

        return view
    }

    private fun loadTracks() {
        val url = "http://localhost/tracks.php"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val name = jsonObject.getString("name")
                        val description = jsonObject.getString("description")
                        val color = jsonObject.getString("color")
                        trackList.add(Track(name, description, color))
                    }
                    adapter.notifyDataSetChanged()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )
        requestQueue.add(stringRequest)
    }
}

class TracksAdapter(private val trackList: List<Track>) : RecyclerView.Adapter<TracksAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = trackList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val track = trackList[position]
        holder.tvTrack.text = track.name
        holder.tvTrackDesc.text = track.description
        holder.tvTrack.setBackgroundColor(Color.parseColor(track.color))
        val colorDesc = Color.parseColor(track.color)
        holder.tvTrackDesc.setBackgroundColor(Color.argb(100, Color.red(colorDesc), Color.green(colorDesc), Color.blue(colorDesc)))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTrack: TextView = itemView.findViewById(R.id.tvTrackTitle)
        val tvTrackDesc: TextView = itemView.findViewById(R.id.tvTrackDesc)
    }
}

data class Track(val name: String, val description: String, val color: String)