package jpabook.jpashop.api;

import java.util.List;
import java.util.stream.Collectors;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.dto.OrderDto;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.service.query.OrderQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderQueryService orderQueryService;

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        return orderQueryService.ordersV2();
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        return orderQueryService.ordersV3();
    }

    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                        @RequestParam(value = "limit", defaultValue = "100") int limit) {
        return orderQueryService.ordersV3_page(offset, limit);
    }
}
