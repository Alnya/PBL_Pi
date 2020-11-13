/*
 PBL Group 2.
 This is a program to calculate the value of Pi.
*/


// ���������l������BigDecimal�A�����ۂ߂邽�߂�RoundingMode��import.

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PBL02 {

    //    100��:  6��̎��s�񐔂Ŋ��S����  0.0025 sec.
    //   1000��:  9��̎��s�񐔂Ŋ��S����  0.024 sec.
    //  10000��: 12��̎��s�񐔂Ŋ��S����  0.88 sec.
    // 100000��: 16��̎��s�񐔂Ŋ��S����  42 sec.

    private static final int count = 20;
    // ���Z�񐔂���

    private static final int digit = 10000;
    // ���߂�����������


    private static final int digits = digit + 5;
    // ���߂錅����菭�����������Ōv�Z���邽��

    private static BigDecimal ansTmp = BigDecimal.ZERO;
    // ��O�̉��Z���ʂ��i�[���邽�߂̕ϐ�

    private static int calculateCount = 0;
    // ���Z�񐔂�\������Ƃ��Ɏg�p

    private static long timeStart;
    // ���Z�J�n���Ԃ������ɋL�^

    private static BigDecimal a = BigDecimal.ONE;
    // BigDecimal�^�̏����l1����

    private static BigDecimal b = sqrt(BigDecimal.valueOf(2)).divide(
            BigDecimal.valueOf(2), digits, RoundingMode.HALF_EVEN);
    /*
     BigDecimal�^�̏����l�𕽕����v�Z�Ə��v�Z�ő��
     ���x��digits�AHALF_EVEN�Ŋۂߏ����B
     */

    private static BigDecimal p = BigDecimal.ONE;
    //BigDecimal�^�̏����l1����

    private static BigDecimal t = BigDecimal.valueOf(0.25);
    // BigDecimal�^�̏����l0.25����


    public static void main(String[] args) {

        timeStart = System.nanoTime();
        // ���Z�J�n���Ԃ��L�^

        BigDecimal ans = calculateRepetition();
        // BigDecimal�^�ōŏI�I�Ȍv�Z���ʂ�ans�֑��

        long timeEnd = System.nanoTime();
        // ���Z�̏I�����Ԃ��L�^

        double timeElapsed = (timeEnd - timeStart) / ((double) 1000000000);
        // ���Z���Ԃ��v�Z���Anano sec����sec�֒���

        System.out.println(ans);
        // ans���o��

        System.out.println("���Z��: " + calculateCount + " ��");
        // ���Z�񐔂��o��

        System.out.println("���Z����: " + timeElapsed + " sec.");
        // ���Z���Ԃ��o��

        if (calculateCount > 0) {
            // ���Z���ʂ�����������
            System.out.println("���������߂������_�ȉ�����: " + digit + "��");
        } else {
            // ���Z���ʂ��������Ȃ�������
            System.out.println("���Z���ʂ̎������m�F�ł��܂���ł����B");
        }

        System.out.println("�o�͂��������_�ȉ�����: " + (digit + 1) + "��");
        // �o�͂��������_�ȉ��������o��
    }

    public static BigDecimal calculateRepetition() {
        /*
         calculateRepetition�ł́AGauss=Legendre�̃A���S���Y����for���̃��[�v�ŉ��Z���܂��B
         Progress(i);�̃R�����g�A�E�g���������ƂŁA�v�Z�̓r���o�߂�\�������邱�Ƃ��o���܂��B
         */

        for (int i = 1; i <= count; i++) {
            // count�񉉎Z���܂�

            BigDecimal y = a;
            // BigDecimal�^�ŁAa�̑ޔ�p�ϐ�y��錾���܂�

            a = a.add(b).divide(BigDecimal.valueOf(2), digits, RoundingMode.HALF_EVEN);
            /*
             a = (a+b)/2
             ���x��digits�AHALF_EVEN�Ŋۂߏ����B
            */

            b = sqrt(b.multiply(y));
            // b = ��(b*y)

            BigDecimal tmp = y.subtract(a);
            tmp = tmp.pow(2).multiply(p);
            t = t.subtract(tmp);
            // t = t - p*(y - a)^2

            p = p.multiply(BigDecimal.valueOf(2));
            // p = 2p


            Progress(i);
            // �r���o�߂�\��


            BigDecimal fragAnsTmp = finalProcessing();
            // �����_�ł̂̍ŏI���Z���ʂ��i�[

            if (ansTmp.equals(fragAnsTmp)) {
                /*
                 ��O�̉��Z���ʂƓ����l�Ȃ�A�������I�����A
                 ���������ŏI�I�Ȓl��Ԃ��A
                 ���ۂɎ��������񐔂�calculateCount�ɑ��
                 */

                calculateCount = i - 1;
                return ansTmp;
            } else {
                /*
                 �������Ă��Ȃ��Ȃ�΁A
                 �����_�ł̍ŏI���Z���ʂ�ansTmp�Ɋi�[���A
                 �����𑱍s����
                 */
                ansTmp = fragAnsTmp;
            }
        }

        calculateCount = -count;
        /*
         count����Ŏ������Ȃ�������A
         calculateCount�ɕ��̒l��������
         */

        return ansTmp;
        // �ŏI�������s���A�������Ă��Ȃ��b��I�Ȓl��Ԃ��B
    }

    public static BigDecimal sqrt(BigDecimal num) {
        /*
         BigDecimal�^�̕������v�Z�����邽�߂̊֐�
         */

        BigDecimal tmp1 = BigDecimal.ZERO;
        // BigDecimal�^�̏����l0����

        BigDecimal tmp2 = BigDecimal.valueOf(Math.sqrt(num.doubleValue()));
        // BigDecimal�^�ŁA�{���x�̏����l��num����

        while (!tmp1.equals(tmp2)) {
            /*
             tmp1��tmp2�������l�ɂȂ�܂ŌJ��Ԃ�
             */

            tmp1 = tmp2;
            tmp2 = num.divide(tmp1, digits, RoundingMode.HALF_UP);
            /*
             tmp2 = num / tmp1
             ���x��digits�A�؂�グ�B
             */

            tmp2 = tmp2.add(tmp1);
            // tmp2 += tmp1

            tmp2 = tmp2.divide(BigDecimal.valueOf(2), digits, RoundingMode.HALF_UP);
            /*
             tmp2 /= 2
             ���x��digits�A�؂�グ�B
             */
        }

        return tmp2;
        // ���x��digits��BigDecimal�^�̉��Z���ʂ�Ԃ�
    }

    public static BigDecimal finalProcessing() {
        /*
         �ŏI�������s���֐�
         �ԋp�l��BigDecimal�^�A���x��digits�AHALF_EVEN�Ŋۂߏ����B
        */

        BigDecimal tmp = a.add(b);
        tmp = tmp.pow(2);
        tmp = tmp.divide(BigDecimal.valueOf(4), digits, RoundingMode.HALF_EVEN);
        tmp = tmp.divide(t, digits, RoundingMode.HALF_EVEN);
        /*
         tmp = (a+b)^2 / 4*t
         ���x��digits�AHALF_EVEN�Ŋۂߏ����B
        */

        tmp = tmp.setScale(digit + 1, RoundingMode.HALF_EVEN);
        // ���x��digit+1�AHALF_EVEN�Ŋۂߏ����B

        return tmp;
        // �ŏI�I�ȉ��Z���ʂ�Ԃ�
    }

    public static void Progress(int i) {
        /*
         ���Z�̐i���󋵂��o�͂���֐�
        */
        if (i % (count / 10) == 0) {
            /*
             count���\�������Đi�����o��
            */

            // BigDecimal tmp = finalProcessing();
            // System.out.println("count:" + i + "    " + tmp);
            // ���Z�r���̉��Z���ʂ�\��

            long timeEnd = System.nanoTime();
            // ���̎��_�ł̎��Ԃ��i�[

            double timeElapsed = (timeEnd - timeStart) / ((double) 1000000000);
            // ���̎��_�ł̌o�ߎ�����sec�ɕϊ����Čv�Z

            System.out.println("-----�r���o��----- count:=" + i + " �o�ߎ���:" + timeElapsed + " sec.");
            // ���݂̉��Z�񐔂ƌo�ߎ��Ԃ��o��
        }
    }
}