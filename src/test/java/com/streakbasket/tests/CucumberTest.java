package com.streakbasket.tests;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectPackages("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.streakbasket.tests.steps")
public class CucumberTest {
}

// Não esquecer que este arquivo é apenas para rodar os testes do Cucumber sem
// necessidade de chamar um código no terminal! :p