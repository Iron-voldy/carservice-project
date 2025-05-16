package com.carservice.model.service;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

/**
 * Custom linked list implementation for service records
 */
public class ServiceLinkedList implements Iterable<ServiceRecord> {

    // Node class for the linked list
    private class Node {
        ServiceRecord data;
        Node next;

        Node(ServiceRecord data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    // Constructor
    public ServiceLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    // Add a service record to the end of the list
    public void add(ServiceRecord record) {
        if (record == null) {
            return; // Don't add null records
        }

        Node newNode = new Node(record);

        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }

        size++;
    }

    // Add a service record at a specific position
    public void add(int index, ServiceRecord record) {
        if (record == null) {
            return; // Don't add null records
        }

        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node newNode = new Node(record);

        if (index == 0) {
            // Insert at the beginning
            newNode.next = head;
            head = newNode;
            if (tail == null) {
                tail = newNode;
            }
        } else if (index == size) {
            // Insert at the end
            add(record);
            return;
        } else {
            // Insert in the middle
            Node current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            newNode.next = current.next;
            current.next = newNode;
        }

        size++;
    }

    // Get a service record at a specific index
    public ServiceRecord get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return current.data;
    }

    // Remove a service record at a specific index
    public ServiceRecord remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        ServiceRecord removedRecord;

        if (index == 0) {
            // Remove from the beginning
            removedRecord = head.data;
            head = head.next;
            if (head == null) {
                tail = null;
            }
        } else {
            // Remove from the middle or end
            Node current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }

            removedRecord = current.next.data;
            current.next = current.next.next;

            if (current.next == null) {
                tail = current;
            }
        }

        size--;
        return removedRecord;
    }

    // Remove a specific service record
    public boolean remove(ServiceRecord record) {
        if (head == null || record == null) {
            return false;
        }

        if (head.data.getRecordId().equals(record.getRecordId())) {
            head = head.next;
            size--;
            if (head == null) {
                tail = null;
            }
            return true;
        }

        Node current = head;
        while (current.next != null && !current.next.data.getRecordId().equals(record.getRecordId())) {
            current = current.next;
        }

        if (current.next != null) {
            current.next = current.next.next;
            size--;
            if (current.next == null) {
                tail = current;
            }
            return true;
        }

        return false;
    }

    // Find a service record by ID
    public ServiceRecord findById(String recordId) {
        if (recordId == null) {
            return null;
        }

        Node current = head;
        while (current != null) {
            if (current.data.getRecordId().equals(recordId)) {
                return current.data;
            }
            current = current.next;
        }
        return null;
    }

    // Filter records based on a predicate
    public ServiceLinkedList filter(Predicate<ServiceRecord> predicate) {
        if (predicate == null) {
            return new ServiceLinkedList(); // Return empty list if predicate is null
        }

        ServiceLinkedList filteredList = new ServiceLinkedList();

        Node current = head;
        while (current != null) {
            try {
                if (predicate.test(current.data)) {
                    filteredList.add(current.data);
                }
            } catch (Exception e) {
                System.err.println("Error applying predicate: " + e.getMessage());
            }
            current = current.next;
        }

        return filteredList;
    }

    // Get all service records for a specific car
    public ServiceLinkedList getServicesByCarId(String carId) {
        if (carId == null) {
            return new ServiceLinkedList(); // Return empty list if carId is null
        }

        return filter(record -> carId.equals(record.getCarId()));
    }

    // Get all service records for a specific user
    public ServiceLinkedList getServicesByUserId(String userId) {
        if (userId == null) {
            return new ServiceLinkedList(); // Return empty list if userId is null
        }

        return filter(record -> userId.equals(record.getUserId()));
    }

    // Get upcoming service records (due within a number of days)
    public ServiceLinkedList getUpcomingServices(int daysThreshold) {
        return filter(record -> !record.isCompleted() && record.isServiceDueSoon(daysThreshold));
    }

    // Get overdue service records
    public ServiceLinkedList getOverdueServices() {
        return filter(record -> !record.isCompleted() && record.isServiceDue());
    }

    // Update a service record
    public boolean update(ServiceRecord updatedRecord) {
        if (updatedRecord == null) {
            return false;
        }

        Node current = head;
        while (current != null) {
            if (current.data.getRecordId().equals(updatedRecord.getRecordId())) {
                current.data = updatedRecord;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // Selection sort implementation for service records
    public void sort(Comparator<ServiceRecord> comparator) {
        if (head == null || head.next == null || comparator == null) {
            return; // Empty list, single element, or null comparator
        }

        Node current = head;
        while (current != null) {
            Node minNode = current;
            Node innerCurrent = current.next;

            // Find the minimum element
            while (innerCurrent != null) {
                try {
                    if (comparator.compare(innerCurrent.data, minNode.data) < 0) {
                        minNode = innerCurrent;
                    }
                } catch (Exception e) {
                    System.err.println("Error comparing records: " + e.getMessage());
                }
                innerCurrent = innerCurrent.next;
            }

            // Swap the data of the current node and the minimum node
            if (minNode != current) {
                ServiceRecord temp = current.data;
                current.data = minNode.data;
                minNode.data = temp;
            }

            current = current.next;
        }
    }

    // Sort by service date
    public void sortByServiceDate() {
        sort((record1, record2) -> {
            if (record1.getServiceDate() == null && record2.getServiceDate() == null) {
                return 0;
            } else if (record1.getServiceDate() == null) {
                return -1;
            } else if (record2.getServiceDate() == null) {
                return 1;
            }
            return record1.getServiceDate().compareTo(record2.getServiceDate());
        });
    }

    // Sort by next service date
    public void sortByNextServiceDate() {
        sort((record1, record2) -> {
            if (record1.getNextServiceDate() == null && record2.getNextServiceDate() == null) {
                return 0;
            } else if (record1.getNextServiceDate() == null) {
                return -1;
            } else if (record2.getNextServiceDate() == null) {
                return 1;
            }
            return record1.getNextServiceDate().compareTo(record2.getNextServiceDate());
        });
    }

    // Clear the list
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    // Check if the list contains a specific record
    public boolean contains(ServiceRecord record) {
        if (record == null) {
            return false;
        }

        Node current = head;
        while (current != null) {
            if (current.data.getRecordId().equals(record.getRecordId())) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // Check if the list is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Get the size of the list
    public int size() {
        return size;
    }

    // Convert to array
    public ServiceRecord[] toArray() {
        ServiceRecord[] array = new ServiceRecord[size];
        Node current = head;
        int index = 0;

        while (current != null) {
            array[index++] = current.data;
            current = current.next;
        }

        return array;
    }

    // Custom iterator implementation
    @Override
    public Iterator<ServiceRecord> iterator() {
        return new Iterator<ServiceRecord>() {
            private Node current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public ServiceRecord next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                ServiceRecord data = current.data;
                current = current.next;
                return data;
            }
        };
    }
}