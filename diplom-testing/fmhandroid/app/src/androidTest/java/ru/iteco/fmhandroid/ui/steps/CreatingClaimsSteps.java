package ru.iteco.fmhandroid.ui.steps;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;

import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.PickerActions.setDate;

import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import static org.hamcrest.core.AllOf.allOf;


import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.RootMatchers;


import org.hamcrest.Matchers;

import java.time.LocalDateTime;


import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.data.TestUtils;

public class CreatingClaimsSteps {
    private static ClaimsPageSteps claimsPageSteps = new ClaimsPageSteps();
    private static ControlPanelSteps controlPanelSteps = new ControlPanelSteps();

    public void replaceTextClaimExecutor(String nameExecutor) {
        TestUtils.waitView(claimsPageSteps.executorClaimField).check(matches(isDisplayed())).perform(click(), replaceText(nameExecutor));
        Espresso.closeSoftKeyboard();
    }

    public void selectAClaimExecutorFromTheList(ViewInteraction nameExecutor) {
        TestUtils.waitView(claimsPageSteps.executorClaimField).perform(click());
        Espresso.closeSoftKeyboard();
        nameExecutor.perform(click());
    }


    public void setDateToDatePicker(LocalDateTime dueDate) {
        TestUtils.waitView(claimsPageSteps.dateClaimField).perform(click());
        TestUtils.waitView(controlPanelSteps.datePicker).check(matches(isDisplayed()));
        TestUtils.waitView(controlPanelSteps.datePicker).perform(setDate(dueDate.getYear(), dueDate.getMonthValue(), dueDate.getDayOfMonth()));
    }


    public void setTimeToTimeField(LocalDateTime dueDate) {
        TestUtils.waitView(claimsPageSteps.timeClaimField).perform(click());
        controlPanelSteps.setTimeToTimePicker(dueDate.getHour(), dueDate.getMinute());
        controlPanelSteps.okButtonClick();
    }

    public void openClaimTimePicker() {
        TestUtils.waitView(claimsPageSteps.timeClaimField).perform(click());
    }


    public void fillingOutTheFormCreatingClaim(DataHelper.CreateClaim createClaim) {
        replaceTitleClaimText(createClaim.getClaimName());
        setDateToDatePicker(createClaim.getDueDate());
        controlPanelSteps.okButtonClick();
        setTimeToTimeField(createClaim.getDueDate());
        TestUtils.waitView(claimsPageSteps.descriptionClaimField).perform(replaceText(createClaim.getClaimDescription()));
    }

    public void isFillEmptyFieldsMessage() {
        TestUtils.waitView(claimsPageSteps.messageFillEmptyFields).check(matches(isDisplayed()));
        TestUtils.waitView(controlPanelSteps.okBut).check(matches(isDisplayed()));
    }

    public void isWrongEmptyFormClaim() {
        TestUtils.waitView(allOf(withId(R.id.text_input_end_icon), withParent(withParent(withParent(withParent(withId(R.id.title_text_input_layout))))))).check(matches(isDisplayed()));
        TestUtils.waitView(Matchers.allOf(withId(R.id.text_input_end_icon), withParent(withParent(withParent(withParent(withId(R.id.date_in_plan_text_input_layout))))))).check(matches(isDisplayed()));
        TestUtils.waitView(Matchers.allOf(withId(R.id.text_input_end_icon), withParent(withParent(withParent(withParent(withId(R.id.time_in_plan_text_input_layout))))))).check(matches(isDisplayed()));
        TestUtils.waitView(Matchers.allOf(withId(R.id.text_input_end_icon), withParent(withParent(withParent(withParent(withId(R.id.description_text_input_layout))))))).check(matches(isDisplayed()));
    }

    public ViewInteraction getClaimTime() {
        return TestUtils.waitView(claimsPageSteps.timeClaimField);
    }

    public ViewInteraction getClaimDateInPlane() {
        return TestUtils.waitView(claimsPageSteps.dateClaimField);
    }

    public void replaceTitleClaimText(String title) {
        TestUtils.waitView(claimsPageSteps.titleClaimField).perform(replaceText(title));
    }

    public void createClaim(DataHelper.CreateClaim claim) {

        TestUtils.waitView(claimsPageSteps.addNewClaimBut).perform(click());
        if (claim.getClaimStatus() != "Open") {
            selectAClaimExecutorFromTheList(onView(withText(claim.getExecutorName())).inRoot((RootMatchers.isPlatformPopup())));
        }
        fillingOutTheFormCreatingClaim(claim);
        TestUtils.waitView(controlPanelSteps.saveBut).perform(click());
        if (claim.getClaimStatus() == "Executed") {
            //Открыть карточку заявки со статусом
            claimsPageSteps.openClaimCard(claim);
            //Изменить статус заявки
            claimsPageSteps.setStatusExecute();
            claimsPageSteps.isStatusCommentDialog();
            claimsPageSteps.replaceClaimStatusCommentText(DataHelper.getComment());
            controlPanelSteps.okButtonClick();
            claimsPageSteps.closeImButtonClick();
        }
        if (claim.getClaimStatus() == "Canceled") {
            claimsPageSteps.openClaimCard(claim);
            //Изменить статус заявки
            claimsPageSteps.setStatusOpen();
            claimsPageSteps.isStatusCommentDialog();
            claimsPageSteps.replaceClaimStatusCommentText(DataHelper.getComment());
            controlPanelSteps.okButtonClick();
            claimsPageSteps.setStatusCanceled();
            claimsPageSteps.closeImButtonClick();
        }
    }

    public void createClaims(DataHelper.CreateClaim... array) {
        for (DataHelper.CreateClaim claim : array) {
            createClaim(claim);
        }
    }

    public void isDialogWindowMessageTryAgainLatter() {
        TestUtils.waitView(withText("Something went wrong. Try again later.")).check(matches(isDisplayed()));
        TestUtils.waitView(controlPanelSteps.okBut).check(matches(isDisplayed()));
    }

}
