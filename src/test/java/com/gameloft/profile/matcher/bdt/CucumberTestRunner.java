package com.gameloft.profile.matcher.bdt;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber-reports.html", "json:target/cucumber.json"},
        features = "src/test/resources/features",
        glue = "com.gameloft.profile.matcher.bdt"
)
public class CucumberTestRunner {

}

