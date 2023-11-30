package ru.nk.rschir.components.carts;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nk.rschir.components.carts.dto.CartPutResult;
import ru.nk.rschir.components.products.ProductEntity;
import ru.nk.rschir.components.products.ProductRepo;
import ru.nk.rschir.components.stocks.StockRepo;
import ru.nk.rschir.components.stocks.dto.StockProduct;
import ru.nk.rschir.general.requests.ServiceFunctions;
import ru.nk.rschir.types.StatusCode;
import ru.nk.rschir.users.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CartService {
    private final ServiceFunctions functions;
    private final CartRepo cartRepo;
    private final StockRepo stockRepo;
    private final ProductRepo productRepo;

    public StatusCode save(CartEntity cart, HttpServletRequest request) {
        User user = functions.getUserByHttpRequest(request);
        if (user == null) {
            return StatusCode.create(403);
        }

        cart.setUserId(user.getId());

        cartRepo.save(cart);

        return StatusCode.create(200);
    }
    public CartPutResult change(CartEntity cart, HttpServletRequest request) {
        User user = functions.getUserByHttpRequest(request);
        if (user == null) {
            return CartPutResult.builder().status(403).build();
        }

        CartEntity dbCart = cartRepo.findByUserId(user.getId()).orElse(null);
        if (dbCart == null) {
            return CartPutResult.builder().status(500).build();
        }

        cart.setUserId(user.getId());
        cart.setId(dbCart.getId());

        List<StockProduct> products = cart.getProducts();

        List<StockProduct> mergedProducts = new ArrayList<>();
        List<Long> requestedProductsIds = new ArrayList<>();
        for (StockProduct product : products) {
            requestedProductsIds.add(product.getProductId());

            boolean amountChanged = false;
            for (StockProduct mergedProduct : mergedProducts) {
                if (Objects.equals(mergedProduct.getProductId(), product.getProductId())) {
                    mergedProduct.setAmount(mergedProduct.getAmount() + product.getAmount());
                    amountChanged = true;
                    break;
                }
            }
            if (!amountChanged) {
                mergedProducts.add(product);
            }
        }

        List<StockProduct> notExistedProducts = new ArrayList<>();
        for (StockProduct product : mergedProducts) {
            notExistedProducts.add(StockProduct.builder()
                    .productId(product.getProductId())
                    .amount(product.getAmount())
                    .build());
        }

        List<Object[]> remaining = stockRepo.remainingGoodsByProductsIds(requestedProductsIds);
        for (Object[] data : remaining) {
            for (StockProduct product : notExistedProducts) {
                int dbAmount = Integer.parseInt(String.valueOf(data[1]));

                if (Objects.equals(product.getProductId(), data[0])) {
                    int productAmount = product.getAmount();
                    if (productAmount > dbAmount) {
                        product.setAmount(productAmount - dbAmount);
                    } else {
                        notExistedProducts.remove(product);
                    }
                    break;
                }
            }
        }

        CartPutResult result = new CartPutResult();
        result.setProducts(notExistedProducts);
        if (notExistedProducts.size() > 0) {
            result.setStatus(404);
        } else {

            List<ProductEntity> productsData = productRepo.findAllById(requestedProductsIds);
            int sum = 0;
            for (ProductEntity product : productsData) {
                int amount = mergedProducts.stream().filter(
                        (stockProduct) -> Objects.equals(stockProduct.getProductId(), product.getId())
                ).findFirst().get().getAmount();
                sum += (product.getPrice() * amount);
            }
            cart.setSum(sum);

            result.setStatus(200);
            cartRepo.save(cart);

        }

        return result;
    }
}
