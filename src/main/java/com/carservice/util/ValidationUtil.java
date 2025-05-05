package com.carservice.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Utility class for input validation
 */
public class ValidationUtil {
    // Email validation regex pattern
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    // Username validation regex pattern
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9_]{3,20}$";

    // Password validation regex pattern
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";

    // License plate validation regex pattern
    private static final String LICENSE_PLATE_REGEX = "^[A-Z0-9]{2,8}$";

    // VIN (Vehicle Identification Number) validation regex pattern
    private static final String VIN_REGEX = "^[A-HJ-NPR-Z0-9]{17}$";

    // Car ID validation regex pattern (alphanumeric)
    private static final String CAR_ID_REGEX = "^[a-zA-Z0-9_-]{3,15}$";

    // Phone number validation regex pattern
    private static final String PHONE_REGEX = "^\\+?[0-9]{10,15}$";

    // Validate email address
    public static boolean isValidEmail(String email) {
        return email != null && Pattern.matches(EMAIL_REGEX, email);
    }

    // Validate username
    public static boolean isValidUsername(String username) {
        return username != null && Pattern.matches(USERNAME_REGEX, username);
    }

    // Validate password
    public static boolean isValidPassword(String password) {
        return password != null && Pattern.matches(PASSWORD_REGEX, password);
    }

    // Validate license plate
    public static boolean isValidLicensePlate(String licensePlate) {
        return licensePlate != null && Pattern.matches(LICENSE_PLATE_REGEX, licensePlate);
    }

    // Validate VIN
    public static boolean isValidVIN(String vin) {
        return vin != null && Pattern.matches(VIN_REGEX, vin);
    }

    // Validate car ID
    public static boolean isValidCarId(String carId) {
        return carId != null && Pattern.matches(CAR_ID_REGEX, carId);
    }

    // Validate phone number
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && Pattern.matches(PHONE_REGEX, phoneNumber);
    }

    // Check if string is null or empty
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    // Validate integer within a range
    public static boolean isValidIntegerRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    // Validate double within a range
    public static boolean isValidDoubleRange(double value, double min, double max) {
        return value >= min && value <= max;
    }

    // Validate date format
    public static boolean isValidDateFormat(String dateStr, String format) {
        if (isNullOrEmpty(dateStr)) {
            return false;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setLenient(false);

        try {
            dateFormat.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // Validate date is in the future
    public static boolean isDateInFuture(Date date) {
        if (date == null) {
            return false;
        }

        Date now = new Date();
        return date.after(now);
    }

    // Validate date is in the past
    public static boolean isDateInPast(Date date) {
        if (date == null) {
            return false;
        }

        Date now = new Date();
        return date.before(now);
    }

    // Calculate age from birth date
    public static int calculateAge(Date birthDate) {
        if (birthDate == null) {
            return 0;
        }

        Date now = new Date();
        long diffInMillies = now.getTime() - birthDate.getTime();

        // Convert milliseconds to years (approximate)
        return (int) (diffInMillies / (365.25 * 24 * 60 * 60 * 1000));
    }

    // Trim and clean input string to prevent XSS
    public static String cleanInput(String input) {
        if (input == null) {
            return "";
        }

        // Remove any HTML tags
        String sanitized = input.replaceAll("<[^>]*>", "");

        // Replace potentially dangerous characters
        sanitized = sanitized
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;")
                .replace("/", "&#x2F;");

        return sanitized.trim();
    }

    // Validate mileage (non-negative number)
    public static boolean isValidMileage(double mileage) {
        return mileage >= 0;
    }

    // Validate cost (non-negative number)
    public static boolean isValidCost(double cost) {
        return cost >= 0;
    }

    // Validate year (reasonable range for cars)
    public static boolean isValidCarYear(int year) {
        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        return year >= 1886 && year <= currentYear + 1; // +1 for upcoming model year
    }
}