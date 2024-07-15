package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.bootstrap.BootstrapData;
import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.services.BeerCsvService;
import guru.springframework.spring6restmvc.services.BeerCsvServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({BootstrapData.class, BeerCsvServiceImpl.class})
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testGetBeerByName(){
        List<Beer> beers = beerRepository.findAllByBeerNameIsLikeIgnoreCase("%IPA%");

        assertThat(beers.size()).isEqualTo(336);

    }

    @Test
    void testSaveBeer(){
        Beer beer = beerRepository.save(Beer.builder()
                .beerName("My Beer")
                .beerStyle(BeerStyle.PALE_ALE)
                        .upc("1244555")
                        .price(new BigDecimal("11.01"))
                .build());

        beerRepository.flush();

        assertNotNull(beer);
        assertEquals("My Beer", beer.getBeerName());

    }
}