package com.example.damproject20

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class FragmentAdminComments : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: QuestionsAdapter
    private lateinit var requestQueue: RequestQueue
    private val questionList = ArrayList<Question>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_activity_admin_accept_comments, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewQuestions)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = QuestionsAdapter(questionList, this::updateQuestionStatus)
        recyclerView.adapter = adapter

        requestQueue = Volley.newRequestQueue(requireContext())

        loadQuestions()

        return view
    }

    private fun loadQuestions() {
        val url = "http://localhost/manage_comments.php"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val id = jsonObject.getInt("id")
                        val idArticle = jsonObject.getInt("idArticle")
                        val question = jsonObject.getString("question")
                        val active = jsonObject.getInt("active")

                        questionList.add(Question(id, idArticle, question, active))
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

    private fun updateQuestionStatus(questionId: Int, isActive: Boolean) {
        val url = "http://localhost/manage_comments.php"
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            { response ->
                if (isActive == true){
                    Toast.makeText(requireContext(), "Accepted Comment", Toast.LENGTH_SHORT).show()
                    activity?.recreate()
                }
                else{
                    Toast.makeText(requireContext(), "Rejected Comment", Toast.LENGTH_SHORT).show()
                    deleteComments(questionId)
                }
            },
            { error ->
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["id"] = questionId.toString()
                params["active"] = if (isActive) "1" else "0"
                return params
            }
        }
        requestQueue.add(stringRequest)
    }

    private fun deleteComments(questionId: Int) {
        val url = "http://localhost/deletecomment.php"

        // Initialize the request queue with the provided context
        val requestQueue: RequestQueue = Volley.newRequestQueue(requireContext())

        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                val jsonResponse = JSONObject(response)
                val success = jsonResponse.getBoolean("success")
                val message = jsonResponse.getString("message")
                if (success) {
                    // Handle successful deletion
                    Toast.makeText(requireContext(), "Comment Deleted", Toast.LENGTH_SHORT).show()
                    activity?.recreate()
                } else {
                    // Handle failed deletion
                    Toast.makeText(requireContext(), "Comment not Deleted: $message", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["id"] = questionId.toString()
                return params
            }
        }

        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest)
    }


    inner class QuestionsAdapter(
        private val questionList: List<Question>,
        private val listener: (Int, Boolean) -> Unit
    ) : RecyclerView.Adapter<QuestionsAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.admin_accept_item_comment, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val question = questionList[position]
            holder.tvQuestion.text = question.question

            holder.btnApprove.setOnClickListener { listener(question.id, true) }
            holder.btnReject.setOnClickListener { listener(question.id, false) }
        }

        override fun getItemCount(): Int = questionList.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvQuestion: TextView = itemView.findViewById(R.id.tvQuestion)
            val btnApprove: Button = itemView.findViewById(R.id.btnApprove)
            val btnReject: Button = itemView.findViewById(R.id.btnReject)
        }
    }
}

data class Question(val id: Int, val idArticle: Int, val question: String, val active: Int)