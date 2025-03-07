package com.swen_646_project_1.TestPackage;

import com.swen_646_project_1.Manager;

/**
 * Base test class to provide a shared Manager instance.
 */
public class BaseTest {
    protected static final Manager manager = TestHelper.getManager();
}
