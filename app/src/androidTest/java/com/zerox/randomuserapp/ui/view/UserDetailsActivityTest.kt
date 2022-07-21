package com.zerox.randomuserapp.ui.view

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.zerox.randomuserapp.R
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
internal class UserDetailsActivityTest{

    @get:Rule var activityScenarioRule = activityScenarioRule<UserDetailsActivity>()

    fun checkActivityVisibility(){
        onView(withId(R.id.layout_user_details_activity))
            .check(matches(isDisplayed()))
    }
    fun checkProgressBarVisibility(){
        onView(withId(R.id.pb_loading_user))
            .check(matches(isDisplayed()))
    }

    fun checkCardViewVisibility(){
        onView(withId(R.id.cv_user))
            .check(matches(isDisplayed()))
    }
    fun checkMapFragmentVisibility(){
        onView(withId(R.id.frag_map))
            .check(matches(isDisplayed()))
    }

    fun checkTextInCardView(){
        onView(withId(R.id.tv_user_name))
            .check(matches(withText("Miss. Laura Woods")))

        onView(withId(R.id.tv_user_email))
            .check(matches(withText("laura.woods@example.com")))
    }
    fun backPressToMainActivity(){
        onView(withId(android.R.id.home))
            .perform(click())

        onView(withId(R.id.layout_user_details_activity))
            .check(matches(isDisplayed()))

        pressBack()

        onView(withId(R.id.layout_main_activity))
            .check(matches(isDisplayed()))
    }
}