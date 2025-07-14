package com.calyma.backend.repository;

import com.calyma.backend.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    // Buscar pedidos ordenados por fecha de creación (más recientes primero)
    List<Pedido> findAllByOrderByFechaCreacionDesc();
    
    // Buscar pedidos ordenados por fecha de creación (más antiguos primero)
    List<Pedido> findAllByOrderByFechaCreacionAsc();
    
    // Buscar pedidos por estado ordenados por fecha
    List<Pedido> findByEstadoOrderByFechaCreacionDesc(Pedido.EstadoPedido estado);
    
    // Contar pedidos por estado
    long countByEstado(Pedido.EstadoPedido estado);
    
    // Buscar pedidos creados hoy
    @Query("SELECT p FROM Pedido p WHERE DATE(p.fechaCreacion) = CURRENT_DATE")
    List<Pedido> findPedidosDeHoy();
    
    // Buscar pedidos con total mayor a un valor
    @Query("SELECT p FROM Pedido p WHERE p.id IN (SELECT lp.pedido.id FROM LineaPedido lp GROUP BY lp.pedido.id HAVING SUM(lp.producto.precio * lp.cantidad) > :totalMinimo)")
    List<Pedido> findPedidosConTotalMayorA(@Param("totalMinimo") Double totalMinimo);

    List<Pedido> findByUsuarioId(Long usuarioId);
} 