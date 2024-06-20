package com.example.damproject20

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
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
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class FragmentArticleActivity : Fragment() {
    private lateinit var queue: RequestQueue
    private lateinit var presentationTitle: TextView
    private lateinit var presentationDate: TextView
    private lateinit var presentationTime: TextView
    private lateinit var presentationRoom: TextView
    private lateinit var presentationSpeaker: TextView
    private lateinit var presentationAbstract: TextView
    private lateinit var followButton: Button
    private lateinit var commentContainer: RecyclerView
    private lateinit var editTextComment: EditText
    private lateinit var submitQuestionButton: Button
    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var trackColor: TextView
    private val questionList = mutableListOf<Question>()

    companion object {
        private const val ARG_ARTICLE_ID = "article_id"

        fun newInstance(articleId: String): FragmentArticleActivity {
            val fragment = FragmentArticleActivity()
            val args = Bundle()
            args.putString(ARG_ARTICLE_ID, articleId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_article_details, container, false)
        queue = Volley.newRequestQueue(requireContext())

        presentationTitle = rootView.findViewById(R.id.presentationTitle)
        presentationDate = rootView.findViewById(R.id.presentationDate)
        presentationTime = rootView.findViewById(R.id.presentationTime)
        presentationRoom = rootView.findViewById(R.id.presentationRoom)
        presentationSpeaker = rootView.findViewById(R.id.presentationSpeaker)
        presentationAbstract = rootView.findViewById(R.id.presentationAbstract)
        followButton = rootView.findViewById(R.id.followButton)
        commentContainer = rootView.findViewById(R.id.questionRecyclerView)
        editTextComment = rootView.findViewById(R.id.editTextComment)
        submitQuestionButton = rootView.findViewById(R.id.submitButton)
        trackColor = rootView.findViewById(R.id.track)

        commentContainer.layoutManager = LinearLayoutManager(requireContext())
        questionAdapter = QuestionAdapter(questionList)
        commentContainer.adapter = questionAdapter

        var change: Boolean

        val articleId = arguments?.getString("articleId")
        val idUser = arguments?.getString("idUser")

        if (articleId != null) {
            fetchArticleDetails(articleId)
        }
        if (articleId != null) {
            fetchQuestions(articleId)
        }

        if (articleId != null) {
            if (idUser != null) {
                change = false
                setfollowing(articleId, idUser, change)
            }
        }

        submitQuestionButton.setOnClickListener {
            val question = editTextComment.text.toString()
            if (question.isNotBlank()) {
                if (articleId != null) {
                    if (idUser != null) {
                        addQuestion(articleId, idUser, question)
                    }
                }
                editTextComment.text.clear()  // Clear the EditText after submission
            }
        }

        followButton.setOnClickListener {
            if (idUser != null) {
                if (articleId != null) {
                    change = true
                    setfollowing(articleId, idUser, change)
                    change = false
                }
            }
        }

        return rootView
    }

    private fun setfollowing(idArticle: String, idUser: String, change: Boolean) {
        val url = "http://localhost/insert_followed_id.php?idArticle=$idArticle&&idUser=$idUser&&change=$change"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val message = jsonObject.getString("message")
                    if (message == "Followed") {
                        // Change the button's layout when clicked
                        followButton.setBackgroundColor(Color.WHITE) // Change background color
                        followButton.setTextColor(Color.BLACK) // Change text color
                        followButton.text = "Following"
                        if (change == true){
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        followButton.setBackgroundColor(Color.parseColor("#2E2E2E")) // Change background color
                        followButton.setTextColor(Color.WHITE) // Change text color
                        followButton.text = "Follow"
                        if (change == true){
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            { error ->
                Log.e("Volley", "Error: ${error.message}")
            }

        )

        queue.add(stringRequest)
    }

    private fun fetchArticleDetails(idArticle: String) {
        val url = "http://localhost/get_article_details.php?idArticle=$idArticle"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    val jsonObject = JSONObject(response)
                    presentationTitle.text = jsonObject.optString("title", "N/A")
                    presentationDate.text = jsonObject.optString("date", "N/A")
                    presentationTime.text = jsonObject.optString("hour", "N/A")
                    presentationRoom.text = jsonObject.optString("room", "N/A")
                    presentationSpeaker.text = jsonObject.optString("authors", "N/A")
                    presentationAbstract.text = jsonObject.optString("description", "N/A")
                    trackColor.text = jsonObject.optString("name", "N/A")
                    val color = jsonObject.optString("color", "N/A")

                    trackColor.setBackgroundColor(Color.parseColor(color))

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            { error ->
                Log.e("Volley", "Error: ${error.message}")
            }
        )

        queue.add(stringRequest)
    }

    private fun fetchQuestions(idArticle: String) {
        val url = "http://localhost/get_questions.php?idArticle=$idArticle"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    val jsonArray = JSONArray(response)
                    questionList.clear()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val idArticle = jsonObject.getString("idArticle")
                        val question = jsonObject.getString("question")
                        val username = jsonObject.getString("username")
                        questionList.add(Question(idArticle, question, username))
                    }
                    updateQuestionList()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            { error ->
                Log.e("Volley", "Error: ${error.message}")
            }
        )

        queue.add(stringRequest)
    }

    private fun updateQuestionList() {
        questionAdapter.updateQuestions(questionList)
    }

    private fun addQuestion(idArticle: String, idUser: String, question: String) {
        val url = "http://localhost/add_question.php"

        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val status = jsonObject.getString("status")
                    val message = jsonObject.getString("message")
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    if (status == "success") {
                        // Refresh questions after adding a new one
                        fetchQuestions(idArticle)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Log.e("Volley", "Error: ${error.message}")
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["idArticle"] = idArticle
                params["idUser"] = idUser
                params["question"] = question
                params["active"] = "0"  // Set active to 0 by default
                return params
            }
        }
        queue.add(stringRequest)
    }

    class QuestionAdapter(private var questions: List<Question>) :
        RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

        class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val usernameTextView: TextView? = itemView.findViewById(R.id.username)
            val questionTextView: TextView? = itemView.findViewById(R.id.questionTextView)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false)
            return QuestionViewHolder(view)
        }

        override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
            holder.usernameTextView!!.text = questions[position].username
            holder.questionTextView!!.text = questions[position].question}

        override fun getItemCount(): Int = questions.size

        fun updateQuestions(newQuestions: List<Question>) {
            questions = newQuestions
            notifyDataSetChanged()
        }
    }

    data class Question(val id: String, val question: String, val username: String)
}
