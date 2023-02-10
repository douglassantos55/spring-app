package br.com.fastfood.restaurant.service.delivery;

import br.com.fastfood.restaurant.entity.OrderItem;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class GrpcDeliveryService implements DeliveryService {
    private DeliveryGrpc.DeliveryBlockingStub client;

    @Autowired
    public GrpcDeliveryService(DeliveryConfiguration config) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(config.getUrl(), config.getPort()).usePlaintext().build();
        this.client = DeliveryGrpc.newBlockingStub(channel);
    }

    public int getQuote(String source, String destination, List<OrderItem> items) {
        GetQuoteRequest.Builder quoteBuilder = GetQuoteRequest.newBuilder();

        for (OrderItem item : items) {
            Item.Builder itemBuilder = Item.newBuilder();
            itemBuilder.setQty(item.getQty());

            // Fake dimensions
            itemBuilder.setDepth(10).setHeight(2).setWeight(5);

            quoteBuilder.addItems(itemBuilder.build());
        }

        quoteBuilder.setCarrier("local").setDestination(destination).setOrigin(source);
        GetQuoteRequest request = quoteBuilder.build();

        Quote quote = this.client.getQuote(request);
        return (int) quote.getValue() * 100;
    }
}
