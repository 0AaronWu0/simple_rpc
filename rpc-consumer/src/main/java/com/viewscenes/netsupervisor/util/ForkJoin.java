package com.viewscenes.netsupervisor.util;

import java.util.stream.LongStream;

/**
 * Title: ForkJoin
 * Description: TODO
 *
 * @author gejx
 * @version V1.0
 * @date 2019-06-30
 */
public class ForkJoin {
    public static void main(String[] args) {

        long reduce = LongStream.rangeClosed(0, 10000000000L).parallel().reduce(0, Long::sum);
        System.out.println(reduce);
    }
}
