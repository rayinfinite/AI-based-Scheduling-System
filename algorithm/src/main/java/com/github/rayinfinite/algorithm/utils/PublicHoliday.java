package com.github.rayinfinite.algorithm.utils;

import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.component.VEvent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@SuppressWarnings("unused")
public class PublicHoliday {
    private static final String ICS_URL = "https://www.mom.gov.sg/-/media/mom/documents/employment-practices/public" +
            "-holidays/public-holidays-sg-2025.ics";
    private static final Set<Integer> parsedYears = ConcurrentHashMap.newKeySet();
    private static final Map<LocalDate, String> holidays = new ConcurrentHashMap<>();

    private PublicHoliday() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isPublicHoliday(LocalDate date) {
        int year = date.getYear();
        if (year < 2018 || year > LocalDate.now().getYear() + 1) {
            throw new IllegalArgumentException("Year must be between 2018 and next year");
        }
        if (!parsedYears.contains(year)) {
            try {
                parseDownloadedIcsContent(year);
                parsedYears.add(year);
            } catch (Exception e) {
                log.error("Failed to parse ICS content", e);
            }
        }
        return holidays.containsKey(date);
    }

    public static String details(LocalDate date) {
        return isPublicHoliday(date) ? holidays.get(date) : null;
    }

    //TODO: retry network request if failed
    private static synchronized void parseDownloadedIcsContent(int year) throws IOException, ParserException {
        String urlString = ICS_URL.replace("2025", String.valueOf(year));
        if (parsedYears.contains(year)) {
            return;
        }
        URL url = URI.create(urlString).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed to download file: HTTP error code : " + responseCode);
        }

        try (InputStream inputStream = connection.getInputStream()) {
            byte[] icsContent = inputStream.readAllBytes();

            try (InputStream icsStream = new ByteArrayInputStream(icsContent)) {
                CalendarBuilder builder = new CalendarBuilder();
                Calendar calendar = builder.build(icsStream);
                for (Component component : calendar.getComponents()) {
                    if (component instanceof VEvent event) {
                        var getSummary = event.getSummary();
                        var getDateTimeStart = event.getDateTimeStart();
                        if (getSummary.isPresent() && getDateTimeStart.isPresent()) {
                            String summary = getSummary.get().getValue();
                            LocalDate start = LocalDate.from(getDateTimeStart.get().getDate());
                            holidays.put(start, summary);
                        }
                    }
                }
            }
        }
    }
}
