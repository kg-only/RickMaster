package com.example.rickmaster.presentation.cameras

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickmaster.domain.repository.CameraRepository
import com.example.rickmaster.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(private val repo: CameraRepository) : ViewModel() {

    private val _cameraState: MutableStateFlow<CameraState> = MutableStateFlow(CameraState())
    val cameraState: StateFlow<CameraState> = _cameraState.asStateFlow()

    private val _revealedCardIdsList = MutableStateFlow(listOf<Int>())
    val revealedCardIdsList: StateFlow<List<Int>> get() = _revealedCardIdsList


    init {
        getDataFromDb()
    }

    fun getCameraList() {
        repo.fetchCameras().onEach { result ->
            when (result) {
                is Resource.Loading -> _cameraState.value = CameraState(isLoading = true)
                is Resource.Success -> _cameraState.value = CameraState(cameraData = result.data)
                is Resource.Error -> _cameraState.value = CameraState(error = result.message!!)
            }
        }.launchIn(viewModelScope)
    }

    fun getDataFromDb() {
        repo.fetchCameraFromDataBase().onEach {
            if (it == null) getCameraList()
            else _cameraState.value = CameraState(cameraData = it)
        }.launchIn(viewModelScope)
    }

    fun setFavorite(cameraId: Int) {
        viewModelScope.launch {
            val response = repo.setFavorite(cameraId)
            if (response == null) getDataFromDb()
            else _cameraState.value = CameraState(cameraData = response)
        }
    }

    fun onItemExpanded(cardId: Int) {
        if (_revealedCardIdsList.value.contains(cardId)) return
        _revealedCardIdsList.value = _revealedCardIdsList.value.toMutableList().also { list ->
            list.clear()
            list.add(cardId)
        }
    }

    fun onItemCollapsed(cardId: Int) {
        if (!_revealedCardIdsList.value.contains(cardId)) return
        _revealedCardIdsList.value = _revealedCardIdsList.value.toMutableList().also { list ->
            list.remove(cardId)
        }
    }
}