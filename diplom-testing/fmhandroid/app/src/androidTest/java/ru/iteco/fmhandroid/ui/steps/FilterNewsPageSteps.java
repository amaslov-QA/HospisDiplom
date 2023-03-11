package ru.iteco.fmhandroid.ui.steps;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.PickerActions.setDate;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import android.view.View;

import androidx.test.espresso.ViewInteraction;

import org.hamcrest.Matcher;

import java.time.LocalDateTime;
import java.util.Calendar;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.TestUtils;

public class FilterNewsPageSteps {
    private static ControlPanelSteps controlPanelSteps = new ControlPanelSteps();
    Calendar date = Calendar.getInstance();

    public Matcher<View> filterNewsTitleTextView = withId(R.id.filter_news_title_text_view);
    public Matcher<View> newsItemPublishDateStartField = withId(R.id.news_item_publish_date_start_text_input_edit_text);
    public Matcher<View> newsItemPublishDateEndField = withId(R.id.news_item_publish_date_end_text_input_edit_text);
    public Matcher<View> filterBut = withId(R.id.filter_button);
    public Matcher<View> filterNewsActiveCheckBox = withId(R.id.filter_news_active_material_check_box);
    public Matcher<View> filterNewsInactiveCheckBox = withId(R.id.filter_news_inactive_material_check_box);

    public void isFilterNewsForm() {
        TestUtils.waitView(filterNewsTitleTextView).check(matches(isDisplayed()));
        TestUtils.waitView(controlPanelSteps.newsItemCategoryField).check(matches(isDisplayed()));
        TestUtils.waitView(newsItemPublishDateStartField).check(matches(isDisplayed()));
        TestUtils.waitView(newsItemPublishDateEndField).check(matches(isDisplayed()));
        TestUtils.waitView(filterBut).check(matches(isDisplayed()));
        TestUtils.waitView(controlPanelSteps.cancelBut).check(matches(isDisplayed()));
    }

    public void isFilterNewsFormControlPanel() {
        isFilterNewsForm();
        TestUtils.waitView(filterNewsActiveCheckBox).check(matches(isDisplayed()));
        TestUtils.waitView(filterNewsInactiveCheckBox).check(matches(isDisplayed()));
    }

    public void fillingOutTheFilterNewsForm(String nameCategory, LocalDateTime startDate, LocalDateTime endDate) {
        controlPanelSteps.selectANewsCategoryFromTheList(nameCategory);

        setDateToDatePicker(newsItemPublishDateStartField, startDate);
        TestUtils.waitView(controlPanelSteps.okBut).perform(click());

        setDateToDatePicker(newsItemPublishDateEndField, endDate);
        TestUtils.waitView(controlPanelSteps.okBut).perform(click());
    }

    public void setDateToDatePicker(Matcher<View> nameDatePicker, LocalDateTime date) {
        TestUtils.waitView(nameDatePicker).perform(click());
        TestUtils.waitView(controlPanelSteps.datePicker).check(matches(isDisplayed()));
        TestUtils.waitView(controlPanelSteps.datePicker).perform(setDate(date.getYear(), date.getMonthValue(), date.getDayOfMonth()));
    }

    public void isMessageWrongPeriod() {
        TestUtils.waitView(withText("Wrong period")).check(matches(isDisplayed()));
        TestUtils.waitView(controlPanelSteps.okBut).check(matches(isDisplayed()));
    }

    public void filterNewsButtonClick() {
        TestUtils.waitView(filterBut).perform(click());
    }

    public ViewInteraction getNewsFilterCategoryField() {
        return TestUtils.waitView(controlPanelSteps.newsItemCategoryField);
    }

    public ViewInteraction getNewsFilterPublishDateStartField() {
        return TestUtils.waitView(newsItemPublishDateStartField);
    }

    public ViewInteraction getNewsFilterPublishDateEndField() {
        return TestUtils.waitView(newsItemPublishDateEndField);
    }

    public void cancelFilterNewsButtonClick() {
        TestUtils.waitView(controlPanelSteps.cancelBut).perform(click());
    }
}
