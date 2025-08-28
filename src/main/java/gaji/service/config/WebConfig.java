package gaji.service.config;

import gaji.service.domain.post.converter.PostStatusConverter;
import gaji.service.domain.post.converter.PostTypeConverter;
import gaji.service.domain.post.converter.SortTypeConverter;
import gaji.service.domain.recruit.converter.CategoryConverter;
import gaji.service.domain.recruit.converter.FilterConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
//WebConfig
    @Override

    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new PostTypeConverter());
        registry.addConverter(new SortTypeConverter());
        registry.addConverter(new FilterConverter());
        registry.addConverter(new PostStatusConverter());
        registry.addConverter(new CategoryConverter());
    }
}
