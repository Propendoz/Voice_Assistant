package com.example.lab1;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.util.Consumer;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.temporal.ChronoUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

class AI {

    private static HashMap<String, String> questionAnswer= new HashMap<>();

    @RequiresApi(api = Build.VERSION_CODES.O)

    static void getAnswer(String question, final Consumer<String> callback) throws ParseException {

        question = question.toUpperCase();
        final String[] answer = {"Вопрос понял. Думаю..."};

        if (question.contains("ПРИВЕТ")) {
            answer[0] = "Привет";
            callback.accept(String.join(", ", answer[0]));
        }
        else if (question.contains("КАК ДЕЛА")){
            answer[0] = "Неплохо";
            callback.accept(String.join(", ", answer[0]));
        }
        else if (question.contains("ЧЕМ ЗАНИМАЕШЬСЯ")) {
            answer[0] = "Отвечаю на вопросы";
            callback.accept(String.join(", ", answer[0]));
        }
        else if (question.contains("КАКОЙ СЕГОДНЯ ДЕНЬ")){
            answer[0] = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            callback.accept(String.join(", ", answer[0]));
        }
        else if(question.contains("КОТОРЫЙ ЧАС")){
            answer[0] = new SimpleDateFormat("HH:mm:ss").format(new Date());
            callback.accept(String.join(", ", answer[0]));
        }
        else if(question.contains("КАКОЙ ДЕНЬ НЕДЕЛИ")){
            answer[0] = new SimpleDateFormat("EEEE").format(new Date());
            callback.accept(String.join(", ", answer[0]));
        }
        else if(question.contains("СКОЛЬКО ДНЕЙ ДО ЛЕТА")){
            //2020-04-10

            LocalDate date = LocalDate.of(2020, 06, 01);
            LocalDate now = LocalDate.now();
            long countDay = ChronoUnit.DAYS.between(now, date);

            answer[0] = Long.toString(countDay);
            callback.accept(String.join(", ", answer[0]));
        }
        else if (question.contains("ПОГОДА В ГОРОДЕ")){
            Pattern cityPattern = Pattern.compile("ПОГОДА В ГОРОДЕ (\\p{L}+)",
                    Pattern.CASE_INSENSITIVE);
            Matcher matcher = cityPattern.matcher(question);
            if (matcher.find()){
                final String cityName = matcher.group(1);
                String finalQuestion = question;
                ForecastToString.getForecast(cityName, new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        if (s!=null) {
                            answer[0] = s;
                            questionAnswer.put(finalQuestion, answer[0]);
                            callback.accept(answer[0]);
                        } else{
                            answer[0] = "Не знаю я, какая там погода у вас в городе " + cityName;
                            questionAnswer.put(finalQuestion, answer[0]);
                            callback.accept(answer[0]);
                        }
                    }
                });

            }

        }
        else if (question.contains("ПЕРЕВОД ЧИСЛА")){
            final  String number = question.replaceAll("[^0-9\\+]", "");
            String finalQuestion = question;
            ConvertNumberToString.getConvertNumber(number, new java.util.function.Consumer<String>() {
                @Override
                public void accept(String s) {
                    if (s != null) {
                        answer[0] = s;
                        questionAnswer.put(finalQuestion, answer[0]);
                        callback.accept(answer[0]);
                    } else {
                        answer[0] = "Не могу перевести. Отстаньте от меня!";
                        questionAnswer.put(finalQuestion, answer[0]);
                        callback.accept(answer[0]);
                    }
                }

            });
        }
        else if (question.contains("ПРАЗДНИК")){
            String findDate = getDate(question);
            String finalQuestion = question;

/*            AsyncTask at = new AsyncTask<String, Integer, Void>(){

                @Override
                protected Void doInBackground(String... strings) {
                    answer[0] = "";
                    for (int i = 0; i < strings.length; i++) {
                        try {
                            answer[0] += strings[i] +": " + ParsingHtmlService.getHoliday(strings[i]) + "\n";

                        } catch (IOException e) {
                            answer[0] = "Не могу сказать. Отстаньте от меня!";
                        }
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void result){
                    super.onPostExecute(result);
                    questionAnswer.put(finalQuestion, answer[0]);
                    callback.accept(answer[0]);
                }
             };

             at.execute(findDate.split(","));*/

                String[] strings = findDate.split(",");

            Observable.fromCallable(() ->{
                answer[0] = "";
                for (int i = 0; i < strings.length; i++) {
                    try {
                        answer[0] += strings[i] +": " + ParsingHtmlService.getHoliday(strings[i]) + "\n";

                    } catch (IOException e) {
                        answer[0] = "Не могу сказать. Отстаньте от меня!";
                    }
                }
                return answer[0];
                    })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe((result)->{
                questionAnswer.put(finalQuestion, answer[0]);
                callback.accept(answer[0]);

          });
        }
    }

    static String getStr(String text) {
        return text.replaceAll("[\\s]{1,}", " ");
    }

    static String getDegreeEnding(int b) {
        int a = Math.abs(b);
        if(a>=11 && a <=14)
            return " градусов ";
        if(a%10==0 || a%10 >=5 && a%10 <=9)
            return " градусов ";
        if(a%10 == 1)
            return " градус ";
        if(a >= 2 && a <=4)
            return " градуса ";
        return "";
    }
    
    static String getDate(String question) throws ParseException {
        String date = "";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat =  new SimpleDateFormat("dd MMMM YYYY", myDateFormatSymbols);

        if(question.contains("СЕГОДНЯ")){
            Date today = calendar.getTime();
            date = dateFormat.format(today) + ",";
        }
        if(question.contains("ЗАВТРА")){
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            Date tomorrow = calendar.getTime();
            date += dateFormat.format(tomorrow) + ",";
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }
        if(question.contains("ВЧЕРА")){
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            Date yesterday = calendar.getTime();
            date += dateFormat.format(yesterday) + ",";
            calendar.add(Calendar.DAY_OF_YEAR, +1);
        }
        String pattern="\\d{1,2}\\.\\d{1,2}\\.\\d{4}";
        Matcher matcher=Pattern.compile(pattern).matcher(question);
        String tempDate = null;
        if(!matcher.find()) {
            tempDate = null;
        }else {
            tempDate = question.substring(matcher.start(), matcher.end());
        }
        if (tempDate != null) {
            Date currentDate = new SimpleDateFormat("dd.MM.yyyy").parse(tempDate);
            date += dateFormat.format(currentDate);
        }
        return  date;
    }

    private static DateFormatSymbols myDateFormatSymbols = new DateFormatSymbols(){

        @Override
        public String[] getMonths() {
            return new String[]{"января", "февраля", "марта", "апреля", "мая", "июня",
                    "июля", "августа", "сентября", "октября", "ноября", "декабря"};
        }

    };
}
