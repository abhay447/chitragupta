package com.chitragupta.commons;

import java.util.function.Consumer;

public class BasicRetryUtils {

    public static <T> void runWithRetries(int maxRetries, Consumer<T> t, T arg) throws InterruptedException {
        int count = 0;
        while (count < maxRetries) {
            try {
                t.accept(arg);
                System.out.println("succeeded at attempt : "+ count+1);
                return;
            } catch (Exception e) {
                count += 1;
                System.out.println("failed at attempt : "+ count);
                if (count >= maxRetries){
                    throw e;
                }
                int retrySleepSeconds = (int) Math.exp(count);
                Thread.sleep(retrySleepSeconds * 1000L);
            }
        }
    }
}
