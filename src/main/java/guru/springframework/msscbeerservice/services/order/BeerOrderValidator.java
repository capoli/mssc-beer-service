package guru.springframework.msscbeerservice.services.order;

import guru.sfg.brewery.model.events.BeerOrderDto;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

/**
 * @author Olivier Cappelle
 * @version x.x.x
 * @see
 * @since x.x.x 26/12/2020
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class BeerOrderValidator {
    private final BeerRepository beerRepository;

    /*
    Solution from course
     */
    /*public Boolean validateOrder(BeerOrderDto beerOrder) {
        AtomicInteger beersNotFound = new AtomicInteger();
        beerOrder.getBeerOrderLines().forEach(beerOrderLine -> {
            if (isNull(beerRepository.findByUpc(beerOrderLine.getUpc()))) {
                beersNotFound.incrementAndGet();
            }
        });
        return beersNotFound.get() == 0;
    }*/

    /*
    Own solution
     */
    public Boolean validateOrder(BeerOrderDto beerOrder) {
        return beerOrder.getBeerOrderLines().stream()
                .noneMatch(beerOrderLine -> isNull(beerRepository.findByUpc(beerOrderLine.getUpc())));
    }
}
