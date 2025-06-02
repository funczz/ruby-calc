package com.github.funczz.ruby_calc.android

import android.app.Application
import android.util.Log
import com.github.funczz.ruby_calc.core.instance.InstanceHolder
import com.github.funczz.ruby_calc.instance.BackupServiceProvider
import com.github.funczz.ruby_calc.instance.ExecutorServiceProvider
import com.github.funczz.ruby_calc.instance.NotifierProvider
import com.github.funczz.ruby_calc.instance.RubyCalcDBResourcesProvider
import com.github.funczz.ruby_calc.instance.RubyCalcStateModelProvider
import com.github.funczz.ruby_calc.instance.RubyServiceProvider
import com.github.funczz.ruby_calc.instance.UiPresenterProvider

class MainApplication : Application(), InstanceHolder {

    override val instances = mutableMapOf<String, Any>()

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate is called.")
        _instance = this

        ExecutorServiceProvider.onInit {
            ExecutorServiceProvider.new()
        }

        NotifierProvider.onInit {
            NotifierProvider.new()
        }

        RubyServiceProvider.onInit {
            RubyServiceProvider.new()
        }

        RubyCalcDBResourcesProvider.onInit {

            //if (BuildConfig.DEBUG) RubyCalcDBResourcesProvider.clear(filesDirectory = filesDir.canonicalFile)

            RubyCalcDBResourcesProvider.new(
                filesDirectory = filesDir.canonicalFile,
                executor = ExecutorServiceProvider.getInstance(),
            )
        }

        RubyCalcStateModelProvider.onInit {
            RubyCalcStateModelProvider.new(
                dbResources = RubyCalcDBResourcesProvider.getInstance(),
                rubyService = RubyServiceProvider.getInstance(),
                notifier = NotifierProvider.getInstance(),
                executor = null,
            )
        }

        BackupServiceProvider.onInit {
            BackupServiceProvider.new(
                dbResources = RubyCalcDBResourcesProvider.getInstance(),
                filesDirectory = filesDir.canonicalFile,
            )
        }

        UiPresenterProvider.onInit {
            UiPresenterProvider.new(
                notifier = NotifierProvider.getInstance(),
            )
        }

        //if (BuildConfig.DEBUG) {
        //    repeat(50) {
        //        val inputData = ErrorStateData.FunctionData {
        //            throw Exception("error message $it", Throwable("cause message"))
        //        }
        //        NotifierProvider.getInstance().accept(input = inputData)
        //    }
        //}

    }

    companion object {

        private val TAG = MainApplication::class.simpleName

        private lateinit var _instance: MainApplication

        fun getInstance(): MainApplication = _instance

    }

}
