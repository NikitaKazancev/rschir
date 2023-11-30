package ru.nk.rschir.documents.orders;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nk.rschir.components.carts.CartEntity;
import ru.nk.rschir.components.carts.CartRepo;
import ru.nk.rschir.components.carts.dto.CartPutResult;
import ru.nk.rschir.components.products.ProductEntity;
import ru.nk.rschir.components.products.ProductRepo;
import ru.nk.rschir.components.stocks.StockEntity;
import ru.nk.rschir.components.stocks.StockRepo;
import ru.nk.rschir.components.stocks.dto.StockProduct;
import ru.nk.rschir.general.requests.ServiceFunctions;
import ru.nk.rschir.types.ResponseWithStatus;
import ru.nk.rschir.types.StatusCode;
import ru.nk.rschir.users.User;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepo orderRepo;
    private final ServiceFunctions functions;
    private final StockRepo stockRepo;
    private final CartRepo cartRepo;

    public ResponseWithStatus<List<OrderEntity>> findAll(HttpServletRequest request) {
        if (functions.isUser(request)) {
            return ResponseWithStatus.create(403, new ArrayList<>());
        }

        return ResponseWithStatus.create(200, orderRepo.findAll());
    }
    public CartPutResult save(HttpServletRequest request) {
        User user = functions.getUserByHttpRequest(request);
        if (user == null) {
            return CartPutResult.builder().status(403).build();
        }

        CartEntity cart = cartRepo.findByUserId(user.getId()).orElse(null);
        if (cart == null) {
            return CartPutResult.builder().status(500).build();
        }
        for (int i = 0; i < cart.getProducts().size(); i++) {
            StockProduct product = cart.getProducts().get(i);
            if (product.getAmount() == 0) {
                cart.getProducts().remove(product);
                i--;
            }
        }
        if (cart.getProducts().size() == 0) {
            return CartPutResult.builder().status(301).build();
        }

        List<StockProduct> products = cart.getProducts();
        List<StockProduct> productsToSave = new ArrayList<>();
        for (StockProduct product : products) {
            productsToSave.add(StockProduct.builder()
                            .productId(product.getProductId())
                            .amount(product.getAmount())
                    .build());
        }

        List<Long> requestedProductsIds = new ArrayList<>();
        for (StockProduct product : products) {
            requestedProductsIds.add(product.getProductId());
        }

        List<Object[]> goods = stockRepo.goodsOfEachStockByProductsIds(requestedProductsIds);

        List<StockEntity> stocksToSave = new ArrayList<>();
        StockEntity currentStock = new StockEntity();
        List<StockProduct> currentProducts = new ArrayList<>();

        int goodsAmount = goods.size();
        for (int i = 0; i < goodsAmount; i++) {
            Object[] data = goods.get(i);
            Long stockId = Long.parseLong(String.valueOf(data[0]));
            String stockName = String.valueOf(data[1]);
            Long productId = Long.parseLong(String.valueOf(data[2]));
            int amount = Integer.parseInt(String.valueOf(data[3]));

            if (i == 0) {
                currentStock.setId(stockId);
                currentStock.setName(stockName);
            }

            if (!Objects.equals(currentStock.getId(), stockId)) {
                currentStock.setProducts(new ArrayList<>(currentProducts));
                stocksToSave.add(currentStock);

                currentProducts.clear();
                currentStock = new StockEntity();

                currentStock.setId(stockId);
                currentStock.setName(stockName);
            }

            for (StockProduct product : products) {
                if (Objects.equals(product.getProductId(), productId)) {
                    int productAmount = product.getAmount();
                    if (amount >= productAmount) {
                        amount -= productAmount;
                        products.remove(product);
                    } else {
                        product.setAmount(productAmount - amount);
                        amount = 0;
                    }
                    break;
                }
            }
            currentProducts.add(StockProduct.builder()
                    .productId(productId)
                    .amount(amount)
                    .build());

            if (i == goodsAmount - 1) {
                currentStock.setProducts(currentProducts);
                stocksToSave.add(currentStock);
            }
        }

        CartPutResult result = new CartPutResult();
        result.setProducts(products);
        if (products.size() > 0) {
            result.setStatus(404);
            return result;
        }

        stockRepo.saveAll(stocksToSave);

        orderRepo.save(OrderEntity.builder()
                .userId(user.getId())
                .products(productsToSave)
                .sum(cart.getSum())
                .build());

        cart.setProducts(new ArrayList<>());
        cartRepo.save(cart);
        result.setStatus(200);

        return result;
    }
}
