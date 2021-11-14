package com.amfysy.bicloodroid.ui.main

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amfysy.bicloodroid.data.repositories.BiclooRecordsRepository
import com.amfysy.bicloodroid.ui.shared.BiclooRecordViewModel
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    val biclooRecords = ObservableArrayList<BiclooRecordViewModel>()
    val selectedRecord = ObservableField<BiclooRecordViewModel>()

    init {
        this.viewModelScope.launch {
            loadData()
        }
    }

    private suspend fun loadData() {
        val biclooRecordsRepository = BiclooRecordsRepository()
        biclooRecordsRepository.getRecords().onFailure {
            // TODO: Display some error message
        }.onSuccess { records ->
            biclooRecords.clear()
            records
                .map { BiclooRecordViewModel(it) }
                .let(biclooRecords::addAll)
        }
    }

    fun pinClicked(selectedBiclooRecord: BiclooRecordViewModel) {
        selectedRecord.set(selectedBiclooRecord)
    }
}