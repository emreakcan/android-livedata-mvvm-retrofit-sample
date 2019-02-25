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

package com.oneclicks.data.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.oneclicks.data.remote.RemoteCurrencyDataSource
import com.oneclicks.model.AvailableExchange
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepository @Inject constructor(private val remoteCurrencyDataSource: RemoteCurrencyDataSource) : Repository {

    val allCompositeDisposable: MutableList<Disposable> = arrayListOf()


    override fun getAvailableExchange(): LiveData<ArrayList<AvailableExchange>> {
        val mutableLiveData = MutableLiveData<ArrayList<AvailableExchange>>()
        val disposable = remoteCurrencyDataSource.requestAvailableExchange()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ currencyResponse ->
                mutableLiveData.value = currencyResponse
            }, { t: Throwable? -> t?.printStackTrace() })

        allCompositeDisposable.add(disposable)
        return mutableLiveData
    }

}
