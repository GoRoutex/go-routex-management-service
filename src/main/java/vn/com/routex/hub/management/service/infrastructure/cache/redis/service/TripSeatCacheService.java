package vn.com.routex.hub.management.service.infrastructure.cache.redis.service;


import vn.com.routex.hub.management.service.infrastructure.cache.redis.models.TripCacheSeat;

import java.util.List;
import java.util.Map;

public interface TripSeatCacheService {

    void putSeats(String routeId, List<TripCacheSeat> cacheSeats);
    List<TripCacheSeat> getSeats(String routeId);
    Map<String, TripCacheSeat> getSpecificSeat(String routeId, List<String> seatNos);
    void updateSeatsStatus(String routeId, List<TripCacheSeat> cacheSeats);
    void evictSeat(String routeId);


}
