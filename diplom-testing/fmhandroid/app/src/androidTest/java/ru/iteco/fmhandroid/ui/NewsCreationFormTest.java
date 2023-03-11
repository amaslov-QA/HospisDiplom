package ru.iteco.fmhandroid.ui;

import static androidx.test.espresso.action.ViewActions.click;
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
import ru.iteco.fmhandroid.ui.data.CustomRecyclerViewActions;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.data.TestUtils;
import ru.iteco.fmhandroid.ui.steps.AuthSteps;
import ru.iteco.fmhandroid.ui.steps.ControlPanelSteps;
import ru.iteco.fmhandroid.ui.steps.MainPageSteps;
import ru.iteco.fmhandroid.ui.steps.NewsPageSteps;

@RunWith(AllureAndroidJUnit4.class)

public class NewsCreationFormTest extends BaseTest {
    private UiDevice device;

    private static AuthSteps authSteps = new AuthSteps();
    private static MainPageSteps mainPageSteps = new MainPageSteps();
    private static NewsPageSteps newsPageSteps = new NewsPageSteps();
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
        mainPageSteps.openNewsPageThroughTheMainMenu();
        newsPageSteps.openControlPanel();
        controlPanelSteps.openCreatingNewsForm();
    }

    @After
    public void disableAirplaneMode() throws RemoteException, UiObjectNotFoundException {
        device.setOrientationNatural();
        TestUtils.disableAirplaneMode();
    }

    @Test
    @DisplayName("Автоподставление в поле Title из поля Category")
    public void shouldSubstituteInTheTitleFieldTheValueOfTheCategoryField() {
        String categoryAnnouncement = "Объявление";
        controlPanelSteps.selectANewsCategoryFromTheList(categoryAnnouncement);
        controlPanelSteps.getNewsItemTitle().check(matches(withText(categoryAnnouncement)));
    }

    @Test
    @DisplayName("Создание Новости с категорией Объявление")
    public void shouldCreateANewsItemWithCategoryAnnouncement() {
        DataHelper.CreateNews announcementNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryAnnouncement()).withDueDate(today).build();
        String categoryAnnouncement = "Объявление";

        controlPanelSteps.selectANewsCategoryFromTheList(categoryAnnouncement);
        controlPanelSteps.fillingOutTheFormCreatingNewsWithDate(announcementNews);
        controlPanelSteps.saveButtonClick();
        //Проверка что отображаются новости с заголовком "Тест новость Объявление"
        controlPanelSteps.scrollToElementInRecyclerList(announcementNews.getNewsName()).check(matches(isDisplayed()));
    }

    @Test
    @DisplayName("Создание Новости с категорией День рождения")
    public void shouldCreateANewsItemWithCategoryBirthday() {
        DataHelper.CreateNews birthdayNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryBirthday()).withDueDate(today).build();
        String categoryBirthday = "День рождения";

        controlPanelSteps.selectANewsCategoryFromTheList(categoryBirthday);
        controlPanelSteps.fillingOutTheFormCreatingNewsWithDate(birthdayNews);
        controlPanelSteps.saveButtonClick();
        //Проверка что отображаются новости с заголовком "Тест новость День рождения"
        controlPanelSteps.scrollToElementInRecyclerList(birthdayNews.getNewsName()).check(matches(isDisplayed()));
    }

    @Test
    @DisplayName("Создание Новости с категорией Зарплата")
    public void shouldCreateANewsItemWithCategorySalary() {
        DataHelper.CreateNews salaryNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategorySalary()).withDueDate(today).build();
        String categorySalary = "Зарплата";

        controlPanelSteps.selectANewsCategoryFromTheList(categorySalary);
        controlPanelSteps.fillingOutTheFormCreatingNewsWithDate(salaryNews);
        controlPanelSteps.saveButtonClick();
        //Проверка что отображаются новости с заголовком "Тест новость Зарплата"
        controlPanelSteps.scrollToElementInRecyclerList(salaryNews.getNewsName()).check(matches(isDisplayed()));
    }

    @Test
    @DisplayName("Создание Новости с категорией Профсоюз")
    public void shouldCreateANewsItemWithCategoryTradeUnion() {
        DataHelper.CreateNews tradeUnionNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryTradeUnion()).withDueDate(today).build();
        String categoryTradeUnion = "Профсоюз";

        controlPanelSteps.selectANewsCategoryFromTheList(categoryTradeUnion);
        controlPanelSteps.fillingOutTheFormCreatingNewsWithDate(tradeUnionNews);
        controlPanelSteps.saveButtonClick();
        //Проверка что отображаются новости с заголовком "Тест новость Профсоюз"
        controlPanelSteps.scrollToElementInRecyclerList(tradeUnionNews.getNewsName()).check(matches(isDisplayed()));
    }

    @Test
    @DisplayName("Создание Новости с категорией Праздник")
    public void shouldCreateANewsItemWithCategoryHoliday() {
        DataHelper.CreateNews holidayNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryHoliday()).withDueDate(today).build();
        String categoryHoliday = "Праздник";

        controlPanelSteps.selectANewsCategoryFromTheList(categoryHoliday);
        controlPanelSteps.fillingOutTheFormCreatingNewsWithDate(holidayNews);
        controlPanelSteps.saveButtonClick();
        //Проверка что отображаются новости с заголовком "Тест новость Праздник"
        controlPanelSteps.scrollToElementInRecyclerList(holidayNews.getNewsName()).check(matches(isDisplayed()));
    }

    @Test
    @DisplayName("Создание Новости с категорией Благодарность")
    public void shouldCreateANewsItemWithCategoryGratitude() {
        DataHelper.CreateNews gratitudeNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryGratitude()).withDueDate(today).build();
        String categoryGratitude = "Благодарность";

        controlPanelSteps.selectANewsCategoryFromTheList(categoryGratitude);
        controlPanelSteps.fillingOutTheFormCreatingNewsWithDate(gratitudeNews);
        controlPanelSteps.saveButtonClick();
        //Проверка что отображаются новости с заголовком "Тест новость Благодарность"
        controlPanelSteps.scrollToElementInRecyclerList(gratitudeNews.getNewsName()).check(matches(isDisplayed()));
    }

    @Test
    @DisplayName("Создание Новости с категорией Массаж")
    public void shouldCreateANewsItemWithCategoryMassage() {
        DataHelper.CreateNews massageNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryMassage()).withDueDate(today).build();
        String categoryMassage = "Массаж";

        controlPanelSteps.selectANewsCategoryFromTheList(categoryMassage);
        controlPanelSteps.fillingOutTheFormCreatingNewsWithDate(massageNews);
        controlPanelSteps.saveButtonClick();
        //Проверка что отображаются новости с заголовком "Тест новость Массаж"
        controlPanelSteps.scrollToElementInRecyclerList(massageNews.getNewsName()).check(matches(isDisplayed()));
    }

    @Test
    @DisplayName("Создание Новости с категорией Нужна помощь")
    public void shouldCreateANewsItemWithCategoryNeedHelp() {
        DataHelper.CreateNews needHelpNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryNeedHelp()).withDueDate(today).build();
        String categoryNeedHelp = "Нужна помощь";

        controlPanelSteps.selectANewsCategoryFromTheList(categoryNeedHelp);
        controlPanelSteps.fillingOutTheFormCreatingNewsWithDate(needHelpNews);
        controlPanelSteps.saveButtonClick();
        //Проверка что отображаются новости с заголовком "Тест новость Массаж"
        controlPanelSteps.scrollToElementInRecyclerList(needHelpNews.getNewsName()).check(matches(isDisplayed()));
    }

    @Test
    @DisplayName("Отмена создания новости")
    public void shouldNotCreateNews() {
        DataHelper.CreateNews needHelpNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryNeedHelp()).withDueDate(today).build();
        String categoryNeedHelp = "Нужна помощь";

        controlPanelSteps.selectANewsCategoryFromTheList(categoryNeedHelp);
        controlPanelSteps.fillingOutTheFormCreatingNewsWithDate(needHelpNews);
        controlPanelSteps.cancelButtonClick();
        controlPanelSteps.getMessageChangesWonTBeSaved().check(matches(isDisplayed()));
        controlPanelSteps.okButtonClick();
        //Проверить отсутствие в списке новостей новости с заголовком "Новость не должна сохраниться"
        controlPanelSteps.getNewsRecyclerList()
                .check(matches(CustomRecyclerViewActions.RecyclerViewMatcher
                        .matchChildViewIsNotExist(controlPanelSteps.newsItemTitleTextView, withText(needHelpNews.getNewsName()))));
    }

    @Test
    @DisplayName("Создание новости с категорией не из списка")
    public void shouldShowAWrongMessageWithTextSelectACategoryFromTheList() {
        DataHelper.CreateNews needHelpNews = DataHelper.newsWithRandomNameAndDescription()
                .withDueDate(today).build();
        String MyCategory = "Тест";

        controlPanelSteps.replaceNewsCategoryText(MyCategory);
        controlPanelSteps.fillingOutTheFormCreatingNewsWithDate(needHelpNews);
        controlPanelSteps.saveButtonClick();
        controlPanelSteps.checkToast("Wrong category selected. Select a category from the list.", true);
    }

    @Test
    @DisplayName("Сохранение пустой формы новости")
    public void shouldNotSaveEmptyNews() {
        controlPanelSteps.saveButtonClick();
        controlPanelSteps.isWrongEmptyFormNews();
    }

    @Test
    @DisplayName("Создание Новости со статусом Не активна")
    public void shouldToggleTurnOffSwitchActive() {
        DataHelper.CreateNews massageNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryMassage()).withDueDate(today).build();
        String categoryMassage = "Массаж";

        controlPanelSteps.selectANewsCategoryFromTheList(categoryMassage);
        controlPanelSteps.fillingOutTheFormCreatingNewsWithDate(massageNews);
        controlPanelSteps.switchNewsStatus();
        controlPanelSteps.getSwitcherNoteActive().check(matches(isDisplayed()));
    }

    @Test
    @DisplayName("Создание Новости с датой публикации завтра")
    public void shouldCreateANewsItemWithPublishDateTomorrow() {
        DataHelper.CreateNews salaryNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategorySalary()).withDueDate(today.plus(1, ChronoUnit.DAYS)).build();
        String categorySalary = "Зарплата";

        String dateExpected = TestUtils.getDateToString(salaryNews.getDueDate());

        controlPanelSteps.selectANewsCategoryFromTheList(categorySalary);
        controlPanelSteps.fillingOutTheFormCreatingNewsWithDate(salaryNews);
        //Проверка, что выбранная дата отображается
        controlPanelSteps.getNewsItemPublishDate().check(matches(withText(dateExpected)));
        controlPanelSteps.saveButtonClick();
        //Проверка что отображаются новости с заголовком "Тест новость Зарплата"
        controlPanelSteps.scrollToElementInRecyclerList(salaryNews.getNewsName()).check(matches(isDisplayed()));
    }

    @Test
    @DisplayName("Создание Новости с датой публикации через месяц")
    public void shouldCreateANewsItemWithPublicationDateInAMonth() {
        DataHelper.CreateNews salaryNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategorySalary()).withDueDate(today.plus(1, ChronoUnit.MONTHS)).build();
        String categorySalary = "Зарплата";

        String dateExpected = TestUtils.getDateToString(salaryNews.getDueDate());

        controlPanelSteps.selectANewsCategoryFromTheList(categorySalary);
        controlPanelSteps.fillingOutTheFormCreatingNewsWithDate(salaryNews);

        controlPanelSteps.getNewsItemPublishDate().check(matches(withText(dateExpected)));
        controlPanelSteps.saveButtonClick();
        //Проверка что отображаются новости с заголовком "Тест новость Зарплата"
        controlPanelSteps.scrollToElementInRecyclerList(salaryNews.getNewsName()).check(matches(isDisplayed()));
    }

    @Ignore//При ручном тестировании этот кейс проходит без ошибки
    @Test
    @DisplayName("Создание Новости с датой публикации вчера")
    public void shouldNotCreateANewsItemWithPublicationDateYesterday() {
        DataHelper.CreateNews salaryNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategorySalary()).withDueDate(today.minus(1, ChronoUnit.DAYS)).build();
        String categorySalary = "Зарплата";
        String dateExpected = TestUtils.getDateToString(today);

        controlPanelSteps.selectANewsCategoryFromTheList(categorySalary);
        controlPanelSteps.setDateToDatePicker(salaryNews.getDueDate());
        controlPanelSteps.okButtonClick();
        //Проверяем, что в поле Дата отображается сегодняшняя дата
        controlPanelSteps.getNewsItemPublishDate().check(matches(withText(dateExpected)));
    }

    @Ignore//При ручном тестировании этот кейс проходит без ошибки
    @Test
    @DisplayName("Создание Новости с датой публикации год назад")
    public void shouldNotCreateANewsItemWithPublicationDateOneYearAgo() {
        DataHelper.CreateNews salaryNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategorySalary()).withDueDate(today.minus(1, ChronoUnit.YEARS)).build();
        String categorySalary = "Зарплата";
        String dateExpected = TestUtils.getDateToString(today);

        controlPanelSteps.selectANewsCategoryFromTheList(categorySalary);
        controlPanelSteps.setDateToDatePicker(salaryNews.getDueDate());
        controlPanelSteps.okButtonClick();
        controlPanelSteps.getNewsItemPublishDate().check(matches(withText(dateExpected)));
    }

    @Test
    @DisplayName("Создание Новости с датой публикации час назад")
    public void shouldNotCreateANewsItemWithPublicationTimeHourAgo() {
        DataHelper.CreateNews salaryNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategorySalary()).withDueDate(today.minus(1, ChronoUnit.HOURS)).build();
        String categorySalary = "Зарплата";
        String timeExpected = TestUtils.getTimeToString(salaryNews.getDueDate());

        controlPanelSteps.selectANewsCategoryFromTheList(categorySalary);
        controlPanelSteps.fillingOutTheFormCreatingNewsWithDate(salaryNews);

        //Проверка,что выбранное время отображается
        controlPanelSteps.getNewsItemPublishTime().check(matches(withText(timeExpected)));

        //Сохраняем
        controlPanelSteps.saveButtonClick();
        //Проверка что отображаются новости с заголовком
        controlPanelSteps.checkToast("Wrong publication date or time.", true);

    }

    @Test
    @DisplayName("Создание Новости с датой публикации через час")
    public void shouldCreateANewsItemWithPublicationTimeInOneHour() {
        DataHelper.CreateNews salaryNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategorySalary()).withDueDate(today.plus(1, ChronoUnit.HOURS)).build();
        String categorySalary = "Зарплата";

        String timeExpected = TestUtils.getTimeToString(salaryNews.getDueDate());


        controlPanelSteps.selectANewsCategoryFromTheList(categorySalary);
        controlPanelSteps.fillingOutTheFormCreatingNewsWithDate(salaryNews);
        //Проверка,что выбранное время отображается
        controlPanelSteps.getNewsItemPublishTime().check(matches(withText(timeExpected)));

        //Сохраняем
        controlPanelSteps.saveButtonClick();
        //Проверка что отображаются новости с заголовком
        controlPanelSteps.scrollToElementInRecyclerList(salaryNews.getNewsName()).check(matches(isDisplayed()));
    }


    @Test
    @DisplayName("Выставление времени публикации Новости вводом цифр")
    public void shouldSetTheTimeByEnteringNumbers() {
        LocalDateTime date = today.plusHours(1);
        String hour = TestUtils.getHourToString(date);
        String minute = TestUtils.getMinuteToString(date);
        String timeExpected = TestUtils.getTimeToString(date);

        //Открываем TimePicker и вводим время с клавиатуры
        controlPanelSteps.openNewsTimePicker();
        controlPanelSteps.setTimeToTimePickerFromTheKeyboard(hour, minute);
        //Проверка
        controlPanelSteps.getNewsItemPublishTime().check(matches(withText(timeExpected)));
    }

    @Test
    @DisplayName("Отмена ввода времени в Форме для создания Новости")
    public void shouldNotSetTime() {
        LocalDateTime date = today.plusHours(1);

        controlPanelSteps.getNewsItemPublishTime().perform(click());
        controlPanelSteps.setTimeToTimePicker(date.getHour(), date.getMinute());
        controlPanelSteps.cancelDeleteButtonClick();
        //Проверяем, что поле Время пустое
        controlPanelSteps.getNewsItemPublishTime().check(matches(withText("")));
    }

    @Test
    @DisplayName("Отмена ввода даты в Форме для создания Новости")
    public void shouldNotSetDate() {
        controlPanelSteps.setDateToDatePicker(today.plus(1, ChronoUnit.HOURS));
        controlPanelSteps.cancelDeleteButtonClick();
        controlPanelSteps.getNewsItemPublishDate().check(matches(withText("")));
    }

    @Test
    @DisplayName("Отмена Создания Новости и отмена выхода из формы")
    public void shouldNotGetOutOfNewsForm() {
        controlPanelSteps.cancelButtonClick();
        controlPanelSteps.getMessageChangesWonTBeSaved().check(matches(isDisplayed()));
        controlPanelSteps.cancelDeleteButtonClick();
        controlPanelSteps.isCreatingNewsForm();
    }

    @Test
    @DisplayName("Небуквенные и нецифровые знаки в поле Заголовок при создании новости")
    public void shouldShowWarningMessageNewsTitleFieldIsIncorrect() {
        String nonLetterTitle = ";&&";
        DataHelper.CreateNews announcementNews = DataHelper.news().withName(nonLetterTitle).withDescription(DataHelper.getDescription())
                .withCategory(DataHelper.getCategoryAnnouncement()).withDueDate(today).build();
        String categoryAnnouncement = "Объявление";

        controlPanelSteps.selectANewsCategoryFromTheList(categoryAnnouncement);
        controlPanelSteps.fillingOutTheFormCreatingNewsWithDate(announcementNews);
        controlPanelSteps.saveButtonClick();
        //Проверка, что отображается сообщение
        controlPanelSteps.checkToast("The field must not contain \";&&\" characters.", true);
    }

    @Test
    @DisplayName("Небуквенные и нецифровые знаки в поле Описание при создании новости")
    public void shouldShowWarningMessageNewsDescriptionFieldIsIncorrect() {
        String nonLetterDescription = ";&&";
        DataHelper.CreateNews announcementNews = DataHelper.news().withName(DataHelper.getTitle()).withDescription(nonLetterDescription)
                .withCategory(DataHelper.getCategoryAnnouncement()).withDueDate(today).build();
        String categoryAnnouncement = "Объявление";

        controlPanelSteps.selectANewsCategoryFromTheList(categoryAnnouncement);
        controlPanelSteps.fillingOutTheFormCreatingNewsWithDate(announcementNews);
        controlPanelSteps.saveButtonClick();
        //Проверка, что отображается сообщение
        controlPanelSteps.checkToast("The field must not contain \";&&\" characters.", true);
    }

    @Test
    @DisplayName("Разрыв соединения во время создания новости")
    public void shouldShowWarningMessageWhenTheConnectionIsBrokenDuringTheCreationOfTheNews() throws UiObjectNotFoundException {
        DataHelper.CreateNews salaryNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategorySalary()).withDueDate(today).build();
        String categorySalary = "Зарплата";

        controlPanelSteps.selectANewsCategoryFromTheList(categorySalary);
        controlPanelSteps.fillingOutTheFormCreatingNewsWithDate(salaryNews);
        //Включаем режим В самолете
        authSteps.turnOnAirplaneMode();
        //Пытаемся сохранить новость
        controlPanelSteps.saveButtonClick();
        //Проверяем, что отображается сообщение
        controlPanelSteps.checkToast("Saving failed. Try again later.", true);
        //Отключаем режим в самолете
        authSteps.turnOffAirplaneMode();
    }

    @Test
    @DisplayName("Поворот экрана при создании новости")
    public void shouldSaveDataInTheNewsCreationFormOnScreenRotation() throws UiObjectNotFoundException, RemoteException {
        DataHelper.CreateNews salaryNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategorySalary()).withDueDate(today).build();
        String categorySalary = "Зарплата";
        String dateExpected = TestUtils.getDateToString(salaryNews.getDueDate());

        controlPanelSteps.selectANewsCategoryFromTheList(categorySalary);
        controlPanelSteps.fillingOutTheFormCreatingNewsWithDate(salaryNews);
        device.setOrientationLeft();
        //Проверяем, что введенные данные сохранились
        controlPanelSteps.getNewsItemTitle().check(matches(withText(salaryNews.getNewsName())));
        controlPanelSteps.getNewsItemPublishDate().check(matches(withText(dateExpected)));
        controlPanelSteps.getNewsItemDescription().check(matches(withText(salaryNews.getNewsDescription())));
    }


}

