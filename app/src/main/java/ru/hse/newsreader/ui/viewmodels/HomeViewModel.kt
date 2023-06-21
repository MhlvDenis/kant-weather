package ru.hse.newsreader.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import ru.hse.newsreader.entities.Source
import ru.hse.newsreader.other.Credentials.URL
import ru.hse.newsreader.other.Resource
import java.io.IOException

class HomeViewModel : ViewModel() {

    private val sources = mutableListOf<Source>()

    private val _sourceItems = MutableLiveData<Resource<List<Source>>>()
    val sourceItems: LiveData<Resource<List<Source>>> = _sourceItems

    fun loadNewSource(context: Context) {
        _sourceItems.postValue(Resource.loading(null))
        WorkManager.getInstance(context).enqueue(
            OneTimeWorkRequestBuilder<Loader>().build()
        )
    }

    class Loader(
        context: Context,
        workerParams: WorkerParameters
    ) : Worker(context, workerParams) {

        private val client = OkHttpClient()

        override fun doWork(): Result {
            val request = Request.Builder().url(URL).build()
            try {
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        throw IOException("Failed to get source: ${response.message}")
                    }

                    val body = JSONObject(response.body!!.string())
                    val main = body.getJSONObject("main")
                    val weather = body.getJSONArray("weather").getJSONObject(0)
                    val wind = body.getJSONObject("wind")

                    Log.d("JSON Body", body.toString())

                    lastSource = Source(
                        location = body.get("name").toString(),
                        temperature = main.get("temp").toString(),
                        pressure = main.get("pressure").toString(),
                        weather = weather.get("main").toString(),

                        wind = wind.get("speed").toString(),
                        seaLevel = main.get("sea_level").toString()
                    )
                    applySource(lastSource!!)

                    return Result.success()
                }
            } catch (e: Exception) {
                when (e) {
                    is IOException, is JSONException -> {
                        Log.d("WeatherLoader.getNewSource", e.message ?: "")
                    }
                    else -> throw e
                }
            }
            return Result.failure()
        }
    }

    private fun applySource(source: Source) {
        sources.add(source)
        _sourceItems.postValue(Resource.success(sources))
    }

    init {
        applySource = this::applySource
    }

    private companion object {
        var lastSource: Source? = null

        lateinit var applySource: (Source) -> Unit
    }
}
