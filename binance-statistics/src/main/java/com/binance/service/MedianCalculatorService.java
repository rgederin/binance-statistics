package com.binance.service;

import org.springframework.stereotype.Service;

import java.util.PriorityQueue;

@Service
public class MedianCalculatorService {
    public void addToPriorityQueue(double price, PriorityQueue<Double> maxHeap, PriorityQueue<Double> minHeap) {
        if(maxHeap.isEmpty() || maxHeap.peek() >= price){
            maxHeap.add(price);
        }else{
            minHeap.add(price);
        }

        if(maxHeap.size() - minHeap.size() == 2){
            minHeap.add(maxHeap.poll());
        }else if(minHeap.size() - maxHeap.size() == 2){
            maxHeap.add(minHeap.poll());
        }
    }

    public double findMedian(PriorityQueue<Double> maxHeap, PriorityQueue<Double> minHeap) {
        if(maxHeap.size() == minHeap.size()){
            return (maxHeap.peek() + minHeap.peek()) /2;

        } else if(minHeap.size()<maxHeap.size()){
            return maxHeap.peek();
        } else{
            return minHeap.peek();
        }
    }
}
