package com.dingdong.dingdong

import android.app.Application
import com.dingdong.dingdong.data.TaskDatabase

class BaseApplication : Application() {
    val database: TaskDatabase by lazy { TaskDatabase.getDatabase(this) }
}
