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
import java.time.temporal.ChronoUnit;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.junit4.DisplayName;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.data.TestUtils;
import ru.iteco.fmhandroid.ui.steps.AuthSteps;
import ru.iteco.fmhandroid.ui.steps.ControlPanelSteps;
import ru.iteco.fmhandroid.ui.steps.FilterNewsPageSteps;
import ru.iteco.fmhandroid.ui.steps.MainPageSteps;
import ru.iteco.fmhandroid.ui.steps.NewsPageSteps;

@RunWith(AllureAndroidJUnit4.class)

public class FilterNewsPageTest extends BaseTest {
    private UiDevice device;

    private static AuthSteps authSteps = new AuthSteps();
    private static MainPageSteps mainPageSteps = new MainPageSteps();
    private static NewsPageSteps newsPageSteps = new NewsPageSteps();
    private static FilterNewsPageSteps filterNewsPageSteps = new FilterNewsPageSteps();
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
        mainPageSteps.openNewsPageThroughTheMainMenu();
    }

    @Test
    @DisplayName("Фильтрация новостей по Категории Обьявление")
    public void shouldFilterTheNewsWithCategoryAnnouncement() {
        DataHelper.CreateNews firstAnnouncementNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryAnnouncement()).withDueDate(today).build();
        DataHelper.CreateNews secondAnnouncementNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryAnnouncement()).withDueDate(today).build();
        DataHelper.CreateNews thirdAnnouncementNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryAnnouncement()).withDueDate(today).build();
        DataHelper.CreateNews forthBirthdayNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryBirthday()).withDueDate(today).build();
        DataHelper.CreateNews fifthBirthdayNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryBirthday()).withDueDate(today).build();
        DataHelper.CreateNews sixthBirthdayNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryBirthday()).withDueDate(today).build();

        String categoryAnnouncement = "Объявление";
        String publishDateStartExpected = TestUtils.getDateToString(today);
        String publishDateEndExpected = TestUtils.getDateToString(today.plus(1, ChronoUnit.DAYS));

        //Создание новостей с категорией объявление для фильтрации
        //newsPageSteps.openControlPanel();
        controlPanelSteps.createNews(firstAnnouncementNews, secondAnnouncementNews, thirdAnnouncementNews,
                forthBirthdayNews, fifthBirthdayNews, sixthBirthdayNews);
        //Переходим в раздел Новости
        controlPanelSteps.isControlPanel();
        mainPageSteps.openNewsPageThroughTheMainMenu();
        newsPageSteps.isNewsPage();
        //Открываем форму фильтра и заполняем её
        newsPageSteps.openFilterNews();
        filterNewsPageSteps.isFilterNewsForm();
        filterNewsPageSteps.fillingOutTheFilterNewsForm(categoryAnnouncement, today, today.plus(1, ChronoUnit.DAYS));
        //Проверяем, что в полях формы отображаются введенные данные, а не другие/ На 06.12.2022 в поле дата отображалась неверная дата
        filterNewsPageSteps.getNewsFilterCategoryField().check(matches(withText(categoryAnnouncement)));
        filterNewsPageSteps.getNewsFilterPublishDateStartField().check(matches(withText(publishDateStartExpected)));
        filterNewsPageSteps.getNewsFilterPublishDateEndField().check(matches(withText(publishDateEndExpected)));
        //Включаем фильтрацию
        filterNewsPageSteps.filterNewsButtonClick();
        //Проверка, что отображаются новости с категорией Объявление
        newsPageSteps.isNewsPage();
        controlPanelSteps.checkNewsIsPresent(firstAnnouncementNews);
        controlPanelSteps.checkNewsIsPresent(secondAnnouncementNews);
        controlPanelSteps.checkNewsIsPresent(thirdAnnouncementNews);
        //Проверка, что новости с категорией День рождения не отображаются
        controlPanelSteps.checkNewsDoesNotPresent(forthBirthdayNews);
        controlPanelSteps.checkNewsDoesNotPresent(fifthBirthdayNews);
        controlPanelSteps.checkNewsDoesNotPresent(sixthBirthdayNews);
    }

    @Test
    @DisplayName("Фильтрация новостей по Категории День рождения")
    public void shouldFilterTheNewsWithCategoryBirthday() {
        DataHelper.CreateNews firstAnnouncementNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryAnnouncement()).withDueDate(today).build();
        DataHelper.CreateNews secondAnnouncementNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryAnnouncement()).withDueDate(today).build();
        DataHelper.CreateNews thirdAnnouncementNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryAnnouncement()).withDueDate(today).build();
        DataHelper.CreateNews forthBirthdayNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryBirthday()).withDueDate(today).build();
        DataHelper.CreateNews fifthBirthdayNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryBirthday()).withDueDate(today).build();
        DataHelper.CreateNews sixthBirthdayNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryBirthday()).withDueDate(today).build();

        String categoryBirthday = "День рождения";
        String publishDateStartExpected = TestUtils.getDateToString(today);
        String publishDateEndExpected = TestUtils.getDateToString(today.plus(1, ChronoUnit.DAYS));
        //Создание новостей с категорией объявление для фильтрации
        controlPanelSteps.createNews(firstAnnouncementNews, secondAnnouncementNews, thirdAnnouncementNews,
                forthBirthdayNews, fifthBirthdayNews, sixthBirthdayNews);
        //Переходим в раздел Новости
        controlPanelSteps.isControlPanel();
        mainPageSteps.openNewsPageThroughTheMainMenu();
        newsPageSteps.isNewsPage();
        //Открываем форму фильтра и заполняем её
        newsPageSteps.openFilterNews();
        filterNewsPageSteps.isFilterNewsForm();
        filterNewsPageSteps.fillingOutTheFilterNewsForm(categoryBirthday,
                today, today.plus(1, ChronoUnit.DAYS));
        //Проверяем, что в полях формы отображаются введенные данные, а не другие
        filterNewsPageSteps.getNewsFilterCategoryField().check(matches(withText(categoryBirthday)));
        filterNewsPageSteps.getNewsFilterPublishDateStartField().check(matches(withText(publishDateStartExpected)));
        filterNewsPageSteps.getNewsFilterPublishDateEndField().check(matches(withText(publishDateEndExpected)));
        //Включаем фильтрацию
        filterNewsPageSteps.filterNewsButtonClick();
        //Проверка, что отображаются новости с категорией День рождения
        newsPageSteps.isNewsPage();
        controlPanelSteps.checkNewsIsPresent(forthBirthdayNews);
        controlPanelSteps.checkNewsIsPresent(fifthBirthdayNews);
        controlPanelSteps.checkNewsIsPresent(sixthBirthdayNews);
        //Проверка, что новости с категорией День рождения не отображаются
        controlPanelSteps.checkNewsDoesNotPresent(firstAnnouncementNews);
        controlPanelSteps.checkNewsDoesNotPresent(secondAnnouncementNews);
        controlPanelSteps.checkNewsDoesNotPresent(thirdAnnouncementNews);
    }

    @Test
    @DisplayName("Фильтрация новостей по Категории Зарплата")
    public void shouldFilterTheNewsWithCategorySalary() {
        DataHelper.CreateNews firstSalaryNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategorySalary()).withDueDate(today).build();
        DataHelper.CreateNews secondSalaryNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategorySalary()).withDueDate(today).build();
        DataHelper.CreateNews thirdSalaryNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategorySalary()).withDueDate(today).build();
        DataHelper.CreateNews forthTradeUnionNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryTradeUnion()).withDueDate(today).build();
        DataHelper.CreateNews fifthTradeUnionNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryTradeUnion()).withDueDate(today).build();
        DataHelper.CreateNews sixthTradeUnionNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryTradeUnion()).withDueDate(today).build();

        String categorySalary = "Зарплата";
        String publishDateStartExpected = TestUtils.getDateToString(today);
        String publishDateEndExpected = TestUtils.getDateToString(today.plus(1, ChronoUnit.DAYS));
        //Создание новостей с категорией объявление для фильтрации
        controlPanelSteps.createNews(firstSalaryNews, secondSalaryNews, thirdSalaryNews,
                forthTradeUnionNews, fifthTradeUnionNews, sixthTradeUnionNews);

        //Переходим в раздел Новости
        controlPanelSteps.isControlPanel();
        mainPageSteps.openNewsPageThroughTheMainMenu();
        newsPageSteps.isNewsPage();
        //Открываем форму фильтра и заполняем её
        newsPageSteps.openFilterNews();
        filterNewsPageSteps.isFilterNewsForm();
        filterNewsPageSteps.fillingOutTheFilterNewsForm(categorySalary,
                today, today.plus(1, ChronoUnit.DAYS));
        //Проверяем, что в полях формы отображаются введенные данные, а не другие
        filterNewsPageSteps.getNewsFilterCategoryField().check(matches(withText(categorySalary)));
        filterNewsPageSteps.getNewsFilterPublishDateStartField().check(matches(withText(publishDateStartExpected)));
        filterNewsPageSteps.getNewsFilterPublishDateEndField().check(matches(withText(publishDateEndExpected)));
        //Включаем фильтрацию
        filterNewsPageSteps.filterNewsButtonClick();
        //Проверка, что отображаются новости с категорией Зарплата
        newsPageSteps.isNewsPage();
        controlPanelSteps.checkNewsIsPresent(firstSalaryNews);
        controlPanelSteps.checkNewsIsPresent(secondSalaryNews);
        controlPanelSteps.checkNewsIsPresent(thirdSalaryNews);
        //Проверка, что новости с категорией Профсоюз не отображаются
        controlPanelSteps.checkNewsDoesNotPresent(forthTradeUnionNews);
        controlPanelSteps.checkNewsDoesNotPresent(fifthTradeUnionNews);
        controlPanelSteps.checkNewsDoesNotPresent(sixthTradeUnionNews);
    }

    @Test
    @DisplayName("Фильтрация новостей по Категории Профсоюз")
    public void shouldFilterTheNewsWithCategoryTradeUnion() {
        DataHelper.CreateNews firstSalaryNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategorySalary()).withDueDate(today).build();
        DataHelper.CreateNews secondSalaryNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategorySalary()).withDueDate(today).build();
        DataHelper.CreateNews thirdSalaryNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategorySalary()).withDueDate(today).build();
        DataHelper.CreateNews forthTradeUnionNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryTradeUnion()).withDueDate(today).build();
        DataHelper.CreateNews fifthTradeUnionNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryTradeUnion()).withDueDate(today).build();
        DataHelper.CreateNews sixthTradeUnionNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryTradeUnion()).withDueDate(today).build();

        String categoryTradeUnion = "Профсоюз";
        String publishDateStartExpected = TestUtils.getDateToString(today);
        String publishDateEndExpected = TestUtils.getDateToString(today.plus(1, ChronoUnit.DAYS));
        //Создание новостей с категорией объявление для фильтрации
        controlPanelSteps.createNews(firstSalaryNews, secondSalaryNews, thirdSalaryNews,
                forthTradeUnionNews, fifthTradeUnionNews, sixthTradeUnionNews);

        //Переходим в раздел Новости
        controlPanelSteps.isControlPanel();
        mainPageSteps.openNewsPageThroughTheMainMenu();
        newsPageSteps.isNewsPage();
        //Открываем форму фильтра и заполняем её
        newsPageSteps.openFilterNews();
        filterNewsPageSteps.isFilterNewsForm();
        filterNewsPageSteps.fillingOutTheFilterNewsForm(categoryTradeUnion,
                today, today.plus(1, ChronoUnit.DAYS));
        //Проверяем, что в полях формы отображаются введенные данные, а не другие
        filterNewsPageSteps.getNewsFilterCategoryField().check(matches(withText(categoryTradeUnion)));
        filterNewsPageSteps.getNewsFilterPublishDateStartField().check(matches(withText(publishDateStartExpected)));
        filterNewsPageSteps.getNewsFilterPublishDateEndField().check(matches(withText(publishDateEndExpected)));
        //Включаем фильтрацию
        filterNewsPageSteps.filterNewsButtonClick();
        //Проверка, что отображаются новости с категорией Профсоюз
        newsPageSteps.isNewsPage();
        controlPanelSteps.checkNewsIsPresent(forthTradeUnionNews);
        controlPanelSteps.checkNewsIsPresent(fifthTradeUnionNews);
        controlPanelSteps.checkNewsIsPresent(sixthTradeUnionNews);
        //Проверка, что новости с категорией Зарплата не отображаются
        controlPanelSteps.checkNewsDoesNotPresent(firstSalaryNews);
        controlPanelSteps.checkNewsDoesNotPresent(secondSalaryNews);
        controlPanelSteps.checkNewsDoesNotPresent(thirdSalaryNews);
    }

    @Test
    @DisplayName("Фильтрация новостей по Категории Праздник")
    public void shouldFilterTheNewsWithCategoryHoliday() {
        DataHelper.CreateNews firstHolidayNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryHoliday()).withDueDate(today).build();
        DataHelper.CreateNews secondHolidayNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryHoliday()).withDueDate(today).build();
        DataHelper.CreateNews thirdHolidayNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryHoliday()).withDueDate(today).build();
        DataHelper.CreateNews forthMassageNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryMassage()).withDueDate(today).build();
        DataHelper.CreateNews fifthMassageNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryMassage()).withDueDate(today).build();
        DataHelper.CreateNews sixthMassageNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryMassage()).withDueDate(today).build();

        String categoryHoliday = "Праздник";
        String publishDateStartExpected = TestUtils.getDateToString(today);
        String publishDateEndExpected = TestUtils.getDateToString(today.plus(1, ChronoUnit.DAYS));
        //Создание новостей с категорией объявление для фильтрации
        controlPanelSteps.createNews(firstHolidayNews, secondHolidayNews, thirdHolidayNews,
                forthMassageNews, fifthMassageNews, sixthMassageNews);

        //Переходим в раздел Новости
        controlPanelSteps.isControlPanel();
        mainPageSteps.openNewsPageThroughTheMainMenu();
        newsPageSteps.isNewsPage();
        //Открываем форму фильтра и заполняем её
        newsPageSteps.openFilterNews();
        filterNewsPageSteps.isFilterNewsForm();
        filterNewsPageSteps.fillingOutTheFilterNewsForm(categoryHoliday,
                today, today.plus(1, ChronoUnit.DAYS));
        //Проверяем, что в полях формы отображаются введенные данные, а не другие
        filterNewsPageSteps.getNewsFilterCategoryField().check(matches(withText(categoryHoliday)));
        filterNewsPageSteps.getNewsFilterPublishDateStartField().check(matches(withText(publishDateStartExpected)));
        filterNewsPageSteps.getNewsFilterPublishDateEndField().check(matches(withText(publishDateEndExpected)));
        //Включаем фильтрацию
        filterNewsPageSteps.filterNewsButtonClick();
        //Проверка, что отображаются новости с категорией Праздник
        newsPageSteps.isNewsPage();
        controlPanelSteps.checkNewsIsPresent(firstHolidayNews);
        controlPanelSteps.checkNewsIsPresent(secondHolidayNews);
        controlPanelSteps.checkNewsIsPresent(thirdHolidayNews);
        //Проверка, что новости с категорией Массаж не отображаются
        controlPanelSteps.checkNewsDoesNotPresent(forthMassageNews);
        controlPanelSteps.checkNewsDoesNotPresent(fifthMassageNews);
        controlPanelSteps.checkNewsDoesNotPresent(sixthMassageNews);
    }


    @Test
    @DisplayName("Фильтрация новостей по Категории Массаж")
    public void shouldFilterTheNewsWithCategoryMassage() {
        DataHelper.CreateNews firstHolidayNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryHoliday()).withDueDate(today).build();
        DataHelper.CreateNews secondHolidayNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryHoliday()).withDueDate(today).build();
        DataHelper.CreateNews thirdHolidayNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryHoliday()).withDueDate(today).build();
        DataHelper.CreateNews forthMassageNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryMassage()).withDueDate(today).build();
        DataHelper.CreateNews fifthMassageNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryMassage()).withDueDate(today).build();
        DataHelper.CreateNews sixthMassageNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryMassage()).withDueDate(today).build();

        String categoryMassage = "Массаж";
        String publishDateStartExpected = TestUtils.getDateToString(today);
        String publishDateEndExpected = TestUtils.getDateToString(today.plus(1, ChronoUnit.DAYS));
        //Создание новостей с категорией объявление для фильтрации
        controlPanelSteps.createNews(firstHolidayNews, secondHolidayNews, thirdHolidayNews,
                forthMassageNews, fifthMassageNews, sixthMassageNews);
        //Переходим в раздел Новости
        controlPanelSteps.isControlPanel();
        mainPageSteps.openNewsPageThroughTheMainMenu();
        newsPageSteps.isNewsPage();
        //Открываем форму фильтра и заполняем её
        newsPageSteps.openFilterNews();
        filterNewsPageSteps.isFilterNewsForm();
        filterNewsPageSteps.fillingOutTheFilterNewsForm(categoryMassage,
                today, today.plus(1, ChronoUnit.DAYS));
        //Проверяем, что в полях формы отображаются введенные данные, а не другие
        filterNewsPageSteps.getNewsFilterCategoryField().check(matches(withText(categoryMassage)));
        filterNewsPageSteps.getNewsFilterPublishDateStartField().check(matches(withText(publishDateStartExpected)));
        filterNewsPageSteps.getNewsFilterPublishDateEndField().check(matches(withText(publishDateEndExpected)));
        //Включаем фильтрацию
        filterNewsPageSteps.filterNewsButtonClick();
        //Проверка, что отображаются новости с категорией Массаж
        newsPageSteps.isNewsPage();
        controlPanelSteps.checkNewsIsPresent(forthMassageNews);
        controlPanelSteps.checkNewsIsPresent(fifthMassageNews);
        controlPanelSteps.checkNewsIsPresent(sixthMassageNews);
        //Проверка, что новости с категорией Праздник не отображаются
        controlPanelSteps.checkNewsDoesNotPresent(firstHolidayNews);
        controlPanelSteps.checkNewsDoesNotPresent(secondHolidayNews);
        controlPanelSteps.checkNewsDoesNotPresent(thirdHolidayNews);
    }


    @Test
    @DisplayName("Фильтрация новостей по Категории Благодарность")
    public void shouldFilterTheNewsWithCategoryGratitude() {
        DataHelper.CreateNews firstGratitudeNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryGratitude()).withDueDate(today).build();
        DataHelper.CreateNews secondGratitudeNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryGratitude()).withDueDate(today).build();
        DataHelper.CreateNews thirdGratitudeNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryGratitude()).withDueDate(today).build();
        DataHelper.CreateNews forthNeedHelpNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryNeedHelp()).withDueDate(today).build();
        DataHelper.CreateNews fifthNeedHelpNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryNeedHelp()).withDueDate(today).build();
        DataHelper.CreateNews sixthNeedHelpNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryNeedHelp()).withDueDate(today).build();

        String categoryGratitude = "Благодарность";
        String publishDateStartExpected = TestUtils.getDateToString(today);
        String publishDateEndExpected = TestUtils.getDateToString(today.plus(1, ChronoUnit.DAYS));
        //Создание новостей с категорией объявление для фильтрации
        controlPanelSteps.createNews(firstGratitudeNews, secondGratitudeNews, thirdGratitudeNews,
                forthNeedHelpNews, fifthNeedHelpNews, sixthNeedHelpNews);

        //Переходим в раздел Новости
        controlPanelSteps.isControlPanel();
        mainPageSteps.openNewsPageThroughTheMainMenu();
        newsPageSteps.isNewsPage();
        //Открываем форму фильтра и заполняем её
        newsPageSteps.openFilterNews();
        filterNewsPageSteps.isFilterNewsForm();
        filterNewsPageSteps.fillingOutTheFilterNewsForm(categoryGratitude,
                today, today.plus(1, ChronoUnit.DAYS));
        //Проверяем, что в полях формы отображаются введенные данные, а не другие
        filterNewsPageSteps.getNewsFilterCategoryField().check(matches(withText(categoryGratitude)));
        filterNewsPageSteps.getNewsFilterPublishDateStartField().check(matches(withText(publishDateStartExpected)));
        filterNewsPageSteps.getNewsFilterPublishDateEndField().check(matches(withText(publishDateEndExpected)));
        //Включаем фильтрацию
        filterNewsPageSteps.filterNewsButtonClick();
        //Проверка, что отображаются новости с категорией Благодарность
        newsPageSteps.isNewsPage();
        controlPanelSteps.checkNewsIsPresent(firstGratitudeNews);
        controlPanelSteps.checkNewsIsPresent(secondGratitudeNews);
        controlPanelSteps.checkNewsIsPresent(thirdGratitudeNews);
        //Проверка, что новости с категорией Нужна помощь не отображаются
        controlPanelSteps.checkNewsDoesNotPresent(forthNeedHelpNews);
        controlPanelSteps.checkNewsDoesNotPresent(fifthNeedHelpNews);
        controlPanelSteps.checkNewsDoesNotPresent(sixthNeedHelpNews);
    }


    @Test
    @DisplayName("Фильтрация новостей по Категории Нужна помощь")
    public void shouldFilterTheNewsWithCategoryNeedHelp() {
        DataHelper.CreateNews firstGratitudeNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryGratitude()).withDueDate(today).build();
        DataHelper.CreateNews secondGratitudeNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryGratitude()).withDueDate(today).build();
        DataHelper.CreateNews thirdGratitudeNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryGratitude()).withDueDate(today).build();
        DataHelper.CreateNews forthNeedHelpNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryNeedHelp()).withDueDate(today).build();
        DataHelper.CreateNews fifthNeedHelpNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryNeedHelp()).withDueDate(today).build();
        DataHelper.CreateNews sixthNeedHelpNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryNeedHelp()).withDueDate(today).build();

        String categoryNeedHelp = "Нужна помощь";
        String publishDateStartExpected = TestUtils.getDateToString(today);
        String publishDateEndExpected = TestUtils.getDateToString(today.plus(1, ChronoUnit.DAYS));
        //Создание новостей с категорией объявление для фильтрации
        controlPanelSteps.createNews(firstGratitudeNews, secondGratitudeNews, thirdGratitudeNews,
                forthNeedHelpNews, fifthNeedHelpNews, sixthNeedHelpNews);
        //Переходим в раздел Новости
        controlPanelSteps.isControlPanel();
        mainPageSteps.openNewsPageThroughTheMainMenu();
        newsPageSteps.isNewsPage();
        //Открываем форму фильтра и заполняем её
        newsPageSteps.openFilterNews();
        filterNewsPageSteps.isFilterNewsForm();
        filterNewsPageSteps.fillingOutTheFilterNewsForm(categoryNeedHelp,
                today, today.plus(1, ChronoUnit.DAYS));
        //Проверяем, что в полях формы отображаются введенные данные, а не другие
        filterNewsPageSteps.getNewsFilterCategoryField().check(matches(withText(categoryNeedHelp)));
        filterNewsPageSteps.getNewsFilterPublishDateStartField().check(matches(withText(publishDateStartExpected)));
        filterNewsPageSteps.getNewsFilterPublishDateEndField().check(matches(withText(publishDateEndExpected)));
        //Включаем фильтрацию
        filterNewsPageSteps.filterNewsButtonClick();
        //Проверка, что отображаются новости с категорией Нужна помощь
        newsPageSteps.isNewsPage();
        controlPanelSteps.checkNewsIsPresent(forthNeedHelpNews);
        controlPanelSteps.checkNewsIsPresent(fifthNeedHelpNews);
        controlPanelSteps.checkNewsIsPresent(sixthNeedHelpNews);
        //Проверка, что новости с категорией Благодарность не отображаются
        controlPanelSteps.checkNewsDoesNotPresent(firstGratitudeNews);
        controlPanelSteps.checkNewsDoesNotPresent(secondGratitudeNews);
        controlPanelSteps.checkNewsDoesNotPresent(thirdGratitudeNews);
    }


    @Test
    @DisplayName("Отмена филтрации новостей")
    public void shouldNotFilterNewsByCategoryAnnouncement() {
        DataHelper.CreateNews firstAnnouncementNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryAnnouncement()).withDueDate(today).build();
        DataHelper.CreateNews secondBirthdayNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryBirthday()).withDueDate(today).build();
        DataHelper.CreateNews thirdHolidayNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryHoliday()).withDueDate(today).build();
        DataHelper.CreateNews forthMassageNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryMassage()).withDueDate(today).build();
        DataHelper.CreateNews fifthGratitudeNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryGratitude()).withDueDate(today).build();
        DataHelper.CreateNews sixthTradeUnionNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryTradeUnion()).withDueDate(today).build();

        String categoryAnnouncement = "Объявление";
        String publishDateStartExpected = TestUtils.getDateToString(today);
        String publishDateEndExpected = TestUtils.getDateToString(today.plus(1, ChronoUnit.DAYS));
        //Создание новостей с категорией объявление для фильтрации
        controlPanelSteps.createNews(firstAnnouncementNews, secondBirthdayNews, thirdHolidayNews,
                forthMassageNews, fifthGratitudeNews, sixthTradeUnionNews);
        //Переходим в раздел Новости
        controlPanelSteps.isControlPanel();
        mainPageSteps.openNewsPageThroughTheMainMenu();
        newsPageSteps.isNewsPage();
        //Открываем форму фильтра и заполняем её
        newsPageSteps.openFilterNews();
        filterNewsPageSteps.isFilterNewsForm();
        filterNewsPageSteps.fillingOutTheFilterNewsForm(categoryAnnouncement,
                today, today.plus(1, ChronoUnit.DAYS));
        //Проверяем, что в полях формы отображаются введенные данные, а не другие
        filterNewsPageSteps.getNewsFilterCategoryField().check(matches(withText(categoryAnnouncement)));
        filterNewsPageSteps.getNewsFilterPublishDateStartField().check(matches(withText(publishDateStartExpected)));
        filterNewsPageSteps.getNewsFilterPublishDateEndField().check(matches(withText(publishDateEndExpected)));
        //Отменяем фильтрацию
        filterNewsPageSteps.cancelFilterNewsButtonClick();
        //Проверка, что фильтр не включился и отображаются все новости
        newsPageSteps.isNewsPage();
        controlPanelSteps.checkNewsIsPresent(firstAnnouncementNews);
        controlPanelSteps.checkNewsIsPresent(secondBirthdayNews);
        controlPanelSteps.checkNewsIsPresent(thirdHolidayNews);
        controlPanelSteps.checkNewsIsPresent(forthMassageNews);
        controlPanelSteps.checkNewsIsPresent(fifthGratitudeNews);
        controlPanelSteps.checkNewsIsPresent(sixthTradeUnionNews);
    }


    @Test
    @DisplayName("Фильтрация новостей по несуществующей категории")
    public void shouldShowAMessageSelectACategoryFromTheList() {
        String myCategoryTitle = "Моя категория";
        //Открываем форму фильтра и заполняем её
        newsPageSteps.openFilterNews();
        filterNewsPageSteps.isFilterNewsForm();
        controlPanelSteps.replaceNewsCategoryText(myCategoryTitle);
        filterNewsPageSteps.setDateToDatePicker(filterNewsPageSteps.newsItemPublishDateStartField,
                today);
        controlPanelSteps.okButtonClick();
        filterNewsPageSteps.setDateToDatePicker(filterNewsPageSteps.newsItemPublishDateEndField,
                today.plus(1, ChronoUnit.DAYS));
        controlPanelSteps.okButtonClick();
        filterNewsPageSteps.filterNewsButtonClick();
        //Проверка, что появляется сообщение
        controlPanelSteps.checkToast("Invalid category. Select a category from the list.", true);
    }


    @Test
    @DisplayName("Фильрация актуальных новостей за период из будущего")
    public void shouldShowATextThereIsNothingHere() {
        DataHelper.CreateNews firstAnnouncementNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryAnnouncement()).withDueDate(today.plus(1, ChronoUnit.MONTHS)).build();

        String categoryAnnouncement = "Объявление";
        String publishDateStartExpected = TestUtils.getDateToString(today.plus(1, ChronoUnit.MONTHS));
        String publishDateEndExpected = TestUtils.getDateToString(today.plus(2, ChronoUnit.MONTHS));
        //Создаем новость с датой публикации в будущем
        newsPageSteps.openControlPanel();
        controlPanelSteps.creatingNews(firstAnnouncementNews);
        //Переходим в раздел Новости
        controlPanelSteps.isControlPanel();
        mainPageSteps.openNewsPageThroughTheMainMenu();
        newsPageSteps.isNewsPage();
        //Открываем форму фильтра и заполняем её
        newsPageSteps.openFilterNews();
        filterNewsPageSteps.isFilterNewsForm();
        filterNewsPageSteps.fillingOutTheFilterNewsForm(categoryAnnouncement,
                today.plus(1, ChronoUnit.MONTHS),
                today.plus(2, ChronoUnit.MONTHS));
        //Проверяем, что в полях формы отображаются введенные данные, а не другие
        filterNewsPageSteps.getNewsFilterCategoryField().check(matches(withText(categoryAnnouncement)));
        filterNewsPageSteps.getNewsFilterPublishDateStartField().check(matches(withText(publishDateStartExpected)));
        filterNewsPageSteps.getNewsFilterPublishDateEndField().check(matches(withText(publishDateEndExpected)));
        //Включаем фильтрацию
        filterNewsPageSteps.filterNewsButtonClick();
        //Проверка, что отображается кнопка REFRESH, текст и картинка пустого списка новостей
        newsPageSteps.isEmptyNewsList();
    }


    @Test
    @DisplayName("Фильтрация новостей без заданного периода")
    public void shouldShowAllActualNewsCategoryAnnouncement() {
        DataHelper.CreateNews firstAnnouncementNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryAnnouncement()).withDueDate(today.minus(1, ChronoUnit.MONTHS)).build();
        DataHelper.CreateNews secondAnnouncementNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryAnnouncement()).withDueDate(today.minus(10, ChronoUnit.DAYS)).build();
        DataHelper.CreateNews thirdAnnouncementNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryAnnouncement()).withDueDate(today).build();

        String categoryAnnouncement = "Объявление";

        //Создаем новость
        controlPanelSteps.createNews(firstAnnouncementNews, secondAnnouncementNews, thirdAnnouncementNews);

        //Переходим в раздел Новости
        controlPanelSteps.isControlPanel();
        mainPageSteps.openNewsPageThroughTheMainMenu();
        newsPageSteps.isNewsPage();
        //Открываем форму фильтра и заполняем её
        newsPageSteps.openFilterNews();
        filterNewsPageSteps.isFilterNewsForm();
        controlPanelSteps.selectANewsCategoryFromTheList(categoryAnnouncement);
        filterNewsPageSteps.filterNewsButtonClick();
        //Проверка, что отображаются все актуальные новости категории Объявление
        newsPageSteps.isNewsPage();
        controlPanelSteps.checkNewsIsPresent(firstAnnouncementNews);
        controlPanelSteps.checkNewsIsPresent(secondAnnouncementNews);
        controlPanelSteps.checkNewsIsPresent(thirdAnnouncementNews);
    }

    @Test
    @DisplayName("Фильтрация новости с заданным периодом от и незаданным периодом до")
    public void shouldShowMessageWrongPeriodPublishDateEndField() {
        String categoryAnnouncement = "Объявление";
        //Открываем форму фильтра и заполняем её
        newsPageSteps.openFilterNews();
        filterNewsPageSteps.isFilterNewsForm();
        controlPanelSteps.selectANewsCategoryFromTheList(categoryAnnouncement);
        filterNewsPageSteps.setDateToDatePicker(filterNewsPageSteps.newsItemPublishDateStartField,
                today.minus(1, ChronoUnit.MONTHS));
        controlPanelSteps.okButtonClick();
        filterNewsPageSteps.filterNewsButtonClick();
        //Проверка сообщения "Wrong period"
        filterNewsPageSteps.isMessageWrongPeriod();
    }

    @Test
    @DisplayName("Фильтрация новости с заданным периодом до и незаданным периодом от")
    public void shouldShowMessageWrongPeriodWithEmptyPublishDateStartField() {
        String categoryAnnouncement = "Объявление";
        //Открываем форму фильтра и заполняем её
        newsPageSteps.openFilterNews();
        filterNewsPageSteps.isFilterNewsForm();
        controlPanelSteps.selectANewsCategoryFromTheList(categoryAnnouncement);
        filterNewsPageSteps.setDateToDatePicker(filterNewsPageSteps.newsItemPublishDateEndField,
                today);
        controlPanelSteps.okButtonClick();
        filterNewsPageSteps.filterNewsButtonClick();
        //Проверка сообщения "Wrong period"
        filterNewsPageSteps.isMessageWrongPeriod();
    }


}
