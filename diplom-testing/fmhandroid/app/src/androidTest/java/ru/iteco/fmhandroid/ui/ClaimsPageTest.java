package ru.iteco.fmhandroid.ui;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static ru.iteco.fmhandroid.ui.data.DataHelper.authInfo;

import android.os.RemoteException;

import androidx.test.espresso.PerformException;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.junit4.DisplayName;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.steps.AuthSteps;
import ru.iteco.fmhandroid.ui.steps.ClaimsPageSteps;
import ru.iteco.fmhandroid.ui.steps.ControlPanelSteps;
import ru.iteco.fmhandroid.ui.steps.CreatingClaimsSteps;
import ru.iteco.fmhandroid.ui.steps.MainPageSteps;

@RunWith(AllureAndroidJUnit4.class)

public class ClaimsPageTest extends BaseTest {
    private UiDevice device;
    private static AuthSteps authSteps = new AuthSteps();
    private static MainPageSteps mainPageSteps = new MainPageSteps();
    private static ClaimsPageSteps claimsPageSteps = new ClaimsPageSteps();
    private static CreatingClaimsSteps creatingClaimsSteps = new CreatingClaimsSteps();
    private static ControlPanelSteps controlPanelSteps = new ControlPanelSteps();


    LocalDateTime date = LocalDateTime.now();

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
    @DisplayName("Открытие фильтра заявок по кнопке Filter")
    public void shouldOpenTheClaimsFilterSettingsForm() {
        claimsPageSteps.openClaimsFilter();
        claimsPageSteps.isClaimsFilteringDialog();
    }

    @Test
    @DisplayName("Открытие формы Создания заявки в разделе Заявки")
    public void shouldOpenTheCreateClaimForm() {
        claimsPageSteps.openCreatingClaimsCard();
        claimsPageSteps.isClaimsForm();
    }

    @Test
    @DisplayName("Окрытие Заявки с помощью кнопки со стрелкой")
    public void shouldOpenTheClaimCard() {
        DataHelper.CreateClaim inProgressClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.INPROGRESS).withDueDate(date).withExecutor(DataHelper.getExecutorIvanov()).build();

        //Создать заявку
        creatingClaimsSteps.createClaim(inProgressClaim);

        //Открыть карточку заявки
        claimsPageSteps.openClaimCard(inProgressClaim);
        //Проверить, что отображается карточка созданной заявки
        claimsPageSteps.isClaimCard(inProgressClaim);
    }

    @Test
    @DisplayName("Добавление комментария к заявке")
    public void shouldAddACommentToTheClaim() {
        DataHelper.CreateClaim inProgressClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.INPROGRESS).withDueDate(date).withExecutor(DataHelper.getExecutorIvanov()).build();
        String comment = DataHelper.getComment();
        //Создать заявку
        creatingClaimsSteps.createClaim(inProgressClaim);
        //Открываем заявку для редактирования
        claimsPageSteps.openClaimCard(inProgressClaim);
        //Добавить комментарий
        claimsPageSteps.addComment(comment);
        controlPanelSteps.saveButtonClick();
        //Проверить что комментарий сохранился
        claimsPageSteps.checkCommentIsPresent(comment);
    }

    @Test
    @DisplayName("Добавление пустого комментария")
    public void shouldShowMessageFieldCannotBeEmpty() {
        DataHelper.CreateClaim inProgressClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.INPROGRESS).withDueDate(date).withExecutor(DataHelper.getExecutorIvanov()).build();
        //Создать заявку
        creatingClaimsSteps.createClaim(inProgressClaim);
        //Открываем заявку для редактирования
        claimsPageSteps.openClaimCard(inProgressClaim);
        //Добавить пустой комментарий
        claimsPageSteps.addComment("");
        controlPanelSteps.saveButtonClick();
        //Проверить что отображается сообщение
        controlPanelSteps.checkToast("The field cannot be empty.", true);
    }

    @Test
    @DisplayName("Отмена добавления комментария")
    public void shouldNotSaveCommentWhenCancelButtonIsClicked() {
        DataHelper.CreateClaim inProgressClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.INPROGRESS).withDueDate(date).withExecutor(DataHelper.getExecutorIvanov()).build();
        String comment = DataHelper.getComment();
        //Создать заявку
        creatingClaimsSteps.createClaim(inProgressClaim);
        //Открываем заявку для редактирования
        claimsPageSteps.openClaimCard(inProgressClaim);
        //Добавить комментарий
        claimsPageSteps.addComment(comment);
        controlPanelSteps.cancelButtonClick();
        //Проверить что комментарий не сохранился
        claimsPageSteps.checkCommentDoesNotPresent(comment);
    }

    @Test
    @DisplayName("Редактирование комментария")
    public void shouldEditTheComment() {
        DataHelper.CreateClaim inProgressClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.INPROGRESS).withDueDate(date).withExecutor(DataHelper.getExecutorIvanov()).build();
        String comment = DataHelper.getComment();
        String newComment = DataHelper.getComment();

        //Создать заявку
        creatingClaimsSteps.createClaim(inProgressClaim);
        //Открываем заявку для редактирования
        claimsPageSteps.openClaimCard(inProgressClaim);
        //Добавить комментарий
        claimsPageSteps.addComment(comment);
        controlPanelSteps.saveButtonClick();
        //Редактируем комменарий
        claimsPageSteps.editComment(newComment);
        //Проверяем,что сохранился новый комментарий
        claimsPageSteps.getCommentDescriptionText().check(matches(withText(newComment)));
    }

    @Test
    @DisplayName("Небуквенные и нецифровые знаки в поле Комментарий при редактировании заявки")
    public void shouldShowWarningMessageClaimCommentFieldIsIncorrect() {
        DataHelper.CreateClaim inProgressClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.INPROGRESS).withDueDate(date).withExecutor(DataHelper.getExecutorIvanov()).build();
        String nonLetterComment = ";&&";

        //Создать заявку
        creatingClaimsSteps.createClaim(inProgressClaim);
        //Открываем заявку для редактирования
        claimsPageSteps.openClaimCard(inProgressClaim);
        //Добавить комментарий
        claimsPageSteps.addComment(nonLetterComment);
        controlPanelSteps.saveButtonClick();
        controlPanelSteps.checkToast("The field must not contain \";&&\" characters.", true);
    }

    @Test
    @DisplayName("Отображение только что закрытой заявки на экране в списке заявок")
    public void shouldFindOnTheScreenJustClosedClaim() {
        DataHelper.CreateClaim inProgressClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.INPROGRESS).withDueDate(date).withExecutor(DataHelper.getExecutorIvanov()).build();
        //Создать заявку
        creatingClaimsSteps.createClaim(inProgressClaim);
        //Находим заявку в списке и отркываем ее
        claimsPageSteps.openClaimCard(inProgressClaim);
        //Закрываем заявку
        claimsPageSteps.closeImButtonClick();
        //Проверяем, что только-что закрытая заявка видна на экране
        claimsPageSteps.checkClaimIsPresentOnScreen(inProgressClaim);
    }

}
