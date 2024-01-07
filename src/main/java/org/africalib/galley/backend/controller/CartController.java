package org.africalib.galley.backend.controller;

import org.africalib.galley.backend.entity.Cart;
import org.africalib.galley.backend.entity.Item;
import org.africalib.galley.backend.repository.CartRepository;
import org.africalib.galley.backend.repository.ItemRepository;
import org.africalib.galley.backend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
public class CartController {


    @Autowired
    ItemRepository itemRepository;

    @Autowired
    JwtService jwtService;

    @Autowired
    CartRepository cartRepository;


    @GetMapping("/api/cart/items")
    public ResponseEntity getCartItem(@CookieValue(value = "token", required = false) String token){
        System.out.println("token : "+token);
        if(!jwtService.isValid(token)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        int memberId = jwtService.getId(token);
        List<Cart> carts = cartRepository.findByMemberId(memberId);
        List<Integer> itemIds = carts.stream().map(Cart::getItemId).toList();

        List<Item> items = itemRepository.findByIdIn(itemIds);
        System.out.println(items);

        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @PostMapping("/api/cart/items/{itemId}")
    public ResponseEntity pushCartItem(@PathVariable("itemId") int itemId,
                                           @CookieValue(value = "token", required = false) String token){
        if(!jwtService.isValid(token)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        int memberId = jwtService.getId(token);
        Cart cart =  cartRepository.findByMemberIdAndItemId(memberId, itemId);

        if(cart == null){
            Cart newCart = new Cart();
            newCart.setMemberId(memberId);
            newCart.setItemId(itemId);
            cartRepository.save(newCart);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/api/cart/items/{itemId}")
    public ResponseEntity removeCartItem(@PathVariable("itemId") int itemId,
                                       @CookieValue(value = "token", required = false) String token) {
        if (!jwtService.isValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        int memberId = jwtService.getId(token);
        Cart cart = cartRepository.findByMemberIdAndItemId(memberId, itemId);
        cartRepository.delete(cart);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
