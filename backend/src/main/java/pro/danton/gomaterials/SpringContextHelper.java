package pro.danton.gomaterials;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import lombok.SneakyThrows;

@Component
public class SpringContextHelper implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    @SneakyThrows
    public static ConfigurableApplicationContext loadContext(String[] args) {
        @SpringBootApplication
        class App {
        }

        ConfigurableApplicationContext context = new SpringApplicationBuilder(App.class)
                .web(WebApplicationType.NONE)
                .run(args);
        return context;
    }
}
