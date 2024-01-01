package jpabook.jpashop.service.query;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.dto.OrderDto;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true) // OSIV를 끈 상태에서는 이렇게 트랜잭션을 사용하여 호출하도록 한다. 트랜잭션 내에서는 영속성 컨텍스트가 살아있음
@RequiredArgsConstructor
@Service
// 실시간 데이터 요청이 많은 어플리케이션에서는 OSIV를 끄는 것이 좋다. 데이터 커넥션을 빠르게 반환
public class OrderQueryService {

    private final OrderRepository orderRepository;

    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return result;
    }

    public List<OrderDto> ordersV3() {
        // 쿼리 한 방에 나감
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return result;
    }

    public List<OrderDto> ordersV3_page(int offset, int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return result;
    }
}
