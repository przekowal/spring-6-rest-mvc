package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService{

    private Map<UUID, Beer> beerMap;

    public BeerServiceImpl(){
        this.beerMap = new HashMap<>();

        Beer beer1 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer2 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Cransk")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12345666")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(396)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beerMap.put(beer1.getId(), beer1);
        beerMap.put(beer2.getId(), beer2);
    }

    @Override
    public List<Beer> listBeers(){
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Beer getBeerById(UUID id) {
        log.debug("Get beer by ID called: " + id.toString());
        return beerMap.get(id);
    }

    @Override
    public Beer saveNewBeer(Beer beer) {
        Beer savedBeer = Beer.builder()
                .id(UUID.randomUUID())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .beerName(beer.getBeerName())
                .beerStyle(beer.getBeerStyle())
                .quantityOnHand(beer.getQuantityOnHand())
                .upc(beer.getUpc())
                .price(beer.getPrice())
                .build();
        beerMap.put(savedBeer.getId(), savedBeer);

        return savedBeer;
    }

    @Override
    public void updateBeerById(UUID beerId, Beer beer) {
        Beer existing = beerMap.get(beerId);
        existing.setBeerName(beer.getBeerName());
        existing.setPrice(beer.getPrice());
        existing.setQuantityOnHand(beer.getQuantityOnHand());
        existing.setUpc(beer.getUpc());
        beerMap.put(existing.getId(), existing);

    }

    @Override
    public void deleteById(UUID beerId) {
        beerMap.remove(beerId);
    }

    @Override
    public void patchBeerByID(UUID beerId, Beer beer) {

        Beer existing = beerMap.get(beerId);

        if(StringUtils.hasText(beer.getBeerName())){
            existing.setBeerName(beer.getBeerName());
        }
        if(beer.getBeerStyle() != null){
            existing.setBeerStyle(beer.getBeerStyle());
        }
        if(beer.getQuantityOnHand() != null){
            existing.setQuantityOnHand(beer.getQuantityOnHand());
        }
        if(StringUtils.hasText(beer.getUpc())){
            existing.setUpc(beer.getUpc());
        }
        if(beer.getPrice() != null){
            existing.setPrice(beer.getPrice());
        }
    }
}

