package com.sdmd.assignment3


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.sdmd.assignment3.ui.MainActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class DialogTriggerTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun dialogTriggerTest() {
        // User presses Delete button
        onView(
            allOf(
                withId(R.id.deleteButton), withContentDescription("Delete"),
                childAtPosition(
                    allOf(
                        withId(R.id.cardButtonLayout),
                        childAtPosition(
                            withId(R.id.cardLayout),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        ).perform(click())

        // Check that a dialog appears
        onView(
            allOf(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java), isDisplayed())
        ).check(matches(isDisplayed()))

        // User presses No in the dialog
        onView(
            allOf(
                withId(android.R.id.button2), withText("No"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    2
                )
            )
        ).perform(scrollTo(), click())

        // User presses Add button
        onView(
            allOf(
                withId(R.id.addFabButton), withContentDescription("Add"),
                childAtPosition(
                    allOf(
                        withId(R.id.main),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        ).perform(click())

        // User presses Cancel button
        onView(
            allOf(
                withId(R.id.inputCancelButton),
                withText("Cancel"),
                withContentDescription("Cancel"),
                childAtPosition(
                    allOf(
                        withId(R.id.personalInputLayout),
                        childAtPosition(
                            withId(R.id.inputFragmentLayout),
                            0
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        ).perform(click())

        // Checks that a dialog appears
        onView(
            allOf(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java), isDisplayed())
        ).check(matches(isDisplayed()))

        // User presses No in the dialog
        onView(
            allOf(
                withId(android.R.id.button2), withText("No"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    2
                )
            )
        ).perform(scrollTo(), click())

        // User enter Test as profile name
        onView(
            allOf(
                withId(R.id.fullNameEditText),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.fullNameInputField),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        ).perform(replaceText("Test"), closeSoftKeyboard())

        // User enters 10/10/1000 as profile birthday
        onView(
            allOf(
                withId(R.id.dobEditText),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.dobInputField),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        ).perform(replaceText("10/10/1000"), closeSoftKeyboard())

        // User presses Next button
        onView(
            allOf(
                withId(R.id.inputNextButton), withText("Next"), withContentDescription("Next"),
                childAtPosition(
                    allOf(
                        withId(R.id.personalInputLayout),
                        childAtPosition(
                            withId(R.id.inputFragmentLayout),
                            0
                        )
                    ),
                    5
                ),
                isDisplayed()
            )
        ).perform(click())

        // User enters 0444 444 444 as profile phone number
        onView(
            allOf(
                withId(R.id.phoneEditText),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.phoneInputField),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        ).perform(replaceText("0444 444 444"), closeSoftKeyboard())

        // User presses Save button
        onView(
            allOf(
                withId(R.id.inputSaveButton), withText("Save"), withContentDescription("Save"),
                childAtPosition(
                    allOf(
                        withId(R.id.contactInputLayout),
                        childAtPosition(
                            withId(R.id.inputFragmentLayout),
                            0
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        ).perform(click())

        // Check that a dialog appears
        onView(
            allOf(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java), isDisplayed())
        ).check(matches(isDisplayed()))
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
