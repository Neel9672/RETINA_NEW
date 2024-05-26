package com.example.retina

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val titleTextView: TextView = findViewById(R.id.title_api)
        val descriptionTextView: TextView = findViewById(R.id.description_api)
        val shareButton: TextView = findViewById(R.id.share_btn1)
        val shareButton2: TextView = findViewById(R.id.share_btn)
        val copyButton: TextView = findViewById(R.id.copy_btn1)
        val copyButton2: TextView = findViewById(R.id.copy_btn)

        shareButton.setOnClickListener {

            val textToShare = titleTextView.text.toString()


            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, textToShare)
                type = "text/plain"
            }


            try {
                startActivity(Intent.createChooser(shareIntent, "Share via"))
            } catch (e: Exception) {
                Toast.makeText(this, "No app available to share the text", Toast.LENGTH_SHORT).show()
            }
        }


        shareButton2.setOnClickListener {

            val textToShare = descriptionTextView.text.toString()


            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, textToShare)
                type = "text/plain"
            }


            try {
                startActivity(Intent.createChooser(shareIntent, "Share via"))
            } catch (e: Exception) {
                Toast.makeText(this, "No app available to share the text", Toast.LENGTH_SHORT).show()
            }
        }

        copyButton.setOnClickListener {
            val textToCopy = titleTextView.text.toString()


            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            val clip = ClipData.newPlainText("Copied Text", textToCopy)


            clipboard.setPrimaryClip(clip)

            Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
        }

        copyButton2.setOnClickListener {
            val textToCopy = descriptionTextView.text.toString()

            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            val clip = ClipData.newPlainText("Copied Text", textToCopy)

            clipboard.setPrimaryClip(clip)

            Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
        }


        fetchAndPopulateData(titleTextView, descriptionTextView)
    }

    private fun fetchAndPopulateData(titleTextView: TextView, descriptionTextView: TextView) {
        thread {
            try {
                val url = URL("https://run.mocky.io/v3/f4613593-a726-4908-84cf-08b5b96c4a57")
                val urlConnection = url.openConnection() as HttpURLConnection

                try {
                    val response = urlConnection.inputStream.bufferedReader().readText()

                    val jsonResponse = JSONObject(response)
                    val choicesArray = jsonResponse.getJSONArray("choices")
                    val messageObject = choicesArray.getJSONObject(0).getJSONObject("message")
                    val contentString = messageObject.getString("content")

                    val contentJson = JSONObject(contentString)
                    val title = contentJson.getString("title")
                    val description = contentJson.getString("description")


                    runOnUiThread {
                        titleTextView.text = title
                        descriptionTextView.text = description
                    }
                } finally {
                    urlConnection.disconnect()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }



}