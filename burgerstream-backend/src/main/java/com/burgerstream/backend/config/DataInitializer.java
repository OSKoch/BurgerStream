package com.burgerstream.backend.config;

import com.burgerstream.backend.model.menu.Drink;
import com.burgerstream.backend.model.menu.Side;
import com.burgerstream.backend.model.menu.SizeOption;
import com.burgerstream.backend.model.menu.Burger;
import com.burgerstream.backend.model.order.Order;
import com.burgerstream.backend.model.order.OrderItem;
import com.burgerstream.backend.repository.menu.DrinkRepository;
import com.burgerstream.backend.repository.menu.SideRepository;
import com.burgerstream.backend.repository.menu.SizeOptionRepository;
import com.burgerstream.backend.repository.menu.BurgerRepository;
import com.burgerstream.backend.repository.order.OrderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final SizeOptionRepository sizeOptionRepository;
    private final BurgerRepository burgerRepository;
    private final SideRepository sideRepository;
    private final DrinkRepository drinkRepository;
    private final OrderRepository orderRepository;

    public DataInitializer(SizeOptionRepository sizeOptionRepository,
                           BurgerRepository burgerRepository,
                           SideRepository sideRepository,
                           DrinkRepository drinkRepository,
                           OrderRepository orderRepository) {
        this.sizeOptionRepository = sizeOptionRepository;
        this.burgerRepository = burgerRepository;
        this.sideRepository = sideRepository;
        this.drinkRepository = drinkRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (burgerRepository.count() > 0){
            return;
        }
        // Sizes
        SizeOption smallBeverage = new SizeOption();
        smallBeverage.setLabel("Cold Beverage");
        smallBeverage.setSizeLabel("Small");
        smallBeverage.setExtraPrice(BigDecimal.valueOf(0.00));

        SizeOption mediumBeverage = new SizeOption();
        mediumBeverage.setLabel("Cold Beverage");
        mediumBeverage.setSizeLabel("Medium");
        mediumBeverage.setExtraPrice(BigDecimal.valueOf(5.00));

        SizeOption largeFries = new SizeOption();
        largeFries.setLabel("Fries Pack");
        largeFries.setSizeLabel("Large");
        largeFries.setExtraPrice(BigDecimal.valueOf(10.00));

        SizeOption oneSize = new SizeOption();
        oneSize.setLabel("Bought Product");
        oneSize.setSizeLabel("One size");
        oneSize.setExtraPrice(BigDecimal.valueOf(0.00));

        sizeOptionRepository.saveAll(Set.of(smallBeverage, mediumBeverage, largeFries, oneSize));

        // Burgers
        Burger classicBurger = new Burger();
        classicBurger.setName("Classic Burger");
        classicBurger.setDescription("Juicy beef patty with cheese and lettuce");
        classicBurger.setBasePrice(BigDecimal.valueOf(75.00));
        classicBurger.setImageUrl("burger1.jpg");

        Burger veganDelight = new Burger();
        veganDelight.setName("Vegan Delight");
        veganDelight.setDescription("Plant-based patty with avocado and sprouts");
        veganDelight.setBasePrice(BigDecimal.valueOf(80.00));
        veganDelight.setImageUrl("burger2.jpg");
        veganDelight.setIsVegan(true);
        burgerRepository.saveAll(Set.of(veganDelight, classicBurger));

        //Sides
        Side fries = new Side();
        fries.setName("French Fries");
        fries.setDescription("Crispy golden fries");
        fries.setBasePrice(BigDecimal.valueOf(25.00));
        fries.setImageUrl("side3.jpg");
        fries.setIsShareable(true);
        fries.setSizeOptions(Set.of(largeFries));

        Side macNCheese = new Side();
        macNCheese.setName("Mac n Cheese");
        macNCheese.setDescription("Delicious Mac n Cheese");
        macNCheese.setBasePrice(BigDecimal.valueOf(30.00));
        macNCheese.setImageUrl("side4.jpg");
        macNCheese.setSizeOptions(Set.of(oneSize));
        sideRepository.saveAll(Set.of(macNCheese, fries));

        //Drinks
        Drink cola = new Drink();
        cola.setName("Cola");
        cola.setDescription("Refreshing soft drink");
        cola.setBasePrice(BigDecimal.valueOf(20.00));
        cola.setImageUrl("drink5.jpg");
        cola.setIsCarbonated(true);
        cola.setIsLactoseFree(true);
        cola.setSizeOptions(Set.of(mediumBeverage, smallBeverage));

        Drink milkshake = new Drink();
        milkshake.setName("Chocolate Milkshake");
        milkshake.setDescription("Thick creamy milkshake");
        milkshake.setBasePrice(BigDecimal.valueOf(35.00));
        milkshake.setImageUrl("drink6.jpg");
        milkshake.setSizeOptions(Set.of(mediumBeverage, smallBeverage));
        drinkRepository.saveAll(Set.of(milkshake, cola));

        //Order
        Order order = new Order();
        order.setDate(LocalDateTime.now());
        order.setOrderEndPrice(BigDecimal.valueOf(100.00));
        order.setCustomerName("John Doe");
        order.setCustomerEmail("John@Example.com");

        OrderItem item1 = new OrderItem();
        item1.setMenuItem(cola);
        item1.setSizeOption(mediumBeverage);
        item1.setAmount(1);
        item1.setItemEndPrice(BigDecimal.valueOf(30.00));

        OrderItem item2 = new OrderItem();
        item2.setMenuItem(fries);
        item2.setSizeOption(largeFries);
        item2.setAmount(2);
        item2.setItemEndPrice(BigDecimal.valueOf(70.00));

        order.addOrderItem(item1);
        order.addOrderItem(item2);
        orderRepository.save(order);
    }
}