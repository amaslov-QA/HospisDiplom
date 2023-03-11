package ru.iteco.fmhandroid.ui;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static ru.iteco.fmhandroid.ui.data.DataHelper.authInfo;

import android.os.RemoteException;

import androidx.test.espresso.PerformException;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.junit4.DisplayName;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.data.TestUtils;
import ru.iteco.fmhandroid.ui.steps.AuthSteps;
import ru.iteco.fmhandroid.ui.steps.ClaimsPageSteps;
import ru.iteco.fmhandroid.ui.steps.ControlPanelSteps;
import ru.iteco.fmhandroid.ui.steps.CreatingClaimsSteps;
import ru.iteco.fmhandroid.ui.steps.MainPageSteps;

@RunWith(AllureAndroidJUnit4.class)

public class ClaimEditTest extends BaseTest {
    private UiDevice device;
    private static AuthSteps authSteps = new AuthSteps();
    private static MainPageSteps mainPageSteps = new MainPageSteps();
    private static ClaimsPageSteps claimsPageSteps = new ClaimsPageSteps();
    private static CreatingClaimsSteps creatingClaimsSteps = new CreatingClaimsSteps();
    private static ControlPanelSteps controlPanelSteps = new ControlPanelSteps();


    LocalDateTime today = LocalDateTime.now();

    @Before
    public void logoutCheck() throws RemoteException {
        device =
                UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.setOrientationNatural();
        try {
            authSteps.isAuthScreen();
        } catch (PerformException e) {
            mainPageSteps.clickLogOutBut();
        }
        authSteps.authWithValidData(authInfo());
        mainPageSteps.isMainPage();
        mainPageSteps.openClaimsPageThroughTheMainMenu();
    }

    @Test
    @DisplayName("Редактирование заявки")
    public void shouldSaveClaimChanges() {
        DataHelper.CreateClaim openClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.OPEN).withDueDate(today).build();
        DataHelper.CreateClaim newOpenClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.OPEN).withDueDate(today.plus(1, ChronoUnit.HOURS)).build();
        String planeDate = TestUtils.getDateToString(newOpenClaim.dueDate);
        String planeTime = TestUtils.getTimeToString(newOpenClaim.dueDate);

        //Создать заявку
        creatingClaimsSteps.createClaim(openClaim);
        //Открыть карточку заявки
        claimsPageSteps.openClaimCard(openClaim);
        //Отредактировать заявку
        claimsPageSteps.editClaim(newOpenClaim);
        //Проверить что внесенные изменения сохранились
        claimsPageSteps.isClaimCard(newOpenClaim);
    }

    @Test
    @DisplayName("Смена статуса заявки на In progress")
    public void shouldChangeTheStatusOfTheClaimToInProgress() {
        DataHelper.CreateClaim openClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.OPEN).withDueDate(today).build();

        //Создать заявку
        creatingClaimsSteps.createClaim(openClaim);
        //Открыть карточку заявки
        claimsPageSteps.openClaimCard(openClaim);
        //Изменить статус заявки
        claimsPageSteps.setStatusInProcess();
        //Проверить что внесенные изменения сохранились
        claimsPageSteps.getStatusLabel().check(matches(withText("In progress")));
    }

    @Test
    @DisplayName("Смена статуса заявки с In progress на To execute")
    public void shouldChangeTheStatusOfTheClaimToInToExecute() {
        DataHelper.CreateClaim inProgressClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.INPROGRESS).withDueDate(today).withExecutor(DataHelper.getExecutorIvanov()).build();
        String comment = DataHelper.getComment();

        //Создать заявку
        creatingClaimsSteps.createClaim(inProgressClaim);
        //Открыть карточку заявки
        claimsPageSteps.openClaimCard(inProgressClaim);
        //Изменить статус заявки
        claimsPageSteps.setStatusExecute();
        claimsPageSteps.isStatusCommentDialog();
        claimsPageSteps.replaceClaimStatusCommentText(comment);
        controlPanelSteps.okButtonClick();
        //Проверить что статус изменился
        claimsPageSteps.getStatusLabel().check(matches(withText("Executed")));
        //Проверяем что у заявки появился комментарий
        claimsPageSteps.checkCommentIsPresent(comment);
    }

    @Test
    @DisplayName("Сброс статуса заявки В работе ")
    public void shouldChangeTheStatusOfTheClaimToOpen() {
        DataHelper.CreateClaim inProgressClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.INPROGRESS).withDueDate(today).withExecutor(DataHelper.getExecutorIvanov()).build();
        String comment = DataHelper.getComment();

        //Создать заявку
        creatingClaimsSteps.createClaim(inProgressClaim);

        //Открыть карточку заявки
        claimsPageSteps.openClaimCard(inProgressClaim);
        //Изменить статус заявки
        claimsPageSteps.setStatusOpen();
        claimsPageSteps.isStatusCommentDialog();
        claimsPageSteps.replaceClaimStatusCommentText(comment);
        controlPanelSteps.okButtonClick();
        //Проверить что статус изменился
        claimsPageSteps.getStatusLabel().check(matches(withText("Open")));
        //Проверяем что у заявки появился комментарий
        claimsPageSteps.checkCommentIsPresent(comment);
    }

    @Test
    @DisplayName("Смена статуса заявки с Open на Canceled")
    public void shouldChangeTheStatusOfTheClaimToCanceled() {
        DataHelper.CreateClaim openClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.OPEN).withDueDate(today).build();

        //Создать заявку
        creatingClaimsSteps.createClaim(openClaim);
        //Открыть карточку заявки
        claimsPageSteps.openClaimCard(openClaim);
        //Изменить статус заявки
        claimsPageSteps.setStatusCanceled();
        //Проверить что внесенные изменения сохранились
        claimsPageSteps.getStatusLabel().check(matches(withText("Canceled")));
    }

    @Test
    @DisplayName("Работа кнопки назад в карточке заявки")
    public void shouldExitTheClaimCardByClickingTheButtonClose() {
        DataHelper.CreateClaim inProgressClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.INPROGRESS).withDueDate(today).withExecutor(DataHelper.getExecutorIvanov()).build();

        //Создать заявку
        creatingClaimsSteps.createClaim(inProgressClaim);
        //Открыть карточку заявки
        claimsPageSteps.openClaimCard(inProgressClaim);
        //Выйти из карточки заявки, кликнув кнопку Close
        claimsPageSteps.closeImButtonClick();
        //Проверить карточка заявки закрылась
        claimsPageSteps.isClaimsPage();
    }

    @Ignore //Не удается подобрать проверку
    @Test
    @DisplayName("Сброс статуса заявки В работе с другим исполнителем")
    public void shouldNotChangeTheStatusOfTheClaimIfExecutorSmirnov() {
        DataHelper.CreateClaim inProgressClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.INPROGRESS).withDueDate(today).withExecutor(DataHelper.getExecutorSmirnov()).build();
        String title = DataHelper.getTitle();
        //Создать заявку
        creatingClaimsSteps.createClaim(inProgressClaim);
        //Открыть карточку заявки
        claimsPageSteps.openClaimCard(inProgressClaim);
        //Проверить, что невозможно изменить статус заявки
        TestUtils.waitView(claimsPageSteps.statusProcessingImBut).perform(click());
        TestUtils.waitView(claimsPageSteps.throwOffMenuItem).check(doesNotExist());
    }

    @Test
    @DisplayName("Редактирование заявки со статусом In progress")
    public void shouldShowMessageTheClaimCanBeEditedOnlyInTheOpenStatus() {
        DataHelper.CreateClaim inProgressClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.INPROGRESS).withDueDate(today).withExecutor(DataHelper.getExecutorIvanov()).build();

        //Создать заявку
        creatingClaimsSteps.createClaim(inProgressClaim);
        //Открыть карточку заявки
        claimsPageSteps.openClaimCard(inProgressClaim);
        //Тапнуть кнопку редактировать
        claimsPageSteps.editClaimButClick();
        //Проверить появление сообщения "The Claim can be edited only in the Open status."
        controlPanelSteps.checkToast("The Claim can be edited only in the Open status.", true);
    }
}
