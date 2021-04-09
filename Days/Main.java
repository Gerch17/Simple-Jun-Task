package Days;

import javax.swing.text.DateFormatter;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Boolean isCorrect = true;
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime birthDate = LocalDateTime.now();
        System.out.println("Введите дату в формате гггг/ММ/дд");
        while (isCorrect) {
            String birthday = sc.nextLine();
            try{
                birthDate = LocalDateTime.parse(birthday + "T18:24:33.312956");
                if(birthDate.isAfter(LocalDateTime.now())){
                    System.out.println("Некорректный ввод. Попробуйте ещё раз");
                }else {
                    isCorrect = false;
                }
            }catch (Exception e){
                System.out.println("Некорректный ввод. Попробуйте ещё раз");
            }
        }
        birthDate.plusYears(LocalDate.now().getYear() - birthDate.getYear());
        if(birthDate.isBefore(LocalDateTime.now())){
            birthDate.plusYears(1);
        }
        int days = (int) Duration.between(birthDate, LocalDateTime.now()).getSeconds();
        System.out.println(days/86400);
    }
}
