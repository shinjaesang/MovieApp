package kr.ac.movieapp

object ReservationManager {
    private val _reservations = mutableListOf<Reservation>()
    val reservations: List<Reservation>
        get() = _reservations

    fun addReservation(reservation: Reservation) {
        _reservations.add(reservation)
    }

    fun removeReservation(index: Int) {
        if (index >= 0 && index < _reservations.size) {
            _reservations.removeAt(index)
        }
    }

    fun updateReservation(index: Int, updatedReservation: Reservation) {
        if (index >= 0 && index < _reservations.size) {
            _reservations[index] = updatedReservation
        }
    }
}
