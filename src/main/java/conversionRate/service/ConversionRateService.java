package conversionRate.service;

import conversionRate.dto.ConversionInformationDTO;

import java.util.Optional;

public interface ConversionRateService {
    Optional<ConversionInformationDTO>  getConversionRateInformationFromExternalService();

}
