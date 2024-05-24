package com.yurakamri.ajrcollection.repository;

import com.yurakamri.ajrcollection.entity.Product;
import com.yurakamri.ajrcollection.entity.ProductAttachment;
import com.yurakamri.ajrcollection.entity.ProductColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ProductAttachmentRepository extends JpaRepository<ProductAttachment, UUID> {

    boolean existsByProductColor_Product(Product productColor_product);

//    ProductAttachment getAllByProductColorId(UUID productColor_id);


    List<ProductAttachment> findAllByProductColorId(UUID id);
}
