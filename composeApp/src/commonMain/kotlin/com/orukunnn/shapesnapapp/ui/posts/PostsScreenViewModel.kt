package com.orukunnn.shapesnapapp.ui.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orukunnn.shapesnapapp.data.model.user.UserPost
import com.orukunnn.shapesnapapp.data.repository.auth.AuthRepository
import com.orukunnn.shapesnapapp.data.repository.user.UserPostsRepository
import com.orukunnn.shapesnapapp.ui.common.LoadState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PostsScreenViewModel(
    private val authRepository: AuthRepository,
    private val userPostsRepository: UserPostsRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<LoadState<List<UserPost>>>(LoadState.Loading)
    val state: StateFlow<LoadState<List<UserPost>>> = _state.asStateFlow()

    private var nextCursor: String? = null
    private var loadingPage = false

    init {
        viewModelScope.launch {
            refresh(reset = true)
        }
    }

    fun loadMore() {
        if (loadingPage || nextCursor == null) return
        viewModelScope.launch {
            append()
        }
    }

    private suspend fun refresh(reset: Boolean) {
        val uid = authRepository.currentUser.filterNotNull().first().uid
        loadingPage = true
        if (reset) {
            _state.value = LoadState.Loading
            nextCursor = null
        }
        val result =
            userPostsRepository.fetchPage(
                uid = uid,
                pageSize = PAGE_SIZE,
                startAfterDocumentId = null,
            )
        if (result.isSuccess) {
            val page = result.getOrThrow()
            nextCursor = page.nextCursor
            _state.value = LoadState.Success(page.items)
        } else {
            val e = result.exceptionOrNull() ?: Throwable("unknown")
            _state.value = LoadState.Error(e.message ?: "error")
        }
        loadingPage = false
    }

    private suspend fun append() {
        val uid = authRepository.currentUser.filterNotNull().first().uid
        val current = (_state.value as? LoadState.Success)?.data ?: return
        loadingPage = true
        val result =
            userPostsRepository.fetchPage(
                uid = uid,
                pageSize = PAGE_SIZE,
                startAfterDocumentId = nextCursor,
            )
        if (result.isSuccess) {
            val page = result.getOrThrow()
            nextCursor = page.nextCursor
            _state.value = LoadState.Success(current + page.items)
        } else {
            val e = result.exceptionOrNull() ?: Throwable("unknown")
            _state.value = LoadState.Error(e.message ?: "error")
        }
        loadingPage = false
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
