package br.com.fastfood.restaurant.authentication;

import br.com.fastfood.restaurant.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository extends JdbcUserDetailsManager {
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private br.com.fastfood.restaurant.repository.CustomerRepository repository;

    public CustomerRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected List<UserDetails> loadUsersByUsername(String username) {
        List<UserDetails> users = new ArrayList<>();
        List<Customer> customers = this.repository.findAllByEmail(username);
        System.out.println(customers);
        for (Customer customer : customers) {
            users.add(
                    User
                            .withUsername(username)
                            .password(customer.getPassword())
                            .authorities(customer.getAuthorities())
                            .build()
            );
        }
        return users;
    }

    @Override
    protected List<GrantedAuthority> loadUserAuthorities(String username) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        Customer customer = this.repository.findByEmail(username);
        authorities.addAll(customer.getAuthorities());
        return authorities;
    }
}
