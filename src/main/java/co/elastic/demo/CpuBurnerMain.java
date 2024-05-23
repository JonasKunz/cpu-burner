package co.elastic.demo;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.instrumentation.annotations.WithSpan;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class CpuBurnerMain {

    private static final Tracer tracer = GlobalOpenTelemetry.getTracer("manual-tracer");
    public static volatile Object memoryDrain;

    public static volatile Object sink;

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while (true) {
                try {
                    fibonacciFun();
                } catch (Exception e) {}
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("Stopping fibonacci transactions due to interrupt");
                    break;
                }
            }
        });


        Thread t2 = new Thread(() -> {
            while (true) {
                shaShenanigans();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("Stopping sha transactions due to interrupt");
                    break;
                }
            }
        });

        Thread t3 = new Thread(() -> {
            while (true) {
                try {
                    backgroundAllocate();
                } catch (Exception e) {}
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        Thread[] threads = {
                t1,
                t2,
                t3
        };
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }

    private static void backgroundAllocate() {
        for (int i=0; i<200; i++) {
            memoryDrain = new byte[512*1024];
        }
    }

    public static void fibonacciFun() {
        Span span = tracer.spanBuilder("fibonacciFun").startSpan();
        try (var scope = span.makeCurrent()) {
            long start = System.nanoTime();
            while ((System.nanoTime() - start) < 100_000_000L) {
                sink = computeFibonacci();
            }
        } finally {
            span.end();
        }
    }

    public static void shaShenanigans() {
        Span span = tracer.spanBuilder("shaShenanigans").startSpan();
        try (var scope = span.makeCurrent()) {
            long start = System.nanoTime();
            while ((System.nanoTime() - start) < 100_000_000L) {
                sink = hashRandomStuff();
            }
        } finally {
            span.end();
        }
    }

    @WithSpan
    private static byte[] hashRandomStuff() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            Random rnd = new Random();
            byte[] buffer = new byte[1024];
            for (int i = 0; i < 5000; i++) {
                rnd.nextBytes(buffer);
                digest.update(buffer);
            }
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    @WithSpan
    private static long computeFibonacci() {
        return fibonacci(35);
    }

    private static long fibonacci(long n) {
        if(n == 0) return 0;
        if(n == 1) return 1;
        return fibonacci(n-1) + fibonacci(n - 2);
    }
}
