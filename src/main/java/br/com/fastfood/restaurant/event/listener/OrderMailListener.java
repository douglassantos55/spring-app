package br.com.fastfood.restaurant.event.listener;

import br.com.fastfood.restaurant.entity.Order;
import br.com.fastfood.restaurant.event.OrderCanceledEvent;
import br.com.fastfood.restaurant.event.OrderPlacedEvent;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

@Component
public class OrderMailListener {
    private final JavaMailSender sender;

    private final Configuration cfg;

    @Autowired
    public OrderMailListener(JavaMailSender sender, Configuration cfg) {
        this.cfg = cfg;
        this.sender = sender;
    }

    @EventListener
    public void sendOrderPlacedEmail(OrderPlacedEvent event) {
        try {
            Order order = event.getOrder();

            Template template = this.cfg.getTemplate("email/order_placed.ftl");

            MimeMessage message = this.sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setTo(order.getCustomer().getEmail());
            helper.setSubject("Your new order was registered!");
            helper.setText(FreeMarkerTemplateUtils.processTemplateIntoString(template, order), true);

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

            Template template = this.cfg.getTemplate("email/order_canceled.ftl");
            helper.setText(FreeMarkerTemplateUtils.processTemplateIntoString(template, order), true);

            this.sender.send(message);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
