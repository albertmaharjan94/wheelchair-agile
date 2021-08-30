package com.softwarica.wheelchairapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test

class StartTesting {
    @get:Rule
    val testRule3 = ActivityScenarioRule(TabActivity::class.java)

        @Test
    fun OnCheck() {

        onView(withId(R.id.startbtn)).perform(click());

        Thread.sleep(1000)

        val imageButton = onView(
            Matchers.allOf(
                withId(R.id.reverse),
                ViewMatchers.withParent(
                    Matchers.allOf(
                        withId(R.id.utilityLay),
                        ViewMatchers.withParent(withId(R.id.relativeLayout))
                    )
                ),
                ViewMatchers.isDisplayed()
            )
        )
        imageButton.check(matches(ViewMatchers.isDisplayed()))

    }

}