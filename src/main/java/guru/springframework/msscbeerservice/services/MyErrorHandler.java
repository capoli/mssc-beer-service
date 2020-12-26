package guru.springframework.msscbeerservice.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ErrorHandler;

/**
 * @author Olivier Cappelle
 * @version x.x.x
 * @see
 * @since x.x.x 20/12/2020
 **/
@Slf4j
@Service
public class MyErrorHandler implements ErrorHandler {
    @Override
    public void handleError(Throwable throwable) {
        log.error("Error in listener", throwable);
    }
}
