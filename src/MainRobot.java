import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;


/**
 * генерирует текст generateRoute("RLRFR", 100); +
 * считает количество команд поворота направо (буквы 'R'); +
 * выводит на экран результат. +
 * Количество потоков равно количеству генерируемых маршрутов и равно 1000. +
 * Самое частое количество повторений 61 (встретилось 9 раз) +
 * Другие размеры: +
 * - 60 (5 раз) +
 * - 64 (3 раз)
 * - 62 (6 раз)
 */

public class MainRobot {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    static int max = -1, resultR, maxR = 0;

    public static void main(String[] args) throws InterruptedException {
        String[] route = new String[1000];                          // String[1000];   массив строк
        Runnable flowLogic = () -> {
            for (int i = 0; i < route.length; i++) {
                route[i] = generateRoute("RLRFR", 100);  // заполняю массив
                resultR = countingTurns(route[i]);                           // подчёт R
                System.out.println(" " + route[i].substring(0, 25) + "\t: -->" + resultR); // вывод
                check(resultR);
            }
        };

        for (int i = 0; i < route.length; i++) {           // запускаю цикл запуска потоков
            Thread thread = new Thread(flowLogic);
            thread.start();
            thread.join();
        }
        mapSorting(sizeToFreq);
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static Integer countingTurns(String line) {
        return Math.toIntExact(line.chars().filter(ch -> ch == 'R').count());
    }

    public static void check(int key) {      // key  51 повтор R --> value   1 раз
        synchronized (sizeToFreq) {
            if (!sizeToFreq.containsKey(key)) {
                sizeToFreq.put(key, 1);                      // записать по ключу key  значение  1
                max = sizeToFreq.get(key) > max ? sizeToFreq.get(key) : max;
            } else {
                sizeToFreq.put(key, (sizeToFreq.get(key) + 1));
                if (sizeToFreq.get(key) > max) {
                    max = sizeToFreq.get(key);
                    maxR = key;
                }
            }
        }
    }

    public static void mapSorting(Map<Integer, Integer> map) {
        int i = 1;
        Set<Map.Entry<Integer, Integer>> mapEntries = map.entrySet();
        for (Map.Entry<Integer, Integer> pair : mapEntries) {
            Integer key = pair.getKey(),
                    value = pair.getValue();
            if (i == 1) {
                System.out.printf("\nСамое частое количество повторений %d (встретилось %d раз)\n" +
                        " \tДругие размеры:\n", maxR, max);
                i++;
            }
            if (maxR == key) {
                continue;
            }
            System.out.printf("\t-  %d  ( %d )\n ", key, value);
        }
    }

}
