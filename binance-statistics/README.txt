# In memory solution for capturing binance symbols statistics

How to run locally:

```
1. checkout repository
2. in binance-statistics folder run command (you should to have Maven and Java inslalled): `mvn spring-boot:run`
```

Three endpoints available:

1. Health endpoint

```
GET http://localhost:8080/api/v1/health

OK
```

2. Symbols endpoint

```
GET http://localhost:8080/api/v1/symbols

{
    "binanceSymbols": [
        "ETHBTC",
        "LTCBTC",
        "BNBBTC",
        "NEOBTC",
        "QTUMETH",
        "EOSETH",
        "SNTETH",
        "BNTETH",
        "BCCBTC",
        "GASBTC",
        "BNBETH",
        .......
        "ALGOETH"
```

3. Symbol statistic endpoint

```
GET http://localhost:8080/api/v1/ethbtc

{
    "symbol": "ETHBTC",
    "appearance": 787,
    "priceMedian": 0.066628,
    "lastPrice": 0.066606
}
```

# Short explanation

In order to build combined streams - before opening WebSocket connections, REST call to the binance's symbols endpoint executed.
According to the binance's documentation it is possible to open 1024 streams but for some reasons this is not working (I believe connection URL is too long).
So I am connected to 800 streams per connection.

I am using some Java Web Socket client. 

## Algo explanation

### Hash map

For implementing custom hash map I am using separate chaining technique. Due to limited time my hash map not supported dynamic resizing however in case of collisions linked list are building in the bucket.

Resizing implementation is quite straight forward - we could use arraycopy for resize bucket array when it is 3/4 full and shrink when it is half empty.

### Infinite median

This is quite classic problem - calculating median value for the stream of numbers. I am using two Priority Queues for this. 

To find the median, we need to focus on the middle elements. The key point is we do not need to maintain the entire sorted order for median.

Imagine a sorted array ~ maxHeap on left and minHeap on right will make sure middle elements are in their correct position as long as we maintain their sizes equal (of course, not fully sorted).

addNum always adds to maxHeap and if sizes are not equal transfer the middle element to minHeap. (just a convention to return appropriately in findMedian if sizes not equal).
Also make sure maxHeap.peek() is always smaller than minHeap.peek() to keep the correct middle elements accessible all the time.

# Possible (and necessary improvements)

This is quite naive implementation for such interesting challenge (I do not have much time to be honest) however I clearly seeing some improvements which could be implemented here:

1. Decouple web socket part which is responsible for gathering information from the WebSocket from API for getting results.
2. Implement web socket part in some more low level language (GO for example) and implement storing information about statistics in some noSQL storage
3. I believe my solution will die in some time since I am using in memory storage)) So as I said some noSQL storage should be used for storing live data and statistics computation should run over the data from the storage.
4. Implement cache for /symbols endpoint
5. Add production-like logging using ELK
6. Play with concurrency - since it is testing challenge I didn't pay too much attention for this.
7. If we are talking about custom hash map implementation - as I mentioned in the code comments, I did not implement dynamic resizing, so this could be improved.
8. Also I omit unit/integration tests but this is quite obvious.



