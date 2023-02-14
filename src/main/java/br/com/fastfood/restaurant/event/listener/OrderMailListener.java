package br.com.fastfood.restaurant.event.listener;

import br.com.fastfood.restaurant.entity.Order;
import br.com.fastfood.restaurant.event.OrderCanceledEvent;
import br.com.fastfood.restaurant.event.OrderPlacedEvent;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class OrderMailListener {
    private final JavaMailSender sender;

    @Autowired
    public OrderMailListener(JavaMailSender sender) {
        this.sender = sender;
    }

    @EventListener
    public void sendOrderPlacedEmail(OrderPlacedEvent event) {
        try {
            Order order = event.getOrder();

            MimeMessage message = this.sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setTo(order.getCustomer().getEmail());
            helper.setSubject("Your new order was registered!");
            helper.setText(String.format("<h1>Hi, %s!</h1> <p>Your new order ID is %s. Thank you!</p>", order.getCustomer().getName(), order.getId()), true);

            this.sender.send(message);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @EventListener
    public void sendOrderCanceledEmail(OrderCanceledEvent event) {
        try {
            Order order = (Order) event.getSource();
            MimeMessage message = this.sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setTo(order.getCustomer().getEmail());
            helper.setSubject("Your order was canceled");
            helper.setText(String.format("Hello, %s. Your order %s was canceled.", order.getCustomer().getName(), order.getId()), true);

            this.sender.send(message);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
