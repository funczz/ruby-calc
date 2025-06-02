package com.github.funczz.ruby_calc.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.github.funczz.kotlin.notifier.Notifier
import com.github.funczz.ruby_calc.android.destination.main.MainDestination
import com.github.funczz.ruby_calc.android.ui.theme.RubyCalcTheme
import com.github.funczz.ruby_calc.core.service.BackupService
import com.github.funczz.ruby_calc.instance.BackupServiceProvider
import com.github.funczz.ruby_calc.instance.ExecutorServiceProvider
import com.github.funczz.ruby_calc.instance.NotifierProvider
import com.github.funczz.ruby_calc.instance.UiPresenterProvider
import java.util.concurrent.Executor

class MainActivity(

    private val presenter: Presenter<UiState> = UiPresenterProvider.getInstance(),

    private val notifier: Notifier = NotifierProvider.getInstance(),

    private val backupService: BackupService = BackupServiceProvider.getInstance(),

    private val executor: Executor = ExecutorServiceProvider.getInstance(),

    ) : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RubyCalcTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainDestination(
                        presenter = presenter,
                        notifier = notifier,
                        backupService = backupService,
                        executor = executor,
                    )
                }
            }
        }
    }
}
