package com.softwarica.wheelchairapp

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.softwarica.wheelchairapp.ui.main.Auth.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)
class LoginTesting {

    @get:Rule
    val testRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun checkLogin() {

        onView(withId(R.id.usernametxt))
            .perform(ViewActions.typeText("raj@gmail.com"))
        Thread.sleep(1000)

        onView(withId(R.id.passwordtxt))
            .perform(ViewActions.typeText("1234567890"))
        Thread.sleep(1000)

        closeSoftKeyboard()

        onView(withId(R.id.lgnbtn))
            .perform(ViewActions.click())

        onView(withId(R.id.lgnbtn))
            .check(doesNotExist())

    }
}