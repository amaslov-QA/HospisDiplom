package ru.iteco.fmhandroid.ui;

import androidx.test.espresso.PerformException;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.junit4.DisplayName;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.steps.AuthSteps;
import ru.iteco.fmhandroid.ui.steps.MainPageSteps;

import static ru.iteco.fmhandroid.ui.data.DataHelper.invalidAuthInfo;

import android.os.RemoteException;

@RunWith(AllureAndroidJUnit4.class)

public class AuthPageInvalidTest extends BaseTest {
    private UiDevice device;

    private static AuthSteps authSteps = new AuthSteps();
    private static MainPageSteps mainPageSteps = new MainPageSteps();

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
    }

    @Test
    @DisplayName("Вход с невалидными данными")
    public void shouldNotLogInWithInvalidData() {
        authSteps.authWithInvalidData(invalidAuthInfo());
        authSteps.checkToast(R.string.wrong_login_or_password, true);
    }

    @Test
    @DisplayName("Вход с пустыми полями")
    public void shouldNotLogInWithEmptyFields() {
        authSteps.authWithEmptyFields();
        authSteps.checkToast(R.string.empty_login_or_password, true);
    }
}
