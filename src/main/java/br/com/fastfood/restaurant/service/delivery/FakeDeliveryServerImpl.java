package br.com.fastfood.restaurant.service.delivery;

import io.grpc.stub.StreamObserver;

import java.util.List;

public class FakeDeliveryServerImpl extends DeliveryGrpc.DeliveryImplBase {
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
