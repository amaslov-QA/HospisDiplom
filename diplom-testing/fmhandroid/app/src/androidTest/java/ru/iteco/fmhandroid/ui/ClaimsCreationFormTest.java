package ru.iteco.fmhandroid.ui;


import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static ru.iteco.fmhandroid.ui.data.DataHelper.authInfo;

import android.os.RemoteException;

import androidx.test.espresso.PerformException;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObjectNotFoundException;

import org.junit.After;
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

public class ClaimsCreationFormTest extends BaseTest {

    private UiDevice device;

    private static final String BASIC_PACKAGE = "ru.iteco.fmhandroid";

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
        try {
            authSteps.isAuthScreen();
        } catch (PerformException e) {
            mainPageSteps.clickLogOutBut();
        }
        authSteps.authWithValidData(authInfo());
        mainPageSteps.isMainPage();
        mainPageSteps.openClaimsPageThroughTheMainMenu();
        claimsPageSteps.openCreatingClaimsCard();
    }

    @After
    public void disableAirplaneMode() throws RemoteException, UiObjectNotFoundException {
        device.setOrientationNatural();
        TestUtils.disableAirplaneMode();
    }

    @Test
    @DisplayName("Создание заявки")
    public void shouldCreateAClaim() {
        DataHelper.CreateClaim inProgressClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.INPROGRESS).withDueDate(today).withExecutor("Иванов Данил Данилович").build();
        //Создать заявку
        creatingClaimsSteps.fillingOutTheFormCreatingClaim(inProgressClaim);
        controlPanelSteps.saveButtonClick();
        //Проверка что отображается заявка
        claimsPageSteps.checkClaimIsPresent(inProgressClaim);
    }


    @Test
    @DisplayName("Сохранение пустой заявки")
    public void shouldNotCreateEmptyClaim() {
        controlPanelSteps.saveButtonClick();
        creatingClaimsSteps.isFillEmptyFieldsMessage();
        controlPanelSteps.okButtonClick();
        creatingClaimsSteps.isWrongEmptyFormClaim();
    }

    @Test
    @DisplayName("Сохранение заявки без исполнителя")
    public void shouldCreateAClaimWithoutChoosingExecutor() {
        DataHelper.CreateClaim openClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.OPEN).withDueDate(today).build();
        //Создать заявку
        creatingClaimsSteps.fillingOutTheFormCreatingClaim(openClaim);
        controlPanelSteps.saveButtonClick();
        //Проверка что отображается заявка
        claimsPageSteps.checkClaimIsPresent(openClaim);
    }

    @Ignore //Выбирает вчерашнюю дату. При ручном тестировании невозможно выбрать вчерашнюю дату
    @Test
    @DisplayName("Выбор вчерашней даты в заявке")
    public void shouldNotChoosePublicationDateYesterday() {
        String dateExpected = TestUtils.getDateToString(today);

        //Выбираем в календаре вчерашнюю дату
        creatingClaimsSteps.setDateToDatePicker(today.minus(1, ChronoUnit.DAYS));
        controlPanelSteps.okButtonClick();
        //Проверяем, что в поле Дата отображается сегодняшняя дата
        creatingClaimsSteps.getClaimDateInPlane().check(matches(withText(dateExpected)));
    }

    @Ignore//Выбирает вчерашнюю дату. При ручном тестировании невозможно выбрать вчерашнюю дату
    @Test
    @DisplayName("Выбор даты год назад в заявке")
    public void shouldNotChoosePublicationDateYearAgo() {
        String dateExpected = TestUtils.getDateToString(today);

        //Выбираем в календаре дату год назад
        creatingClaimsSteps.setDateToDatePicker(today.minus(1, ChronoUnit.YEARS));
        controlPanelSteps.okButtonClick();
        //Проверяем, что в поле Дата отображается сегодняшняя дата
        creatingClaimsSteps.getClaimDateInPlane().check(matches(withText(dateExpected)));
    }

    @Test
    @DisplayName("Выбор даты завтра в заявке")
    public void shouldChoosePublicationDateTomorrow() {
        String dateExpected = TestUtils.getDateToString(today.plus(1, ChronoUnit.DAYS));

        //Выбираем в календаре дату завтра
        creatingClaimsSteps.setDateToDatePicker(today.plus(1, ChronoUnit.DAYS));
        controlPanelSteps.okButtonClick();
        //Проверяем, что в поле Дата отображается выбранная дата
        creatingClaimsSteps.getClaimDateInPlane().check(matches(withText(dateExpected)));
    }

    @Test
    @DisplayName("Выбор даты через год в заявке")
    public void shouldChoosePublicationDateInAYear() {
        String dateExpected = TestUtils.getDateToString(today.plus(1, ChronoUnit.YEARS));

        //Выбираем в календаре дату через год
        creatingClaimsSteps.setDateToDatePicker(today.plus(1, ChronoUnit.YEARS));
        controlPanelSteps.okButtonClick();
        //Проверяем, что в поле Дата отображается выбранная дата
        creatingClaimsSteps.getClaimDateInPlane().check(matches(withText(dateExpected)));
    }


    @Test
    @DisplayName("Создание заявки. В поле Время  на час больше текущего")
    public void shouldChoosePublicationTimeInOneHour() {
        String timeExpected = TestUtils.getTimeToString(today.plus(1, ChronoUnit.HOURS));

        //Выбираем в часах время на час больше текущего
        creatingClaimsSteps.setTimeToTimeField(today.plus(1, ChronoUnit.HOURS));
        //Проверяем, что в поле Время отображается выбранное время
        creatingClaimsSteps.getClaimTime().check(matches(withText(timeExpected)));
    }

    @Test
    @DisplayName("Создание заявки. В поле Время  на час меньше текущего")
    public void shouldChoosePublicationTimeHourAgo() {
        String timeExpected = TestUtils.getTimeToString(today.minus(1, ChronoUnit.HOURS));

        //Выбираем в часах время на час меньше текущего
        creatingClaimsSteps.setTimeToTimeField(today.minus(1, ChronoUnit.HOURS));
        //Проверяем, что в поле Время отображается выбранное время
        creatingClaimsSteps.getClaimTime().check(matches(withText(timeExpected)));
    }


    @Test
    @DisplayName("Создание заявки. В поле Время  на минуту больше текущего")
    public void shouldChoosePublicationTimeInOneMinute() {
        String timeExpected = TestUtils.getTimeToString(today.plus(1, ChronoUnit.MINUTES));

        //Выбираем в часах время на минуту больше текущего
        creatingClaimsSteps.setTimeToTimeField(today.plus(1, ChronoUnit.MINUTES));
        //Проверяем, что в поле Время отображается выбранное время
        creatingClaimsSteps.getClaimTime().check(matches(withText(timeExpected)));
    }

    @Test
    @DisplayName("Создание заявки. В поле Время  на минуту меньше текущего")
    public void shouldChoosePublicationTimeMinuteAgo() {
        String timeExpected = TestUtils.getTimeToString(today.minus(1, ChronoUnit.MINUTES));

        //Выбираем в часах время на минуту меньше текущего
        creatingClaimsSteps.setTimeToTimeField(today.minus(1, ChronoUnit.MINUTES));
        //Проверяем, что в поле Время отображается выбранное время
        creatingClaimsSteps.getClaimTime().check(matches(withText(timeExpected)));
    }

    @Test
    @DisplayName("Создание заявки. В поле Время  нереальное время")
    public void shouldShowTextEnteredInvalidValue() {
        String hour = "99";
        String minutes = "99";

        //Вводим в часах нереальное время
        creatingClaimsSteps.openClaimTimePicker();
        controlPanelSteps.setTimeToTimePickerFromTheKeyboard(hour, minutes);
        //Проверяем, что отображается текст "Enter a valid time"
        claimsPageSteps.getLabelError().check(matches(isDisplayed()));
    }

    @Test
    @DisplayName("Создание заявки с заголовком начинающимся с пробела")
    public void shouldCreateAClaimWithATitleStartsWithoutASpaceAtTheBeginning() {
        DataHelper.CreateClaim inProgressClaim = DataHelper.claim().withName(DataHelper.getClaimTitleWithASpace())
                .withDescription(DataHelper.getClaimTitleWithASpace()).withStatus(DataHelper.ClaimStatus.INPROGRESS)
                .withDueDate(today).withExecutor(DataHelper.getExecutorIvanov()).build();
        String titleWithoutSpace = inProgressClaim.getClaimName().substring(1);

        //Создать заявку
        creatingClaimsSteps.fillingOutTheFormCreatingClaim(inProgressClaim);
        //Проверка, что в поле Заголовок не отображается введенный текст
        claimsPageSteps.getDescriptionClaimField().check(matches(withText(titleWithoutSpace)));
        controlPanelSteps.saveButtonClick();

        //Проверка что отображается заявка
        claimsPageSteps.scrollToElementInRecyclerList(titleWithoutSpace).check(matches(isDisplayed()));
    }

    @Test
    @DisplayName("Создание заявки с заголовком из 50 знаков")
    public void shouldCreateAClaimWithATitleWith50Characters() {
        DataHelper.CreateClaim inProgressClaim = DataHelper.claim().withName(DataHelper.getClaimTitle50Characters())
                .withDescription(DataHelper.getClaimTitle50Characters()).withStatus(DataHelper.ClaimStatus.INPROGRESS)
                .withDueDate(today).withExecutor(DataHelper.getExecutorIvanov()).build();

        //Создать заявку
        creatingClaimsSteps.fillingOutTheFormCreatingClaim(inProgressClaim);
        //Проверка, что в поле Title 50 символов
        claimsPageSteps.getTitleClaim().check(matches(withText(inProgressClaim.getClaimName())));
        controlPanelSteps.saveButtonClick();
        //Проверка что отображается заявка
        claimsPageSteps.checkClaimIsPresent(inProgressClaim);
    }

    @Test
    @DisplayName("Создание заявки с заголовком из 49 знаков")
    public void shouldCreateAClaimWithATitleWith49Characters() {
        DataHelper.CreateClaim inProgressClaim = DataHelper.claim().withName(DataHelper.getClaimTitle49Characters())
                .withDescription(DataHelper.getClaimTitle49Characters()).withStatus(DataHelper.ClaimStatus.INPROGRESS)
                .withDueDate(today).withExecutor(DataHelper.getExecutorIvanov()).build();
        //Создать заявку
        creatingClaimsSteps.fillingOutTheFormCreatingClaim(inProgressClaim);
        //Проверка, что в поле Title отображается 49 символов
        claimsPageSteps.getTitleClaim().check(matches(withText(inProgressClaim.getClaimName())));
        controlPanelSteps.saveButtonClick();
        //Проверка что отображается заявка
        claimsPageSteps.checkClaimIsPresent(inProgressClaim);
    }

    @Test
    @DisplayName("Создание заявки с заголовком из 51 знаков")
    public void shouldCreateAClaimWithATitleWith51Characters() {
        DataHelper.CreateClaim inProgressClaim = DataHelper.claim().withName(DataHelper.getClaimTitle51Characters())
                .withDescription(DataHelper.getClaimTitle51Characters()).withStatus(DataHelper.ClaimStatus.INPROGRESS)
                .withDueDate(today).withExecutor(DataHelper.getExecutorIvanov()).build();
        String titleWhichToBeKept = inProgressClaim.getClaimName().substring(0, 50);

        //Создать заявку
        creatingClaimsSteps.fillingOutTheFormCreatingClaim(inProgressClaim);
        //Проверка, что в поле Title отображается только 50 символов
        claimsPageSteps.getTitleClaim().check(matches(withText(titleWhichToBeKept)));
        controlPanelSteps.saveButtonClick();
        //Проверка что отображается заявка
        claimsPageSteps.scrollToElementInRecyclerList(titleWhichToBeKept).check(matches(isDisplayed()));
    }


    @Test
    @DisplayName("Создание заявки. Исполнитель  не из списка")
    public void shouldCreateAClaimWithoutAnExecutor() {
        String MayExecutor = "Козлов Константин Анатольевич";
        DataHelper.CreateClaim inProgressClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.INPROGRESS).withDueDate(today).withExecutor(MayExecutor).build();

        //Создать заявку
        creatingClaimsSteps.replaceTextClaimExecutor(inProgressClaim.getExecutorName());
        creatingClaimsSteps.fillingOutTheFormCreatingClaim(inProgressClaim);
        controlPanelSteps.saveButtonClick();
        //Проверка что отображается заявка. Открытие ее
        claimsPageSteps.openClaimCard(inProgressClaim);
        //Проверка что исполнитель не назначен
        claimsPageSteps.getItemClaimExecutorName(inProgressClaim.getClaimName()).check(matches(withText("")));
    }

    @Test
    @DisplayName("Создание заявки.  В заголовке небуквенные и нецифровые симовлы")
    public void shouldNotDisplayNonAlphabeticAndNonNumericCharactersInTheFieldTitle() {
        String nonLetterTitle = ";&&";
        DataHelper.CreateClaim inProgressClaim = DataHelper.claim().withName(nonLetterTitle)
                .withDescription(DataHelper.getDescription()).withStatus(DataHelper.ClaimStatus.INPROGRESS)
                .withDueDate(today).withExecutor(DataHelper.getExecutorIvanov()).build();

        //Создать заявку
        creatingClaimsSteps.fillingOutTheFormCreatingClaim(inProgressClaim);
        //Проверка, что в поле Заголовок не отображается введенный текст
        claimsPageSteps.getTitleClaim().check(matches(withText("")));
        controlPanelSteps.saveButtonClick();
        //Проверка, что появляется диалоговое окно  с текстом
        creatingClaimsSteps.isFillEmptyFieldsMessage();
    }

    @Test
    @DisplayName("Создание заявки.  Описание заявки небуквенные и нецифровые символы")
    public void shouldNotDisplayNonAlphabeticAndNonNumericCharactersInTheFieldDescription() {
        String nonLetterDescription = ";&&";
        DataHelper.CreateClaim inProgressClaim = DataHelper.claim().withName(DataHelper.getTitle())
                .withDescription(nonLetterDescription).withStatus(DataHelper.ClaimStatus.INPROGRESS)
                .withDueDate(today).withExecutor(DataHelper.getExecutorIvanov()).build();

        //Создать заявку
        creatingClaimsSteps.fillingOutTheFormCreatingClaim(inProgressClaim);
        //Проверка, что в поле Заголовок не отображается введенный текст
        claimsPageSteps.getDescriptionClaimField().check(matches(withText("")));
        controlPanelSteps.saveButtonClick();
        //Проверка, что появляется диалоговое окно  с текстом
        creatingClaimsSteps.isFillEmptyFieldsMessage();
    }

    @Test
    @DisplayName("Сворачивание приложения во время создания заявки")
    public void shouldOpenTheAppOnTheClaimAfterPressHome() throws UiObjectNotFoundException {
        DataHelper.CreateClaim openClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.OPEN).withDueDate(today).build();
        creatingClaimsSteps.replaceTitleClaimText(openClaim.getClaimName());
        device.pressHome();
        TestUtils.waitForPackage(BASIC_PACKAGE);
        //Проверка что отображается заявка, которую начали создавать
        //Тест падает, потому как сначала открывается главная страница и после ожидания отктывается создаваемая заявка. Сколько бы не прибавлялось время ожидания элемента, тест падает.
        claimsPageSteps.getTitleClaim().check(matches(withText(openClaim.getClaimName())));
    }

    @Test
    @DisplayName("Разрыв соединения во время создания заявки")
    public void shouldShowWarningMessageWhenTheConnectionIsBrokenDuringTheCreationOfTheClaim() throws UiObjectNotFoundException {
        DataHelper.CreateClaim inProgressClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.INPROGRESS).withExecutor(DataHelper.getExecutorIvanov()).withDueDate(today).build();

        creatingClaimsSteps.fillingOutTheFormCreatingClaim(inProgressClaim);
        //Включаем режим В самолете
        authSteps.turnOnAirplaneMode();
        //Пытаемся сохранить заявку
        controlPanelSteps.saveButtonClick();
        //Проверяем, что отображается сообщение
        creatingClaimsSteps.isDialogWindowMessageTryAgainLatter();
        //Отключаем режим в самолете
        TestUtils.disableAirplaneMode();
    }

    @Test
    @DisplayName("Поворот экрана при создании заявки")
    public void shouldSaveDataOnScreenRotation() throws RemoteException {
        DataHelper.CreateClaim inProgressClaim = DataHelper.claimWithRandomNameAndDescription()
                .withStatus(DataHelper.ClaimStatus.INPROGRESS).withExecutor(DataHelper.getExecutorIvanov()).withDueDate(today).build();
        String dateExpected = TestUtils.getDateToString(today);
        String timeExpected = TestUtils.getTimeToString(today);

        creatingClaimsSteps.fillingOutTheFormCreatingClaim(inProgressClaim);
        //Поворачиваем экран
        device.setOrientationLeft();
        //Проверяем, что введенные данные сохранились
        claimsPageSteps.getTitleClaim().check(matches(withText(inProgressClaim.getClaimName())));
        creatingClaimsSteps.getClaimDateInPlane().check(matches(withText(dateExpected)));
        creatingClaimsSteps.getClaimTime().check(matches(withText(timeExpected)));
        claimsPageSteps.getDescriptionClaimField().check(matches(withText(inProgressClaim.getClaimDescription())));
        device.setOrientationNatural();
    }


}
