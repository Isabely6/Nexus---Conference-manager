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

class FragmentList : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListAdapter
    private lateinit var requestQueue: RequestQueue
    private var idUser: String? = null
    private val listList = ArrayList<ListItem>()
    private val filteredList = ArrayList<ListItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        idUser = arguments?.getString("idUser")
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
        val url = "http://localhost/list.php"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val articleId = jsonObject.getString("idArticle")
                        val author = jsonObject.getString("authors")
                        val trackName = jsonObject.getString("trackName")
                        val date = jsonObject.getString("date")
                        val time = jsonObject.getString("hour")
                        val name = jsonObject.getString("title")
                        val room = jsonObject.getString("room")
                        val color = jsonObject.getString("color") // Retrieve the "color" attribute

                        val listItem = ListItem(articleId, author, trackName, date, time, name, room, color)
                        listList.add(listItem)
                    }
                    filterList("") // Initialize filteredList with all items
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

    private fun filterList(query: String) {
        filteredList.clear()
        if (query.isEmpty()) {
            filteredList.addAll(listList)
        } else {
            val lowerCaseQuery = query.lowercase()
            for (item in listList) {
                if (item.name.lowercase().contains(lowerCaseQuery) ||
                    item.date.lowercase().contains(lowerCaseQuery) ||
                    item.time.lowercase().contains(lowerCaseQuery) ||
                    item.room.lowercase().contains(lowerCaseQuery) ||
                    item.trackName.lowercase().contains(lowerCaseQuery) ||
                    item.author.lowercase().contains(lowerCaseQuery)) {
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
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = trackList.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val list = trackList[position]
            holder.tvDate.text = list.date
            holder.tvTime.text = list.time
            holder.tvName.text = list.name
            holder.tvRoom.text = list.room
            holder.colorTime.setBackgroundColor(Color.parseColor(list.color))
            val colorDesc = Color.parseColor(list.color)
            holder.colorDesc.setBackgroundColor(Color.argb(100, Color.red(colorDesc), Color.green(colorDesc), Color.blue(colorDesc)))
            holder.colorBtn.setBackgroundColor(Color.argb(100, Color.red(colorDesc), Color.green(colorDesc), Color.blue(colorDesc)))
            holder.itemView.setOnClickListener {
                listener(position, true)
            }

            holder.articleBtn.setOnClickListener {
                val fragment = FragmentArticleActivity()
                val bundle = Bundle()
                bundle.putString("idUser", idUser.toString()) // Assuming idUser is available in this context
                bundle.putString("articleId", list.articleId) // Pass articleId if needed
                fragment.arguments = bundle

                val fragmentManager = (holder.itemView.context as AppCompatActivity).supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragment_container, fragment) // Replace with FragmentArticleDetails
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
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

    data class ListItem(val articleId: String, val author:String, val trackName: String, val date: String, val time: String, val name: String, val room: String, val color: String)
}
