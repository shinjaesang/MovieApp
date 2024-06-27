package kr.ac.movieapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.ListView

class ReservationListActivity : AppCompatActivity() {
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_list)

        val reservationListView = findViewById<ListView>(R.id.reservation_list_view)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, getReservationStrings())
        reservationListView.adapter = adapter

        reservationListView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, ReservationDetailsActivity::class.java)
            intent.putExtra("reservationIndex", position)
            startActivityForResult(intent, 1)
        }

        reservationListView.setOnItemLongClickListener { parent, view, position, id ->
            showCancelDialog(position)
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            adapter.clear()
            adapter.addAll(getReservationStrings())
            adapter.notifyDataSetChanged()
        }
    }

    private fun showCancelDialog(position: Int) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("예매 취소")
            .setMessage("이 예매를 취소하시겠습니까?")
            .setPositiveButton("확인") { _, _ ->
                ReservationManager.removeReservation(position)
                adapter.clear()
                adapter.addAll(getReservationStrings())
                adapter.notifyDataSetChanged()
            }
            .setNegativeButton("취소", null)
            .create()

        dialog.show()
    }

    private fun getReservationStrings(): List<String> {
        return ReservationManager.reservations.map { "${it.movieName}: ${it.date} ${it.time}" }
    }
}
