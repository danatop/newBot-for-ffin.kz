package conversionRate.repository;

import conversionRate.model.ConversionRate;
import conversionRate.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConversionRateRepository extends JpaRepository<ConversionRate, Long> {
    Optional<ConversionRate> findByFromAndTo(Currency from, Currency to);
}
