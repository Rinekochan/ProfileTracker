package com.sdmd.assignment3


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.sdmd.assignment3.ui.MainActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class CategoryNavigationTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun categoryNavigation() {
        // Check that there's card with the name of Viet Hoang Pham
        onView(
            allOf(
                withId(R.id.cardTitle), withText("Viet Hoang Pham"),
                withParent(
                    allOf(
                        withId(R.id.cardContentLayout),
                        withParent(withId(R.id.cardLayout))
                    )
                ),
                isDisplayed()
            )
        ).check(matches(withText("Viet Hoang Pham")))

        // Check that there's card with the name of Viet Demo
        onView(
            allOf(
                withId(R.id.cardTitle), withText("Viet Demo"),
                withParent(
                    allOf(
                        withId(R.id.cardContentLayout),
                        withParent(withId(R.id.cardLayout))
                    )
                ),
                isDisplayed()
            )
        ).check(matches(withText("Viet Demo")))

        // User presses on Family category chip
        onView(
            allOf(
                withId(R.id.familyFilterButton),
                childAtPosition(
                    allOf(
                        withId(R.id.filterGroup),
                        childAtPosition(
                            withId(R.id.filterGroupLayout),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        ).perform(click())

        // Check that there's card with the name of Viet Hoang Pham
        onView(
            allOf(
                withId(R.id.cardTitle), withText("Viet Hoang Pham"),
                withParent(
                    allOf(
                        withId(R.id.cardContentLayout),
                        withParent(withId(R.id.cardLayout))
                    )
                ),
                isDisplayed()
            )
        ).check(matches(withText("Viet Hoang Pham")))

        // User presses on Friend category chip
        onView(
            allOf(
                withId(R.id.friendFilterButton),
                childAtPosition(
                    allOf(
                        withId(R.id.filterGroup),
                        childAtPosition(
                            withId(R.id.filterGroupLayout),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        ).perform(click())

        // Check that there's card with the name of Viet Demo
        onView(
            allOf(
                withId(R.id.cardTitle), withText("Viet Demo"),
                withParent(
                    allOf(
                        withId(R.id.cardContentLayout),
                        withParent(withId(R.id.cardLayout))
                    )
                ),
                isDisplayed()
            )
        ).check(matches(withText("Viet Demo")))
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
