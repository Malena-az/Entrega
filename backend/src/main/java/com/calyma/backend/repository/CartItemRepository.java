package com.calyma.backend.repository;

import com.calyma.backend.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUsuarioId(Long usuarioId);
    void deleteByUsuarioId(Long usuarioId);
    void deleteByUsuarioIdAndProductoId(Long usuarioId, Long productoId);
} 