package com.example.damproject20

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import java.time.LocalDateTime

class FragmentSchedule : Fragment() {

    private lateinit var recyclerViewList1_10: RecyclerView
    private lateinit var recyclerViewList1_11: RecyclerView
    private lateinit var recyclerViewList1_12: RecyclerView
    private lateinit var recyclerViewList1_14: RecyclerView
    private lateinit var recyclerViewList1_15: RecyclerView
    private lateinit var recyclerViewList1_16: RecyclerView
    private lateinit var recyclerViewList1_17: RecyclerView

    private lateinit var recyclerViewList2_10: RecyclerView
    private lateinit var recyclerViewList2_11: RecyclerView
    private lateinit var recyclerViewList2_12: RecyclerView
    private lateinit var recyclerViewList2_14: RecyclerView
    private lateinit var recyclerViewList2_15: RecyclerView
    private lateinit var recyclerViewList2_16: RecyclerView
    private lateinit var recyclerViewList2_17: RecyclerView

    private lateinit var recyclerViewList3_10: RecyclerView
    private lateinit var recyclerViewList3_11: RecyclerView
    private lateinit var recyclerViewList3_12: RecyclerView
    private lateinit var recyclerViewList3_14: RecyclerView
    private lateinit var recyclerViewList3_15: RecyclerView
    private lateinit var recyclerViewList3_16: RecyclerView
    private lateinit var recyclerViewList3_17: RecyclerView

    private lateinit var textViewList1_10: TextView
    private lateinit var textViewList1_11: TextView
    private lateinit var textViewList1_12: TextView
    private lateinit var textViewList1_14: TextView
    private lateinit var textViewList1_15: TextView
    private lateinit var textViewList1_16: TextView
    private lateinit var textViewList1_17: TextView

    private lateinit var textViewList2_10: TextView
    private lateinit var textViewList2_11: TextView
    private lateinit var textViewList2_12: TextView
    private lateinit var textViewList2_14: TextView
    private lateinit var textViewList2_15: TextView
    private lateinit var textViewList2_16: TextView
    private lateinit var textViewList2_17: TextView

    private lateinit var textViewList3_10: TextView
    private lateinit var textViewList3_11: TextView
    private lateinit var textViewList3_12: TextView
    private lateinit var textViewList3_14: TextView
    private lateinit var textViewList3_15: TextView
    private lateinit var textViewList3_16: TextView
    private lateinit var textViewList3_17: TextView

    private var idUser: String? = null

    private lateinit var requestQueue: RequestQueue
    private val scheduleList = ArrayList<Schedule>()
    private val scheduleMap = mutableMapOf<String, ArrayList<Schedule>>()

    data class Schedule(
        val idArticle:String,
        val date: String,
        val hour: String,
        val title: String,
        val room: String,
        val color: String,
        var dateTime: LocalDateTime? = null
    ) {
        val hourInt: Int
            get() = hour.toIntOrNull() ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_fragment_schedule, container, false)

        recyclerViewList1_10 = view.findViewById(R.id.recyclerViewList1_10)
        recyclerViewList1_11 = view.findViewById(R.id.recyclerViewList1_11)
        recyclerViewList1_12 = view.findViewById(R.id.recyclerViewList1_12)
        recyclerViewList1_14 = view.findViewById(R.id.recyclerViewList1_14)
        recyclerViewList1_15 = view.findViewById(R.id.recyclerViewList1_15)
        recyclerViewList1_16 = view.findViewById(R.id.recyclerViewList1_16)
        recyclerViewList1_17 = view.findViewById(R.id.recyclerViewList1_17)

        recyclerViewList2_10 = view.findViewById(R.id.recyclerViewList2_10)
        recyclerViewList2_11 = view.findViewById(R.id.recyclerViewList2_11)
        recyclerViewList2_12 = view.findViewById(R.id.recyclerViewList2_12)
        recyclerViewList2_14 = view.findViewById(R.id.recyclerViewList2_14)
        recyclerViewList2_15 = view.findViewById(R.id.recyclerViewList2_15)
        recyclerViewList2_16 = view.findViewById(R.id.recyclerViewList2_16)
        recyclerViewList2_17 = view.findViewById(R.id.recyclerViewList2_17)

        recyclerViewList3_10 = view.findViewById(R.id.recyclerViewList3_10)
        recyclerViewList3_11 = view.findViewById(R.id.recyclerViewList3_11)
        recyclerViewList3_12 = view.findViewById(R.id.recyclerViewList3_12)
        recyclerViewList3_14 = view.findViewById(R.id.recyclerViewList3_14)
        recyclerViewList3_15 = view.findViewById(R.id.recyclerViewList3_15)
        recyclerViewList3_16 = view.findViewById(R.id.recyclerViewList3_16)
        recyclerViewList3_17 = view.findViewById(R.id.recyclerViewList3_17)

        textViewList1_10 = view.findViewById(R.id.textViewList1_10)
        textViewList1_11 = view.findViewById(R.id.textViewList1_11)
        textViewList1_12 = view.findViewById(R.id.textViewList1_12)
        textViewList1_14 = view.findViewById(R.id.textViewList1_14)
        textViewList1_15 = view.findViewById(R.id.textViewList1_15)
        textViewList1_16 = view.findViewById(R.id.textViewList1_16)
        textViewList1_17 = view.findViewById(R.id.textViewList1_17)

        textViewList2_10 = view.findViewById(R.id.textViewList2_10)
        textViewList2_11 = view.findViewById(R.id.textViewList2_11)
        textViewList2_12 = view.findViewById(R.id.textViewList2_12)
        textViewList2_14 = view.findViewById(R.id.textViewList2_14)
        textViewList2_15 = view.findViewById(R.id.textViewList2_15)
        textViewList2_16 = view.findViewById(R.id.textViewList2_16)
        textViewList2_17 = view.findViewById(R.id.textViewList2_17)

        textViewList3_10 = view.findViewById(R.id.textViewList3_10)
        textViewList3_11 = view.findViewById(R.id.textViewList3_11)
        textViewList3_12 = view.findViewById(R.id.textViewList3_12)
        textViewList3_14 = view.findViewById(R.id.textViewList3_14)
        textViewList3_15 = view.findViewById(R.id.textViewList3_15)
        textViewList3_16 = view.findViewById(R.id.textViewList3_16)
        textViewList3_17 = view.findViewById(R.id.textViewList3_17)

        idUser = arguments?.getString("idUser")

        requestQueue = Volley.newRequestQueue(requireContext())
        loadList()

        return view
    }

    private fun loadList() {
        val url = "http://localhost/schedule.php"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val idArticle = jsonObject.getString("idArticle")
                        val date = jsonObject.getString("date")
                        val hour = jsonObject.getString("hour")
                        val title = jsonObject.getString("title")
                        val room = jsonObject.getString("room")
                        val color = jsonObject.getString("color")

                        val schedule = Schedule(idArticle, date, hour, title, room, color)
                        scheduleList.add(schedule)
                        // Add the schedule to the corresponding day and hour in the scheduleMap
                        val dayKey = when (schedule.date) {
                            "June 26" -> "day1"
                            "June 27" -> "day2"
                            "June 28" -> "day3"
                            else -> "unknown"
                        }
                        scheduleMap.getOrPut(dayKey) { arrayListOf() }.add(schedule)
                    }

                    // Sort each day's schedule by hour
                    scheduleMap.values.forEach { dayList ->
                        dayList.sortBy { it.hourInt }
                    }

                    // Update the adapters for each RecyclerView
                    updateRecyclerView(recyclerViewList1_10, textViewList1_10, filterByHour(scheduleMap["day1"]?: arrayListOf(), "10:00:00"))
                    updateRecyclerView(recyclerViewList1_11, textViewList1_11, filterByHour(scheduleMap["day1"]?: arrayListOf(), "11:00:00"))
                    updateRecyclerView(recyclerViewList1_12, textViewList1_12, filterByHour(scheduleMap["day1"]?: arrayListOf(), "12:00:00"))
                    updateRecyclerView(recyclerViewList1_14, textViewList1_14, filterByHour(scheduleMap["day1"]?: arrayListOf(), "14:00:00"))
                    updateRecyclerView(recyclerViewList1_15, textViewList1_15, filterByHour(scheduleMap["day1"]?: arrayListOf(), "15:00:00"))
                    updateRecyclerView(recyclerViewList1_16, textViewList1_16, filterByHour(scheduleMap["day1"]?: arrayListOf(), "16:00:00"))
                    updateRecyclerView(recyclerViewList1_17, textViewList1_17, filterByHour(scheduleMap["day1"]?: arrayListOf(), "17:00:00"))

                    updateRecyclerView(recyclerViewList2_10, textViewList2_10, filterByHour(scheduleMap["day2"]?: arrayListOf(), "10:00:00"))
                    updateRecyclerView(recyclerViewList2_11, textViewList2_11, filterByHour(scheduleMap["day2"]?: arrayListOf(), "11:00:00"))
                    updateRecyclerView(recyclerViewList2_12, textViewList2_12, filterByHour(scheduleMap["day2"]?: arrayListOf(), "12:00:00"))
                    updateRecyclerView(recyclerViewList2_14, textViewList2_14, filterByHour(scheduleMap["day2"]?: arrayListOf(), "14:00:00"))
                    updateRecyclerView(recyclerViewList2_15, textViewList2_15, filterByHour(scheduleMap["day2"]?: arrayListOf(), "15:00:00"))
                    updateRecyclerView(recyclerViewList2_16, textViewList2_16, filterByHour(scheduleMap["day2"]?: arrayListOf(), "16:00:00"))
                    updateRecyclerView(recyclerViewList2_17, textViewList2_17, filterByHour(scheduleMap["day2"]?: arrayListOf(), "17:00:00"))

                    updateRecyclerView(recyclerViewList3_10, textViewList3_10, filterByHour(scheduleMap["day3"]?: arrayListOf(), "10:00:00"))
                    updateRecyclerView(recyclerViewList3_11, textViewList3_11, filterByHour(scheduleMap["day3"]?: arrayListOf(), "11:00:00"))
                    updateRecyclerView(recyclerViewList3_12, textViewList3_12, filterByHour(scheduleMap["day3"]?: arrayListOf(), "12:00:00"))
                    updateRecyclerView(recyclerViewList3_14, textViewList3_14, filterByHour(scheduleMap["day3"]?: arrayListOf(), "14:00:00"))
                    updateRecyclerView(recyclerViewList3_15, textViewList3_15, filterByHour(scheduleMap["day3"]?: arrayListOf(), "15:00:00"))
                    updateRecyclerView(recyclerViewList3_16, textViewList3_16, filterByHour(scheduleMap["day3"]?: arrayListOf(), "16:00:00"))
                    updateRecyclerView(recyclerViewList3_17, textViewList3_17, filterByHour(scheduleMap["day3"]?: arrayListOf(), "17:00:00"))

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

    private fun filterByHour(list: List<Schedule>, hour: String): List<Schedule> {
        // Filter the list to include only schedules for a specific hour
        return list.filter { it.hour == hour }
    }

    private fun updateRecyclerView(recyclerView: RecyclerView, textView: TextView, filteredList: List<Schedule>) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ScheduleAdapter(filteredList) { position, isClicked ->
                // Handle item click if needed
            }

            val adapter = ScheduleAdapter(filteredList) { position, isClicked ->
                // Handle item click if needed
            }
            this.adapter = adapter
            if (adapter.itemCount > 0) {
                recyclerView.visibility = View.VISIBLE
                textView.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.GONE
                textView.visibility = View.GONE
            }
        }
    }

    inner class ScheduleAdapter(
        private val scheduleList: List<Schedule>,
        private val listener: (Int, Boolean) -> Unit
    ) : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_schedule, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val schedule = scheduleList[position]
            holder.tvDate.text = schedule.date
            holder.tvTime.text = schedule.hour
            holder.tvName.text = schedule.title
            holder.tvRoom.text = schedule.room
            holder.colorTime.setBackgroundColor(Color.parseColor(schedule.color))
            val colorDesc = Color.parseColor(schedule.color)
            holder.colorDesc.setBackgroundColor(Color.argb(100, Color.red(colorDesc), Color.green(colorDesc), Color.blue(colorDesc)))
            holder.colorBtn.setBackgroundColor(Color.argb(100, Color.red(colorDesc), Color.green(colorDesc), Color.blue(colorDesc)))
            holder.itemView.setOnClickListener {
                listener(position, true)
            }

            holder.articleBtn.setOnClickListener {
                val fragment = FragmentArticleActivity()
                val bundle = Bundle()
                bundle.putString("idUser", idUser.toString()) // Assuming idUser is available in this context
                bundle.putString("articleId", schedule.idArticle) // Pass articleId if needed
                fragment.arguments = bundle

                val fragmentManager = (holder.itemView.context as AppCompatActivity).supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragment_container, fragment) // Replace with FragmentArticleDetails
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }

        override fun getItemCount(): Int = scheduleList.size

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
}

