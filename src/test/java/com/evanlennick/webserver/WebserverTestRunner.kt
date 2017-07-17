package com.evanlennick.webserver

import org.junit.runner.notification.RunNotifier
import org.junit.runners.BlockJUnit4ClassRunner
import org.junit.runners.model.InitializationError

class WebserverTestRunner @Throws(InitializationError::class)
constructor(clazz: Class<*>) : BlockJUnit4ClassRunner(clazz) {

    override fun run(notifier: RunNotifier) {
        Main.main(arrayOf("3353"))
        Main.isTestMode = true
        try {
            super.run(notifier)
        } finally {
            Main.stop()
        }
    }
}
