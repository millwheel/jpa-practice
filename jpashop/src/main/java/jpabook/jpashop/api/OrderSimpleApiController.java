package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.dto.OrderSimpleQueryDto;
import jpabook.jpashop.dto.SimpleOrderDto;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.OrderSimpleQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

//    @GetMapping("/api/v1/simple-orders")
//    public List<Order> ordersV1() {
//        List<Order> all = orderRepository.findAllByString(new OrderSearch());
//        for (Order order : all) {
//            order.getMember().getName(); // getName을 하게 되면 실제 객체 필요. LAZY가 강제 초기화가 된다. member까지 끌고 온다.
//            order.getDelivery().getAddress(); // 마찬가지로 Address를 끌고 온다. LAZY 강체 초기화
//        }
//        return all;
//    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        // Order 가 여러 개
        // 1 + N (member) + N (delivery) 쿼리 날라감
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(toList());
        return result;
    }

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        // 실제 성능 문제의 90%는 N + 1 문제 관련
        // 이를 해결하기 위해 fetch join을 사용한다.
        // 쿼리 딱 한 방에 가져옴
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(toList());
        return result;
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        // 이렇게 DTO로 바로 가져오는 방식은 재사용성이 조금 떨어진다.
        return orderSimpleQueryRepository.findOrderDtos();
    }
}
