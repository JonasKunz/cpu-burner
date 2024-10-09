package co.elastic.demo;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import org.bouncycastle.jcajce.provider.digest.SHA3;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class CpuBurnerMain {


    public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException {
        CPULoad.getInstance().execute(true);
        Thread.sleep(999999999);
    }


}
