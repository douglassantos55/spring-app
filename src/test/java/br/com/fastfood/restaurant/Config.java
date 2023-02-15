package br.com.fastfood.restaurant;

import br.com.fastfood.restaurant.service.delivery.FakeDeliveryServerImpl;
import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.mail.javamail.JavaMailSender;

@TestConfiguration
public class Config {
    @MockBean
    private JavaMailSender mockMailer;

    @Bean
    public EmbeddedDatabase database() {
        return new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .ignoreFailedDrops(true)
                .build();
    }

    @Bean
    public JavaMailSender mailer() {
        return this.mockMailer;
    }

    @Bean
    public ManagedChannel channel() throws Exception {
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
