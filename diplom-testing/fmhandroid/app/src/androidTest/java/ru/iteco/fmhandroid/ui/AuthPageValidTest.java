package ru.iteco.fmhandroid.ui;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.os.RemoteException;

import androidx.test.espresso.PerformException;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObjectNotFoundException;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.junit4.DisplayName;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.iteco.fmhandroid.ui.data.TestUtils;
import ru.iteco.fmhandroid.ui.steps.AuthSteps;
import ru.iteco.fmhandroid.ui.steps.ControlPanelSteps;
import ru.iteco.fmhandroid.ui.steps.MainPageSteps;

import static ru.iteco.fmhandroid.ui.data.DataHelper.authInfo;


@RunWith(AllureAndroidJUnit4.class)

public class AuthPageValidTest extends BaseTest {
    private UiDevice device;

    private static AuthSteps authSteps = new AuthSteps();
    private static MainPageSteps mainPageSteps = new MainPageSteps();
    private static ControlPanelSteps controlPanelSteps = new ControlPanelSteps();


    @Before
    public void logoutCheck() throws RemoteException {
        device =
                UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        try {
            authSteps.isAuthScreen();
        } catch (PerformException e) {
            mainPageSteps.clickLogOutBut();
        }
    }

    @After
    public void disableAirplaneMode() throws RemoteException, UiObjectNotFoundException {
        device.setOrientationNatural();
        TestUtils.disableAirplaneMode();
    }

    @Test
    @DisplayName("Проверка элементов экрана авторизации")
    public void shouldCheckAuthPageElements() {
        authSteps.isAuthScreen();
    }

    @Test
    @DisplayName("Вход с валидными данными")
    public void shouldLogInWithValidData() {
        authSteps.authWithValidData(authInfo());
        mainPageSteps.isMainPage();
    }

    @Test
    @DisplayName("Разрыв соединения во время авторизации")
    public void shouldShowAToastSomethingWrong() throws UiObjectNotFoundException {
        //Вводим логин и пароль
        authSteps.enterAValidUsernameAndPassword(authInfo());
        //Включаем режим В самолете
        authSteps.turnOnAirplaneMode();
        //Нажимаем кнопку signIn
        authSteps.signBtnClick();
        //Проверяем, что отображается сообщение
        controlPanelSteps.checkToast("Something went wrong. Try again later.", true);
        //Отключаем режим в самолете
        TestUtils.disableAirplaneMode();
    }

    @Test
    @DisplayName("Поворот экрана во время авторизации")
    public void shouldSaveDataOnTheAuthPageOnScreenRotation() throws UiObjectNotFoundException, RemoteException {
        String expectedLogin = "login2";
        String expectedPassword = "•••••••••";
        //Вводим логин и пароль
        authSteps.enterAValidUsernameAndPassword(authInfo());
        device.setOrientationLeft();
        //Проверяем, что введенные данные сохранились
        authSteps.getLoginText().check(matches(withText("login2")));
        authSteps.getPasswordText().check(matches(withText("•••••••••")));
    }
}
