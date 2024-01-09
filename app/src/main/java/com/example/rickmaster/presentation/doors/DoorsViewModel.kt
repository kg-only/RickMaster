package com.example.rickmaster.presentation.doors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickmaster.domain.repository.DoorsRepository
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
class DoorsViewModel @Inject constructor(private val repo: DoorsRepository) : ViewModel() {

    private val _doorsState: MutableStateFlow<DoorsState> = MutableStateFlow(DoorsState())
    val doorsState: StateFlow<DoorsState> = _doorsState.asStateFlow()

    private val _revealedCardIdsList = MutableStateFlow(listOf<Int>())
    val revealedCardIdsList: StateFlow<List<Int>> get() = _revealedCardIdsList


    init {
        getDataFromDb()
    }

    fun getDoorsList() {
        repo.fetchDoors().onEach { result ->
            when (result) {
                is Resource.Loading -> _doorsState.value = DoorsState(isLoading = true)
                is Resource.Success -> _doorsState.value = DoorsState(doorsData = result.data)
                is Resource.Error -> _doorsState.value = DoorsState(error = result.message!!)
            }
        }.launchIn(viewModelScope)
    }

    fun getDataFromDb() {
        repo.fetchDoorsFromDataBase().onEach {
            if (it == null) getDoorsList()
            else _doorsState.value = DoorsState(doorsData = it)
        }.launchIn(viewModelScope)
    }

    fun edit(doorId: Int, newName: String? = null) {
        viewModelScope.launch {
            val response = repo.edit(doorId, newName)
            if (response == null) getDataFromDb()
            else _doorsState.value = DoorsState(doorsData = response)
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
    fun clearExpandedItems() {
        _revealedCardIdsList.value = _revealedCardIdsList.value.toMutableList().also { list ->
            list.clear()
        }
    }
}