//package com.storytel.login
//
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.lifecycle.Lifecycle
//import androidx.lifecycle.LifecycleOwner
//import androidx.lifecycle.LifecycleRegistry
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.Observer
//import com.nhaarman.mockitokotlin2.mock
//import com.nhaarman.mockitokotlin2.whenever
//import com.storytel.base.util.ResourceProvider
//import com.storytel.base.vo.Resource
//import com.storytel.login.api.StorytelApiResponse
//import com.storytel.login.feature.login.LoginRepository
//import com.storytel.login.pojo.LoginInput
//import com.storytel.login.pojo.LoginResponse
//import com.storytel.login.feature.login.UserLoginUiModel
//import com.storytel.login.feature.login.LoginViewModel
//import org.junit.After
//import org.junit.Assert
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.mockito.Mockito.mock
//import org.mockito.Mockito.verify
//
//
//class LoginViewModelTest {
//    @get:Rule
//    var instantTaskExecutorRule = InstantTaskExecutorRule()
//    private val repository = mock(LoginRepository::class.java)
//    private val res = mock(ResourceProvider::class.java)
//    private lateinit var viewModel: LoginViewModel
//    val lifecycle = LifecycleRegistry(mock(LifecycleOwner::class.java))
//
//
//
//    @Before
//    fun setUp() {
//        viewModel = LoginViewModel(repository, res)
//        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
//    }
//
//    @After
//    fun tearDown() {
//
//    }
//
//    @Test
//    fun attemptLoginIsLoading() {
//        val observer = mock<Observer<UserLoginUiModel>>()
//        viewModel.loginUiModel.observeForever(observer)
//
//        val uId = LoginInput(1, "abcd")
//        val password = LoginInput(2, "1234")
//        val loadingResource = Resource.loading<StorytelApiResponse<LoginResponse>>()
//        val liveDataLoading =
//                MutableLiveData<Resource<StorytelApiResponse<LoginResponse>>>().apply { value = loadingResource }
//
//        whenever(repository.login(uId, password)).thenReturn(liveDataLoading)
//        viewModel.attemptLogin(uId, password)
//        verify(repository).login(uId, password)
//        Assert.assertNotNull(viewModel.loginUiModel.value)
//        Assert.assertTrue(viewModel.loginUiModel.value!!.isLoading)
//
//        viewModel.loginUiModel.removeObserver(observer)
//    }
//}