package ru.iteco.fmhandroid.ui.data;


import android.os.IBinder;

import android.view.WindowManager;

import androidx.test.espresso.Root;


import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;


import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class DataHelper {


    private DataHelper() {
    }


    public static class AuthInfo {
        private final String login;
        private final String pass;

        public AuthInfo(String login, String pass) {
            this.login = login;
            this.pass = pass;
        }

        public String getLogin() {
            return login;
        }

        public String getPass() {
            return pass;
        }
    }

    public static AuthInfo authInfo() {
        String login = "login2";
        String pass = "password2";
        return new AuthInfo(login, pass);
    }

    public static AuthInfo invalidAuthInfo() {
        String login = "invalid";
        String pass = "invalid";
        return new AuthInfo(login, pass);
    }

    public static class ToastMatcher extends TypeSafeMatcher<Root> {

        @Override
        public void describeTo(Description description) {
            description.appendText("is toast");
        }

        @Override
        public boolean matchesSafely(Root root) {
            int type = root.getWindowLayoutParams().get().type;
            if ((type == WindowManager.LayoutParams.TYPE_TOAST)) {
                IBinder windowToken = root.getDecorView().getWindowToken();
                IBinder appToken = root.getDecorView().getApplicationWindowToken();
                if (windowToken == appToken) {
                    return true;
                }
            }
            return false;
        }
    }

    public static String generateTitleId() {
        String titleId = UUID.randomUUID().toString();
        return titleId;
    }

    public static LocalDateTime generateDate(int plusDays) {
        return LocalDateTime.now().plusDays(plusDays);
    }

    public static LocalDateTime getValidDate() {
        int randomNumber = ThreadLocalRandom.current().nextInt(1, 368);
        LocalDateTime date = generateDate(randomNumber);
        return date;
    }


    public static String getTitle() {
        return "Заголовок" + " " + generateTitleId();
    }

    public static String getClaimTitleWithASpace() {
        return " Заголовок" + " " + generateTitleId();
    }

    public static String getClaimTitle50Characters() {
        return "Заголовок" + " " + DataHelper.generateTitleId() + " " + "50з";
    }

    public static String getClaimTitle49Characters() {
        return "Заголовок" + " " + DataHelper.generateTitleId() + " " + "49";
    }

    public static String getClaimTitle51Characters() {
        return "Заголовок" + " " + DataHelper.generateTitleId() + " " + "51зн";
    }

    public static String getDescription() {
        return "Описание" + " " + generateTitleId();
    }

    public static String getExecutorIvanov() {
        return "Иванов Данил Данилович";
    }

    public static String getExecutorSmirnov() {
        return "Смирнов Петр Петрович";
    }

    public static String getCategoryAnnouncement() {
        return "Объявление";
    }

    public static String getCategoryBirthday() {
        return "День рождения";
    }

    public static String getCategorySalary() {
        return "Зарплата";
    }

    public static String getCategoryTradeUnion() {
        return "Профсоюз";
    }

    public static String getCategoryHoliday() {
        return "Праздник";
    }

    public static String getCategoryGratitude() {
        return "Благодарность";
    }

    public static String getCategoryMassage() {
        return "Массаж";
    }

    public static String getCategoryNeedHelp() {
        return "Нужна помощь";
    }


    public static String getComment() {
        return "Комментарий" + " " + DataHelper.generateTitleId();
    }

    public static class CreateClaim {
        public String status;
        public LocalDateTime dueDate;
        public String name;
        public String description;
        public String executorName;

        private CreateClaim(Builder builder) {
            status = builder.status;
            dueDate = builder.dueDate;
            name = builder.name;
            description = builder.description;
            executorName = builder.executorName;
        }

        public static class Builder {
            //Обязательные параметры
            private String name;
            private String description;

            //Необязательные параметры
            private String status = ClaimStatus.OPEN;
            private LocalDateTime dueDate = LocalDateTime.now();
            private String executorName = null;

            public Builder(String claimTitle, String claimDescription) {
                this.name = claimTitle;
                this.description = claimDescription;
            }

            public Builder() {

            }


            public Builder withStatus(String val) {
                this.status = val;
                return this;
            }

            public Builder withDueDate(LocalDateTime val) {
                this.dueDate = val;
                return this;
            }

            public Builder withName(String name) {
                this.name = name;
                return this;
            }

            public Builder withDescription(String description) {
                this.description = description;
                return this;
            }

            public Builder withExecutor(String executorName) {
                this.executorName = executorName;
                return this;
            }


            public Builder(String status, LocalDateTime dueDate, String name, String description, String executorName) {
                this.status = status;
                this.dueDate = dueDate;
                this.name = name;
                this.description = description;
                this.executorName = executorName;
            }

            public CreateClaim build() {
                return new CreateClaim(this);
            }
        }

        public String getClaimStatus() {
            return status;
        }

        public LocalDateTime getDueDate() {
            return dueDate;
        }

        public String getClaimName() {
            return name;
        }

        public String getClaimDescription() {
            return description;
        }

        public String getExecutorName() {
            return executorName;
        }


    }

    public static class ClaimStatus {
        public static final String OPEN = "Open";
        public static final String INPROGRESS = "In progress";
        public static final String EXECUTED = "Executed";
        public static final String CANCELED = "Canceled";
    }


    public static CreateClaim.Builder claimWithRandomNameAndDescription() {
        CreateClaim.Builder createClaim = new CreateClaim.Builder(getTitle(), getDescription());
        return createClaim;
    }

    public static CreateClaim.Builder claim() {
        CreateClaim.Builder createClaim = new CreateClaim.Builder();
        return createClaim;
    }

    public static class CreateNews {
        public String category;
        public LocalDateTime dueDate;
        public String name;
        public String description;

        public CreateNews(Builder builder) {
            category = builder.category;
            dueDate = builder.dueDate;
            name = builder.name;
            description = builder.description;
        }


        public static class Builder {
            //Обязательные параметры
            private String name;
            private String description;

            //Необязательные параметры
            private String status = ClaimStatus.OPEN;
            private LocalDateTime dueDate = LocalDateTime.now();
            private String category = DataHelper.getCategoryAnnouncement();


            public Builder(String newsTitle, String newsDescription) {
                this.name = newsTitle;
                this.description = newsDescription;
            }

            public Builder() {

            }

            public Builder withCategory(String val) {
                this.category = val;
                return this;
            }

            public Builder withDueDate(LocalDateTime val) {
                this.dueDate = val;
                return this;
            }

            public Builder withName(String name) {
                this.name = name;
                return this;
            }

            public Builder withDescription(String description) {
                this.description = description;
                return this;
            }

            public Builder(String category, LocalDateTime dueDate, String name, String description) {
                this.category = category;
                this.dueDate = dueDate;
                this.name = name;
                this.description = description;
            }

            public CreateNews build() {
                return new CreateNews(this);
            }
        }

        public String getNewsCategory() {
            return category;
        }

        public LocalDateTime getDueDate() {
            return dueDate;
        }

        public String getNewsName() {
            return name;
        }

        public String getNewsDescription() {
            return description;
        }

    }

    public static CreateNews.Builder newsWithRandomNameAndDescription() {
        CreateNews.Builder createNews = new CreateNews.Builder(getTitle(), getDescription());
        return createNews;
    }

    public static CreateNews.Builder news() {
        CreateNews.Builder createNews = new CreateNews.Builder();
        return createNews;
    }


}