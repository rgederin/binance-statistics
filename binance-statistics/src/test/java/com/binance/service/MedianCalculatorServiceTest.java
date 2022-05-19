package com.binance.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.*;

class MedianCalculatorServiceTest {
    private PriorityQueue<Double> minHeap;
    private PriorityQueue<Double> maxHeap;
    private final MedianCalculatorService medianCalculatorService = new MedianCalculatorService();

    @BeforeEach
    public void setupQueues(){
        minHeap = new PriorityQueue<>();
        maxHeap = new PriorityQueue<>(Collections.reverseOrder());
    }

    @Test
    public void threeDigitsTest(){
        medianCalculatorService.addToPriorityQueue(1, maxHeap, minHeap);
        medianCalculatorService.addToPriorityQueue(3, maxHeap, minHeap);
        medianCalculatorService.addToPriorityQueue(2, maxHeap, minHeap);

        assertEquals(2.0, medianCalculatorService.findMedian(maxHeap, minHeap));
    }

    @Test
    public void sevenDigitsTest(){
        medianCalculatorService.addToPriorityQueue(3, maxHeap, minHeap);
        medianCalculatorService.addToPriorityQueue(4, maxHeap, minHeap);
        medianCalculatorService.addToPriorityQueue(2, maxHeap, minHeap);
        medianCalculatorService.addToPriorityQueue(1, maxHeap, minHeap);
        medianCalculatorService.addToPriorityQueue(5, maxHeap, minHeap);
        medianCalculatorService.addToPriorityQueue(8, maxHeap, minHeap);
        medianCalculatorService.addToPriorityQueue(2.5, maxHeap, minHeap);

        assertEquals(3.0, medianCalculatorService.findMedian(maxHeap, minHeap));
    }

    @Test
    public void fourDigitsTest(){
        medianCalculatorService.addToPriorityQueue(1, maxHeap, minHeap);
        medianCalculatorService.addToPriorityQueue(3, maxHeap, minHeap);
        medianCalculatorService.addToPriorityQueue(2, maxHeap, minHeap);
        medianCalculatorService.addToPriorityQueue(4, maxHeap, minHeap);

        assertEquals(2.5, medianCalculatorService.findMedian(maxHeap, minHeap));
    }
}
