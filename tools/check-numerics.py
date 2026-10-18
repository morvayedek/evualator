"""Cross-language numerical checks against Python's independent standard library."""
from pathlib import Path
import json,math,random,statistics,subprocess

root=Path(__file__).resolve().parents[1]
cases=[]
def add(name,args,expected): cases.append((name+'('+','.join(repr(x) for x in args)+')',float(expected)))
rng=random.Random(20261018)
for _ in range(100):
    x=rng.uniform(-100,100)
    add('asinh',[x],math.asinh(x))
    x=rng.uniform(1,100)
    add('acosh',[x],math.acosh(x))
    x=rng.uniform(-.999,.999)
    add('atanh',[x],math.atanh(x))
    x=rng.uniform(-700,700)
    add('logistic',[x],1/(1+math.exp(-x)))
    p=rng.uniform(.001,.999)
    add('logit',[p],math.log(p/(1-p)))
    add('softplus',[x],max(x,0)+math.log1p(math.exp(-abs(x))))
    vals=[rng.uniform(.1,20) for _ in range(rng.randrange(2,15))]
    add('rms',vals,math.sqrt(statistics.mean(v*v for v in vals)))
    add('geometricMean',vals,statistics.geometric_mean(vals))
    add('harmonicMean',vals,statistics.harmonic_mean(vals))
    add('sampleStddev',vals,statistics.stdev(vals))
    add('coefficientVariation',vals,statistics.pstdev(vals)/statistics.mean(vals))
    add('meanAbsoluteDeviation',vals,statistics.mean(abs(v-statistics.mean(vals)) for v in vals))
    med=statistics.median(vals)
    add('medianAbsoluteDeviation',vals,statistics.median(abs(v-med) for v in vals))
    p=rng.random(); ordered=sorted(vals); position=p*(len(vals)-1); lo=math.floor(position); hi=min(lo+1,len(vals)-1)
    add('quantile',[p,*vals],ordered[lo]+(ordered[hi]-ordered[lo])*(position-lo))
    x,y,z=[rng.uniform(-1e100,1e100) for _ in range(3)]
    add('distance3d',[0,0,0,x,y,z],math.hypot(x,y,z))
    n=rng.randrange(0,60); k=rng.randrange(n+1); p=rng.uniform(.05,.95)
    add('binomialProbability',[n,k,p],math.comb(n,k)*p**k*(1-p)**(n-k))
    k=rng.randrange(0,40); rate=rng.uniform(.01,20)
    add('poissonProbability',[k,rate],math.exp(-rate)*rate**k/math.factorial(k))
    x,mean,sigma=rng.uniform(-20,20),rng.uniform(-5,5),rng.uniform(.1,10)
    add('normalPdf',[x,mean,sigma],statistics.NormalDist(mean,sigma).pdf(x))
    x=rng.uniform(-1e6,1e6)
    add('degreesWrap',[x],(x+180)%360-180)
    add('celsiusToFahrenheit',[x],x*9/5+32)
    add('fahrenheitToCelsius',[x],(x-32)*5/9)

run=subprocess.run(['java','-jar',str(root/'build/evaluator.jar'),'--stdin','--json'],
    input='\n'.join(expr for expr,_ in cases)+'\n',capture_output=True,text=True,check=True)
lines=run.stdout.splitlines()
assert len(lines)==len(cases),(len(lines),len(cases))
for (expression,expected),line in zip(cases,lines):
    obj=json.loads(line)
    actual=obj['result']
    tolerance=max(math.ulp(expected)*16,abs(expected)*2e-10)
    assert math.isfinite(actual) and abs(actual-expected)<=tolerance,(expression,expected,actual,tolerance)
result={'checks':len(cases),'reference':'Python math/statistics standard library','seed':20261018,'passed':True}
(root/'build/numerical-checks.json').write_text(json.dumps(result,indent=2))
print(json.dumps(result))
