package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    
    // 주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송 정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);
        // cascade 옵션 때문에 order에만 저장해주면 deliver와 order item 모두 엔티티가 함께 생성된다.
        // delivery와 order item은 order에 의해서만 사용됨.
        // 다른 것이 참조할 수 없는 private owner인 경우 cascade all 옵션을 사용한다.
        // order를 persist할 때 delivery와 order item을 함께 persist 해야하는 경우임

        return order.getId();
    }

    @Transactional
    public void cancelOrder(Long orderId){
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        // 주문 취소
        order.cancle();
        // JPA 사용하면 엔티티의 값이 바뀔 때 알아서 변경 내역 감지해서 변경된 엔티티에 대해 알아서 업데이트 쿼리가 날라간다.
        // 더티 체킹. 변경 내역 감지 기능
        // MyBatis나 Jdbc template 사용하면 이외 변경 사항(item)에 대해 일일이 쿼리 다 날려줘야함.
    }
}
