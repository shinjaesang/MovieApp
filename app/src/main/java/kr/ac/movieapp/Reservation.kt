package kr.ac.movieapp

import java.io.Serializable

data class Reservation(
    val movieName: String,
    val date: String,
    val time: String,
    val adults: Int,
    val children: Int,
    val disabled: Int,
    val detailUrl: String,
    val seats: List<String> = emptyList()
) : Serializable
