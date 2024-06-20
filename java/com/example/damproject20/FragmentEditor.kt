package com.example.damproject20

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.util.HashMap

class FragmentEditor : Fragment() {

    private lateinit var tvArticleName: TextView
    private lateinit var tvTrackName: TextView
    private lateinit var tvTrackDesc: TextView
    private lateinit var tvAuthor: TextView
    private lateinit var dateSpinner: Spinner
    private lateinit var timeSpinner: Spinner
    private lateinit var tvRoom: TextView
    private lateinit var tvAbstract: TextView
    private lateinit var btnSave: Button
    private lateinit var btnDelete: Button
    private lateinit var requestQueue: RequestQueue

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_edit_session, container, false)

        tvArticleName = view.findViewById(R.id.title)
        tvTrackName = view.findViewById(R.id.track)
        tvTrackDesc = view.findViewById(R.id.trackdesc)
        tvAuthor = view.findViewById(R.id.author)
        dateSpinner = view.findViewById(R.id.date)
        timeSpinner = view.findViewById(R.id.time)
        tvRoom = view.findViewById(R.id.room)
        tvAbstract = view.findViewById(R.id.description)
        btnSave = view.findViewById(R.id.save_button)
        btnDelete = view.findViewById(R.id.delete_button)

        val args = arguments
        val idArticle = args?.getString("idArticle")

        if (args!= null) {
            tvArticleName.text = args.getString("articleName")
            tvTrackName.text = args.getString("trackName")
            tvTrackDesc.text = args.getString("trackDesc")
            tvAuthor.text = args.getString("author")

            val dateValue = args.getString("date")
            val timeValue = args.getString("time")

            val dateAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item)
            dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            dateSpinner.adapter = dateAdapter

            val timeAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item)
            timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            timeSpinner.adapter = timeAdapter

            val days = listOf("June 26", "June 27", "June 28")
            val times = listOf("10:00:00", "11:00:00", "12:00:00", "14:00:00", "15:00:00", "16:00:00", "17:00:00")

            dateAdapter.addAll(days)
            timeAdapter.addAll(times)

            val dateIndex = days.indexOf(dateValue)
            val timeIndex = times.indexOf(timeValue)

            dateSpinner.setSelection(dateIndex)
            timeSpinner.setSelection(timeIndex)

            tvRoom.text = args.getString("room")
            tvAbstract.text = args.getString("abstract")
        }
        requestQueue = Volley.newRequestQueue(requireContext())

        btnSave.setOnClickListener {
            val articleName = tvArticleName.text.toString()
            val trackName = tvTrackName.text.toString()
            val trackDesc = tvTrackDesc.text.toString()
            val author = tvAuthor.text.toString()
            val date = dateSpinner.selectedItem.toString() // Get the selected date from the spinner
            val time = timeSpinner.selectedItem.toString() // Get the selected time from the spinner
            val room = tvRoom.text.toString()
            val abstract = tvAbstract.text.toString()

            // Send data to database
            val url = "http://localhost/update_session.php"
            val stringRequest = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener { _ ->
                    Toast.makeText(requireContext(), "Session updated successfully", Toast.LENGTH_SHORT).show()
                    val transaction = parentFragmentManager.beginTransaction()
                    transaction.remove(this@FragmentEditor)
                    transaction.commit()

                    val fragment = FragmentAdminEditSession()
                    val fragmentManager = (requireContext() as AppCompatActivity).supportFragmentManager
                    fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit()
                },
                Response.ErrorListener { error ->
                    Log.e("FragmentEditor", "Error: ${error.message}")
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            ) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["idArticle"] = idArticle!!
                    params["articleName"] = articleName
                    params["trackName"] = trackName
                    params["trackDesc"] = trackDesc
                    params["author"] = author
                    params["date"] = date
                    params["time"] = time
                    params["room"] = room
                    params["abstract"] = abstract
                    return params
                }
            }
            requestQueue.add(stringRequest)
        }

        btnDelete.setOnClickListener {
            val url = "http://localhost/delete_session.php"
            val stringRequest = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener { _ ->
                    Toast.makeText(requireContext(), "Session deleted successfully", Toast.LENGTH_SHORT).show()
                    val transaction = parentFragmentManager.beginTransaction()
                    transaction.remove(this@FragmentEditor)
                    transaction.commit()

                    val fragment = FragmentAdminEditSession()
                    val fragmentManager = (requireContext() as AppCompatActivity).supportFragmentManager
                    fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit()
                },
                Response.ErrorListener { error ->
                    Log.e("FragmentEditor", "Error: ${error.message}")
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            ) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["idArticle"] = idArticle!!
                    return params
                }
            }
            requestQueue.add(stringRequest)
        }

        return view
    }
}
