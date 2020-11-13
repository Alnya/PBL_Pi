/*
 PBL Group 2.
 This is a program to calculate the value of Pi.
*/


// 長桁数を考慮してBigDecimal、桁を丸めるためのRoundingModeをimport.

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PBL02 {

    //    100桁:  6回の試行回数で完全収束  0.0025 sec.
    //   1000桁:  9回の試行回数で完全収束  0.024 sec.
    //  10000桁: 12回の試行回数で完全収束  0.88 sec.
    // 100000桁: 16回の試行回数で完全収束  42 sec.

    private static final int count = 20;
    // 演算回数を代入

    private static final int digit = 10000;
    // 求めたい桁数を代入


    private static final int digits = digit + 5;
    // 求める桁数より少し多い桁数で計算するため

    private static BigDecimal ansTmp = BigDecimal.ZERO;
    // 一つ前の演算結果を格納するための変数

    private static int calculateCount = 0;
    // 演算回数を表示するときに使用

    private static long timeStart;
    // 演算開始時間をここに記録

    private static BigDecimal a = BigDecimal.ONE;
    // BigDecimal型の初期値1を代入

    private static BigDecimal b = sqrt(BigDecimal.valueOf(2)).divide(
            BigDecimal.valueOf(2), digits, RoundingMode.HALF_EVEN);
    /*
     BigDecimal型の初期値を平方根計算と除計算で代入
     精度はdigits、HALF_EVENで丸め処理。
     */

    private static BigDecimal p = BigDecimal.ONE;
    //BigDecimal型の初期値1を代入

    private static BigDecimal t = BigDecimal.valueOf(0.25);
    // BigDecimal型の初期値0.25を代入


    public static void main(String[] args) {

        timeStart = System.nanoTime();
        // 演算開始時間を記録

        BigDecimal ans = calculateRepetition();
        // BigDecimal型で最終的な計算結果をansへ代入

        long timeEnd = System.nanoTime();
        // 演算の終了時間を記録

        double timeElapsed = (timeEnd - timeStart) / ((double) 1000000000);
        // 演算時間を計算し、nano secからsecへ直す

        System.out.println(ans);
        // ansを出力

        System.out.println("演算回数: " + calculateCount + " 回");
        // 演算回数を出力

        System.out.println("演算時間: " + timeElapsed + " sec.");
        // 演算時間を出力

        if (calculateCount > 0) {
            // 演算結果が収束したら
            System.out.println("正しく求めた小数点以下桁数: " + digit + "桁");
        } else {
            // 演算結果が収束しなかったら
            System.out.println("演算結果の収束が確認できませんでした。");
        }

        System.out.println("出力した小数点以下桁数: " + (digit + 1) + "桁");
        // 出力した小数点以下桁数を出力
    }

    public static BigDecimal calculateRepetition() {
        /*
         calculateRepetitionでは、Gauss=Legendreのアルゴリズムをfor文のループで演算します。
         Progress(i);のコメントアウトを消すことで、計算の途中経過を表示させることも出来ます。
         */

        for (int i = 1; i <= count; i++) {
            // count回演算します

            BigDecimal y = a;
            // BigDecimal型で、aの退避用変数yを宣言します

            a = a.add(b).divide(BigDecimal.valueOf(2), digits, RoundingMode.HALF_EVEN);
            /*
             a = (a+b)/2
             精度はdigits、HALF_EVENで丸め処理。
            */

            b = sqrt(b.multiply(y));
            // b = √(b*y)

            BigDecimal tmp = y.subtract(a);
            tmp = tmp.pow(2).multiply(p);
            t = t.subtract(tmp);
            // t = t - p*(y - a)^2

            p = p.multiply(BigDecimal.valueOf(2));
            // p = 2p


            Progress(i);
            // 途中経過を表示


            BigDecimal fragAnsTmp = finalProcessing();
            // 現時点でのの最終演算結果を格納

            if (ansTmp.equals(fragAnsTmp)) {
                /*
                 一つ前の演算結果と同じ値なら、処理を終了し、
                 収束した最終的な値を返し、
                 実際に収束した回数をcalculateCountに代入
                 */

                calculateCount = i - 1;
                return ansTmp;
            } else {
                /*
                 収束していないならば、
                 現時点での最終演算結果をansTmpに格納し、
                 処理を続行する
                 */
                ansTmp = fragAnsTmp;
            }
        }

        calculateCount = -count;
        /*
         count回内で収束しなかったら、
         calculateCountに負の値を代入する
         */

        return ansTmp;
        // 最終処理を行い、収束していない暫定的な値を返す。
    }

    public static BigDecimal sqrt(BigDecimal num) {
        /*
         BigDecimal型の平方根計算をするための関数
         */

        BigDecimal tmp1 = BigDecimal.ZERO;
        // BigDecimal型の初期値0を代入

        BigDecimal tmp2 = BigDecimal.valueOf(Math.sqrt(num.doubleValue()));
        // BigDecimal型で、倍精度の初期値√numを代入

        while (!tmp1.equals(tmp2)) {
            /*
             tmp1とtmp2が同じ値になるまで繰り返し
             */

            tmp1 = tmp2;
            tmp2 = num.divide(tmp1, digits, RoundingMode.HALF_UP);
            /*
             tmp2 = num / tmp1
             精度はdigits、切り上げ。
             */

            tmp2 = tmp2.add(tmp1);
            // tmp2 += tmp1

            tmp2 = tmp2.divide(BigDecimal.valueOf(2), digits, RoundingMode.HALF_UP);
            /*
             tmp2 /= 2
             精度はdigits、切り上げ。
             */
        }

        return tmp2;
        // 精度がdigitsのBigDecimal型の演算結果を返す
    }

    public static BigDecimal finalProcessing() {
        /*
         最終処理を行う関数
         返却値はBigDecimal型、精度はdigits、HALF_EVENで丸め処理。
        */

        BigDecimal tmp = a.add(b);
        tmp = tmp.pow(2);
        tmp = tmp.divide(BigDecimal.valueOf(4), digits, RoundingMode.HALF_EVEN);
        tmp = tmp.divide(t, digits, RoundingMode.HALF_EVEN);
        /*
         tmp = (a+b)^2 / 4*t
         精度はdigits、HALF_EVENで丸め処理。
        */

        tmp = tmp.setScale(digit + 1, RoundingMode.HALF_EVEN);
        // 精度はdigit+1、HALF_EVENで丸め処理。

        return tmp;
        // 最終的な演算結果を返す
    }

    public static void Progress(int i) {
        /*
         演算の進捗状況を出力する関数
        */
        if (i % (count / 10) == 0) {
            /*
             countを十等分して進捗を出力
            */

            // BigDecimal tmp = finalProcessing();
            // System.out.println("count:" + i + "    " + tmp);
            // 演算途中の演算結果を表示

            long timeEnd = System.nanoTime();
            // この時点での時間を格納

            double timeElapsed = (timeEnd - timeStart) / ((double) 1000000000);
            // この時点での経過時刻をsecに変換して計算

            System.out.println("-----途中経過----- count:=" + i + " 経過時間:" + timeElapsed + " sec.");
            // 現在の演算回数と経過時間を出力
        }
    }
}