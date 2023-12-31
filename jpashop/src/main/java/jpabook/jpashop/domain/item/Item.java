package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {
    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<Category>();

    // 비즈니스 로직
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    // setter를 가지고 바깥에서 변경해서 넣는 것이 아니라
    // 이렇게 도메인 모델 안에서 값 변경 로직을 작성해준다. 
    // 이것이 가장 객체 지향적인 방식
    public void removeStock(int quentity){
        int restStock = this.stockQuantity - quentity;
        if (restStock < 0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
