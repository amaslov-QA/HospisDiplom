package ru.iteco.fmhandroid.ui;

import static ru.iteco.fmhandroid.ui.data.DataHelper.authInfo;

import android.os.RemoteException;

import androidx.test.espresso.PerformException;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.junit4.DisplayName;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.steps.AuthSteps;
import ru.iteco.fmhandroid.ui.steps.ClaimsPageSteps;
import ru.iteco.fmhandroid.ui.steps.ControlPanelSteps;
import ru.iteco.fmhandroid.ui.steps.CreatingClaimsSteps;
import ru.iteco.fmhandroid.ui.steps.FilterClaimsPageSteps;
import ru.iteco.fmhandroid.ui.steps.MainPageSteps;


@RunWith(AllureAndroidJUnit4.class)
public class FilterClaimsPageTest extends BaseTest {
    private UiDevice device;
    private static AuthSteps authSteps = new AuthSteps();
    private static MainPageSteps mainPageSteps = new MainPageSteps();
    private static ClaimsPageSteps claimsPageSteps = new ClaimsPageSteps();
    private static CreatingClaimsSteps creatingClaimsSteps = new CreatingClaimsSteps();
    private static ControlPanelSteps controlPanelSteps = new ControlPanelSteps();
    private static FilterClaimsPageSteps filterClaimsPageSteps = new FilterClaimsPageSteps();


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
    @DisplayName("Фильтрация заявок со статусом Открыта")
    public void shouldFilterClaimsWithStatusOpen() {
        DataHelper.CreateClaim firstOpenClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.OPEN).withDueDate(DataHelper.getValidDate()).build();
        DataHelper.CreateClaim secondOpenClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.OPEN).withDueDate(DataHelper.getValidDate()).build();
        DataHelper.CreateClaim thirdOpenClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.OPEN).withDueDate(DataHelper.getValidDate()).build();
        DataHelper.CreateClaim forthInProgressClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.INPROGRESS).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();
        DataHelper.CreateClaim fifthInProgressClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.INPROGRESS).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();
        DataHelper.CreateClaim sixthInProgressClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.INPROGRESS).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();

        //Создать заявки для теста
        creatingClaimsSteps.createClaims(firstOpenClaim, secondOpenClaim, thirdOpenClaim, forthInProgressClaim, fifthInProgressClaim, sixthInProgressClaim);

        //Фильтруем заявки со статусом Open
        filterClaimsPageSteps.filterClaims(true, false, false, false);

        //Проверяем, что отфильтровались только заявки со статусом Open
        //Поиск элементов в RecyclerView работает нестабильно
        claimsPageSteps.checkClaimIsPresent(firstOpenClaim);
        claimsPageSteps.checkClaimIsPresent(secondOpenClaim);
        claimsPageSteps.checkClaimIsPresent(thirdOpenClaim);

        //Проверяем, что заявки со статусом In progress не отображаются
        claimsPageSteps.checkClaimDoesNotPresent(forthInProgressClaim);
        claimsPageSteps.checkClaimDoesNotPresent(fifthInProgressClaim);
        claimsPageSteps.checkClaimDoesNotPresent(sixthInProgressClaim);
    }

    @Test
    @DisplayName("Фильтрация заявок со статусом В работе")
    public void shouldFilterClaimsWithStatusInProgress() {
        DataHelper.CreateClaim firstOpenClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.OPEN).withDueDate(DataHelper.getValidDate()).build();
        DataHelper.CreateClaim secondOpenClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.OPEN).withDueDate(DataHelper.getValidDate()).build();
        DataHelper.CreateClaim thirdOpenClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.OPEN).withDueDate(DataHelper.getValidDate()).build();
        DataHelper.CreateClaim forthInProgressClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.INPROGRESS).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();
        DataHelper.CreateClaim fifthInProgressClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.INPROGRESS).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();
        DataHelper.CreateClaim sixthInProgressClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.INPROGRESS).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();
        //Создать заявки для теста
        creatingClaimsSteps.createClaims(firstOpenClaim, secondOpenClaim, thirdOpenClaim, forthInProgressClaim, fifthInProgressClaim, sixthInProgressClaim);

        //Фильтруем заявки со статусом In progress
        filterClaimsPageSteps.filterClaims(false, true, false, false);

        //Проверяем, что отфильтровались только заявки со статусом In progress
        //Поиск элементов в RecyclerView работает нестабильно
        claimsPageSteps.checkClaimIsPresent(forthInProgressClaim);
        claimsPageSteps.checkClaimIsPresent(fifthInProgressClaim);
        claimsPageSteps.checkClaimIsPresent(sixthInProgressClaim);

        //Проверяем, что заявки со статусом Open не отображаются
        claimsPageSteps.checkClaimDoesNotPresent(firstOpenClaim);
        claimsPageSteps.checkClaimDoesNotPresent(secondOpenClaim);
        claimsPageSteps.checkClaimDoesNotPresent(thirdOpenClaim);
    }

    @Test
    @DisplayName("Фильтрация заявок со статусом Выполнена")
    public void shouldFilterClaimsWithStatusExecuted() {
        DataHelper.CreateClaim firstOpenClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.OPEN).withDueDate(DataHelper.getValidDate()).build();
        DataHelper.CreateClaim secondOpenClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.OPEN).withDueDate(DataHelper.getValidDate()).build();
        DataHelper.CreateClaim thirdOpenClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.OPEN).withDueDate(DataHelper.getValidDate()).build();
        DataHelper.CreateClaim forthExecutedClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.EXECUTED).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();
        DataHelper.CreateClaim fifthExecutedClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.EXECUTED).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();
        DataHelper.CreateClaim sixthExecutedClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.EXECUTED).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();

        //Создать заявки для теста
        creatingClaimsSteps.createClaims(firstOpenClaim, secondOpenClaim, thirdOpenClaim, forthExecutedClaim, fifthExecutedClaim, sixthExecutedClaim);

        //Фильтруем заявки со статусом Executed
        filterClaimsPageSteps.filterClaims(false, false, true, false);

        //Проверяем, что отфильтровались только заявки со статусом Executed
        //Поиск элементов в RecyclerView работает нестабильно
        claimsPageSteps.checkClaimIsPresent(forthExecutedClaim);
        claimsPageSteps.checkClaimIsPresent(fifthExecutedClaim);
        claimsPageSteps.checkClaimIsPresent(sixthExecutedClaim);

        //Проверяем, что заявки со статусом Open не отображаются
        claimsPageSteps.checkClaimDoesNotPresent(firstOpenClaim);
        claimsPageSteps.checkClaimDoesNotPresent(secondOpenClaim);
        claimsPageSteps.checkClaimDoesNotPresent(thirdOpenClaim);
    }

    @Test
    @DisplayName("Фильтрация заявок со статусом Отменена")
    public void shouldFilterClaimsWithStatusCanceled() {
        DataHelper.CreateClaim firstCanceledClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.CANCELED).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();
        DataHelper.CreateClaim secondCanceledClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.CANCELED).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();
        DataHelper.CreateClaim thirdCanceledClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.CANCELED).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();
        DataHelper.CreateClaim forthInProgressClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.INPROGRESS).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();
        DataHelper.CreateClaim fifthInProgressClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.INPROGRESS).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();
        DataHelper.CreateClaim sixthInProgressClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.INPROGRESS).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();

        //Создать заявки для теста
        creatingClaimsSteps.createClaims(firstCanceledClaim, secondCanceledClaim, thirdCanceledClaim, forthInProgressClaim, fifthInProgressClaim, sixthInProgressClaim);

        //Фильтруем заявки со статусом Canceled
        filterClaimsPageSteps.filterClaims(false, false, false, true);

        //Проверяем, что отфильтровались только заявки со статусом Canceled
        //Поиск элементов в RecyclerView работает нестабильно
        claimsPageSteps.checkClaimIsPresent(firstCanceledClaim);
        claimsPageSteps.checkClaimIsPresent(secondCanceledClaim);
        claimsPageSteps.checkClaimIsPresent(thirdCanceledClaim);
        //Проверяем, что заявки со статусом In progress не отображаются
        claimsPageSteps.checkClaimDoesNotPresent(forthInProgressClaim);
        claimsPageSteps.checkClaimDoesNotPresent(fifthInProgressClaim);
        claimsPageSteps.checkClaimDoesNotPresent(sixthInProgressClaim);
    }

    @Test
    @DisplayName("Фильтрация заявок со статусом Открыта и В работе")
    public void shouldFilterClaimsWithStatusOpenAndInProgress() {
        DataHelper.CreateClaim firstOpenClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.OPEN).withDueDate(DataHelper.getValidDate()).build();
        DataHelper.CreateClaim secondCanceledClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.CANCELED).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();
        DataHelper.CreateClaim thirdInProgressClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.INPROGRESS).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();
        //Создать заявки для теста
        creatingClaimsSteps.createClaims(firstOpenClaim, secondCanceledClaim, thirdInProgressClaim);

        //Фильтруем заявки со статусом Open и In progress
        filterClaimsPageSteps.filterClaims(true, true, false, false);

        //Проверяем, что отфильтровались только заявки со статусом Open и In progress
        //Поиск элементов в RecyclerView работает нестабильно
        claimsPageSteps.checkClaimIsPresent(firstOpenClaim);
        claimsPageSteps.checkClaimIsPresent(thirdInProgressClaim);

        //Проверяем, что заявки со статусом Canceled не отображаются
        claimsPageSteps.checkClaimDoesNotPresent(thirdInProgressClaim);
    }

    @Test
    @DisplayName("Фильтрация заявок со статусом Выполнена и Отменена")
    public void shouldFilterClaimsWithStatusExecutedAndCanceled() {
        DataHelper.CreateClaim firstOpenClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.OPEN).withDueDate(DataHelper.getValidDate()).build();
        DataHelper.CreateClaim secondExecutedClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.EXECUTED).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();
        DataHelper.CreateClaim thirdCanceledClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.CANCELED).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();

        //Создать заявки для теста
        creatingClaimsSteps.createClaims(firstOpenClaim, secondExecutedClaim, thirdCanceledClaim);

        //Фильтруем заявки со статусом Canceled и Executed
        filterClaimsPageSteps.filterClaims(false, false, true, true);

        //Проверяем, что отфильтровались только заявки со статусом Canceled и Executed
        //Поиск элементов в RecyclerView работает нестабильно
        claimsPageSteps.checkClaimIsPresent(secondExecutedClaim);
        claimsPageSteps.checkClaimIsPresent(thirdCanceledClaim);

        //Проверяем, что заявки со статусом Open не отображаются
        claimsPageSteps.checkClaimDoesNotPresent(firstOpenClaim);
    }

    @Test
    @DisplayName("Фильтрация заявок со статусом В работе и Отменена")
    public void shouldFilterClaimsWithStatusOpenAndInProgressAndCanceled() {
        DataHelper.CreateClaim firstOpenClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.OPEN).withDueDate(DataHelper.getValidDate()).build();
        DataHelper.CreateClaim secondCanceledClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.CANCELED).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();
        DataHelper.CreateClaim thirdInProgressClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.INPROGRESS).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();

        //Создать заявки для теста
        creatingClaimsSteps.createClaims(firstOpenClaim, secondCanceledClaim, thirdInProgressClaim);

        //Фильтруем заявки со статусом In progress и Canceled
        filterClaimsPageSteps.filterClaims(false, true, false, true);

        //Проверяем, что отфильтровались только заявки со статусом In progress и Canceled
        //Поиск элементов в RecyclerView работает нестабильно
        claimsPageSteps.checkClaimIsPresent(secondCanceledClaim);
        claimsPageSteps.checkClaimIsPresent(thirdInProgressClaim);

        //Проверяем, что заявки со статусом Open не отображаются
        claimsPageSteps.checkClaimDoesNotPresent(firstOpenClaim);
    }

    @Test
    @DisplayName("Фильтрация заявок со статусом Открыта и Отменена")
    public void shouldFilterClaimsWithStatusOpenAndOpenAndCanceled() {
        DataHelper.CreateClaim firstOpenClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.OPEN).withDueDate(DataHelper.getValidDate()).build();
        DataHelper.CreateClaim secondCanceledClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.CANCELED).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();
        DataHelper.CreateClaim thirdInProgressClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.INPROGRESS).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();
        //Создать заявки для теста
        creatingClaimsSteps.createClaims(firstOpenClaim, secondCanceledClaim, thirdInProgressClaim);

        //Фильтруем заявки со статусом Open и Canceled
        filterClaimsPageSteps.filterClaims(false, true, false, true);

        //Проверяем, что отфильтровались только заявки со статусом Open и Canceled
        //Поиск элементов в RecyclerView работает нестабильно
        claimsPageSteps.checkClaimIsPresent(firstOpenClaim);
        claimsPageSteps.checkClaimIsPresent(secondCanceledClaim);

        //Проверяем, что заявки со статусом In progress не отображаются
        claimsPageSteps.checkClaimDoesNotPresent(thirdInProgressClaim);
    }

    @Test
    @DisplayName("Фильтрация заявок со статусом Открыта и Выполнена")
    public void shouldFilterClaimsWithStatusOpenAndExecuted() {
        DataHelper.CreateClaim firstOpenClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.OPEN).withDueDate(DataHelper.getValidDate()).build();
        DataHelper.CreateClaim secondExecutedClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.EXECUTED).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();
        DataHelper.CreateClaim thirdCanceledClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.CANCELED).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();

        //Создать заявки для теста
        creatingClaimsSteps.createClaims(firstOpenClaim, secondExecutedClaim, thirdCanceledClaim);

        //Фильтруем заявки со статусом Open и Executed
        filterClaimsPageSteps.filterClaims(true, false, true, false);

        //Проверяем, что отфильтровались только заявки со статусом Open и Executed
        //Поиск элементов в RecyclerView работает нестабильно
        claimsPageSteps.checkClaimIsPresent(firstOpenClaim);
        claimsPageSteps.checkClaimIsPresent(secondExecutedClaim);

        //Проверяем, что заявки со статусом Canceled не отображаются
        claimsPageSteps.checkClaimDoesNotPresent(thirdCanceledClaim);
    }

    @Test
    @DisplayName("Фильтрация заявок со статусом В работе и Выполнена")
    public void shouldFilterClaimsWithStatusInProgressAndExecuted() {
        DataHelper.CreateClaim firstOpenClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.OPEN).withDueDate(DataHelper.getValidDate()).build();
        DataHelper.CreateClaim secondExecutedClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.EXECUTED).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();
        DataHelper.CreateClaim thirdInProgressClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.INPROGRESS).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();
        //Создать заявки для теста
        creatingClaimsSteps.createClaims(firstOpenClaim, secondExecutedClaim, thirdInProgressClaim);

        //Фильтруем заявки со статусом In progress, Executed
        filterClaimsPageSteps.filterClaims(false, true, true, false);

        //Проверяем, что отфильтровались только заявки со статусом In progress, Executed
        //Поиск элементов в RecyclerView работает нестабильно
        claimsPageSteps.checkClaimIsPresent(secondExecutedClaim);
        claimsPageSteps.checkClaimIsPresent(thirdInProgressClaim);

        //Проверяем, что заявки со статусом Open не отображаются
        claimsPageSteps.checkClaimDoesNotPresent(firstOpenClaim);
    }

    @Test
    @DisplayName("Фильтрация заявок со статусом В работе, Открыта и Выполнена")
    public void shouldFilterClaimsWithStatusInProgressOpenAndExecuted() {
        DataHelper.CreateClaim firstOpenClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.OPEN).withDueDate(DataHelper.getValidDate()).build();
        DataHelper.CreateClaim secondExecutedClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.EXECUTED).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();
        DataHelper.CreateClaim thirdInProgressClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.INPROGRESS).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();
        DataHelper.CreateClaim forthCanceledClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.CANCELED).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();
        //Создать заявки для теста
        creatingClaimsSteps.createClaims(firstOpenClaim, secondExecutedClaim, thirdInProgressClaim, forthCanceledClaim);

        //Фильтруем заявки со статусом In progress, Open, Executed
        filterClaimsPageSteps.filterClaims(true, true, true, false);

        //Проверяем, что отфильтровались только заявки со статусом In progress, Open, Executed
        //Поиск элементов в RecyclerView работает нестабильно
        claimsPageSteps.checkClaimIsPresent(firstOpenClaim);
        claimsPageSteps.checkClaimIsPresent(secondExecutedClaim);
        claimsPageSteps.checkClaimIsPresent(thirdInProgressClaim);

        //Проверяем, что заявки со статусом Canceled не отображаются
        claimsPageSteps.checkClaimDoesNotPresent(forthCanceledClaim);
    }

    @Test
    @DisplayName("Фильтрация заявок со статусом Отменена, Открыта и Выполнена")
    public void shouldFilterClaimsWithStatusCanceledOpenAndExecuted() {
        DataHelper.CreateClaim firstOpenClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.OPEN).withDueDate(DataHelper.getValidDate()).build();
        DataHelper.CreateClaim secondExecutedClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.EXECUTED).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();
        DataHelper.CreateClaim thirdInProgressClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.INPROGRESS).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();
        DataHelper.CreateClaim forthCanceledClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.CANCELED).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();
        //Создать заявки для теста
        creatingClaimsSteps.createClaims(firstOpenClaim, secondExecutedClaim, thirdInProgressClaim, forthCanceledClaim);

        //Фильтруем заявки со статусом Open, Executed, Canceled
        filterClaimsPageSteps.filterClaims(true, false, true, true);

        //Проверяем, что отфильтровались только заявки со статусом Open, Executed, Canceled
        //Поиск элементов в RecyclerView работает нестабильно
        claimsPageSteps.checkClaimIsPresent(firstOpenClaim);
        claimsPageSteps.checkClaimIsPresent(secondExecutedClaim);
        claimsPageSteps.checkClaimIsPresent(forthCanceledClaim);

        //Проверяем, что заявки со статусом In progress не отображаются
        claimsPageSteps.checkClaimDoesNotPresent(thirdInProgressClaim);
    }

    @Test
    @DisplayName("Фильтрация заявок со статусом В работе, Выполнена и Отменена")
    public void shouldFilterClaimsWithStatusInProgressExecutedAndCanceled() {
        DataHelper.CreateClaim firstOpenClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.OPEN).withDueDate(DataHelper.getValidDate()).build();
        DataHelper.CreateClaim secondExecutedClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.EXECUTED).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();
        DataHelper.CreateClaim thirdInProgressClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.INPROGRESS).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();
        DataHelper.CreateClaim forthCanceledClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.CANCELED).withDueDate(DataHelper.getValidDate()).withExecutor(DataHelper.getExecutorIvanov()).build();
        //Создать заявки для теста
        creatingClaimsSteps.createClaims(firstOpenClaim, secondExecutedClaim, thirdInProgressClaim, forthCanceledClaim);

        //Фильтруем заявки со статусом In progress Executed, Canceled
        filterClaimsPageSteps.filterClaims(false, true, true, true);

        //Проверяем, что отфильтровались только заявки со статусом In progress Executed, Canceled
        //Поиск элементов в RecyclerView работает нестабильно
        claimsPageSteps.checkClaimIsPresent(secondExecutedClaim);
        claimsPageSteps.checkClaimIsPresent(thirdInProgressClaim);
        claimsPageSteps.checkClaimIsPresent(forthCanceledClaim);

        //Проверяем, что заявки со статусом Open не отображаются
        claimsPageSteps.checkClaimDoesNotPresent(firstOpenClaim);
    }

    @Test
    @DisplayName("Кнопка отмены фильтрации")
    public void shouldCloseTheClaimsFilterSettingsForm() {
        claimsPageSteps.openClaimsFilter();
        claimsPageSteps.claimFilterCancelButtonClick();
        claimsPageSteps.isClaimsPage();
    }
}
