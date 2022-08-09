package com.wordle.dingdong

import android.app.Application
import com.wordle.dingdong.data.TaskDatabase

class BaseApplication : Application() {
    // TODO: provide a ForageDatabase value by lazy here
    val database: TaskDatabase by lazy { TaskDatabase.getDatabase(this) }
}