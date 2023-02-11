package br.com.fastfood.restaurant;

import br.com.fastfood.restaurant.entity.OrderItem;
import br.com.fastfood.restaurant.service.delivery.*;
import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
public class GrpcDeliveryTests {
    private GrpcDeliveryService service;

    @Rule
    private final GrpcCleanupRule rule = new GrpcCleanupRule();

    @BeforeEach
    public void setup() throws Exception {
        String serverName = InProcessServerBuilder.generateName();
        rule.register(
                InProcessServerBuilder
                        .forName(serverName)
                        .directExecutor()
                        .addService(new TestServiceImpl())
                        .build()
                        .start()
        );

        ManagedChannel channel = rule.register(
                InProcessChannelBuilder
                        .forName(serverName)
                        .directExecutor()
                        .build()
        );

        this.service = new GrpcDeliveryService(channel);
    }

    @Test
    public void getQuote() {
        List<OrderItem> items = new ArrayList<>();
        items.add(OrderItem.create("bread", 500, 100));
        int quote = this.service.getQuote("Campinas, São Paulo, SP", "Santos, São Paulo, SP", items);

        assertEquals("quote should be 20000", 20000, quote);
    }

    @Test
    public void getQuoteException() {
        List<OrderItem> items = new ArrayList<>();
        items.add(OrderItem.create("bread", 500, 100));
    }

    private final class TestServiceImpl extends DeliveryGrpc.DeliveryImplBase {
        @Override
        public void getQuote(GetQuoteRequest request, StreamObserver<Quote> responseObserver) {
            int total = 0;
            List<Item> items = request.getItemsList();
            for (Item item : items) {
                total += item.getQty();
            }

            Quote reply = Quote.newBuilder().setValue(total * 2).build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }

    }
}
