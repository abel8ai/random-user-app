package com.zerox.randomuserapp.ui.view


import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.zerox.randomuserapp.R
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
internal class MainActivityTest{

    @get:Rule var activityScenarioRule = activityScenarioRule<MainActivity>()

    fun checkActivityVisibility(){
        onView(withId(R.id.layout_main_activity))
            .check(matches(isDisplayed()))
    }
    fun checkProgressBarVisibility(){
        onView(withId(R.id.pb_loading_user))
            .check(matches(isDisplayed()))
    }

    fun checkRecyclerViewVisibility(){
        onView(withId(R.id.rv_users))
            .check(matches(isDisplayed()))
    }

    fun navigateToUserDetailsActivity(){
        onView(withId(R.id.rv_users)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,click()))

        onView(withId(R.id.layout_user_details_activity))
            .check(matches(isDisplayed()))
    }
}