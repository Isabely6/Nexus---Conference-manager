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
import android.widget.ImageView
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
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONException

class FragmentUserProfile : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListAdapter
    private lateinit var requestQueue: RequestQueue
    private lateinit var profile_image: ImageView
    private lateinit var username: TextView
    private lateinit var tvemail: TextView
    private lateinit var btnLogout: Button
    private var idUser: String? = null
    private val listList = ArrayList<UserItem>()
    private val filteredList = ArrayList<UserItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_fragment_userprofile, container, false)

        val idUser = arguments?.getString("idUser")

        btnLogout = view.findViewById(R.id.logout)

        username = view.findViewById(R.id.username)
        tvemail = view.findViewById(R.id.email)
        profile_image = view.findViewById(R.id.profile_image)

        recyclerView = view.findViewById(R.id.recyclerViewUser)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.visibility = View.GONE  // Initially set visibility to GONE

        // Initialize the adapter with empty list and set it to the recycler view
        adapter = ListAdapter(filteredList) { _, _ -> }
        recyclerView.adapter = adapter

        requestQueue = Volley.newRequestQueue(requireContext())

        if (idUser != null) {
            loadList(idUser)
        }

        btnLogout.setOnClickListener {
            activity?.finish()
        }

        return view
    }

    private fun loadList(idUser: String) {
        val url = "http://localhost/user_profile.php?idUser=$idUser"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    val jsonArray = JSONArray(response)
                    listList.clear()
                    filteredList.clear()
                    var hasNonEmptyTitle = false

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val idArticle = jsonObject.getString("idArticle")
                        val user = jsonObject.getString("user")
                        val email = jsonObject.getString("email")
                        val date = jsonObject.getString("date")
                        val hour = jsonObject.getString("hour")
                        val title = jsonObject.getString("title")
                        val room = jsonObject.getString("room")
                        val color = jsonObject.getString("color")

                        // Update TextViews and ImageView with the user profile data
                        username.text = user
                        tvemail.text = email

                        val userItem = UserItem(idUser, idArticle, user, email, date, hour, title, room, color)
                        listList.add(userItem)
                        filteredList.add(userItem)

                        if (title != "null") {
                            hasNonEmptyTitle = true
                        }
                    }

                    if (hasNonEmptyTitle) {
                        recyclerView.visibility = View.VISIBLE
                    } else {
                        recyclerView.visibility = View.GONE
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


    inner class ListAdapter(
        private val userList: List<UserItem>,
        private val listener: (Int, Boolean) -> Unit
    ) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_following_presentations, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = userList.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val list = userList[position]
            holder.tvDate.text = list.date
            holder.tvTime.text = list.hour
            holder.tvName.text = list.title
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
                bundle.putString("idUser", list.idUser) // Assuming idUser is available in this context
                bundle.putString("articleId", list.idArticle) // Pass articleId if needed
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

    data class UserItem(val idUser: String, val idArticle: String, val user: String, val email: String, val date: String, val hour: String, val title: String, val room: String, val color: String)
}
