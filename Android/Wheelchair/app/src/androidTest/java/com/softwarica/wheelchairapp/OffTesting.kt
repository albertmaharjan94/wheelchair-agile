package com.softwarica.wheelchairapp

import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import org.hamcrest.Matchers
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)
class OffTesting {

    @get:Rule
    val testRule3 = ActivityScenarioRule(TabActivity::class.java)

    @Test
    fun OffCheck() {

        onView(withId(R.id.startbtn)).perform(click());

        Thread.sleep(1000)

        onView(withId(R.id.reverse)).perform(click());

        Thread.sleep(1000)

        onView(withId(R.id.startbtn)).perform(click());
        Thread.sleep(1000)

        val button = onView(
            Matchers.allOf(
                withId(R.id.btnChangeMode), withText("SWITCH MODE"),
                ViewMatchers.withParent(
                    Matchers.allOf(
                        withId(R.id.relativeLayout),
                        ViewMatchers.withParent(IsInstanceOf.instanceOf(ViewGroup::class.java))
                    )
                ),
                ViewMatchers.isDisplayed()
            )
        )
        button.check(matches(ViewMatchers.isDisplayed()))
    }
}