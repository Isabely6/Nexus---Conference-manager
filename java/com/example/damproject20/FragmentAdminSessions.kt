package com.example.damproject20

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class FragmentAdminSessions : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_activity_create_session, container, false)

        val etTitle: EditText = view.findViewById(R.id.title)
        val etTrack: EditText = view.findViewById(R.id.track)
        val etTrackDesc: EditText = view.findViewById(R.id.trackdesc)
        val etAuthor: EditText = view.findViewById(R.id.author)
        val dateSpinner: Spinner = view.findViewById(R.id.date)
        val timeSpinner: Spinner = view.findViewById(R.id.time)
        val etRoom: EditText = view.findViewById(R.id.room)
        val etDescription: EditText = view.findViewById(R.id.description)
        val btnPublish: Button = view.findViewById(R.id.publish_button)

        btnPublish.setOnClickListener {
            val title = etTitle.text.toString()
            val track = etTrack.text.toString()
            val trackdesc = etTrackDesc.text.toString()
            val author = etAuthor.text.toString()
            val date = dateSpinner.selectedItem.toString()
            val time = timeSpinner.selectedItem.toString()
            val room = etRoom.text.toString()
            val description = etDescription.text.toString()

            val editTexts = listOf(etTitle, etTrack, etTrackDesc, etAuthor, etRoom, etDescription)

            if (editTexts.any { it.text.toString().trim().isEmpty() } || dateSpinner.selectedItemPosition == 0) {
                Toast.makeText(requireContext(), "There are one or more fields empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            etTitle.setText("")
            etTrack.setText("")
            etTrackDesc.setText("")
            etAuthor.setText("")
            etRoom.setText("")
            etDescription.setText("")

            uploadData(title, track, trackdesc, author, date, time, room, description)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the Spinner
        val dateSpinner: Spinner = view.findViewById(R.id.date)

        // Create a list of dates
        val dates = listOf("Select a date", "June 26", "June 27", "June 28")

        // Create an ArrayAdapter using the dates list and a default spinner layout
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, dates)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        dateSpinner.adapter = adapter

        // Initialize the Spinner
        val timeSpinner: Spinner = view.findViewById(R.id.time)

        // Create a list of dates
        val times = listOf("Select a time", "10:00", "11:00", "12:00", "14:00", "15:00", "16:00", "17:00")

        // Create an ArrayAdapter using the dates list and a default spinner layout
        val timeadapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, times)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        timeSpinner.adapter = timeadapter
    }

    private fun uploadData(title: String, track: String, trackdesc: String, author: String, date: String, time: String, room: String, description: String) {
        val url = "http://localhost/upload.php"

        val requestQueue: RequestQueue = Volley.newRequestQueue(requireContext())

        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                val jsonResponse = JSONObject(response)
                val success = jsonResponse.getBoolean("success")
                val message = jsonResponse.getString("message")
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                if (success) {
                    // Handle successful publish
                    Toast.makeText(requireContext(), "Publish successful", Toast.LENGTH_SHORT).show()
                } else {
                    // Handle failed publish
                    Toast.makeText(requireContext(), "Publish failed: $message", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["title"] = title
                params["track"] = track
                params["trackdesc"] = trackdesc
                params["author"] = author
                params["date"] = date
                params["time"] = time
                params["room"] = room
                params["description"] = description
                return params
            }
        }
        requestQueue.add(stringRequest)
    }
}
