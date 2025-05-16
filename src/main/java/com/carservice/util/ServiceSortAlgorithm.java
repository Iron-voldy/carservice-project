package com.carservice.util;

import com.carservice.model.service.ServiceRecord;
import com.carservice.model.service.ServiceLinkedList;

import java.util.Comparator;
import java.util.Date;

/**
 * Utility class providing sorting algorithms for service records
 */
public class ServiceSortAlgorithm {

    /**
     * Selection sort implementation for sorting service records by date
     *
     * @param records The linked list of service records to be sorted
     * @param ascending If true, sort in ascending order, otherwise descending
     */
    public static void selectionSortByDate(ServiceLinkedList records, boolean ascending) {
        if (records == null) {
            return;
        }

        Comparator<ServiceRecord> comparator = (r1, r2) -> {
            if (r1 == null && r2 == null) return 0;
            if (r1 == null) return ascending ? -1 : 1;
            if (r2 == null) return ascending ? 1 : -1;

            Date date1 = r1.getServiceDate();
            Date date2 = r2.getServiceDate();

            if (date1 == null && date2 == null) return 0;
            if (date1 == null) return ascending ? -1 : 1;
            if (date2 == null) return ascending ? 1 : -1;

            int result = date1.compareTo(date2);
            return ascending ? result : -result;
        };

        records.sort(comparator);
    }

    /**
     * Selection sort implementation for sorting service records by next service date
     *
     * @param records The linked list of service records to be sorted
     * @param ascending If true, sort in ascending order, otherwise descending
     */
    public static void selectionSortByNextServiceDate(ServiceLinkedList records, boolean ascending) {
        if (records == null) {
            return;
        }

        Comparator<ServiceRecord> comparator = (r1, r2) -> {
            if (r1 == null && r2 == null) return 0;
            if (r1 == null) return ascending ? -1 : 1;
            if (r2 == null) return ascending ? 1 : -1;

            Date date1 = r1.getNextServiceDate();
            Date date2 = r2.getNextServiceDate();

            if (date1 == null && date2 == null) return 0;
            if (date1 == null) return ascending ? -1 : 1;
            if (date2 == null) return ascending ? 1 : -1;

            int result = date1.compareTo(date2);
            return ascending ? result : -result;
        };

        records.sort(comparator);
    }

    /**
     * Selection sort implementation for sorting service records by mileage
     *
     * @param records The linked list of service records to be sorted
     * @param ascending If true, sort in ascending order, otherwise descending
     */
    public static void selectionSortByMileage(ServiceLinkedList records, boolean ascending) {
        if (records == null) {
            return;
        }

        Comparator<ServiceRecord> comparator = (r1, r2) -> {
            if (r1 == null && r2 == null) return 0;
            if (r1 == null) return ascending ? -1 : 1;
            if (r2 == null) return ascending ? 1 : -1;

            int result = Double.compare(r1.getMileage(), r2.getMileage());
            return ascending ? result : -result;
        };

        records.sort(comparator);
    }

    /**
     * Selection sort implementation for sorting service records by cost
     *
     * @param records The linked list of service records to be sorted
     * @param ascending If true, sort in ascending order, otherwise descending
     */
    public static void selectionSortByCost(ServiceLinkedList records, boolean ascending) {
        if (records == null) {
            return;
        }

        Comparator<ServiceRecord> comparator = (r1, r2) -> {
            if (r1 == null && r2 == null) return 0;
            if (r1 == null) return ascending ? -1 : 1;
            if (r2 == null) return ascending ? 1 : -1;

            int result = Double.compare(r1.getCost(), r2.getCost());
            return ascending ? result : -result;
        };

        records.sort(comparator);
    }

    /**
     * Get service records that are due within a specified date range
     *
     * @param records The linked list of service records to search
     * @param startDate The start date of the range (inclusive)
     * @param endDate The end date of the range (inclusive)
     * @return A new linked list containing service records within the date range
     */
    public static ServiceLinkedList getServicesDueInRange(ServiceLinkedList records, Date startDate, Date endDate) {
        if (records == null || startDate == null || endDate == null) {
            return new ServiceLinkedList();
        }

        ServiceLinkedList result = new ServiceLinkedList();

        for (ServiceRecord record : records) {
            if (record == null) continue;

            Date nextServiceDate = record.getNextServiceDate();
            if (nextServiceDate == null) continue;

            // Check if the service date is within the specified range
            if (nextServiceDate.compareTo(startDate) >= 0 && nextServiceDate.compareTo(endDate) <= 0) {
                result.add(record);
            }
        }

        // Sort the result by next service date (ascending)
        selectionSortByNextServiceDate(result, true);

        return result;
    }

    /**
     * Get service records for a specific service type
     *
     * @param records The linked list of service records to search
     * @param serviceType The service type to filter by
     * @return A new linked list containing service records of the specified type
     */
    public static ServiceLinkedList getServicesByType(ServiceLinkedList records, String serviceType) {
        if (records == null || serviceType == null || serviceType.isEmpty()) {
            return new ServiceLinkedList();
        }

        ServiceLinkedList result = new ServiceLinkedList();

        for (ServiceRecord record : records) {
            if (record != null && record.getServiceType() != null &&
                    record.getServiceType().getDescription().equalsIgnoreCase(serviceType)) {
                result.add(record);
            }
        }

        return result;
    }
}