package dev.santo.evaluator;

import java.util.Map;

/** Additional real-valued numerical functions. */
final class ExtendedMath {
    private ExtendedMath() { }
    static void register(Map<String, FunctionSpec> map) {
        map.put("asinh", new FunctionSpec(1, 1, ExtendedMath::asinh));
        map.put("acosh", new FunctionSpec(1, 1, ExtendedMath::acosh));
        map.put("atanh", new FunctionSpec(1, 1, ExtendedMath::atanh));
        map.put("reciprocal", new FunctionSpec(1, 1, ExtendedMath::reciprocal));
        map.put("sinc", new FunctionSpec(1, 1, ExtendedMath::sinc));
        map.put("logBase", new FunctionSpec(2, 2, ExtendedMath::logBase));
        map.put("root", new FunctionSpec(2, 2, ExtendedMath::root));
        map.put("wrap", new FunctionSpec(3, 3, ExtendedMath::wrap));
        map.put("smoothstep", new FunctionSpec(3, 3, ExtendedMath::smoothstep));
        map.put("smootherstep", new FunctionSpec(3, 3, ExtendedMath::smootherstep));
        map.put("logistic", new FunctionSpec(1, 1, ExtendedMath::logistic));
        map.put("logit", new FunctionSpec(1, 1, ExtendedMath::logit));
        map.put("softplus", new FunctionSpec(1, 1, ExtendedMath::softplus));
        map.put("softsign", new FunctionSpec(1, 1, ExtendedMath::softsign));
        map.put("relu", new FunctionSpec(1, 1, ExtendedMath::relu));
        map.put("leakyRelu", new FunctionSpec(2, 2, ExtendedMath::leakyRelu));
        map.put("elu", new FunctionSpec(2, 2, ExtendedMath::elu));
        map.put("square", new FunctionSpec(1, 1, ExtendedMath::square));
        map.put("cube", new FunctionSpec(1, 1, ExtendedMath::cube));
        map.put("fma", new FunctionSpec(3, 3, ExtendedMath::fma));
        map.put("copySign", new FunctionSpec(2, 2, ExtendedMath::copySign));
        map.put("scalb", new FunctionSpec(2, 2, ExtendedMath::scalb));
        map.put("exponent", new FunctionSpec(1, 1, ExtendedMath::exponent));
        map.put("ieeeRemainder", new FunctionSpec(2, 2, ExtendedMath::ieeeRemainder));
        map.put("roundToMultiple", new FunctionSpec(2, 2, ExtendedMath::roundToMultiple));
        map.put("quantile", new FunctionSpec(2, 256, ExtendedMath::quantile));
        map.put("rangeSpread", new FunctionSpec(1, 256, ExtendedMath::rangeSpread));
        map.put("rms", new FunctionSpec(1, 256, ExtendedMath::rms));
        map.put("geometricMean", new FunctionSpec(1, 256, ExtendedMath::geometricMean));
        map.put("harmonicMean", new FunctionSpec(1, 256, ExtendedMath::harmonicMean));
        map.put("meanAbsoluteDeviation", new FunctionSpec(1, 256, ExtendedMath::meanAbsoluteDeviation));
        map.put("medianAbsoluteDeviation", new FunctionSpec(1, 256, ExtendedMath::medianAbsoluteDeviation));
        map.put("sampleStddev", new FunctionSpec(2, 256, ExtendedMath::sampleStddev));
        map.put("coefficientVariation", new FunctionSpec(1, 256, ExtendedMath::coefficientVariation));
        map.put("sumSquares", new FunctionSpec(1, 256, ExtendedMath::sumSquares));
        map.put("product", new FunctionSpec(1, 256, ExtendedMath::product));
        map.put("weightedMean", new FunctionSpec(2, 256, ExtendedMath::weightedMean));
        map.put("dot", new FunctionSpec(2, 256, ExtendedMath::dot));
        map.put("distance2d", new FunctionSpec(4, 4, ExtendedMath::distance2d));
        map.put("distance3d", new FunctionSpec(6, 6, ExtendedMath::distance3d));
        map.put("circleArea", new FunctionSpec(1, 1, ExtendedMath::circleArea));
        map.put("circleCircumference", new FunctionSpec(1, 1, ExtendedMath::circleCircumference));
        map.put("sphereVolume", new FunctionSpec(1, 1, ExtendedMath::sphereVolume));
        map.put("sphereArea", new FunctionSpec(1, 1, ExtendedMath::sphereArea));
        map.put("coneVolume", new FunctionSpec(2, 2, ExtendedMath::coneVolume));
        map.put("cylinderVolume", new FunctionSpec(2, 2, ExtendedMath::cylinderVolume));
        map.put("rectangleDiagonal", new FunctionSpec(2, 2, ExtendedMath::rectangleDiagonal));
        map.put("triangleArea", new FunctionSpec(2, 2, ExtendedMath::triangleArea));
        map.put("trapezoidArea", new FunctionSpec(3, 3, ExtendedMath::trapezoidArea));
        map.put("fahrenheitToCelsius", new FunctionSpec(1, 1, ExtendedMath::fahrenheitToCelsius));
        map.put("celsiusToFahrenheit", new FunctionSpec(1, 1, ExtendedMath::celsiusToFahrenheit));
    }

    /** Stable inverse hyperbolic sine, including tiny and very large inputs. */
    private static double asinh(double[] a) {
        
        double x = Math.abs(a[0]);
        double value = x > 1e154 ? Math.log(x) + Math.log(2) : Math.log1p(x + x*x/(1 + Math.sqrt(1+x*x)));
        return Math.copySign(value, a[0]);
    }

    /** Inverse hyperbolic cosine for x >= 1 without squaring huge inputs. */
    private static double acosh(double[] a) {
        
        Functions.require(a[0] >= 1, "acosh requires x >= 1");
        double x=a[0];
        return x > 1e154 ? Math.log(x)+Math.log(2) : Math.log1p((x-1)+Math.sqrt(x-1)*Math.sqrt(x+1));
    }

    /** Inverse hyperbolic tangent on the open interval (-1,1). */
    private static double atanh(double[] a) {
        
        double x=Math.abs(a[0]);
        Functions.require(x < 1, "atanh requires -1 < x < 1");
        return Math.copySign(.5*Math.log1p(2*x/(1-x)),a[0]);
    }

    /** Reciprocal with explicit rejection of signed zero. */
    private static double reciprocal(double[] a) {
        
        Functions.require(a[0] != 0, "reciprocal requires a nonzero value");
        return 1/a[0];
    }

    /** Unnormalised sinc, sin(x)/x, with its continuous value at zero. */
    private static double sinc(double[] a) {
        return a[0] == 0 ? 1 : Math.sin(a[0])/a[0];
    }

    /** Logarithm of a positive value in any positive base other than one. */
    private static double logBase(double[] a) {
        
        Functions.require(a[0]>0 && a[1]>0 && a[1]!=1, "logBase requires x > 0 and base > 0, base != 1");
        return Math.log(a[0])/Math.log(a[1]);
    }

    /** Real nth roots; negative radicands require an odd positive integer degree. */
    private static double root(double[] a) {
        
        long n=Functions.integer(a[1]);
        Functions.require(n>=1 && n<=1000000, "root degree must be 1..1000000");
        Functions.require(a[0]>=0 || n%2==1, "Negative radicands require an odd degree");
        return a[0]<0 ? -Math.pow(-a[0],1.0/n) : Math.pow(a[0],1.0/n);
    }

    /** Wrap values to [low,high), requiring a finite positive interval width. */
    private static double wrap(double[] a) {
        
        double low=a[1], high=a[2], width=high-low;
        Functions.require(high>low && Double.isFinite(width), "wrap requires finite low < high and finite width");
        double offset=(a[0]%width-low%width)%width;
        if (offset<0) offset+=width;
        double result=low+offset;
        return result>=high ? low : result;
    }

    /** Cubic interpolation between ordered edges with saturation outside the interval. */
    private static double smoothstep(double[] a) {
        
        Functions.require(a[1]>a[0] && Double.isFinite(a[1]-a[0]), "smoothstep requires finite increasing edges");
        if(a[2]<=a[0]) return 0;
        if(a[2]>=a[1]) return 1;
        double t=(a[2]-a[0])/(a[1]-a[0]);
        return t*t*(3-2*t);
    }

    /** Quintic interpolation with zero first and second derivatives at both edges. */
    private static double smootherstep(double[] a) {
        
        Functions.require(a[1]>a[0] && Double.isFinite(a[1]-a[0]), "smootherstep requires finite increasing edges");
        if(a[2]<=a[0]) return 0;
        if(a[2]>=a[1]) return 1;
        double t=(a[2]-a[0])/(a[1]-a[0]);
        return t*t*t*(t*(6*t-15)+10);
    }

    /** Overflow-safe logistic activation for arbitrarily large finite inputs. */
    private static double logistic(double[] a) {
        
        if(a[0]>=0) return 1/(1+Math.exp(-a[0]));
        double e=Math.exp(a[0]);
        return e/(1+e);
    }

    /** Inverse logistic transform for probabilities strictly between zero and one. */
    private static double logit(double[] a) {
        
        Functions.require(a[0]>0 && a[0]<1, "logit requires 0 < p < 1");
        return Math.log(a[0])-Math.log1p(-a[0]);
    }

    /** Stable log(1+exp(x)) activation, retaining small negative tails. */
    private static double softplus(double[] a) {
        return Math.max(a[0],0)+Math.log1p(Math.exp(-Math.abs(a[0])));
    }

    /** Bounded softsign activation x/(1+abs(x)). */
    private static double softsign(double[] a) {
        return a[0]/(1+Math.abs(a[0]));
    }

    /** Rectified linear activation with a nonnegative zero result. */
    private static double relu(double[] a) {
        return Math.max(0,a[0]);
    }

    /** Leaky rectifier with a caller-selected negative slope in [0,1]. */
    private static double leakyRelu(double[] a) {
        
        Functions.require(a[1]>=0 && a[1]<=1, "leakyRelu slope must be in [0,1]");
        return a[0]>=0 ? a[0] : a[0]*a[1];
    }

    /** Exponential linear activation with a positive alpha parameter. */
    private static double elu(double[] a) {
        
        Functions.require(a[1]>0,"elu requires alpha > 0");
        return a[0]>=0 ? a[0] : a[1]*Math.expm1(a[0]);
    }

    /** Square a value; the evaluator rejects results outside the finite-double range. */
    private static double square(double[] a) {
        return a[0]*a[0];
    }

    /** Cube a value, preserving its sign. */
    private static double cube(double[] a) {
        return a[0]*a[0]*a[0];
    }

    /** Fused multiply-add with one final rounding and no intermediate product overflow. */
    private static double fma(double[] a) {
        return Math.fma(a[0],a[1],a[2]);
    }

    /** Copy the sign of the second value to the magnitude of the first, including signed zero. */
    private static double copySign(double[] a) {
        return Math.copySign(a[0],a[1]);
    }

    /** Scale by an integral power of two with a bounded exponent parameter. */
    private static double scalb(double[] a) {
        
        long n=Functions.integer(a[1]);
        Functions.require(n>=-2098 && n<=2098,"scalb exponent must be -2098..2098");
        return Math.scalb(a[0],(int)n);
    }

    /** Return the unbiased binary exponent; zero and subnormals return -1023. */
    private static double exponent(double[] a) {
        return Math.getExponent(a[0]);
    }

    /** IEEE remainder using the nearest ties-to-even quotient, distinct from percent remainder. */
    private static double ieeeRemainder(double[] a) {
        
        Functions.require(a[1]!=0,"ieeeRemainder requires a nonzero divisor");
        return Math.IEEEremainder(a[0],a[1]);
    }

    /** Round to a positive decimal step with ties to even using decimal arithmetic. */
    private static double roundToMultiple(double[] a) {
        
        Functions.require(a[1]>0,"roundToMultiple requires a positive step");
        var value=java.math.BigDecimal.valueOf(a[0]);
        var step=java.math.BigDecimal.valueOf(a[1]);
        return value.divide(step,0,java.math.RoundingMode.HALF_EVEN).multiply(step).doubleValue();
    }

    /** Type-7 linearly interpolated quantile: probability first, then one or more observations. */
    private static double quantile(double[] a) {
        
        Functions.require(a[0]>=0 && a[0]<=1,"quantile probability must be in [0,1]");
        double[] values=java.util.Arrays.copyOfRange(a,1,a.length);
        java.util.Arrays.sort(values);
        double index=a[0]*(values.length-1);
        int low=(int)Math.floor(index), high=Math.min(low+1,values.length-1);
        double fraction=index-low;
        return values[low]*(1-fraction)+values[high]*fraction;
    }

    /** Maximum observation minus minimum observation. */
    private static double rangeSpread(double[] a) {
        return java.util.Arrays.stream(a).max().orElseThrow()-java.util.Arrays.stream(a).min().orElseThrow();
    }

    /** Root-mean-square with scaling to avoid overflow from squaring large observations. */
    private static double rms(double[] a) {
        
        double scale=0;
        for(double x:a) scale=Math.max(scale,Math.abs(x));
        if(scale==0) return 0;
        double sum=0;
        for(double x:a) { double y=x/scale; sum+=y*y; }
        return scale*Math.sqrt(sum/a.length);
    }

    /** Geometric mean of strictly positive observations, calculated in log space. */
    private static double geometricMean(double[] a) {
        
        double sum=0;
        for(double x:a) { Functions.require(x>0,"geometricMean requires positive observations"); sum+=Math.log(x); }
        return Math.exp(sum/a.length);
    }

    /** Harmonic mean of positive observations with reciprocal scaling. */
    private static double harmonicMean(double[] a) {
        
        double scale=Double.POSITIVE_INFINITY;
        for(double x:a) { Functions.require(x>0,"harmonicMean requires positive observations"); scale=Math.min(scale,x); }
        double sum=0;
        for(double x:a) sum+=scale/x;
        return scale*(a.length/sum);
    }

    /** Mean absolute deviation around the arithmetic mean. */
    private static double meanAbsoluteDeviation(double[] a) {
        
        double scale=0;
        for(double x:a) scale=Math.max(scale,Math.abs(x));
        if(scale==0) return 0;
        double mean=0;
        for(double x:a) mean+=x/scale/a.length;
        double sum=0;
        for(double x:a) sum+=Math.abs(x/scale-mean)/a.length;
        return sum*scale;
    }

    /** Median absolute deviation around the median, without normal-distribution scaling. */
    private static double medianAbsoluteDeviation(double[] a) {
        
        double[] sorted=a.clone();
        java.util.Arrays.sort(sorted);
        int n=sorted.length, m=n/2;
        double center=n%2==1?sorted[m]:sorted[m-1]/2+sorted[m]/2;
        for(int i=0;i<n;i++) sorted[i]=Math.abs(a[i]-center);
        java.util.Arrays.sort(sorted);
        return n%2==1?sorted[m]:sorted[m-1]/2+sorted[m]/2;
    }

    /** Sample standard deviation with scaled observations and n-1 denominator. */
    private static double sampleStddev(double[] a) {
        
        double scale=0;
        for(double x:a) scale=Math.max(scale,Math.abs(x));
        if(scale==0) return 0;
        double mean=0;
        for(double x:a) mean+=x/scale/a.length;
        double sum=0;
        for(double x:a) { double d=x/scale-mean; sum+=d*d; }
        return scale*Math.sqrt(sum/(a.length-1));
    }

    /** Population coefficient of variation for strictly positive observations. */
    private static double coefficientVariation(double[] a) {
        
        double scale=0;
        for(double x:a) { Functions.require(x>0,"coefficientVariation requires positive observations"); scale=Math.max(scale,x); }
        double mean=0;
        for(double x:a) mean+=x/scale/a.length;
        double sum=0;
        for(double x:a) { double d=x/scale-mean; sum+=d*d; }
        return Math.sqrt(sum/a.length)/mean;
    }

    /** Sum of squares calculated with a stable Euclidean norm. */
    private static double sumSquares(double[] a) {
        
        double norm=0;
        for(double x:a) norm=Math.hypot(norm,x);
        return norm*norm;
    }

    /** Product accumulated in decimal arithmetic to avoid intermediate double overflow or underflow. */
    private static double product(double[] a) {
        
        var result=java.math.BigDecimal.ONE;
        for(double x:a) result=result.multiply(java.math.BigDecimal.valueOf(x));
        return result.doubleValue();
    }

    /** Weighted mean from value,weight pairs; weights are nonnegative with at least one positive weight. */
    private static double weightedMean(double[] a) {
        
        Functions.require(a.length%2==0,"weightedMean requires value,weight pairs");
        double weightScale=0,valueScale=0;
        for(int i=0;i<a.length;i+=2) { Functions.require(a[i+1]>=0,"Weights must be nonnegative"); weightScale=Math.max(weightScale,a[i+1]); valueScale=Math.max(valueScale,Math.abs(a[i])); }
        Functions.require(weightScale>0,"At least one weight must be positive");
        if(valueScale==0) return 0;
        double weights=0,sum=0;
        for(int i=0;i<a.length;i+=2) { double w=a[i+1]/weightScale; weights+=w; sum+=(a[i]/valueScale)*w; }
        return (sum/weights)*valueScale;
    }

    /** Dot product from interleaved x,y pairs, using decimal products and accumulation. */
    private static double dot(double[] a) {
        
        Functions.require(a.length%2==0,"dot requires x,y pairs");
        var sum=java.math.BigDecimal.ZERO;
        for(int i=0;i<a.length;i+=2) sum=sum.add(java.math.BigDecimal.valueOf(a[i]).multiply(java.math.BigDecimal.valueOf(a[i+1])));
        return sum.doubleValue();
    }

    /** Euclidean distance between two planar points (x1,y1,x2,y2). */
    private static double distance2d(double[] a) {
        return Math.hypot(a[2]-a[0],a[3]-a[1]);
    }

    /** Euclidean distance between two spatial points (x1,y1,z1,x2,y2,z2). */
    private static double distance3d(double[] a) {
        return Math.hypot(Math.hypot(a[3]-a[0],a[4]-a[1]),a[5]-a[2]);
    }

    /** Area of a circle with a nonnegative radius. */
    private static double circleArea(double[] a) {
        Functions.require(a[0]>=0,"Radius must be nonnegative"); return (Math.PI*a[0])*a[0];
    }

    /** Circumference of a circle with a nonnegative radius. */
    private static double circleCircumference(double[] a) {
        Functions.require(a[0]>=0,"Radius must be nonnegative"); return (2*Math.PI)*a[0];
    }

    /** Volume of a sphere with a nonnegative radius. */
    private static double sphereVolume(double[] a) {
        Functions.require(a[0]>=0,"Radius must be nonnegative"); return ((4*Math.PI/3*a[0])*a[0])*a[0];
    }

    /** Surface area of a sphere with a nonnegative radius. */
    private static double sphereArea(double[] a) {
        Functions.require(a[0]>=0,"Radius must be nonnegative"); return (4*Math.PI*a[0])*a[0];
    }

    /** Volume of a cone from nonnegative radius and height; decimal accumulation avoids intermediate overflow. */
    private static double coneVolume(double[] a) {
        Functions.require(a[0]>=0 && a[1]>=0,"Radius and height must be nonnegative"); var r=java.math.BigDecimal.valueOf(a[0]); return r.multiply(r).multiply(java.math.BigDecimal.valueOf(a[1])).multiply(java.math.BigDecimal.valueOf(Math.PI/3)).doubleValue();
    }

    /** Volume of a cylinder from nonnegative radius and height; decimal accumulation avoids intermediate overflow. */
    private static double cylinderVolume(double[] a) {
        Functions.require(a[0]>=0 && a[1]>=0,"Radius and height must be nonnegative"); var r=java.math.BigDecimal.valueOf(a[0]); return r.multiply(r).multiply(java.math.BigDecimal.valueOf(a[1])).multiply(java.math.BigDecimal.valueOf(Math.PI)).doubleValue();
    }

    /** Diagonal of a rectangle with nonnegative side lengths, avoiding square overflow. */
    private static double rectangleDiagonal(double[] a) {
        Functions.require(a[0]>=0 && a[1]>=0,"Side lengths must be nonnegative"); return Math.hypot(a[0],a[1]);
    }

    /** Triangle area from a nonnegative base and perpendicular height, without premature underflow. */
    private static double triangleArea(double[] a) {
        Functions.require(a[0]>=0 && a[1]>=0,"Base and height must be nonnegative"); return java.math.BigDecimal.valueOf(a[0]).multiply(java.math.BigDecimal.valueOf(a[1])).multiply(java.math.BigDecimal.valueOf(.5)).doubleValue();
    }

    /** Trapezoid area from two nonnegative parallel side lengths and a nonnegative height, using decimal intermediates. */
    private static double trapezoidArea(double[] a) {
        Functions.require(a[0]>=0 && a[1]>=0 && a[2]>=0,"Side lengths and height must be nonnegative"); return java.math.BigDecimal.valueOf(a[0]).add(java.math.BigDecimal.valueOf(a[1])).multiply(java.math.BigDecimal.valueOf(a[2])).multiply(java.math.BigDecimal.valueOf(.5)).doubleValue();
    }

    /** Convert Fahrenheit to Celsius without an overflowing initial subtraction. */
    private static double fahrenheitToCelsius(double[] a) {
        return (a[0]/9)*5-(32.0/9)*5;
    }

    /** Convert Celsius to Fahrenheit. */
    private static double celsiusToFahrenheit(double[] a) {
        return Math.fma(a[0],1.8,32);
    }
}
