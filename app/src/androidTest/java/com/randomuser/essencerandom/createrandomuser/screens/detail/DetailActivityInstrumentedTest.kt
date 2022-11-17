package com.randomuser.essencerandom.createrandomuser.screens.detail

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.randomuser.essencerandom.createrandomuser.R
import com.randomuser.essencerandom.createrandomuser.screens.home.HomeActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DetailActivityInstrumentedTest {
    @Rule
    @JvmField
    var homeActivityTestRule = ActivityTestRule(HomeActivity::class.java)

    @Test
    fun testDetailActivityLocationClick() {
        Intents.init()

        // We wait a timeout of 1 second for the request to be made
        Thread.sleep(1_000)

        val recyclerView: ViewInteraction = Espresso.onView(
            Matchers.allOf(
                withId(R.id.recycler_view_users),
                childAtPosition(
                    ViewMatchers.withId(android.R.id.content),
                    0
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, ViewActions.click()))

        // On click, we should be in DetailActivity
        intended(hasComponent(DetailActivity::class.java.name))

        Espresso.onView(withId(R.id.text_view_user_location)).perform(ViewActions.click())
        intended(hasAction(Intent.ACTION_VIEW))

        Intents.release()
    }


    @Test
    fun testDetailActivityOnCellClick() {
        Intents.init()

        // We wait a timeout of 1 second for the request to be made
        Thread.sleep(1_000)

        val recyclerView: ViewInteraction = Espresso.onView(
            Matchers.allOf(
                withId(R.id.recycler_view_users),
                childAtPosition(
                    ViewMatchers.withId(android.R.id.content),
                    0
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, ViewActions.click()))

        // On click, we should be in DetailActivity
        intended(hasComponent(DetailActivity::class.java.name))

        Espresso.onView(withId(R.id.text_view_user_cell)).perform(ViewActions.click())
        intended(hasAction(Intent.ACTION_DIAL))

        Intents.release()
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}