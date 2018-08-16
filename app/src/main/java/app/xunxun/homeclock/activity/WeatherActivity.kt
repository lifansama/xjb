package app.xunxun.homeclock.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.xunxun.homeclock.R
import app.xunxun.homeclock.dao.City
import app.xunxun.homeclock.dao.WeatherDao
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.item_city.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class WeatherActivity : BaseActivity() {
    lateinit var adapter: CityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        adapter = CityAdapter()
        searchEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.v("beforeTextChanged", "s $s start $start count $count after $after text ${searchEt.text}")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.v("onTextChanged", "s $s start $start before $before count $count text ${searchEt.text}")

                val citys = WeatherDao.citys(this@WeatherActivity, searchEt.text.toString())
                Log.v("citys", citys.toString())
                adapter.list.clear()
                adapter.list.addAll(citys)
                adapter.notifyDataSetChanged()

            }
        })
        adapter.citySelect = {
            val data = Intent().apply { putExtra("city", it) }
            setResult(Activity.RESULT_OK, data)
            finish()

        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


    }

}

class CityAdapter : RecyclerView.Adapter<CityViewHolder>() {
    val list: MutableList<City> = arrayListOf()
    var citySelect: ((city: City) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CityViewHolder {
        val itemView = LayoutInflater.from(parent!!.context).inflate(R.layout.item_city, parent, false)
        return CityViewHolder(itemView)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: CityViewHolder?, position: Int) {
        holder?.let {
            it.itemView.nameTv.text = list[position].name
            it.itemView.onClick { citySelect?.invoke(list[position]) }
        }
    }
}

class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
