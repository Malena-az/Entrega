import React, { useState, useEffect } from "react";
import Header from "./Header";
import SelectorCategorias from "./SelectorCategorias";
import ListaProductos from "./ListaProductos";
import CarritoSidebar from "./Carrito";
import LoginRegister from "./LoginRegister";
import Pedidos from "./Pedidos";
import AdminPedidos from "./AdminPedidos";
import 'bootstrap/dist/css/bootstrap.min.css';
import './styles.css';

export default function App() {
  const [categorias, setCategorias] = useState([]);
  const [productos, setProductos] = useState([]);
  const [categoriaSeleccionada, setCategoriaSeleccionada] = useState("");
  const [carrito, setCarrito] = useState([]);
  const [mostrarCarrito, setMostrarCarrito] = useState(false);
  const [mostrarPedidos, setMostrarPedidos] = useState(false);
  const [mostrarAdminPedidos, setMostrarAdminPedidos] = useState(false);
  const [mostrarLogin, setMostrarLogin] = useState(false);
  const [token, setToken] = useState(localStorage.getItem("jwt") || "");
  const [usuario, setUsuario] = useState(null);
  const [rol, setRol] = useState(null);
  const [error, setError] = useState("");
  const [mostrarPago, setMostrarPago] = useState(false);
  const [metodoPago, setMetodoPago] = useState("");
  const [nombreCompleto, setNombreCompleto] = useState("");
  const [direccion, setDireccion] = useState("");
  const [busqueda, setBusqueda] = useState("");
  const [mostrarAgregarProducto, setMostrarAgregarProducto] = useState(false);
  const [nuevoNombre, setNuevoNombre] = useState("");
  const [nuevoPrecio, setNuevoPrecio] = useState("");
  const [nuevaDescripcion, setNuevaDescripcion] = useState("");
  const [nuevaImagen, setNuevaImagen] = useState("");
  const [nuevoStock, setNuevoStock] = useState("");
  const [nuevoIdCategoria, setNuevoIdCategoria] = useState("");
  const [nuevaPresentacion, setNuevaPresentacion] = useState("");
  const [nuevoTipo, setNuevoTipo] = useState("");
  const [nuevaCategoria, setNuevaCategoria] = useState("");
  const [crearNuevaCategoria, setCrearNuevaCategoria] = useState(false);
  const [mostrarEditarProducto, setMostrarEditarProducto] = useState(false);
  const [productoEditando, setProductoEditando] = useState(null);
  const [editarNombre, setEditarNombre] = useState("");
  const [editarDescripcion, setEditarDescripcion] = useState("");
  const [editarPrecio, setEditarPrecio] = useState("");
  const [editarStock, setEditarStock] = useState("");
  const [editarIdCategoria, setEditarIdCategoria] = useState("");
  const [editarPresentacion, setEditarPresentacion] = useState("");
  const [editarTipo, setEditarTipo] = useState("");
  const [editarImagen, setEditarImagen] = useState("");
  const [renderKey, setRenderKey] = useState(0);

  useEffect(() => {
    fetch("/categorias").then(res => res.json()).then(setCategorias);
    fetch("/productos").then(res => res.json()).then(setProductos);
  }, []);


  useEffect(() => {
    if (!token) {

      setMostrarCarrito(false);
      setMostrarPedidos(false);
      setMostrarAdminPedidos(false);
      setMostrarLogin(false);
      setMostrarPago(false);
      setMostrarAgregarProducto(false);
      setMostrarEditarProducto(false);
      setProductoEditando(null);
      
      // Limpiar carrito
      setCarrito([]);
      
      // Limpiar formularios
      setBusqueda("");
      setCategoriaSeleccionada("");
      setMetodoPago("");
      setNombreCompleto("");
      setDireccion("");

      setNuevoNombre("");
      setNuevaDescripcion("");
      setNuevoPrecio("");
      setNuevoStock("");
      setNuevoIdCategoria("");
      setNuevaPresentacion("");
      setNuevoTipo("");
      setNuevaImagen("");
      setNuevaCategoria("");
      setCrearNuevaCategoria(false);
      
      setEditarNombre("");
      setEditarDescripcion("");
      setEditarPrecio("");
      setEditarStock("");
      setEditarIdCategoria("");
      setEditarPresentacion("");
      setEditarTipo("");
      setEditarImagen("");
      

      setError("");
    }
  }, [token]);

  const productosFiltrados = productos.filter(p =>
    (categoriaSeleccionada ? p.categoria.id === Number(categoriaSeleccionada) : true) &&
    (
      p.nombre.toLowerCase().includes(busqueda.toLowerCase()) ||
      p.descripcion.toLowerCase().includes(busqueda.toLowerCase())
    )
  );

  const agregarAlCarrito = (producto) => {
    setCarrito(prev =>
      prev.some(item => item.id === producto.id)
        ? prev.map(item =>
            item.id === producto.id
              ? { ...item, cantidad: item.cantidad + 1 }
              : item
          )
        : [...prev, { ...producto, cantidad: 1 }]
    );
  };

  const vaciarCarrito = () => setCarrito([]);
  const total = carrito.reduce((acc, item) => acc + item.precio * item.cantidad, 0);

  // Login
  const handleLogin = (username, password) => {
    fetch("/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password })
    })
      .then(res => res.ok ? res.json() : Promise.reject(res))
      .then(data => {
        setToken(data.token);
        setUsuario(username);
        setRol(data.rol);
        localStorage.setItem("jwt", data.token);
        setError("");
        setMostrarLogin(false);
      })
      .catch(() => setError("Usuario o contraseña incorrectos"));
  };

  // Registro
  const handleRegister = (username, password) => {
    fetch("/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password })
    })
      .then(res => res.ok ? res.json() : Promise.reject(res))
      .then(() => {
        setError("¡Usuario registrado! Ahora puedes iniciar sesión.");
      })
      .catch(() => setError("Error al registrar usuario"));
  };

  // Logout
  const handleLogout = () => {
    setToken("");
    setUsuario(null);
    setRol(null);
    localStorage.removeItem("jwt");
    setRenderKey(prev => prev + 1);
  };

  const handleEditarProducto = (producto) => {
    setProductoEditando(producto);
    setEditarNombre(producto.nombre);
    setEditarDescripcion(producto.descripcion || "");
    setEditarPrecio(producto.precio.toString());
    setEditarStock(producto.stock.toString());
    setEditarIdCategoria(producto.categoria.id.toString());
    setEditarPresentacion(producto.presentacion || "");
    setEditarTipo(producto.tipo || "");
    setEditarImagen(producto.imagen || "");
    setMostrarEditarProducto(true);
  };

  const handleEliminarProducto = (productoId) => {
    if (window.confirm("¿Estás seguro de que quieres eliminar este producto?")) {
      fetch(`/productos/${productoId}`, {
        method: "DELETE",
        headers: {
          "Authorization": "Bearer " + token
        }
      })
        .then(async res => {
          if (!res.ok) {
            const errorText = await res.text();
            throw new Error(errorText);
          }
          return res;
        })
        .then(() => {
          alert("Producto eliminado correctamente");
          fetch("/productos").then(res => res.json()).then(setProductos);
        })
        .catch(err => alert("Error al eliminar producto: " + err.message));
    }
  };

  const handleComprar = () => {
    if (!token) {
      setMostrarLogin(true);
      return;
    }
    setMostrarPago(true);
  };

  const confirmarCompra = () => {
    fetch("/pedidos", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + token
      },
      body: JSON.stringify({
        items: carrito,
        metodoPago,
        nombreCompleto,
        direccion
      })
    })
      .then(async res => {
        if (!res.ok) {
          const errorText = await res.text();
          throw new Error(errorText);
        }
        return res.json();
      })
      .then(data => {
        setCarrito([]);
        setMostrarPago(false);
        setNombreCompleto("");
        setDireccion("");
        setMetodoPago("");
        alert("¡Compra realizada!");
      })
      .catch(err => {
        alert("Error al crear el pedido: " + err.message);
      });
  };

  return (
    <div key={renderKey}>
      <Header onOpenCart={() => setMostrarCarrito(true)} cartCount={carrito.length} />
      
      {/* Barra de usuario y navegación */}
      <div style={{ padding: "10px", backgroundColor: "#f8f4ff", borderBottom: "2px solid #e8d5f0" }}>
        {token ? (
          <>
            <div style={{ margin: "10px", fontWeight: "bold", color: "#6a4c93", textAlign: "center" }}>
              👤 Usuario en sesión: {usuario}
            </div>
            <div style={{ display: "flex", gap: "10px", flexWrap: "wrap", justifyContent: "center", margin: "10px 0" }}>
              <button 
                onClick={handleLogout}
                style={{
                  backgroundColor: "#f8f4ff",
                  border: "2px solid #b497bd",
                  color: "#6a4c93",
                  padding: "8px 16px",
                  borderRadius: "20px",
                  cursor: "pointer",
                  transition: "all 0.3s ease"
                }}
                onMouseOver={(e) => {
                  e.target.style.backgroundColor = "#e8d5f0";
                  e.target.style.transform = "translateY(-1px)";
                }}
                onMouseOut={(e) => {
                  e.target.style.backgroundColor = "#f8f4ff";
                  e.target.style.transform = "translateY(0)";
                }}
              >
                Cerrar sesión
              </button>
              <button 
                onClick={() => setMostrarPedidos(!mostrarPedidos)}
                style={{
                  backgroundColor: "#f8f4ff",
                  border: "2px solid #b497bd",
                  color: "#6a4c93",
                  padding: "8px 16px",
                  borderRadius: "20px",
                  cursor: "pointer",
                  transition: "all 0.3s ease"
                }}
                onMouseOver={(e) => {
                  e.target.style.backgroundColor = "#e8d5f0";
                  e.target.style.transform = "translateY(-1px)";
                }}
                onMouseOut={(e) => {
                  e.target.style.backgroundColor = "#f8f4ff";
                  e.target.style.transform = "translateY(0)";
                }}
              >
                {mostrarPedidos ? "Ocultar pedidos" : "Ver mis pedidos"}
              </button>
              {rol === "ADMIN" && (
                <button 
                  onClick={() => setMostrarAdminPedidos(!mostrarAdminPedidos)}
                  style={{
                    backgroundColor: "#f8f4ff",
                    border: "2px solid #b497bd",
                    color: "#6a4c93",
                    padding: "8px 16px",
                    borderRadius: "20px",
                    cursor: "pointer",
                    transition: "all 0.3s ease"
                  }}
                  onMouseOver={(e) => {
                    e.target.style.backgroundColor = "#e8d5f0";
                    e.target.style.transform = "translateY(-1px)";
                  }}
                  onMouseOut={(e) => {
                    e.target.style.backgroundColor = "#f8f4ff";
                    e.target.style.transform = "translateY(0)";
                  }}
                >
                  {mostrarAdminPedidos ? "Ocultar panel admin" : "Panel de administración"}
                </button>
              )}
            </div>
          </>
        ) : (
          <div style={{ textAlign: "center" }}>
            <div style={{ marginBottom: "10px", color: "#6a4c93", fontWeight: "bold" }}>
              🛒 Puedes agregar productos al carrito sin registrarte
            </div>
            <button 
              onClick={() => setMostrarLogin(true)}
              style={{
                backgroundColor: "#b497bd",
                color: "white",
                padding: "8px 20px",
                borderRadius: "20px",
                border: "none",
                cursor: "pointer",
                fontSize: "14px",
                fontWeight: "bold",
                transition: "all 0.3s ease"
              }}
              onMouseOver={(e) => {
                e.target.style.backgroundColor = "#a085b0";
                e.target.style.transform = "translateY(-1px)";
              }}
              onMouseOut={(e) => {
                e.target.style.backgroundColor = "#b497bd";
                e.target.style.transform = "translateY(0)";
              }}
            >
              Iniciar sesión / Registrarse
            </button>
          </div>
        )}
      </div>

      {/* Modal de login */}
      {mostrarLogin && !token && (
        <div className="modal-pago-overlay" onClick={() => setMostrarLogin(false)}>
          <div className="modal-pago" onClick={e => e.stopPropagation()}>
            <LoginRegister onLogin={handleLogin} onRegister={handleRegister} error={error} />
          </div>
        </div>
      )}

      {/* Contenido principal */}
      {!mostrarPedidos && !mostrarAdminPedidos && (
        <>
          {token && rol === "ADMIN" && (
            <div style={{ margin: "20px 0", textAlign: "center" }}>
              <button 
                onClick={() => setMostrarAgregarProducto(true)}
                style={{
                  backgroundColor: "#e8d5f0",
                  border: "2px solid #b497bd",
                  color: "#6a4c93",
                  padding: "12px 24px",
                  borderRadius: "25px",
                  fontSize: "16px",
                  fontWeight: "bold",
                  cursor: "pointer",
                  transition: "all 0.3s ease",
                  boxShadow: "0 2px 4px rgba(0,0,0,0.1)"
                }}
                onMouseOver={(e) => {
                  e.target.style.backgroundColor = "#d4c0e0";
                  e.target.style.transform = "translateY(-2px)";
                }}
                onMouseOut={(e) => {
                  e.target.style.backgroundColor = "#e8d5f0";
                  e.target.style.transform = "translateY(0)";
                }}
              >
                ➕ Agregar Nuevo Producto
              </button>
            </div>
          )}
          <SelectorCategorias categorias={categorias} onSelect={setCategoriaSeleccionada} />
          <input
            type="text"
            placeholder="Buscar producto..."
            value={busqueda}
            onChange={e => setBusqueda(e.target.value)}
            style={{
              width: "100%",
              maxWidth: 400,
              margin: "20px auto",
              display: "block",
              padding: "8px",
              borderRadius: "10px",
              border: "1.5px solid #b497bd"
            }}
          />
                      <ListaProductos 
              productos={productosFiltrados} 
              onAgregar={agregarAlCarrito}
              onEditar={handleEditarProducto}
              onEliminar={handleEliminarProducto}
              esAdmin={token && rol === "ADMIN"}
            />
          {mostrarCarrito && (
            <CarritoSidebar
              carrito={carrito}
              total={total}
              onClose={() => setMostrarCarrito(false)}
              onVaciar={vaciarCarrito}
              onComprar={handleComprar}
            />
          )}
          {mostrarPago && token && (
            <>
              <div className="modal-pago-overlay" onClick={() => setMostrarPago(false)} />
              <div className="modal-pago">
                <h4>Finalizar compra</h4>
                <div>
                  <label>Nombre completo:</label>
                  <input
                    type="text"
                    value={nombreCompleto}
                    onChange={e => setNombreCompleto(e.target.value)}
                    required
                  />
                </div>
                <div>
                  <label>Dirección de envío:</label>
                  <input
                    type="text"
                    value={direccion}
                    onChange={e => setDireccion(e.target.value)}
                    required
                  />
                </div>
                <div>
                  <label>Método de pago:</label>
                  <select value={metodoPago} onChange={e => setMetodoPago(e.target.value)} required>
                    <option value="">Selecciona...</option>
                    <option value="mercado_pago">Mercado Pago</option>
                    <option value="modo">Modo</option>
                    <option value="transferencia">Transferencia</option>
                    <option value="tarjeta">Tarjeta</option>
                  </select>
                </div>
                <div style={{ display: "flex", gap: "10px", marginTop: "20px" }}>
                  <button
                    onClick={confirmarCompra}
                    disabled={!nombreCompleto || !direccion || !metodoPago}
                    style={{
                      backgroundColor: nombreCompleto && direccion && metodoPago ? "#b497bd" : "#cccccc",
                      color: "white",
                      padding: "10px 20px",
                      borderRadius: "8px",
                      border: "none",
                      cursor: nombreCompleto && direccion && metodoPago ? "pointer" : "not-allowed",
                      fontSize: "14px",
                      fontWeight: "bold",
                      flex: 1
                    }}
                  >
                    Confirmar compra
                  </button>
                  <button 
                    onClick={() => setMostrarPago(false)}
                    style={{
                      backgroundColor: "#f8f4ff",
                      color: "#6a4c93",
                      padding: "10px 20px",
                      borderRadius: "8px",
                      border: "2px solid #b497bd",
                      cursor: "pointer",
                      fontSize: "14px",
                      fontWeight: "bold",
                      flex: 1
                    }}
                  >
                    Cancelar
                  </button>
                </div>
              </div>
            </>
          )}
          {mostrarAgregarProducto && token && (
            <div className="modal-pago-overlay" onClick={() => setMostrarAgregarProducto(false)}>
              <div className="modal-pago" onClick={e => e.stopPropagation()}>
                <h4>Agregar producto</h4>
                <form
                  onSubmit={async e => {
                    e.preventDefault();
                    
                    let categoriaId = nuevoIdCategoria;
                    
                    if (crearNuevaCategoria) {
                      try {
                        const categoriaRes = await fetch("/categorias", {
                          method: "POST",
                          headers: {
                            "Content-Type": "application/json",
                            "Authorization": "Bearer " + token
                          },
                          body: JSON.stringify({
                            nombre: nuevaCategoria
                          })
                        });
                        
                        if (!categoriaRes.ok) {
                          const errorText = await categoriaRes.text();
                          throw new Error(errorText);
                        }
                        
                        const categoriaData = await categoriaRes.json();
                        categoriaId = categoriaData.id;
                        
                        fetch("/categorias").then(res => res.json()).then(setCategorias);
                      } catch (err) {
                        alert("Error al crear categoría: " + err.message);
                        return;
                      }
                    }
                    
                    fetch("/productos", {
                      method: "POST",
                      headers: {
                        "Content-Type": "application/json",
                        "Authorization": "Bearer " + token
                      },
                      body: JSON.stringify({
                        nombre: nuevoNombre,
                        descripcion: nuevaDescripcion,
                        precio: parseFloat(nuevoPrecio),
                        stock: parseInt(nuevoStock),
                        id_categoria: parseInt(categoriaId),
                        presentacion: nuevaPresentacion,
                        tipo: nuevoTipo,
                        imagen: nuevaImagen
                      })
                    })
                      .then(async res => {
                        if (!res.ok) {
                          const errorText = await res.text();
                          throw new Error(errorText);
                        }
                        return res.json();
                      })
                      .then(data => {
                        alert("Producto agregado correctamente");
                        setMostrarAgregarProducto(false);
                        setNuevoNombre("");
                        setNuevaDescripcion("");
                        setNuevoPrecio("");
                        setNuevoStock("");
                        setNuevoIdCategoria("");
                        setNuevaPresentacion("");
                        setNuevoTipo("");
                        setNuevaImagen("");
                        fetch("/productos").then(res => res.json()).then(setProductos);
                      })
                      .catch(err => alert("Error al agregar producto: " + err.message));
                  }}
                >
                  <label>Nombre:</label>
                  <input type="text" value={nuevoNombre} onChange={e => setNuevoNombre(e.target.value)} required />

                  <label>Descripción:</label>
                  <input type="text" value={nuevaDescripcion} onChange={e => setNuevaDescripcion(e.target.value)} required />

                  <label>Precio:</label>
                  <input type="number" value={nuevoPrecio} onChange={e => setNuevoPrecio(e.target.value)} required />

                  <label>Stock:</label>
                  <input type="number" value={nuevoStock} onChange={e => setNuevoStock(e.target.value)} required />

                  <label>Categoría:</label>
                  <div style={{ marginBottom: "10px" }}>
                    <button 
                      type="button" 
                      onClick={() => setCrearNuevaCategoria(!crearNuevaCategoria)}
                      style={{ 
                        marginRight: "10px", 
                        padding: "5px 10px",
                        backgroundColor: crearNuevaCategoria ? "#b497bd" : "#f0f0f0",
                        border: "1px solid #ccc",
                        borderRadius: "5px",
                        cursor: "pointer"
                      }}
                    >
                      {crearNuevaCategoria ? "Seleccionar existente" : "Crear nueva categoría"}
                    </button>
                  </div>
                  
                  {!crearNuevaCategoria ? (
                    <select value={nuevoIdCategoria} onChange={e => setNuevoIdCategoria(e.target.value)} required>
                      <option value="">Selecciona una categoría...</option>
                      {categorias.map(cat => (
                        <option key={cat.id} value={cat.id}>{cat.nombreCategoria}</option>
                      ))}
                    </select>
                  ) : (
                    <input 
                      type="text" 
                      value={nuevaCategoria} 
                      onChange={e => setNuevaCategoria(e.target.value)} 
                      placeholder="Nombre de la nueva categoría"
                      required 
                    />
                  )}

                  <label>Presentación:</label>
                  <input type="text" value={nuevaPresentacion} onChange={e => setNuevaPresentacion(e.target.value)} required />

                  <label>Tipo:</label>
                  <input type="text" value={nuevoTipo} onChange={e => setNuevoTipo(e.target.value)} required />

                  <label>Imagen (nombre de archivo):</label>
                  <input type="text" value={nuevaImagen} onChange={e => setNuevaImagen(e.target.value)} required />

                  <div style={{ display: "flex", gap: "10px", marginTop: "20px" }}>
                    <button 
                      type="submit"
                      style={{
                        backgroundColor: "#b497bd",
                        color: "white",
                        padding: "10px 20px",
                        borderRadius: "8px",
                        border: "none",
                        cursor: "pointer",
                        fontSize: "14px",
                        fontWeight: "bold",
                        flex: 1
                      }}
                    >
                      Agregar
                    </button>
                    <button 
                      type="button" 
                      onClick={() => setMostrarAgregarProducto(false)}
                      style={{
                        backgroundColor: "#f8f4ff",
                        color: "#6a4c93",
                        padding: "10px 20px",
                        borderRadius: "8px",
                        border: "2px solid #b497bd",
                        cursor: "pointer",
                        fontSize: "14px",
                        fontWeight: "bold",
                        flex: 1
                      }}
                    >
                      Cancelar
                    </button>
                  </div>
                </form>
              </div>
            </div>
          )}
          {mostrarEditarProducto && token && (
            <div className="modal-pago-overlay" onClick={() => setMostrarEditarProducto(false)}>
              <div className="modal-pago" onClick={e => e.stopPropagation()}>
                <h4>Editar producto</h4>
                <form
                  onSubmit={async e => {
                    e.preventDefault();
                    
                    let categoriaId = editarIdCategoria;
                    
                    if (crearNuevaCategoria) {
                      try {
                        const categoriaRes = await fetch("/categorias", {
                          method: "POST",
                          headers: {
                            "Content-Type": "application/json",
                            "Authorization": "Bearer " + token
                          },
                          body: JSON.stringify({
                            nombre: nuevaCategoria
                          })
                        });
                        
                        if (!categoriaRes.ok) {
                          const errorText = await categoriaRes.text();
                          throw new Error(errorText);
                        }
                        
                        const categoriaData = await categoriaRes.json();
                        categoriaId = categoriaData.id;
                        
                        fetch("/categorias").then(res => res.json()).then(setCategorias);
                      } catch (err) {
                        alert("Error al crear categoría: " + err.message);
                        return;
                      }
                    }
                    
                    fetch(`/productos/${productoEditando.id}`, {
                      method: "PUT",
                      headers: {
                        "Content-Type": "application/json",
                        "Authorization": "Bearer " + token
                      },
                      body: JSON.stringify({
                        nombre: editarNombre,
                        descripcion: editarDescripcion,
                        precio: parseFloat(editarPrecio),
                        stock: parseInt(editarStock),
                        id_categoria: parseInt(categoriaId),
                        presentacion: editarPresentacion,
                        tipo: editarTipo,
                        imagen: editarImagen
                      })
                    })
                      .then(async res => {
                        if (!res.ok) {
                          const errorText = await res.text();
                          throw new Error(errorText);
                        }
                        return res.json();
                      })
                      .then(data => {
                        alert("Producto actualizado correctamente");
                        setMostrarEditarProducto(false);
                        setProductoEditando(null);
                        setEditarNombre("");
                        setEditarDescripcion("");
                        setEditarPrecio("");
                        setEditarStock("");
                        setEditarIdCategoria("");
                        setEditarPresentacion("");
                        setEditarTipo("");
                        setEditarImagen("");
                        fetch("/productos").then(res => res.json()).then(setProductos);
                      })
                      .catch(err => alert("Error al actualizar producto: " + err.message));
                  }}
                >
                  <label>Nombre:</label>
                  <input type="text" value={editarNombre} onChange={e => setEditarNombre(e.target.value)} required />

                  <label>Descripción:</label>
                  <input type="text" value={editarDescripcion} onChange={e => setEditarDescripcion(e.target.value)} required />

                  <label>Precio:</label>
                  <input type="number" value={editarPrecio} onChange={e => setEditarPrecio(e.target.value)} required />

                  <label>Stock:</label>
                  <input type="number" value={editarStock} onChange={e => setEditarStock(e.target.value)} required />

                  <label>Categoría:</label>
                  <div style={{ marginBottom: "10px" }}>
                    <button 
                      type="button" 
                      onClick={() => setCrearNuevaCategoria(!crearNuevaCategoria)}
                      style={{ 
                        marginRight: "10px", 
                        padding: "5px 10px",
                        backgroundColor: crearNuevaCategoria ? "#b497bd" : "#f0f0f0",
                        border: "1px solid #ccc",
                        borderRadius: "5px",
                        cursor: "pointer"
                      }}
                    >
                      {crearNuevaCategoria ? "Seleccionar existente" : "Crear nueva categoría"}
                    </button>
                  </div>
                  
                  {!crearNuevaCategoria ? (
                    <select value={editarIdCategoria} onChange={e => setEditarIdCategoria(e.target.value)} required>
                      <option value="">Selecciona una categoría...</option>
                      {categorias.map(cat => (
                        <option key={cat.id} value={cat.id}>{cat.nombreCategoria}</option>
                      ))}
                    </select>
                  ) : (
                    <input 
                      type="text" 
                      value={nuevaCategoria} 
                      onChange={e => setNuevaCategoria(e.target.value)} 
                      placeholder="Nombre de la nueva categoría"
                      required 
                    />
                  )}

                  <label>Presentación:</label>
                  <input type="text" value={editarPresentacion} onChange={e => setEditarPresentacion(e.target.value)} required />

                  <label>Tipo:</label>
                  <input type="text" value={editarTipo} onChange={e => setEditarTipo(e.target.value)} required />

                  <label>Imagen (nombre de archivo):</label>
                  <input type="text" value={editarImagen} onChange={e => setEditarImagen(e.target.value)} required />

                  <div style={{ display: "flex", gap: "10px", marginTop: "20px" }}>
                    <button 
                      type="submit"
                      style={{
                        backgroundColor: "#b497bd",
                        color: "white",
                        padding: "10px 20px",
                        borderRadius: "8px",
                        border: "none",
                        cursor: "pointer",
                        fontSize: "14px",
                        fontWeight: "bold",
                        flex: 1
                      }}
                    >
                      Actualizar
                    </button>
                    <button 
                      type="button" 
                      onClick={() => setMostrarEditarProducto(false)}
                      style={{
                        backgroundColor: "#f8f4ff",
                        color: "#6a4c93",
                        padding: "10px 20px",
                        borderRadius: "8px",
                        border: "2px solid #b497bd",
                        cursor: "pointer",
                        fontSize: "14px",
                        fontWeight: "bold",
                        flex: 1
                      }}
                    >
                      Cancelar
                    </button>
                  </div>
                </form>
              </div>
            </div>
          )}
        </>
      )}
      {mostrarPedidos && token && <Pedidos token={token} />}
      {mostrarAdminPedidos && token && rol === "ADMIN" && <AdminPedidos token={token} />}
    </div>
  );
}