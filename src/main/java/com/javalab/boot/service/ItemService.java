package com.javalab.boot.service;

import com.javalab.boot.domain.item.Item;
import com.javalab.boot.domain.item.ItemRepository;
import com.javalab.boot.domain.user.User;
import com.javalab.boot.domain.user.UserRepository;
import com.javalab.boot.dto.ItemDto;
import com.javalab.boot.dto.UserDto;
import com.javalab.boot.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Log4j2
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    // 상품 등록
    public void save(ItemDto itemDto, MultipartFile file, List<MultipartFile> additionalImages) throws Exception {
        Item item = ItemDto.dtoToEntity(itemDto); // ItemDto를 Item 엔터티로 변환
         // 폴더가 없을 경우 생성
        File directory = new File(projectPath);
        if (!directory.exists()) {
        directory.mkdirs();
        }

        if (file != null) {
            String projectPath = System.getProperty("user.dir") + "/files";
            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(projectPath, fileName);
            file.transferTo(saveFile);

            item.setFilename(fileName);
            item.setFilepath("/files/" + fileName);
        } else {
            item.setFilepath("https://dummyimage.com/450x300/dee2e6/6c757d.jpg");
        }

// 추가 이미지 처리
        List<String> additionalImagePaths = new ArrayList<>();
        if (additionalImages != null) {
            for (MultipartFile additionalImage : additionalImages) {
                // 추가 이미지 파일을 저장하는 로직
                String projectPath = System.getProperty("user.dir") + "\\files\\";
                UUID uuid = UUID.randomUUID();
                String fileName = uuid + "_" + additionalImage.getOriginalFilename();
                File saveFile = new File(projectPath, fileName);

                additionalImage.transferTo(saveFile);
                additionalImagePaths.add("/files/" + fileName);
            }
        }
        // 아래 라인을 변경
        // item.setAdditionalImages(additionalImagePaths);
        item.setAdditionalImages(additionalImagePaths);
        item.setCount(item.getStock());
        item.setSoldout(true);

        itemRepository.save(item);
    }
    /*// 전체 상품 목록 조회
    public List<Item> itemList(){
        return itemRepository.findAll();
    }*/

    // 전체 상품 목록 조회
    public List<Item> itemList() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return itemRepository.findAll(sort);
    }
    // 특정 상품 조회
    public ItemDto itemView(Long id){
        Optional<Item> result = itemRepository.findById(id);
        Item item = result.orElseThrow();
        ItemDto itemDto = Item.fromEntity(item);

        log.info("itemDto : " + itemDto);

        return itemDto;
    }
    // 특정 유저 상품 조회
    @Transactional
    public List<Item> userItemView(UserDto userDto){
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + userDto.getId()));

        // User에 연결된 아이템 리스트 반환
        return user.getItems();
    }
    // 특정 상품 수정
    public void itemModify(Item item, Long id, MultipartFile file)throws Exception{
        String projectPath = System.getProperty("user.dir") + "\\files\\";
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(projectPath,fileName);
        file.transferTo(saveFile);

        Item tempItem = itemRepository.findItemById(id);
        tempItem.setName(item.getName());
        tempItem.setPrice(item.getPrice());
        tempItem.setStock(item.getStock());
        //tempItem.setSoldout(item.isSoldout());
        //tempItem.setCount(item.getCount());
        tempItem.setText(item.getText());
        tempItem.setFilename(fileName);
        tempItem.setFilepath("/files/" + fileName);

        itemRepository.save(tempItem);
    }

    // 특정 상품 삭제
    public void itemDelete(Long id){
        itemRepository.deleteById(id);
    }

}
