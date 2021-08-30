package com.softwarica.wheelchairapp

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.softwarica.wheelchairapp.ui.main.Auth.LoginActivity
import kotlinx.coroutines.withContext
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)
class ChangeModeTesting {

    @get:Rule
    val testRule3 = ActivityScenarioRule(OptionScreenActivity::class.java)

    @Test
    fun switchMode() {

        for(i in 1..10){
            onView(withId(R.id.btnDebug))
                .perform(ViewActions.click())
        }

        Thread.sleep(1000)

        onView(withId(R.id.username))
            .perform(ViewActions.typeText("42069"))


        Thread.sleep(1000)

        onView(withId(android.R.id.button1)).perform(click());

        Thread.sleep(1000)

        onView(withId(R.id.dockmode)).perform(click());
        Thread.sleep(1000)

        onView(withId(R.id.btnChangeMode)).perform(click());

        Thread.sleep(1000)

        onView(withId(android.R.id.button1)).perform(click());
        Thread.sleep(1000)

        val button = onView(
            Matchers.allOf(
                withId(R.id.dockmode),
                ViewMatchers.isDisplayed()
            )
        )
        button.check(matches(ViewMatchers.isDisplayed()))
    }



}