package br.com.fastfood.restaurant.service.delivery;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "delivery")
public class DeliveryConfiguration {
    private String url;

    private int port;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Bean
    public Channel channel() {
        return ManagedChannelBuilder.forAddress(this.getUrl(), this.getPort()).usePlaintext().build();
    }
}
