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
import androidx.test.espresso.matcher.ViewMatchers.withParent
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
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AddNewProfileTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun addNewProfileTest() {
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

        // Checks that the user is in InputFragmentLayout
        onView(
            allOf(
                withId(R.id.inputFragmentLayout),
                withParent(
                    allOf(
                        withId(R.id.input),
                        withParent(withId(android.R.id.content))
                    )
                ),
                isDisplayed()
            )
        ).check(matches(isDisplayed()))

        // User enter Viet Espresso as profile name
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
        ).perform(replaceText("Viet Espresso"), closeSoftKeyboard())

        // User enter 01/01/1001 as profile date of birth
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
        ).perform(replaceText("01/01/1001"), closeSoftKeyboard())

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

        // User enter 0123 456 789 as profile phone
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
        ).perform(replaceText("0123 456 789"), closeSoftKeyboard())

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

        // User presses Yes in the confirmation dialog
        onView(
            allOf(
                withId(android.R.id.button1), withText("Yes"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        ).perform(scrollTo(), click())

        // Checks that the profile category is Family
        onView(
            allOf(
                withId(R.id.cardLabel), withText("Family"), withContentDescription("Family"),
                withParent(
                    allOf(
                        withId(R.id.cardLabelLayout),
                        withParent(withId(R.id.cardContentLayout))
                    )
                ),
                isDisplayed()
            )
        ).check(matches(withText("Family")))

        // Checks that the profile name is Viet Espresso
        onView(
            allOf(
                withId(R.id.cardTitle), withText("Viet Espresso"),
                withParent(
                    allOf(
                        withId(R.id.cardContentLayout),
                        withParent(withId(R.id.cardLayout))
                    )
                ),
                isDisplayed()
            )
        ).check(matches(withText("Viet Espresso")))

        // Checks that the profile name is 01/01/1001
        onView(
            allOf(
                withId(R.id.cardDob), withText("01/01/1001"),
                withParent(
                    allOf(
                        withId(R.id.cardContentLayout),
                        withParent(withId(R.id.cardLayout))
                    )
                ),
                isDisplayed()
            )
        ).check(matches(withText("01/01/1001")))
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
