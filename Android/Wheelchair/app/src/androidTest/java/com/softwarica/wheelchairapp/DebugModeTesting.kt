package com.softwarica.wheelchairapp

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
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
class DebugModeTesting {

    @get:Rule
    val testRule2 = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun debugMode() {

        for(i in 1..10){
            onView(withId(R.id.btnDebug))
                .perform(ViewActions.click())
        }

        Thread.sleep(1000)

        onView(withId(R.id.username))
            .perform(ViewActions.typeText("42069"))

        closeSoftKeyboard()
        Thread.sleep(1000)

        onView(withId(android.R.id.button1)).perform(click());

        Thread.sleep(1000)

        onView(withId(R.id.lgnbtn))
            .check(doesNotExist())

        Thread.sleep(1000)
    }


}