package com.wordle.dingdong

import android.app.Application
import com.wordle.dingdong.data.TaskDatabase

class BaseApplication : Application() {
    val database: TaskDatabase by lazy { TaskDatabase.getDatabase(this) }
}
