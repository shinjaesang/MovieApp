package kr.ac.movieapp

import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SeatSelectionActivity : AppCompatActivity() {

    private lateinit var seatGrid: GridLayout
    private lateinit var confirmButton: Button
    private lateinit var reservation: Reservation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seat_selection)

        reservation = intent.getSerializableExtra("reservation") as Reservation

        seatGrid = findViewById(R.id.seat_grid)
        confirmButton = findViewById(R.id.confirm_button)

        populateSeats()

        confirmButton.setOnClickListener {
            val selectedSeats = getSelectedSeats()
            if (selectedSeats.isEmpty()) {
                Toast.makeText(this, "좌석을 선택해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                val updatedReservation = reservation.copy(seats = selectedSeats)
                ReservationManager.updateReservation(ReservationManager.reservations.indexOf(reservation), updatedReservation)
                Toast.makeText(this, "예매가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun populateSeats() {
        for (i in 1..5) {
            for (j in 1..5) {
                val seatButton = Button(this).apply {
                    text = "A$i$j"
                    setOnClickListener {
                        isSelected = !isSelected
                        updateSeatButtonBackground(this)
                    }
                }
                seatGrid.addView(seatButton)
            }
        }
    }

    private fun updateSeatButtonBackground(button: Button) {
        button.setBackgroundColor(if (button.isSelected) android.graphics.Color.parseColor("#008080") else android.graphics.Color.parseColor("#800080"))
    }

    private fun getSelectedSeats(): List<String> {
        val selectedSeats = mutableListOf<String>()
        for (i in 0 until seatGrid.childCount) {
            val seatButton = seatGrid.getChildAt(i) as Button
            if (seatButton.isSelected) {
                selectedSeats.add(seatButton.text.toString())
            }
        }
        return selectedSeats
    }
}
