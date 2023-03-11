package ru.iteco.fmhandroid.ui.steps;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.TestUtils;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


import static org.hamcrest.core.AllOf.allOf;

import android.view.View;


import androidx.test.espresso.ViewInteraction;


public class OurMissionPageSteps {

    public org.hamcrest.Matcher<View> ourMissionPageTitle = withId(R.id.our_mission_title_text_view);
    public org.hamcrest.Matcher<View> ourMissionItemList = withId(R.id.our_mission_item_list_recycler_view);
    public org.hamcrest.Matcher<View> ourMissionItemListRecyclerView = withId(R.id.our_mission_item_list_recycler_view);

    public void isOurMissionPage() {
        TestUtils.waitView(ourMissionPageTitle).check(matches(isDisplayed()));
        TestUtils.waitView(ourMissionItemList).check(matches(isDisplayed()));
    }

    public ViewInteraction getOurMissionItemDescriptionTextView(int position) {
        return onView(allOf(withId(R.id.our_mission_item_description_text_view),
                TestUtils.childAtPosition(TestUtils.childAtPosition(allOf(withId(R.id.our_mission_item_material_card_view),
                        TestUtils.childAtPosition(withId(R.id.our_mission_item_list_recycler_view), position)), 0), 3)));

    }

    public void openCitation(int missionItemPosition) {
        TestUtils.waitView(ourMissionItemListRecyclerView).perform(actionOnItemAtPosition(missionItemPosition, click()));
    }

}
