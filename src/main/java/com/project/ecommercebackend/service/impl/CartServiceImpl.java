package com.project.ecommercebackend.service.impl;

import com.project.ecommercebackend.dto.response.CartResponse;
import com.project.ecommercebackend.dto.response.ProductResponse;
import com.project.ecommercebackend.exception.APIException;
import com.project.ecommercebackend.exception.DuplicateResourceException;
import com.project.ecommercebackend.exception.InsufficientStockException;
import com.project.ecommercebackend.exception.OutOfStockException;
import com.project.ecommercebackend.exception.ResourceNotFoundException;
import com.project.ecommercebackend.model.Cart;
import com.project.ecommercebackend.model.CartItem;
import com.project.ecommercebackend.model.Product;
import com.project.ecommercebackend.repository.CartItemRepository;
import com.project.ecommercebackend.repository.CartRepository;
import com.project.ecommercebackend.repository.ProductRepository;
import com.project.ecommercebackend.service.CartService;
import com.project.ecommercebackend.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final AuthUtil authUtil;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public CartResponse addProductToCart(Long productId, Integer quantity) {
        Cart cart = getOrCreateCart();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem existingItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);
        if (existingItem != null) {
            throw new DuplicateResourceException("CartItem", "productId", productId);
        }

        if (product.getQuantity() == 0) {
            throw new OutOfStockException(product.getProductName());
        }

        if (product.getQuantity() < quantity) {
            throw new InsufficientStockException(product.getProductName(), quantity, product.getQuantity());
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());

        cartItemRepository.save(newCartItem);

        cart.setTotalAmount(
                cart.getTotalAmount().add(product.getSpecialPrice().multiply(BigDecimal.valueOf(quantity))));
        cartRepository.save(cart);

        return mapCartToResponse(cart);
    }

    @Override
    public List<CartResponse> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        if (carts.isEmpty()) {
            throw new APIException("No cart exists");
        }
        return carts.stream()
                .map(this::mapCartToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CartResponse getLoggedInUserCart() {
        String emailId = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(emailId);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "email", emailId);
        }
        return getCart(emailId, cart.getCartId());
    }

    @Override
    public CartResponse getCart(String emailId, Long cartId) {
        Cart cart = cartRepository.findCartByEmailAndCartId(emailId, cartId);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "cartId", cartId);
        }

        CartResponse cartResponse = modelMapper.map(cart, CartResponse.class);
        List<ProductResponse> products = cart.getCartItems().stream()
                .map(item -> {
                    ProductResponse response = modelMapper.map(item.getProduct(), ProductResponse.class);
                    response.setQuantity(item.getQuantity());
                    return response;
                })
                .toList();
        cartResponse.setProducts(products);
        return cartResponse;
    }

    @Transactional
    @Override
    public CartResponse updateProductQuantityInCart(Long productId, Integer quantity) {
        String emailId = authUtil.loggedInEmail();
        Cart userCart = cartRepository.findCartByEmail(emailId);
        Long cartId = userCart.getCartId();

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if (product.getQuantity() == 0) {
            throw new OutOfStockException(product.getProductName());
        }

        if (product.getQuantity() < quantity) {
            throw new InsufficientStockException(product.getProductName(), quantity, product.getQuantity());
        }

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItem == null) {
            throw new ResourceNotFoundException("CartItem", "productId", productId);
        }

        int newQuantity = cartItem.getQuantity() + quantity;

        if (newQuantity < 0) {
            throw new APIException("The resulting quantity cannot be negative.");
        }

        if (newQuantity == 0) {
            deleteProductFromCart(cartId, productId);
        } else {
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setQuantity(newQuantity);
            cartItem.setDiscount(product.getDiscount());
            cart.setTotalAmount(cart.getTotalAmount()
                    .add(product.getSpecialPrice().multiply(BigDecimal.valueOf(quantity))));
            cartItemRepository.save(cartItem);
            cartRepository.save(cart);
        }

        return mapCartToResponse(cart);
    }

    @Transactional
    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItem == null) {
            throw new ResourceNotFoundException("Product", "productId", productId);
        }

        cart.setTotalAmount(cart.getTotalAmount().subtract(
                cartItem.getProductPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()))));

        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId, productId);

        return "Product " + cartItem.getProduct().getProductName() + " removed from the cart!";
    }

    @Override
    @Transactional
    public void updateProductInCarts(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItem == null) {
            throw new ResourceNotFoundException("CartItem", "productId", productId);
        }

        BigDecimal cartPrice = cart.getTotalAmount()
                .subtract(cartItem.getProductPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));

        cartItem.setProductPrice(product.getSpecialPrice());
        cart.setTotalAmount(cartPrice
                .add(cartItem.getProductPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()))));

        cartItemRepository.save(cartItem);
    }

    private Cart getOrCreateCart() {
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if (userCart != null) {
            return userCart;
        }
        Cart cart = new Cart();
        cart.setTotalAmount(BigDecimal.ZERO);
        cart.setUser(authUtil.loggedInUser());
        return cartRepository.save(cart);
    }

    private CartResponse mapCartToResponse(Cart cart) {
        CartResponse cartResponse = modelMapper.map(cart, CartResponse.class);
        List<ProductResponse> products = cart.getCartItems().stream()
                .map(item -> {
                    ProductResponse response = modelMapper.map(item.getProduct(), ProductResponse.class);
                    response.setQuantity(item.getQuantity());
                    return response;
                })
                .toList();
        cartResponse.setProducts(products);
        return cartResponse;
    }
}
