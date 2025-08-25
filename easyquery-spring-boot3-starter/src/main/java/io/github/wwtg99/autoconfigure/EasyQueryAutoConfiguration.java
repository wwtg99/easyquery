package io.github.wwtg99.autoconfigure;

import io.github.wwtg99.core.extractor.ExtractorHolder;
import io.github.wwtg99.core.extractor.FilterExtractor;
import io.github.wwtg99.core.extractor.SearcherExtractor;
import io.github.wwtg99.core.extractor.SorterExtractor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class EasyQueryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ExtractorHolder extractorHolder() {
        return new ExtractorHolder()
                .addExtractor(new FilterExtractor())
                .addExtractor(new SearcherExtractor())
                .addExtractor(new SorterExtractor());
    }
}
