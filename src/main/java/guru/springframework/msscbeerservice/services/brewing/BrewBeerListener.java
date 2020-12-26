package guru.springframework.msscbeerservice.services.brewing;

import guru.springframework.msscbeerservice.config.JmsConfig;
import guru.springframework.msscbeerservice.domain.Beer;
import guru.sfg.common.events.BrewBeerEvent;
import guru.sfg.common.events.NewInventoryEvent;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * @author Olivier Cappelle
 * @version x.x.x
 * @see
 * @since x.x.x 20/12/2020
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class BrewBeerListener {
    private final BeerRepository beerRepository;
    private final JmsTemplate jmsTemplate;
    //this is commented to use springs objectmapper over jms default objectmapper for deserialization, together with #2
//    private final ObjectMapper objectMapper;

    @Transactional
    @JmsListener(destination = JmsConfig.BREWING_REQUEST_QUEUE)
    public void listen(BrewBeerEvent event) throws IOException {
        //#2, see above
//        BrewBeerEvent event = objectMapper.readValue(eventString, BrewBeerEvent.class);
        BeerDto beerDto = event.getBeerDto();

        Beer beer = beerRepository.getOne(beerDto.getId());

        //Highly simplified example on setting quantity
        beerDto.setQuantityOnHand(beer.getQuantityToBrew());

        NewInventoryEvent newInventoryEvent = new NewInventoryEvent(beerDto);
        log.debug("Brewed beer {} : QOH: {}", beer.getMinOnHand(), beerDto.getQuantityOnHand());

        jmsTemplate.convertAndSend(JmsConfig.NEW_INVENTORY_QUEUE, newInventoryEvent);
    }
}
