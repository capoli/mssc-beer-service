package guru.springframework.msscbeerservice.services.brewing;

import guru.sfg.common.events.BrewBeerEvent;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import guru.springframework.msscbeerservice.services.inventory.BeerInventoryService;
import guru.springframework.msscbeerservice.web.mappers.BeerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static guru.springframework.msscbeerservice.config.JmsConfig.BREWING_REQUEST_QUEUE;

/**
 * @author Olivier Cappelle
 * @version x.x.x
 * @see
 * @since x.x.x 20/12/2020
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class BrewingService {
    public static final int FIXED_BREWING_RATE = 5000;
    private final BeerRepository beerRepository;
    private final BeerInventoryService beerInventoryService;
    private final JmsTemplate jmsTemplate;
    private final BeerMapper beerMapper;

    @Scheduled(fixedRate = FIXED_BREWING_RATE) //every 5 seconds
    public void checkForLowInventory() {
        beerRepository.findAll().forEach(beer -> {
            Integer inQOH = beerInventoryService.getOnhandInventory(beer.getId());

            log.debug("Min OnHand is: {}", beer.getMinOnHand());
            log.debug("Inventory is: {}", inQOH);

            if (beer.getMinOnHand() >= inQOH) {
                jmsTemplate.convertAndSend(BREWING_REQUEST_QUEUE, new BrewBeerEvent(beerMapper.beerToBeerDto(beer)));
            }
        });
    }
}
