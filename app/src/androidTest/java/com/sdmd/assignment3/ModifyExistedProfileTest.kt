package com.sdmd.assignment3


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
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
class ModifyExistedProfileTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun modifyExistedProfileTest() {
        // Checks that the profile category is Family before modifying
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

        // Checks that the profile name is Viet Espresso before modifying
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

        // Checks that the profile date of birth is 01/01/1001 before modifying
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

        // User presses on Detail Button
        onView(
            allOf(
                withId(R.id.detailButton), withContentDescription("Detail"),
                childAtPosition(
                    allOf(
                        withId(R.id.cardButtonLayout),
                        childAtPosition(
                            withId(R.id.cardLayout),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        ).perform(click())

        // User presses on Modify Button
        onView(
            allOf(
                withId(R.id.detailModifyButton),
                withText("Modify"),
                withContentDescription("Modify"),
                childAtPosition(
                    allOf(
                        withId(R.id.detail),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    7
                ),
                isDisplayed()
            )
        ).perform(click())

        // User presses on Friend as profile category
        onView(
            allOf(
                withId(R.id.friendInputChip), withText("Friend"),
                childAtPosition(
                    allOf(
                        withId(R.id.filterInputLabelGroup),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        ).perform(click())

        // User presses on Next Button
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

        // User presses on Save button
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

        // User presses on Yes in the confirmation dialog
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

        // Checks that the category is now Friend after modifying
        onView(
            allOf(
                withId(R.id.cardLabel), withText("Friend"), withContentDescription("Family"),
                withParent(
                    allOf(
                        withId(R.id.cardLabelLayout),
                        withParent(withId(R.id.cardContentLayout))
                    )
                ),
                isDisplayed()
            )
        ).check(matches(withText("Friend")))
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
