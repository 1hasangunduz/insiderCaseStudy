package com.insider.utilities;

import com.insider.base.Driver;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.codehaus.groovy.reflection.ReflectionUtils;
import org.jetbrains.annotations.Contract;
import org.testng.Assert;


public final class Log {
    private static final int DEFAULT_MATCH_LEVEL = 2;

    // hepsi static olduğu için; bir başka yerde objesinin oluşturulmasını imkansız hale getiriyor.
    private Log() {
        throw new IllegalStateException("Log Utility Class");
    }

    static {
        Configurator.setRootLevel(Level.DEBUG);
    }

    public static Logger getLogger(int matchLevel) {
        return LogManager.getLogger(ReflectionUtils.getCallingClass(matchLevel));
    }

    //Info Level Logs
    public static <T> void pass(T message) {
        pass(message, DEFAULT_MATCH_LEVEL);
    }

    public static <T> void pass(T message, int level) {
        if (message == null) return;
        Allure.step(message.toString(), Status.PASSED);
        getLogger(level).info(message);
    }

    //Warn Level Logs
    public static <T> void warning(T message) {
        warning(message, DEFAULT_MATCH_LEVEL);
    }

    public static <T> void warning(T message, int level) {
        if (message == null) return;
        Allure.step(message.toString(), Status.BROKEN);
        getLogger(level).warn(message);
    }

    //Error Level Logs
    public static void error(Throwable e) {
        error(e.getMessage(), e, DEFAULT_MATCH_LEVEL);
    }

    public static <T> void error(T message) {
        error(message, null, DEFAULT_MATCH_LEVEL);
    }

    public static <T> void error(T message, Throwable e) {
        error(message, e, DEFAULT_MATCH_LEVEL);
    }

    @Contract(value = "_, _, _ -> fail")
    public static <T> void error(T message, Throwable e, int level) {
        warning(message, ++level);
        var exceptionMessage = e == null ? "" : "\n\n" + e.getMessage();
        throw new IllegalStateException(message + exceptionMessage, e);
    }

    //Fail Level Logs
    @Contract(value = "_-> fail")
    public static <T> void fail(T message) {
        fail(message, null, DEFAULT_MATCH_LEVEL);
    }

    @Contract(value = "_, _ -> fail")
    public static <T> void fail(T message, Throwable e) {
        fail(message, e, DEFAULT_MATCH_LEVEL);
    }

    @Contract(value = "_, _, _ -> fail")
    public static <T> void fail(T message, Throwable e, int level) {
        if (message == null) return;
        Allure.step(message.toString(), Status.FAILED);
        getLogger(level).error(message, e);
        Assert.fail(message.toString(), e);
    }

    // Extra Logs
    public static <T> void fail(T expected, T actual, String message) {
        fail(expected, actual, message, null);
    }

    public static <T> void pass(T expected, T actual, String message) {
        pass("Expected :" + expected, DEFAULT_MATCH_LEVEL);
        pass("Actual :" + actual, DEFAULT_MATCH_LEVEL);
        pass(message, DEFAULT_MATCH_LEVEL);
    }

    public static <T> void fail(T expected, T actual, String message, Throwable e) {
        warning("Expected: " + expected);
        warning("Actual: " + actual);
        warning("url: " + Driver.getDriver().getCurrentUrl());
        getLogger(2).info("url: " + Driver.getDriver().getCurrentUrl());
        fail("""
                Expected: %s
                Actual: %s
                Message: %s
                Exception: %s
                """.formatted(expected, actual, message, e == null ? "" : e.getMessage()), e);
    }
}
