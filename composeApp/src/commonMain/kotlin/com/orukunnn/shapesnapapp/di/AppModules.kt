package com.orukunnn.shapesnapapp.di

import com.orukunnn.shapesnapapp.data.datasource.AuthDatasource
import com.orukunnn.shapesnapapp.data.datasource.AuthDatasourceImpl
import com.orukunnn.shapesnapapp.data.datasource.EventDatasource
import com.orukunnn.shapesnapapp.data.datasource.EventDatasourceImpl
import com.orukunnn.shapesnapapp.data.datasource.FirestoreDatasource
import com.orukunnn.shapesnapapp.data.datasource.FirestoreDatasourceImpl
import com.orukunnn.shapesnapapp.data.datasource.KeyValueDatasource
import com.orukunnn.shapesnapapp.data.datasource.KeyValueDatasourceImpl
import com.orukunnn.shapesnapapp.data.model.user.UserProfile
import com.orukunnn.shapesnapapp.data.repository.auth.AuthRepository
import com.orukunnn.shapesnapapp.data.repository.auth.AuthRepositoryImpl
import com.orukunnn.shapesnapapp.data.repository.preset.PresetRepository
import com.orukunnn.shapesnapapp.data.repository.preset.PresetRepositoryImpl
import com.orukunnn.shapesnapapp.data.repository.user.UserPostsRepository
import com.orukunnn.shapesnapapp.data.repository.user.UserPostsRepositoryImpl
import com.orukunnn.shapesnapapp.data.repository.user.UserRepository
import com.orukunnn.shapesnapapp.data.repository.user.UserRepositoryImpl
import com.orukunnn.shapesnapapp.domain.EventLogger
import com.orukunnn.shapesnapapp.ui.home.HomeScreenViewModel
import com.orukunnn.shapesnapapp.ui.login.LoginViewModel
import com.orukunnn.shapesnapapp.ui.main.MainScreenViewModel
import com.orukunnn.shapesnapapp.ui.posts.PostsScreenViewModel
import com.orukunnn.shapesnapapp.ui.storage.StorageScreenViewModel
import com.russhwolf.settings.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun appModule() =
    module {
        single { Settings() }
        single { CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate) }
        single<KeyValueDatasource> { KeyValueDatasourceImpl(get()) }
        single<AuthDatasource> { AuthDatasourceImpl() }
        single<EventDatasource> { EventDatasourceImpl() }
        single<FirestoreDatasource> { FirestoreDatasourceImpl() }
        single<AuthRepository> { AuthRepositoryImpl(get()) }
        single<PresetRepository> { PresetRepositoryImpl(get(), get()) }
        single<UserPostsRepository> { UserPostsRepositoryImpl(get()) }
        single<UserRepository> { UserRepositoryImpl(get()) }
        single { EventLogger(get(), get()) }
        viewModel<HomeScreenViewModel> { (userProfile: UserProfile) ->
            HomeScreenViewModel(
                userProfile = userProfile,
                presetRepository = get(),
                userRepository = get(),
            )
        }
        viewModel<PostsScreenViewModel> { (userId: String) ->
            PostsScreenViewModel(
                userId = userId,
                presetRepository = get(),
                userRepository = get(),
            )
        }
        viewModelOf(::MainScreenViewModel)
        viewModelOf(::StorageScreenViewModel)
        viewModelOf(::LoginViewModel)
    }
