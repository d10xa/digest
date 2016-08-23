package ru.d10xa.jobtracker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import ru.d10xa.jobtracker.job.HexGenerator;
import ru.d10xa.jobtracker.job.HexGeneratorImpl;
import ru.d10xa.jobtracker.service.HexService;
import ru.d10xa.jobtracker.service.HexServiceImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class Config {

    @Bean
    public HexService hexService() {
        return new HexServiceImpl(executorService(), hexGenerator());
    }

    @Bean
    public HexGenerator hexGenerator() {
        return new HexGeneratorImpl();
    }

    @Bean(destroyMethod = "shutdownNow")
    public ExecutorService executorService() {
        return Executors.newCachedThreadPool();
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
