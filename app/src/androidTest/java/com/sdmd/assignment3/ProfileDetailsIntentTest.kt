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
class ProfileDetailsIntentTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun profileDetailsIntentTest() {
        // Check if the profile category is Family in MainActivity
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

        // Check if the profile name is Viet Hoang Pham in MainActivity
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

        // Check if the profile date of birth is 10/10/2005 in MainActivity
        onView(
            allOf(
                withId(R.id.cardDob), withText("10/10/2005"),
                withParent(
                    allOf(
                        withId(R.id.cardContentLayout),
                        withParent(withId(R.id.cardLayout))
                    )
                ),
                isDisplayed()
            )
        ).check(matches(withText("10/10/2005")))

        // User presses Detail button of that card
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

        // Check if the profile category is Family in DetailActivity
        onView(
            allOf(
                withId(R.id.profileLabel), withText("Family"), withContentDescription("Family"),
                withParent(
                    allOf(
                        withId(R.id.profileLabelLayout),
                        withParent(withId(R.id.titleSection))
                    )
                ),
                isDisplayed()
            )
        ).check(matches(withText("Family")))

        // Check if the profile name is Viet Hoang Pham in DetailActivity
        onView(
            allOf(
                withId(R.id.profileName), withText("Viet Hoang Pham"),
                withParent(
                    allOf(
                        withId(R.id.detail),
                        withParent(withId(android.R.id.content))
                    )
                ),
                isDisplayed()
            )
        ).check(matches(withText("Viet Hoang Pham")))

        // Check if the profile gender is Male in DetailActivity
        onView(
            allOf(
                withId(R.id.genderFieldContent), withText("Male"), withContentDescription("Male"),
                withParent(
                    allOf(
                        withId(R.id.genderFieldLayout),
                        withParent(withId(R.id.genderFieldCard))
                    )
                ),
                isDisplayed()
            )
        ).check(matches(withText("Male")))

        // User presses Modify button
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

        // Check if the profile name is Viet Hoang Pham in InputActivity
        onView(
            allOf(
                withId(R.id.fullNameEditText), withText("Viet Hoang Pham"),
                withParent(withParent(withId(R.id.fullNameInputField))),
                isDisplayed()
            )
        ).check(matches(withText("Viet Hoang Pham")))

        // Check if the profile gender is Male in InputActivity
        onView(
            allOf(
                withId(R.id.genderEditText), withText("Male"),
                withParent(withParent(withId(R.id.genderInputField))),
                isDisplayed()
            )
        ).check(matches(withText("Male")))
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
