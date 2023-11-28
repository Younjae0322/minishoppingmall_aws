package com.javalab.boot.domain.item;

import com.javalab.boot.domain.cart_item.Cart_item;
import com.javalab.boot.domain.order_item.Order_item;
import com.javalab.boot.domain.user.User;
import com.javalab.boot.dto.ItemDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString(exclude = "user")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // 아이템 고유번호

    private String name; // 상품이름

    private int price; // 가격

    private int stock; // 재고

    private boolean isSoldout; // 판매여부

    private int count; // 팔린 갯수

    private String text; // 상품설명

    private String filename; // 상품 사진 파일이름

    private String filepath; // 상품 사진 파일경로

    // 판매자랑 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "item")
    @Builder.Default
    private List<Cart_item> cart_items = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Order_item> order_items = new ArrayList<>();

    public void changeStock(int count){
        this.stock = this.stock + count;
    }

    /**
     * Entity -> Dto 변환 메소드
     */
    public static ItemDto fromEntity(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setPrice(item.getPrice());
        itemDto.setStock(item.getStock());
        itemDto.setSoldout(item.isSoldout());
        itemDto.setCount(item.getCount());
        itemDto.setText(item.getText());
        itemDto.setFilename(item.getFilename());
        itemDto.setFilepath(item.getFilepath());

        // 판매자의 아이디만을 전달
        itemDto.setUserId(item.getUser().getId());

        // 다른 필요한 필드들도 추가할 수 있습니다.

        return itemDto;
    }
}
