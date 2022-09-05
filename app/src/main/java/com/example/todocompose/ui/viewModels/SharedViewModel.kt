package com.example.todocompose.ui.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todocompose.data.model.Priority
import com.example.todocompose.data.model.ToDoTask
import com.example.todocompose.data.repository.DataStoreRepository
import com.example.todocompose.data.repository.ToDoRepository
import com.example.todocompose.utils.Action
import com.example.todocompose.utils.Contants.MAX_TITLE_LENGTH
import com.example.todocompose.utils.RequestState
import com.example.todocompose.utils.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val toDoRespository : ToDoRepository,
    private val dataStoreRepository: DataStoreRepository
    ): ViewModel() {

    //val action: MutableState<Action> = mutableStateOf(Action.NO_ACTION)

    var action by mutableStateOf(Action.NO_ACTION)
        private set

    val id: MutableState<Int> = mutableStateOf(0)
    val title: MutableState<String> = mutableStateOf("")
    val description: MutableState<String> = mutableStateOf("")
    val priority: MutableState<Priority> = mutableStateOf(Priority.LOW)

    val searchAppBarState: MutableState<SearchAppBarState> =
        mutableStateOf(SearchAppBarState.CLOSED)
    val searchTextState: MutableState<String> =
        mutableStateOf("")

    private val _allTasks: MutableStateFlow<RequestState<List<ToDoTask>>> = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val allTasks: StateFlow<RequestState<List<ToDoTask>>> = _allTasks

    private val _searchedTasks: MutableStateFlow<RequestState<List<ToDoTask>>> = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val searchedTasks: StateFlow<RequestState<List<ToDoTask>>> = _searchedTasks

    fun getAllTasks(){
        _allTasks.value = RequestState.Loading

        try {

            viewModelScope.launch {
                toDoRespository.getAllTasks.collect{ data ->
                    _allTasks.value = RequestState.Success(data)
                }
            }

        }catch (e: Exception){
            _allTasks.value = RequestState.Error(e)
        }

    }

    fun searchDatabase(searchQuery: String){
        _searchedTasks.value = RequestState.Loading

        try {

            viewModelScope.launch {
                toDoRespository.searchDatabase(searchQuery = "%${searchQuery.trim()}%").collect{ searchData ->
                    _searchedTasks.value = RequestState.Success(searchData)
                }
            }

        }catch (e: Exception){
            _searchedTasks.value = RequestState.Error(e)
        }

        searchAppBarState.value = SearchAppBarState.TRIGGERED

    }

    private val _selectedTask: MutableStateFlow<ToDoTask?> = MutableStateFlow(null)
    val selectedTask: StateFlow<ToDoTask?> = _selectedTask

    fun getSelectedTask(taskId: Int){

        viewModelScope.launch {

            toDoRespository.getSelectedTask(taskId).collect{ task ->

                _selectedTask.value = task

            }

        }

    }

    fun updateTaskFields(selectedTask: ToDoTask?){

        if(selectedTask != null){
            id.value = selectedTask.id
            title.value = selectedTask.title
            description.value = selectedTask.description
            priority.value = selectedTask.priority
        }else{
            id.value = 0
            title.value = ""
            description.value = ""
            priority.value =  Priority.LOW
        }

    }

    fun updateTitle(newTitle: String){
        if(newTitle.length < MAX_TITLE_LENGTH){
            title.value = newTitle
        }
    }

    fun validateFields(): Boolean{
        return title.value.isNotEmpty() && description.value.isNotEmpty()
    }

    private fun addTask(){
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                title = title.value,
                priority = priority.value,
                description = description.value
            )

            toDoRespository.addTask(toDoTask = toDoTask)
        }
        searchAppBarState.value = SearchAppBarState.CLOSED
    }

    private fun updateTask(){
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id.value,
                title = title.value,
                priority = priority.value,
                description = description.value
            )

            toDoRespository.updateTask(toDoTask = toDoTask)
        }
    }

    private fun deleteTask(){
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id.value,
                title = title.value,
                priority = priority.value,
                description = description.value
            )

            toDoRespository.deleteTask(toDoTask = toDoTask)
        }
    }

    private fun deleteAllTask(){
        viewModelScope.launch(Dispatchers.IO) {
            toDoRespository.deleteAllTasks()
        }
    }

    fun handleDatabaseActions(action: Action){

        when(action){

            Action.ADD -> {
                addTask()
            }

            Action.UPDATE -> {
                updateTask()
            }

            Action.DELETE -> {
                deleteTask()
            }

            Action.DELETE_ALL -> {
                deleteAllTask()
            }

            Action.UNDO -> {
                addTask()
            }

            else -> {

            }

        }

    }

    val lowPriority: StateFlow<List<ToDoTask>> =
        toDoRespository.sortByLowPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )

    val highPriority: StateFlow<List<ToDoTask>> =
        toDoRespository.sortByHighPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )

    private val _sortState = MutableStateFlow<RequestState<Priority>>(RequestState.Idle)
    val sortState: StateFlow<RequestState<Priority>> = _sortState

    fun readSortState(){
        _sortState.value = RequestState.Loading
        try {

            viewModelScope.launch {

                dataStoreRepository.readSortState
                    .map { Priority.valueOf(it) }
                    .collect{
                        _sortState.value = RequestState.Success(it)
                    }

            }

        }catch (e: Exception){
            _sortState.value = RequestState.Error(e)
        }
    }

    fun persistSortingState(priority: Priority){

        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.persistSortState(priority)
        }

    }

    fun updateAction(newAction: Action){
        action = newAction
    }

    init {
        getAllTasks()
        readSortState()
    }

}