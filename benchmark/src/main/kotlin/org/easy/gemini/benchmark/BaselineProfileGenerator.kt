package org.easy.gemini.benchmark

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.uiautomator.By
import org.junit.Rule
import org.junit.Test


class BaselineProfileGenerator {
    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generateBaselineProfile() = baselineProfileRule.collect(
        packageName = InfoConstant.PACKAGE_NAME,
        profileBlock = {
            pressHome()
            startActivityAndWait()
            device.findObject(By.res("chat_menu")).click()
        }
    )
}