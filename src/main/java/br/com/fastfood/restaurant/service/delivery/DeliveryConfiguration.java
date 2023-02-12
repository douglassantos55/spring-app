package br.com.fastfood.restaurant.service.delivery;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

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
    @Profile("production")
    public ManagedChannel channel() {
        return ManagedChannelBuilder
                .forAddress(this.getUrl(), this.getPort())
                .usePlaintext()
                .build();
    }

    @Bean
    public ManagedChannel fakeChannel() throws Exception {
        String serverName = InProcessServerBuilder.generateName();

        InProcessServerBuilder
                .forName(serverName)
                .directExecutor()
                .addService(new FakeDeliveryServerImpl())
                .build()
                .start();

        return InProcessChannelBuilder
                .forName(serverName)
                .directExecutor()
                .build();
    }
}
