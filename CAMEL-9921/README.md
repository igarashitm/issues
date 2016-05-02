## A reproducer for CAMEL-9921

#### Run testcases
```
mvn clean package
```

#### Expected results
The variables are not resolved to its value in PaxExamTest while they are successfully resolved in CamelBlueprintTest.
```
test(org.apache.camel.component.dozer.PaxExamTest)  Time elapsed: 15.878 sec  <<< FAILURE!
org.junit.ComparisonFailure: expected:<[ACME-SALES]> but was:<[${CUST_ID}]>
	at org.apache.camel.component.dozer.PaxExamTest.test(PaxExamTest.java:89)
```

#### FIXME
getting some stacktrace around pax-logging in CamelBlueprintTest. Test itself runs successfully though.
