import React, { useState, useEffect } from 'react';

export default function AdminPedidos({ token }) {
  const [pedidos, setPedidos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchPedidos();
  }, []);

  const fetchPedidos = async () => {
    try {
      const response = await fetch("/pedidos/admin", {
        headers: {
          "Authorization": "Bearer " + token
        }
      });
      
      if (!response.ok) {
        throw new Error("Error al cargar pedidos");
      }
      
      const data = await response.json();
      setPedidos(data);
    } catch (err) {
      setError("Error al cargar pedidos: " + err.message);
    } finally {
      setLoading(false);
    }
  };

  const cambiarEstado = async (pedidoId, nuevoEstado) => {
    try {
      const response = await fetch(`/pedidos/${pedidoId}/estado`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer " + token
        },
        body: JSON.stringify({ estado: nuevoEstado })
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText);
      }

      alert("Estado actualizado correctamente");
      fetchPedidos();
    } catch (err) {
      alert("Error al actualizar estado: " + err.message);
    }
  };

  const getEstadoColor = (estado) => {
    switch (estado) {
      case "PENDIENTE": return "#ffc107";
      case "CONFIRMADO": return "#17a2b8";
      case "EN_PROCESO": return "#007bff";
      case "ENVIADO": return "#28a745";
      case "ENTREGADO": return "#6c757d";
      case "CANCELADO": return "#dc3545";
      default: return "#6c757d";
    }
  };

  if (loading) {
    return <div className="text-center mt-4">Cargando pedidos...</div>;
  }

  if (error) {
    return <div className="alert alert-danger mt-4">{error}</div>;
  }

  return (
    <div className="container mt-4">
      <h2 className="text-center mb-4">Panel de Administración - Pedidos</h2>
      
      {pedidos.length === 0 ? (
        <div className="alert alert-info">No hay pedidos para mostrar.</div>
      ) : (
        <div className="row">
          {pedidos.map(pedido => (
            <div key={pedido.id} className="col-md-6 col-lg-4 mb-4">
              <div className="card h-100">
                <div className="card-header d-flex justify-content-between align-items-center">
                  <h6 className="mb-0">Pedido #{pedido.id}</h6>
                  <span 
                    className="badge"
                    style={{ 
                      backgroundColor: getEstadoColor(pedido.estado),
                      color: "white"
                    }}
                  >
                    {pedido.estado}
                  </span>
                </div>
                
                <div className="card-body">
                  <p><strong>Cliente:</strong> {pedido.usuario}</p>
                  <p><strong>Fecha:</strong> {new Date(pedido.fechaCreacion).toLocaleString()}</p>
                  <p><strong>Total:</strong> ${pedido.costoTotal}</p>
                  
                  <div className="mb-3">
                    <strong>Productos:</strong>
                    <div className="mt-2">
                      {pedido.lineas.map((linea, index) => (
                        <div key={index} className="d-flex align-items-center mb-2">
                          <img 
                            src={linea.producto.imagen ? `/img/${linea.producto.imagen}` : "/img/logo_calyma.png"} 
                            alt={linea.producto.nombre}
                            style={{ width: "30px", height: "30px", objectFit: "cover", marginRight: "10px" }}
                          />
                          <span className="flex-grow-1">{linea.producto.nombre}</span>
                          <span className="badge bg-secondary">{linea.cantidad}x</span>
                          <span className="ms-2">${linea.subtotal}</span>
                        </div>
                      ))}
                    </div>
                  </div>
                  
                  <div className="mt-3">
                    <label><strong>Cambiar estado:</strong></label>
                    <select 
                      className="form-select mt-1"
                      value={pedido.estado}
                      onChange={(e) => cambiarEstado(pedido.id, e.target.value)}
                    >
                      <option value="PENDIENTE">Pendiente</option>
                      <option value="CONFIRMADO">Confirmado</option>
                      <option value="EN_PROCESO">En Proceso</option>
                      <option value="ENVIADO">Enviado</option>
                      <option value="ENTREGADO">Entregado</option>
                      <option value="CANCELADO">Cancelado</option>
                    </select>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
} 