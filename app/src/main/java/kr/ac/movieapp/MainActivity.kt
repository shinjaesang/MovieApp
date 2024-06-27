package kr.ac.movieapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var imgv: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val list = findViewById<ListView>(R.id.list)
        imgv = findViewById<ImageView>(R.id.imgv)
        val viewReservationsButton = findViewById<Button>(R.id.view_reservations_button)

        val movies = arrayOf("파묘", "라라랜드", "너의 이름은", "겟 아웃", "어벤져스", "HER", "비긴 어게인")
        val imgRes = arrayOf(R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4, R.drawable.p5, R.drawable.p6, R.drawable.p7)
        val movieUrls = arrayOf(
            "https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&ssc=tab.nx.all&query=%ED%8C%8C%EB%AC%98&oquery=%ED%8C%8C%EC%9A%94&tqi=iFWkYwqVOswssUBm8gVsssssscK-474741",
            "https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&mra=bkEw&pkid=68&os=2324869&qvt=0&query=%EC%98%81%ED%99%94%20%EB%9D%BC%EB%9D%BC%EB%9E%9C%EB%93%9C",
            "https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&ssc=tab.nx.all&query=%EB%84%88%EC%9D%98+%EC%9D%B4%EB%A6%84%EC%9D%80&oquery=%EC%98%81%ED%99%94+%EB%9D%BC%EB%9D%BC%EB%9E%9C%EB%93%9C&tqi=iFWk8lqVOsVssM01Gw8ssssstmG-405680",
            "https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&ssc=tab.nx.all&query=%EA%B2%9F+%EC%95%84%EC%9B%83&oquery=%EB%84%88%EC%9D%98+%EC%9D%B4%EB%A6%84%EC%9D%80&tqi=iFWNtdqVN8ossKdpt08ssssssFs-126557",
            "https://search.naver.com/search.naver?sm=tab_sug.top&where=nexearch&ssc=tab.nx.all&query=%EC%96%B4%EB%B2%A4%EC%A0%B8%EC%8A%A4+%EC%97%94%EB%93%9C%EA%B2%8C%EC%9E%84&oquery=%EA%B2%9F+%EC%95%84%EC%9B%83&tqi=iFWNZlqVN8Vss63sxWZsssssssZ-085131&acq=%EC%96%B4%EB%B2%A4%EC%A0%B8%EC%8A%A4+%EC%97%94%EB%93%9C&acr=1&qdt=0",
            "https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&ssc=tab.nx.all&query=%EC%98%81%ED%99%94+her&oquery=%EC%96%B4%EB%B2%A4%EC%A0%B8%EC%8A%A4+%EC%97%94%EB%93%9C%EA%B2%8C%EC%9E%84&tqi=iFWN7wqpts0ssTndpNdssssss6G-443570",
            "https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&mra=bkEw&pkid=68&os=1829626&qvt=0&query=%EC%98%81%ED%99%94%20%EB%B9%84%EA%B8%B4%20%EC%96%B4%EA%B2%8C%EC%9D%B8"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, movies)
        list.adapter = adapter

        list.setOnItemClickListener { parent, view, position, id ->
            imgv.setImageResource(imgRes[position])
            val intent = Intent(this, MovieDetailsActivity::class.java)
            intent.putExtra("movieName", movies[position])
            intent.putExtra("detailUrl", movieUrls[position])
            intent.putExtra("imgResId", imgRes[position])
            startActivity(intent)
        }

        viewReservationsButton.setOnClickListener {
            val intent = Intent(this, ReservationListActivity::class.java)
            startActivity(intent)
        }
    }
}
