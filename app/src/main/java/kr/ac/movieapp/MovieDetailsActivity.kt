package kr.ac.movieapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import java.util.Calendar

class MovieDetailsActivity : AppCompatActivity() {
    private lateinit var movieName: String
    private lateinit var detailUrl: String
    private var imgResId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        movieName = intent.getStringExtra("movieName") ?: ""
        detailUrl = intent.getStringExtra("detailUrl") ?: ""
        imgResId = intent.getIntExtra("imgResId", -1)

        val movieDetailsTextView = findViewById<TextView>(R.id.movie_details_text_view)
        val detailLinkButton = findViewById<Button>(R.id.detail_link_button)
        val reserveButton = findViewById<Button>(R.id.reserve_button)
        val movieImageView = findViewById<ImageView>(R.id.movie_image_view)

        movieDetailsTextView.text = "영화: $movieName\n상세 정보 보기"
        if (imgResId != -1) {
            movieImageView.setImageResource(imgResId)
        }

        detailLinkButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(detailUrl))
            startActivity(intent)
        }

        reserveButton.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(this,
            { _, year, month, dayOfMonth ->
                showTimePickerDialog("$year/${month + 1}/$dayOfMonth")
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showTimePickerDialog(date: String) {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(this,
            { _, hourOfDay, minute ->
                showPersonCountDialog(date, "$hourOfDay:$minute")
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    private fun showPersonCountDialog(date: String, time: String) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_person_count, null)
        val adultsSpinner = dialogView.findViewById<Spinner>(R.id.adults_spinner)
        val childrenSpinner = dialogView.findViewById<Spinner>(R.id.children_spinner)
        val disabledSpinner = dialogView.findViewById<Spinner>(R.id.disabled_spinner)

        val spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.person_count, android.R.layout.simple_spinner_item)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        adultsSpinner.adapter = spinnerAdapter
        childrenSpinner.adapter = spinnerAdapter
        disabledSpinner.adapter = spinnerAdapter

        val dialog = AlertDialog.Builder(this)
            .setTitle("인원 선택")
            .setView(dialogView)
            .setPositiveButton("확인", null)
            .setNegativeButton("취소", null)
            .create()

        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                val adults = adultsSpinner.selectedItem.toString().toInt()
                val children = childrenSpinner.selectedItem.toString().toInt()
                val disabled = disabledSpinner.selectedItem.toString().toInt()

                if (adults + children + disabled < 1) {
                    Toast.makeText(this, "최소 한 명 이상 선택해야 합니다.", Toast.LENGTH_SHORT).show()
                } else {
                    val reservation = Reservation(movieName, date, time, adults, children, disabled, detailUrl)
                    ReservationManager.addReservation(reservation)
                    dialog.dismiss()
                    // 좌석 선택 화면으로 이동
                    val intent = Intent(this, SeatSelectionActivity::class.java)
                    intent.putExtra("reservation", reservation)
                    startActivity(intent)
                    finish()
                }
            }
        }

        dialog.show()
    }
}
