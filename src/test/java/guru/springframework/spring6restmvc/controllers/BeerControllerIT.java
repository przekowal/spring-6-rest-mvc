package guru.springframework.spring6restmvc.controllers;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.mappers.BeerMapperImpl;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerControllerIT {

    @Autowired
    private BeerRepository beerRepository;
    @Autowired
    private BeerController beerController;

    @Autowired
    BeerMapper beerMapper;

    @Test
    @Rollback
    @Transactional
    void deleteByIdFound(){
        Beer beer = beerRepository.findAll().get(0);
        ResponseEntity responseEntity = beerController.deleteById(beer.getId());
        assertEquals(beerRepository.findById(beer.getId()).isEmpty(), true);
    }

    @Test
    void testDeleteNotFound(){
        assertThrows(NotFoundException.class, () -> {
            beerController.deleteById(UUID.randomUUID());
        });
    }

    @Test
    void testUpdateNotFound(){
        assertThrows(NotFoundException.class, () -> {
            beerController.updateById(UUID.randomUUID(), BeerDTO.builder().build());
        });
    }

    @Test
    @Rollback
    @Transactional
    void updateExistingBeer(){
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO beerDTO = beerMapper.beerToBeerDTO(beer);
        beerDTO.setId(null);
        beerDTO.setVersion(null);
        final String beerName = "UPDATED";
        beerDTO.setBeerName(beerName);

        ResponseEntity responseEntity = beerController.updateById(beer.getId(), beerDTO);
        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(204));

        Beer updatedBeer = beerRepository.findById(beer.getId()).get();
        assertEquals(updatedBeer.getBeerName(), beerName);
    }

    @Test
    void testListBeers(){
        List<BeerDTO> dtos = beerController.listBeers();

        assertEquals(dtos.size(), 2);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList(){
        beerRepository.deleteAll();
        List<BeerDTO> dtos = beerController.listBeers();

        assertEquals(dtos.size(), 0);
    }

    @Test
    void testGetById(){
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO dto = beerController.getBeerById(beer.getId());

        assertNotNull(dto);
    }

    @Test
    void testBeerIdNotFound(){
        assertThrows(NotFoundException.class, () -> {
            beerController.getBeerById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void saveNewBeerTest(){
        BeerDTO beerDTO = BeerDTO.builder()
                .beerName("New Beer")
                .build();

        ResponseEntity responseEntity = beerController.handlePost(beerDTO);

        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(201));
        assertNotNull(responseEntity.getHeaders().getLocation());

        String[] location = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(location[location.length-1]);

        Beer beer = beerRepository.findById(savedUUID).get();
        assertNotNull(beer);
    }
}