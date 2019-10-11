package com.infernal93.rxtemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val single = Single.just(1)
        val observable = Observable.just(1, 2, 3)
        val flowable = Flowable.just(1, 2, 3)

//        val dispos = single.subscribe({
//            Log.e(TAG, "new data $it" )
//        }, {
//
//        })

        val dispos1 = observable.subscribe {
            Log.e(TAG, "new data $it")
        }

        val dispos2 = flowable.subscribe {
            Log.e(TAG, "new data $it")
        }

        val disp = daSourceOne()

        val dispose = dataSource()
                // перевод на другой поток
            .subscribeOn(Schedulers.newThread())
                // смотрим результат в мэйн потоке
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                btn_test.text = "Next int $it"
                Log.e(TAG, "next int $it")
            },
                {

            }, {

            })

        btn_test.setOnClickListener {
            Log.e(TAG, "click click")
        }

    }

    fun dataSource(): Observable<Int> {
        return Observable.create { suscriber ->
            for (i in 0..100) {
                Thread.sleep(10000)
                suscriber.onNext(i)
            }
        }
    }

   fun daSourceOne(): Flowable<Int> {
       return Flowable.create({ subscrib ->
           for (i in 0..100) {
               //Thread.sleep(1000)
               subscrib.onNext(i)
           }
            subscrib.onComplete()
       }, BackpressureStrategy.DROP)
   }
}
