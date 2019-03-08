package conversionRate.task;

import conversionRate.dto.ConversionInformationDTO;
import conversionRate.model.ConversionRate;
import conversionRate.model.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import conversionRate.repository.ConversionRateRepository;
import conversionRate.service.ConversionRateService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Component
public class ConversionRateQueryTask {

    private static final Logger logger = LoggerFactory.getLogger(ConversionRateQueryTask.class);

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Autowired
    private ConversionRateService conversionRateService;

    @Autowired
    private ConversionRateRepository conversionRateRepository;

    @Scheduled(fixedRate = 10000)
    public void scheduleTaskWithFixedRate() {
        logger.info("Fixed Rate Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()) );
        Optional<ConversionInformationDTO> information = conversionRateService.getConversionRateInformationFromExternalService();
        if (information.isPresent() && Currency.USD.toString().equals(information.get().getBase()))
        {
            Double rate = information.get().getRates().get(Currency.KZT.toString());
            if (nonNull(rate))
            {
                Optional<ConversionRate> conversionRateOpt = conversionRateRepository.findByFromAndTo(Currency.USD, Currency.KZT);
                ConversionRate conversionRate = conversionRateOpt.orElse(new ConversionRate(Currency.USD, Currency.KZT));
                conversionRate.setRateDate(new Date(information.get().getTimestamp() * 600000));
                conversionRate.setRate(rate);
                conversionRateRepository.save(conversionRate);

            }
        }
    }
}
