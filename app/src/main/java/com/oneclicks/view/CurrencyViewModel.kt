/**
 * Copyright 2017 Erik Jhordan Rey.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.oneclicks.view

import android.arch.lifecycle.Lifecycle.Event.ON_DESTROY
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.oneclicks.data.repository.CurrencyRepository
import com.oneclicks.di.Application
import com.oneclicks.model.AvailableExchange
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CurrencyViewModel : ViewModel(), LifecycleObserver {

  @Inject lateinit var currencyRepository: CurrencyRepository

  private val compositeDisposable = CompositeDisposable()
  private var liveAvailableExchange: LiveData<ArrayList<AvailableExchange>>? = null

  init {
    initializeDagger()
  }

  fun getAvailableExchange(): LiveData<ArrayList<AvailableExchange>>? {
    liveAvailableExchange = null
    liveAvailableExchange = MutableLiveData<ArrayList<AvailableExchange>>()
    liveAvailableExchange = currencyRepository.getAvailableExchange()
    return liveAvailableExchange
  }


  @OnLifecycleEvent(ON_DESTROY)
  fun unSubscribeViewModel() {
    for (disposable in currencyRepository.allCompositeDisposable) {
      compositeDisposable.addAll(disposable)
    }
    compositeDisposable.clear()
  }

  private fun isRoomEmpty(currenciesTotal: Int) = currenciesTotal == 0


  override fun onCleared() {
    unSubscribeViewModel()
    super.onCleared()
  }

  private fun initializeDagger() = Application.appComponent.inject(this)

}
