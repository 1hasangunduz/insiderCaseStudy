package com.insider.utilities;

import com.insider.base.BaseTest;
import com.insider.pages.homepage.InsiderHomePage;

public class StepInit extends BaseTest {

    private StepInit() {
        throw new IllegalStateException("Utility class");
    }

    public static final InsiderHomePage INSIDER_HOME_PAGE = new InsiderHomePage();

}
