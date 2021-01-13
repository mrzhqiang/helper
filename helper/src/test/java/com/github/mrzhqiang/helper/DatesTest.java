package com.github.mrzhqiang.helper;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author mrzhqiang
 */
public final class DatesTest {

    private Instant instant;

    private Instant nowInstant;
    private Instant secondsInstant;
    private Instant minutesInstant;
    private Instant hoursInstant;
    private Instant dayInstant;
    private Instant monthInstant;
    private Instant yearInstant;

    private Config local;
    private Config until;
    private Config display;

    @Before
    public void setUp() {
        instant = Instant.parse("2009-06-23T14:05:30Z");

        nowInstant = Instant.now();
        secondsInstant = nowInstant.minus(Duration.ofSeconds(1));
        minutesInstant = nowInstant.minus(Duration.ofMinutes(1));
        hoursInstant = nowInstant.minus(Duration.ofHours(1));
        dayInstant = nowInstant.minus(Duration.ofDays(1));
        monthInstant = nowInstant.minus(Duration.ofDays(62));
        yearInstant = nowInstant.minus(Duration.ofDays(366));

        Config config = ConfigFactory.load().getConfig("helper.datetime");
        local = config.getConfig("local");
        until = config.getConfig("until");
        display = config.getConfig("display");
    }

    @Test
    public void format() {
        try {
            String format = Dates.format(null);
            // 如果没有抛出空指针异常，那么下面这个检查会抛出 AssertionError 异常
            assertNotNull(format);
        } catch (NullPointerException ignore) {
        }
        String expected = DateTimeFormatter.ofPattern(local.getString("datetime"))
                .format(instant.atZone(ZoneId.systemDefault()));
        String actual = Dates.format(Date.from(instant));
        assertEquals(expected, actual);
    }

    @Test
    public void formatDate() {
        String expected = DateTimeFormatter.ofPattern(local.getString("date"))
                .format(instant.atZone(ZoneId.systemDefault()));
        String format = Dates.formatDate(Date.from(instant));
        assertEquals(expected, format);
    }

    @Test
    public void formatTime() {
        String expected = DateTimeFormatter.ofPattern(local.getString("time"))
                .format(instant.atZone(ZoneId.systemDefault()));
        String format = Dates.formatTime(Date.from(instant));
        assertEquals(expected, format);
    }

    @Test
    public void formatHTTP() {
        String format = Dates.formatHTTP(Date.from(instant));
        assertEquals("Tue, 23 Jun 2009 14:05:30 GMT", format);
    }

    @Test
    public void parse() {
        Date normalDate = Dates.parse(
                DateTimeFormatter.ofPattern(local.getString("datetime"))
                        .format(instant.atZone(ZoneId.systemDefault())));
        // Instant 是一个瞬间时刻，而 DateTimeHelper.parse 方法只解析到秒，所以要进行比较必须截断到秒
        assertEquals(Date.from(instant.truncatedTo(SECONDS)), normalDate);
    }

    @Test
    public void parseHTTP() {
        Date date =
                Dates.parseHTTP(RFC_1123_DATE_TIME.format(instant.atZone(ZoneId.systemDefault())));
        assertEquals(Date.from(instant.truncatedTo(SECONDS)), date);
    }

    @Test
    public void between() {
        Assert.assertEquals(until.getString("now"), Dates.untilNow(Date.from(nowInstant)));
        Assert.assertEquals(String.format(until.getString("seconds"), 1), Dates.untilNow(Date.from(secondsInstant)));
        Assert.assertEquals(String.format(until.getString("minutes"), 1), Dates.untilNow(Date.from(minutesInstant)));
        Assert.assertEquals(String.format(until.getString("hours"), 1), Dates.untilNow(Date.from(hoursInstant)));
        Assert.assertEquals(String.format(until.getString("days"), 1), Dates.untilNow(Date.from(dayInstant)));
        Assert.assertEquals(String.format(until.getString("months"), 2), Dates.untilNow(Date.from(monthInstant)));
        Assert.assertEquals(String.format(until.getString("years"), 1), Dates.untilNow(Date.from(yearInstant)));
    }

    @Test
    public void showTime() {
        Assert.assertEquals(Dates.formatTime(Date.from(nowInstant)),
                Dates.display(Date.from(nowInstant)));
        Assert.assertEquals(Dates.formatTime(Date.from(secondsInstant)),
                Dates.display(Date.from(secondsInstant)));
        Assert.assertEquals(Dates.formatTime(Date.from(minutesInstant)),
                Dates.display(Date.from(minutesInstant)));
        Assert.assertEquals(Dates.formatTime(Date.from(hoursInstant)),
                Dates.display(Date.from(hoursInstant)));
        String expected = String.format(display.getStringList("prefix").get(0),
                Dates.formatTime(Date.from(dayInstant)));
        Assert.assertEquals(expected, Dates.display(Date.from(dayInstant)));
        Assert.assertEquals(Dates.formatDate(Date.from(monthInstant)),
                Dates.display(Date.from(monthInstant)));
        Assert.assertEquals(Dates.formatDate(Date.from(yearInstant)),
                Dates.display(Date.from(yearInstant)));
    }
}
