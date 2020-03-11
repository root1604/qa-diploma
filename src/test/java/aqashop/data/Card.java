package aqashop.data;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Data
@AllArgsConstructor
public class Card {
    String number;
    String month;
    String year;
    String holder;
    String cvc;

    public static String approvedCard = "4444 4444 4444 4441";
    public static String declinedCard = "4444 4444 4444 4442";

    @Step("Генерация действительной карты (статус карты \"APPROVED\")")
    public static Card generateApprovedCard(String local) {
        Faker faker = new Faker(new Locale(local));
        return new Card(approvedCard,
                getMonthNumber(createMonthsList()),
                String.valueOf(LocalDate.now().plusYears(getRandomInt()).getYear()).substring(2),
                faker.name().fullName(),
                faker.numerify("###"));
    }

    @Step("Генерация недействительной карты (статус карты \"DECLINED\")")
    public static Card generateDeclinedCard(String local) {
        Faker faker = new Faker(new Locale(local));
        return new Card(declinedCard,
                getMonthNumber(createMonthsList()),
                String.valueOf(LocalDate.now().plusYears(getRandomInt()).getYear()).substring(2),
                faker.name().fullName(),
                faker.numerify("###"));
    }

    public static List<String> createMonthsList() {
        List<String> monthsList = Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
        return monthsList;
    }

    public static String getMonthNumber(List<String> list) {
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }

    public static int getRandomInt() {
        Random random = new Random();
        return random.nextInt(5) + 1;
    }
}
