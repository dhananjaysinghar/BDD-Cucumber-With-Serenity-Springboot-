package com.user.test;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;
import org.springframework.stereotype.Service;


@RunWith(CucumberWithSerenity.class)
@Service
@CucumberOptions(
        tags = "@Users",
        plugin = {"pretty", "html:target/reports/cucumber.html",
                "json:target/reports/cucumber.json"},
        features = "src/test/resources/",
        snippets = CucumberOptions.SnippetType.CAMELCASE)
public class TestRunner {


}
