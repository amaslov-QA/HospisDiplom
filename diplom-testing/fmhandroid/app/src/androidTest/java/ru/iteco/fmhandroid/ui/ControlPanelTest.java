package ru.iteco.fmhandroid.ui;

import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static ru.iteco.fmhandroid.ui.data.DataHelper.authInfo;

import android.os.RemoteException;

import io.qameta.allure.kotlin.junit4.DisplayName;

import androidx.test.espresso.PerformException;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.data.TestUtils;
import ru.iteco.fmhandroid.ui.steps.AuthSteps;
import ru.iteco.fmhandroid.ui.steps.ControlPanelSteps;
import ru.iteco.fmhandroid.ui.steps.FilterNewsPageSteps;
import ru.iteco.fmhandroid.ui.steps.MainPageSteps;
import ru.iteco.fmhandroid.ui.steps.NewsPageSteps;

@RunWith(AllureAndroidJUnit4.class)

public class ControlPanelTest extends BaseTest {

    private UiDevice device;
    private static AuthSteps authSteps = new AuthSteps();
    private static MainPageSteps mainPageSteps = new MainPageSteps();
    private static NewsPageSteps newsPageSteps = new NewsPageSteps();
    private static FilterNewsPageSteps filterNewsPageSteps = new FilterNewsPageSteps();
    private static ControlPanelSteps controlPanelSteps = new ControlPanelSteps();


    LocalDateTime today = LocalDateTime.now();

    @Before
    public void logoutCheckAndOpenControlPanelPage() throws RemoteException, UiObjectNotFoundException {
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
    }

    @After
    public void disableAirplaneMode() throws RemoteException, UiObjectNotFoundException {
        device.setOrientationNatural();
        TestUtils.disableAirplaneMode();
    }

    @Test
    @DisplayName("Открытие формы создания новости")
    public void shouldOpenCreateNewsForm() {
        controlPanelSteps.openCreatingNewsForm();
        controlPanelSteps.isCreatingNewsForm();
    }

    @Test
    @DisplayName("Отмена удаления новости во вкладке Панель управления")
    public void shouldNotRemoveTheNewsItem() {
        DataHelper.CreateNews announcementNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryAnnouncement()).withDueDate(today).build();

        //Создаем новость для теста
        controlPanelSteps.creatingNews(announcementNews);
        //Находим нашу новость
        controlPanelSteps.scrollToElementInRecyclerList(announcementNews.getNewsName()).check(matches(isDisplayed()));
        //Нажимаем на кнопку удалить в карточке новости
        controlPanelSteps.getItemNewsDeleteElement(announcementNews.getNewsName()).perform(click());
        //Отображается сообщение об удалении
        controlPanelSteps.getMessageAboutDelete().check(matches(isDisplayed()));
        //Отменяем удаление
        controlPanelSteps.cancelDeleteButtonClick();
        //Проверяем, что наша новость осталась в списке
        controlPanelSteps.checkNewsIsPresent(announcementNews);
    }

    @Test
    @DisplayName("Открытие Новости для редактирования")
    public void shouldOpenTheNewsForEditing() {
        DataHelper.CreateNews announcementNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryAnnouncement()).withDueDate(today).build();

        //Создаем новость для теста
        controlPanelSteps.creatingNews(announcementNews);

        //Нажимаем на кнопку Редактировать в карточке новости
        controlPanelSteps.openNewsCard(announcementNews);
        controlPanelSteps.isCardTestNews(announcementNews.getNewsName(), announcementNews.getNewsDescription());
        pressBack();
    }

    @Test
    @DisplayName("Открытие и закрытие Новости для редактирования без внесения изменений")
    public void shouldNotEditTheNews() {
        DataHelper.CreateNews announcementNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryAnnouncement()).withDueDate(today).build();

        //Создаем новость для теста
        controlPanelSteps.creatingNews(announcementNews);
        //Нажимаем на кнопку Редактировать в карточке новости
        controlPanelSteps.openNewsCard(announcementNews);
        //Проверяем, что октрылась наща новость
        controlPanelSteps.isCardTestNews(announcementNews.getNewsName(), announcementNews.getNewsDescription());
        //Отменяем редактирование новости
        controlPanelSteps.cancelButtonClick();
        //Отображается сообщение, что изменения не будут сохранены
        controlPanelSteps.getMessageChangesWonTBeSaved().check(matches(isDisplayed()));
        controlPanelSteps.okButtonClick();
        //Проверяем, что наша новость есть в списке
        controlPanelSteps.checkNewsIsPresent(announcementNews);
    }

    @Test
    @DisplayName("Открытие и сохранение Новости для редактирования без внесения изменений")
    public void shouldKeepTheNewsUnchanged() {
        DataHelper.CreateNews announcementNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryAnnouncement()).withDueDate(today).build();

        //Создаем новость для теста
        controlPanelSteps.creatingNews(announcementNews);
        //Нажимаем на кнопку Редактировать в карточке новости
        controlPanelSteps.openNewsCard(announcementNews);
        //Проверяем, что октрылась наща новость
        controlPanelSteps.isCardTestNews(announcementNews.getNewsName(), announcementNews.getNewsDescription());
        //Сохраняем новость без изменений
        controlPanelSteps.saveButtonClick();
        //Проверяем, что наша новость есть в списке
        controlPanelSteps.checkNewsIsPresent(announcementNews);
    }

    @Test
    @DisplayName("Выключение Активного статуса у Новости")
    public void shouldTurnOffActiveStatus() {
        DataHelper.CreateNews announcementNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryAnnouncement()).withDueDate(today).build();

        //Создаем новость для теста
        controlPanelSteps.creatingNews(announcementNews);
        //Открываем новость для редактирования
        controlPanelSteps.openNewsCard(announcementNews);
        //Проверяем, что октрылась наща новость
        controlPanelSteps.isCardTestNews(announcementNews.getNewsName(), announcementNews.getNewsDescription());
        //Переключаем статус новости из Active в NotActive
        controlPanelSteps.switchNewsStatus();
        controlPanelSteps.getSwitcherNoteActive().check(matches(isDisplayed()));
        //Сохраняем
        controlPanelSteps.saveButtonClick();
        //Проверяем, что наша новость отображается в списке
        controlPanelSteps.checkNewsIsPresent(announcementNews);
    }

    @Test
    @DisplayName("Редактирование даты публикации")
    public void shouldChangeThePublicationDate() {
        String dateExpected = TestUtils.getDateToString(today.plus(1, ChronoUnit.DAYS));

        DataHelper.CreateNews announcementNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryAnnouncement()).withDueDate(today).build();

        //Создаем новость для теста
        controlPanelSteps.creatingNews(announcementNews);
        //Открываем новость для редактирования
        controlPanelSteps.openNewsCard(announcementNews);
        //Проверяем, что октрылась наща новость
        controlPanelSteps.isCardTestNews(announcementNews.getNewsName(), announcementNews.getNewsDescription());
        //Редактируем дату публикации
        controlPanelSteps.setDateToDatePicker(today.plus(1, ChronoUnit.DAYS));
        controlPanelSteps.okButtonClick();
        //Проверяем, что в поле Дата публикации отображается новая дата
        controlPanelSteps.getNewsItemPublishDate().check(matches(withText(dateExpected)));
        //Сохраняем изменения
        controlPanelSteps.saveButtonClick();
        //Проверяем, что наша новость есть в списке
        controlPanelSteps.checkNewsIsPresent(announcementNews);
    }

    @Test
    @DisplayName("Редактирование описания Новости")
    public void shouldChangeTheDescription() {
        DataHelper.CreateNews announcementNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryAnnouncement()).withDueDate(today).build();
        String newDescription = announcementNews.getNewsDescription() + " проверка";

        //Создаем новость для теста
        controlPanelSteps.creatingNews(announcementNews);
        //Открываем новость для редактирования
        controlPanelSteps.openNewsCard(announcementNews);
        //Проверяем, что октрылась наша новость
        controlPanelSteps.isCardTestNews(announcementNews.getNewsName(), announcementNews.getNewsDescription());
        //Редактируем описание новости
        controlPanelSteps.getNewsItemDescription().perform(replaceText(newDescription));
        //Сохраняем изменения
        controlPanelSteps.saveButtonClick();
        //Проверяем, что наша новость есть в списке
        controlPanelSteps.checkNewsIsPresent(announcementNews);
        //Разворачиваем карточку новости и проверяем, что она имеет новое Описание
        controlPanelSteps.openNewsCard(announcementNews);
        controlPanelSteps.getNewsItemDescription().check(matches(withText(newDescription)));
    }

    @Test
    @DisplayName("Редактирование времени публикации Новости")
    public void shouldChangeThePublicationTime() {
        String timeExpected = TestUtils.getTimeToString(today.plus(1, ChronoUnit.HOURS));

        DataHelper.CreateNews announcementNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryAnnouncement()).withDueDate(today).build();

        //Создаем новость для теста
        controlPanelSteps.creatingNews(announcementNews);

        //Открываем новость для редактирования
        controlPanelSteps.openNewsCard(announcementNews);
        //Проверяем, что открылась наша новость
        controlPanelSteps.isCardTestNews(announcementNews.getNewsName(), announcementNews.getNewsDescription());
        //Редактируем время публикации
        controlPanelSteps.setTimeToTimeField(today.plus(1, ChronoUnit.HOURS));
        //Сохраняем изменения
        controlPanelSteps.saveButtonClick();

        //Открываем новость для проверки сохранилось ли измененное время публикации
        controlPanelSteps.openNewsCard(announcementNews);
        controlPanelSteps.getNewsItemPublishTime().check(matches(withText(timeExpected)));

        pressBack();
    }

    @Test
    @DisplayName("Редактирование заголовка Новости")
    public void shouldChangeNewsTitle() {
        DataHelper.CreateNews announcementNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryAnnouncement()).withDueDate(today).build();

        String newTitle = announcementNews.getNewsName() + " проверка";

        //Создаем новость для теста
        controlPanelSteps.creatingNews(announcementNews);
        //Открываем новость для редактирования
        controlPanelSteps.openNewsCard(announcementNews);
        controlPanelSteps.isCardTestNews(announcementNews.getNewsName(), announcementNews.getNewsDescription());
        //Редактируем заголовок
        controlPanelSteps.getNewsItemTitle().perform(replaceText(newTitle));
        //Сохраняем измененную новость
        controlPanelSteps.saveButtonClick();
        //Проверяем что наша новость с новым заголовком отображается в списке
        controlPanelSteps.scrollToElementInRecyclerList(newTitle).check(matches(isDisplayed()));
        controlPanelSteps.getItemNewsEditElement(newTitle).perform(click());
        controlPanelSteps.getNewsItemTitle().check(matches(withText(newTitle)));
    }

    @Ignore
    //Тест работает нестабильно. В режиме дебага все проходит. Падает при попытке сменить категорию
    @Test
    @DisplayName("Редактирование Категории Новости")
    public void shouldChangeNewsCategory() {
        DataHelper.CreateNews changeCategoryNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryAnnouncement()).withDueDate(today).build();
        DataHelper.CreateNews newChangeCategoryNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryBirthday()).withDueDate(today).build();

        //Создаем новость для теста
        controlPanelSteps.creatingNews(changeCategoryNews);
        //Открываем новость для редактирования
        controlPanelSteps.openNewsCard(changeCategoryNews);
        controlPanelSteps.isCardTestNews(changeCategoryNews.getNewsName(), changeCategoryNews.getNewsDescription());
        //Меняем категорию
        controlPanelSteps.selectANewsCategoryFromTheList(DataHelper.getCategoryBirthday());
        //Сохраняем изменения
        controlPanelSteps.saveButtonClick();
        //Проверяем что наша новость с новой категорией есть в списке
        controlPanelSteps.checkNewsIsPresent(newChangeCategoryNews);
        controlPanelSteps.openNewsCard(changeCategoryNews);
        controlPanelSteps.getNewsItemCategory().check(matches(withText(DataHelper.getCategoryBirthday())));

        pressBack();
    }

    @Test
    @DisplayName("Просмотр описания новости из вкладки Панель управления раздела Новости")
    public void shouldOpenNewsDescription() {
        DataHelper.CreateNews announcementNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryAnnouncement()).withDueDate(today).build();

        //Создаем новость для теста
        controlPanelSteps.creatingNews(announcementNews);
        //Находим нашу новость
        controlPanelSteps.checkNewsIsPresent(announcementNews);
        //Разворачиваем карточку новости и проверяем, что отображается описание
        controlPanelSteps.openNewsDescription(announcementNews);
        controlPanelSteps.getItemNewsDescriptionElement(announcementNews.getNewsDescription()).check(matches(isDisplayed()));
    }

    @Test
    @DisplayName("Удаление новости во вкладке Панель управления")
    public void shouldDeleteNewsItem() {
        DataHelper.CreateNews announcementNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryAnnouncement()).withDueDate(today).build();

        //Создаем новость для теста
        controlPanelSteps.creatingNews(announcementNews);
        //Находим нашу новость
        controlPanelSteps.checkNewsIsPresent(announcementNews);
        //Удаляем новость
        controlPanelSteps.deleteItemNews(announcementNews.getNewsName());
        //Проверяем, что новость удалилась
        controlPanelSteps.checkNewsDoesNotPresent(announcementNews);
    }

    @Test
    @DisplayName("Открытие фильтра новостей по кнопке Filter во вкладке Control panel")
    public void shouldOpenTheNewsFilterSettingsForm() {
        newsPageSteps.openFilterNews();
        filterNewsPageSteps.isFilterNewsFormControlPanel();
    }

    @Test
    @DisplayName("Разрыв соединения в разделе Control panel")
    public void shouldShowDialogWindowSomethingWrong() throws UiObjectNotFoundException {
        DataHelper.CreateNews announcementNews = DataHelper.newsWithRandomNameAndDescription()
                .withCategory(DataHelper.getCategoryAnnouncement()).withDueDate(today).build();
        //Нажимаем кнопку добавить новость
        controlPanelSteps.openCreatingNewsForm();
        controlPanelSteps.fillingOutTheFormCreatingNewsWithDate(announcementNews);
        //Включаем режим В самолете
        device.openQuickSettings();
        device.findObject(new UiSelector().description("Airplane mode")).click();
        device.pressBack();
        device.pressBack();
        //Сохраняем Новость
        controlPanelSteps.saveButtonClick();
        //Проверяем, что отображается сообщение
        controlPanelSteps.isDialogWindowMessageSavingFailed();
        //Отключаем режим в самолете
        TestUtils.disableAirplaneMode();
    }

}
