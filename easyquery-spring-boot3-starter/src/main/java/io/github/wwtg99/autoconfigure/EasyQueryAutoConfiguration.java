package io.github.wwtg99.autoconfigure;

import io.github.wwtg99.core.extractor.ExtractorHolder;
import io.github.wwtg99.core.extractor.FilterExtractor;
import io.github.wwtg99.core.extractor.SearcherExtractor;
import io.github.wwtg99.core.extractor.SorterExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EasyQueryAutoConfiguration {

    @Bean
    public ExtractorHolder extractorHolder() {
        return new ExtractorHolder()
                .addExtractor(new FilterExtractor())
                .addExtractor(new SearcherExtractor())
                .addExtractor(new SorterExtractor());
    }
}
