package conversionRate.service.impl;

import conversionRate.dto.ConversionInformationDTO;
import conversionRate.service.ConversionRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class ConversionRateServiceImpl implements ConversionRateService {

    private final static Logger logger = LoggerFactory.getLogger(ConversionRateServiceImpl.class);


    @Autowired
    private RestTemplate restTemplate;

    @Value("${conversionrate.url}")
    private String url;

    @Override
    public Optional<ConversionInformationDTO> getConversionRateInformationFromExternalService() {

        try {
            return Optional.ofNullable(restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    ConversionInformationDTO.class).getBody());

        } catch (Exception e) {
            logger.error("Couldn't get ConversionInformationDTO ", e);
        }
        return Optional.empty();
    }
}
