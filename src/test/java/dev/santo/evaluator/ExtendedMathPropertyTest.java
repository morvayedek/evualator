package dev.santo.evaluator;

import java.math.BigInteger;
import java.util.Random;

/** Independent identities and distribution normalisation for numerical extensions. */
public final class ExtendedMathPropertyTest {
    private static int checks;
    public static void main(String[] args) {
        var e = new Evaluator();
        var random = new Random(20261018);
        for (int i=0; i<200; i++) {
            double x=random.nextDouble()*20-10;
            close(e.evaluate("asinh(sinh("+x+"))"),x);
            close(e.evaluate("atanh(tanh("+x/2+"))"),x/2);
            double p=.001+random.nextDouble()*.998;
            close(e.evaluate("logistic(logit("+p+"))"),p);
            double temperature=random.nextDouble()*1000-200;
            close(e.evaluate("fahrenheitToCelsius(celsiusToFahrenheit("+temperature+"))"),temperature);
            double a=random.nextDouble()*100, b=random.nextDouble()*100;
            close(e.evaluate("weightedMean("+a+",1,"+b+",1)"),e.evaluate("mean("+a+","+b+")"));
            close(e.evaluate("quantile(.5,"+a+","+b+")"),e.evaluate("median("+b+","+a+")"));
            double radius=random.nextDouble()*10, angle=random.nextDouble()*6;
            close(e.evaluate("hypot(polarX("+radius+","+angle+"),polarY("+radius+","+angle+"))"),radius);
        }
        for(int n=0;n<=30;n++) {
            double sum=0;
            for(int k=0;k<=n;k++) sum+=e.evaluate("binomialProbability("+n+","+k+",.37)");
            close(sum,1);
        }
        double poissonSum=0;
        for(int k=0;k<=100;k++) poissonSum+=e.evaluate("poissonProbability("+k+",5)");
        close(poissonSum,1);
        for(int n=0;n<=1000;n++) {
            close(e.evaluate("isPrime("+n+")"),BigInteger.valueOf(n).isProbablePrime(100)?1:0);
        }
        for(int n=2;n<=78;n++) {
            close(e.evaluate("fibonacci("+n+")"),e.evaluate("fibonacci("+(n-1)+")+fibonacci("+(n-2)+")"));
        }
        String manyOnes=",1".repeat(255);
        close(e.evaluate("quantile(.5"+manyOnes+")"),1);
        if(Double.doubleToRawLongBits(e.evaluate("copySign(0,-1)"))!=Double.doubleToRawLongBits(-0.0))
            throw new AssertionError("copySign discarded the sign of zero");
        checks++;
        System.out.println("PASS: "+checks+" extended numerical property checks");
    }
    private static void close(double actual,double expected) {
        if(!Double.isFinite(actual)||Math.abs(actual-expected)>Math.max(1e-11,Math.abs(expected)*1e-11))
            throw new AssertionError("Expected "+expected+", received "+actual);
        checks++;
    }
}
