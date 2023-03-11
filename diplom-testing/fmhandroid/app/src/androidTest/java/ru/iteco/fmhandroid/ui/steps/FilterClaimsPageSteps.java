package ru.iteco.fmhandroid.ui.steps;

import static androidx.test.espresso.action.ViewActions.click;

import ru.iteco.fmhandroid.ui.data.TestUtils;
import ru.iteco.fmhandroid.ui.data.ViewActions;

public class FilterClaimsPageSteps {
    private static ClaimsPageSteps claimsPageSteps = new ClaimsPageSteps();

    public void filterClaims(boolean openStatus, boolean inProgressStatus, boolean executedStatus, boolean cancelledStatus) {
        claimsPageSteps.openClaimsFilter();
        TestUtils.waitView(claimsPageSteps.itemFilterOpen).perform(ViewActions.setChecked(openStatus));
        TestUtils.waitView(claimsPageSteps.itemFilterInProgress).perform(ViewActions.setChecked(inProgressStatus));
        TestUtils.waitView(claimsPageSteps.itemFilterExecuted).perform(ViewActions.setChecked(executedStatus));
        TestUtils.waitView(claimsPageSteps.itemFilterCancelled).perform(ViewActions.setChecked(cancelledStatus));
        TestUtils.waitView(claimsPageSteps.claimListFilterOkBut).perform(click());
    }

}
