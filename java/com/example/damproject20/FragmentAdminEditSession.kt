package com.example.damproject20

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException

class FragmentAdminEditSession : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListAdapter
    private lateinit var requestQueue: RequestQueue
    private val listList = ArrayList<ListItem>()
    private val filteredList = ArrayList<ListItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_fragment_list, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ListAdapter(filteredList) { position, isClicked ->
            // Handle item click
        }
        recyclerView.adapter = adapter

        requestQueue = Volley.newRequestQueue(requireContext())

        val searchEditText: EditText = view.findViewById(R.id.search)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterList(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        loadList()

        return view
    }

    private lateinit var authorsList: MutableList<String>

    private fun loadList() {
        val url = "http://localhost/editsession.php"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val author = jsonObject.getString("author")
                        val trackName = jsonObject.getString("trackName")
                        val trackDesc = jsonObject.getString("trackDesc")
                        val idArticle = jsonObject.getString("idArticle")
                        val articleName = jsonObject.getString("articleName")
                        val date = jsonObject.getString("date")
                        val time = jsonObject.getString("hour")
                        val room = jsonObject.getString("room")
                        val abstract = jsonObject.getString("abstract")
                        val color = jsonObject.getString("color") // Retrieve the "color" attribute

                        val listItem = ListItem(
                            idArticle,
                            articleName,
                            trackName,
                            trackDesc,
                            author,
                            date,
                            time,
                            room,
                            abstract,
                            color
                        )
                        listList.add(listItem)
                    }
                    filterList("") // Initialize filteredList with all items
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        )
        requestQueue.add(stringRequest)
    }

    private fun filterList(query: String) {
        filteredList.clear()
        if (query.isEmpty()) {
            filteredList.addAll(listList)
        } else {
            val lowerCaseQuery = query.lowercase()
            for (item in listList) {
                if (item.articleName.lowercase().contains(lowerCaseQuery) ||
                    item.date.lowercase().contains(lowerCaseQuery) ||
                    item.time.lowercase().contains(lowerCaseQuery) ||
                    item.room.lowercase().contains(lowerCaseQuery) ||
                    item.trackName.lowercase().contains(lowerCaseQuery) ||
                    item.author.lowercase().contains(lowerCaseQuery)
                ) {
                    filteredList.add(item)
                }
            }
        }
        adapter.notifyDataSetChanged()
    }

    inner class ListAdapter(
        private val trackList: List<ListItem>,
        private val listener: (Int, Boolean) -> Unit
    ) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = trackList.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val list = trackList[position]
            holder.tvDate.text = list.date
            holder.tvTime.text = list.time
            holder.tvName.text = list.articleName
            holder.tvRoom.text = list.room
            holder.colorTime.setBackgroundColor(Color.parseColor(list.color))
            val colorDesc = Color.parseColor(list.color)
            holder.colorDesc.setBackgroundColor(
                Color.argb(
                    100,
                    Color.red(colorDesc),
                    Color.green(colorDesc),
                    Color.blue(colorDesc)
                )
            )
            holder.colorBtn.setBackgroundColor(
                Color.argb(
                    100,
                    Color.red(colorDesc),
                    Color.green(colorDesc),
                    Color.blue(colorDesc)
                )
            )
            holder.itemView.setOnClickListener {
                listener(position, true)
            }

            holder.articleBtn.setOnClickListener {
                val fragment = FragmentEditor()
                val args = Bundle()
                args.putString("idArticle", list.idArticle)
                args.putString("articleName", list.articleName)
                args.putString("trackName", list.trackName)
                args.putString("trackDesc", list.trackDesc)
                args.putString("author", list.author)
                args.putString("date", list.date)
                args.putString("time", list.time)
                args.putString("room", list.room)
                args.putString("abstract", list.abstract)
                args.putString("color", list.color)
                fragment.arguments = args

                val fragmentManager = (requireContext() as AppCompatActivity).supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }

        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvDate: TextView = itemView.findViewById(R.id.tvDate)
            val tvTime: TextView = itemView.findViewById(R.id.tvTime)
            val tvName: TextView = itemView.findViewById(R.id.tvTitle)
            val tvRoom: TextView = itemView.findViewById(R.id.tvRoom)
            val colorTime: LinearLayout = itemView.findViewById(R.id.colorTime)
            val colorDesc: LinearLayout = itemView.findViewById(R.id.colorDesc)
            val colorBtn: LinearLayout = itemView.findViewById(R.id.colorBtn)
            val articleBtn: Button = itemView.findViewById(R.id.articleBtn)
        }
    }

    data class ListItem(
        val idArticle: String,
        val articleName: String,
        val trackName: String,
        val trackDesc: String,
        val author: String,
        val date: String,
        val time: String,
        val room: String,
        val abstract: String,
        val color: String
    )
}