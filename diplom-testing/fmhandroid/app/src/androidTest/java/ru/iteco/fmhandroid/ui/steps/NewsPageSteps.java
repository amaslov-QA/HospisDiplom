package ru.iteco.fmhandroid.ui.steps;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.TestUtils;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.core.AllOf.allOf;

import android.view.View;

import androidx.test.espresso.ViewInteraction;

import org.hamcrest.Matcher;

public class NewsPageSteps {
    private static MainPageSteps mainPageSteps = new MainPageSteps();
    public Matcher<View> filterNewsMaterialBut = withId(R.id.filter_news_material_button);
    public Matcher<View> editNewsMaterialBut = withId(R.id.edit_news_material_button);

    public void isNewsPage() {
        TestUtils.waitView(withText("News")).check(matches(isDisplayed()));
        TestUtils.waitView(withId(R.id.news_list_swipe_refresh)).check(matches(isDisplayed()));
    }

    public void isEmptyNewsList() {
        TestUtils.waitView(withId(R.id.empty_news_list_image_view)).check(matches(isDisplayed()));
        TestUtils.waitView(withText("There is nothing here yet…")).check(matches(isDisplayed()));
        TestUtils.waitView(allOf(withId(R.id.news_retry_material_button), withText("REFRESH"))).check(matches(isDisplayed()));
    }

    public void openMainPage() {
        TestUtils.waitView(mainPageSteps.mainMenuImBut).perform(click());
        TestUtils.waitView(mainPageSteps.mainInTheMainMenu).perform(click());
    }

    public void openFilterNews() {
        TestUtils.waitView(filterNewsMaterialBut).perform(click());
    }

    public void openNewsOnNewsPage(int position) {
        TestUtils.waitView(TestUtils.withRecyclerView(R.id.news_list_recycler_view)
                .atPositionOnView(position, R.id.news_item_material_card_view)).perform(click());
    }

    public ViewInteraction getNewsItemDescription(int position) {
        return TestUtils.waitView(TestUtils.withRecyclerView(R.id.news_list_recycler_view).atPositionOnView(position, R.id.news_item_description_text_view));
    }

    public void openControlPanel() {
        TestUtils.waitView(editNewsMaterialBut).perform(click());
    }


}
