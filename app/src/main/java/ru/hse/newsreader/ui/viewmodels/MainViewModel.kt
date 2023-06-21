package ru.hse.newsreader.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.hse.newsreader.entities.Source

class MainViewModel : ViewModel() {

    private val _currentSource = MutableLiveData<Source?>()
    val currentSource: LiveData<Source?> = _currentSource

    fun clearCurrentSource() {
        _currentSource.postValue(null)
    }

    fun postCurrentSource(source: Source) {
        _currentSource.postValue(source)
    }
}