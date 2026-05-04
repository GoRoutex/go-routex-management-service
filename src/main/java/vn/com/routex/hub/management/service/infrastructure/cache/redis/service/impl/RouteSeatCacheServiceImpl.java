package vn.com.routex.hub.management.service.infrastructure.cache.redis.service.impl;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.management.service.infrastructure.cache.redis.models.TripCacheSeat;
import vn.com.routex.hub.management.service.infrastructure.cache.redis.service.RouteSeatCacheService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteSeatCacheServiceImpl implements RouteSeatCacheService {

    private final RedissonClient redissonClient;

    private static final String ROUTE_SEAT_KEY = "route-seat:%s";
    private static final Duration TTL = Duration.ofMinutes(30);

    @Override
    public void putSeats(String routeId, List<TripCacheSeat> cacheSeats) {
        String key = String.format(ROUTE_SEAT_KEY, routeId);
        RMap<String, TripCacheSeat> map = redissonClient.getMap(key,
                new TypedJsonJacksonCodec(String.class, TripCacheSeat.class));

        Map<String, TripCacheSeat> seatMap = cacheSeats.stream()
                .collect(Collectors.toMap(
                        TripCacheSeat::seatNo,
                        seat -> seat
                ));

        map.putAll(seatMap);
        map.expire(TTL);
    }


    @Override
    public List<TripCacheSeat> getSeats(String routeId) {
        String key = String.format(ROUTE_SEAT_KEY, routeId);

        RMap<String, TripCacheSeat> map = redissonClient.getMap(key,
                new TypedJsonJacksonCodec(String.class, TripCacheSeat.class));

        Collection<TripCacheSeat> values = map.readAllValues();
        return new ArrayList<>(values);
    }

    @Override
    public Map<String, TripCacheSeat> getSpecificSeat(String routeId, List<String> seatNos) {
        String key = String.format(ROUTE_SEAT_KEY, routeId);
        RMap<String, TripCacheSeat> map = redissonClient.getMap(key);

        return map.getAll(new HashSet<>(seatNos));
    }

    @Override
    public void updateSeatsStatus(String routeId, List<TripCacheSeat> cacheSeats) {
        String key = String.format(ROUTE_SEAT_KEY, routeId);
        RMap<String, TripCacheSeat> map = redissonClient.getMap(key);
        Map<String, TripCacheSeat> updates = cacheSeats
                .stream()
                .collect(Collectors.toMap(TripCacheSeat::seatNo, s -> s));

        map.putAll(updates);
    }

    @Override
    public void evictSeat(String routeId) {
        String key = String.format(ROUTE_SEAT_KEY, routeId);
        redissonClient.getBucket(key).delete();
    }
}
