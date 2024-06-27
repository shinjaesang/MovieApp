package kr.ac.movieapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import java.util.Calendar

class ReservationDetailsActivity : AppCompatActivity() {
    private var reservationIndex: Int = -1
    private lateinit var reservation: Reservation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_details)

        val reservationDetailsTextView = findViewById<TextView>(R.id.reservation_details_text_view)
        val deleteButton = findViewById<Button>(R.id.delete_button)
        val modifyButton = findViewById<Button>(R.id.modify_button)

        reservationIndex = intent.getIntExtra("reservationIndex", -1)

        if (reservationIndex != -1) {
            reservation = ReservationManager.reservations[reservationIndex]
            reservationDetailsTextView.text = """
                영화: ${reservation.movieName}
                날짜: ${reservation.date}
                시간: ${reservation.time}
                성인: ${reservation.adults}명
                아동: ${reservation.children}명
                장애인: ${reservation.disabled}명
                좌석: ${if (reservation.seats.isNotEmpty()) reservation.seats.joinToString(", ") else "선택되지 않음"}
            """.trimIndent()

            deleteButton.setOnClickListener {
                ReservationManager.removeReservation(reservationIndex)
                Toast.makeText(this, "예매가 취소되었습니다.", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            }

            modifyButton.setOnClickListener {
                showModifyDialog()
            }
        } else {
            reservationDetailsTextView.text = "예약 정보가 없습니다."
        }
    }

    private fun showModifyDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_person_count, null)
        val adultsSpinner = dialogView.findViewById<Spinner>(R.id.adults_spinner)
        val childrenSpinner = dialogView.findViewById<Spinner>(R.id.children_spinner)
        val disabledSpinner = dialogView.findViewById<Spinner>(R.id.disabled_spinner)

        val spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.person_count, android.R.layout.simple_spinner_item)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        adultsSpinner.adapter = spinnerAdapter
        childrenSpinner.adapter = spinnerAdapter
        disabledSpinner.adapter = spinnerAdapter

        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(this,
            { _, year, month, dayOfMonth ->
                val timePickerDialog = TimePickerDialog(this,
                    { _, hourOfDay, minute ->
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
                                    val updatedReservation = Reservation(reservation.movieName, "$year/${month + 1}/$dayOfMonth", "$hourOfDay:$minute", adults, children, disabled, reservation.detailUrl)
                                    ReservationManager.updateReservation(reservationIndex, updatedReservation)
                                    Toast.makeText(this, "예매가 수정되었습니다.", Toast.LENGTH_SHORT).show()
                                    setResult(RESULT_OK)
                                    dialog.dismiss()
                                    // 좌석 선택 화면으로 이동
                                    val intent = Intent(this, SeatSelectionActivity::class.java)
                                    intent.putExtra("reservation", updatedReservation)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }

                        dialog.show()
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )
                timePickerDialog.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}
