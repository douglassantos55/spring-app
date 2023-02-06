package br.com.fastfood.restaurant.payment;

public abstract class PaymentMethodBuilder {
    public static final PaymentMethod get(String method) throws InvalidPaymentMethodException {
        switch (method) {
            case "cc":
                return new CreditCardPaymentMethod();
            default:
                throw new InvalidPaymentMethodException();
        }
    }
}
