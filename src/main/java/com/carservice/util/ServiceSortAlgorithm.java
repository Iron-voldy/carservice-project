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
        Comparator<ServiceRecord> comparator = (r1, r2) -> {
            int result = r1.getServiceDate().compareTo(r2.getServiceDate());
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
        Comparator<ServiceRecord> comparator = (r1, r2) -> {
            int result = r1.getNextServiceDate().compareTo(r2.getNextServiceDate());
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
        Comparator<ServiceRecord> comparator = (r1, r2) -> {
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
        Comparator<ServiceRecord> comparator = (r1, r2) -> {
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
        ServiceLinkedList result = new ServiceLinkedList();

        for (ServiceRecord record : records) {
            Date nextServiceDate = record.getNextServiceDate();
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
        ServiceLinkedList result = new ServiceLinkedList();

        for (ServiceRecord record : records) {
            if (record.getServiceType().getDescription().equalsIgnoreCase(serviceType)) {
                result.add(record);
            }
        }

        return result;
    }
}