export default function ListaProductos({ productos, onAgregar, onEditar, onEliminar, esAdmin }) {
    return (
      <section id="productos" className="contenedorCards container my-5">
        <h2 className="text-center">Productos</h2>
        <div className="d-flex flex-wrap justify-content-around gap-3">
          {productos.map(prod => (
            <div key={prod.id} className="card" style={{ width: "18rem" }}>
              <img src={prod.imagen ? `/img/${prod.imagen}` : "/img/logo_calyma.png"} className="card-img-top" alt={prod.nombre} />
                <div className="card-body">
                <h5 className="card-title">{prod.nombre}</h5>
                <p className="card-text">{prod.descripcion}</p>
                <p className="card-text"><strong>${prod.precio}</strong></p>
                <button className="btn btn-primary" onClick={() => onAgregar(prod)}>
                  Agregar al carrito
                </button>
                
                {esAdmin && (
                  <div style={{ marginTop: "10px" }}>
                    <button 
                      className="btn btn-warning btn-sm" 
                      onClick={() => onEditar(prod)}
                      style={{ marginRight: "5px" }}
                    >
                      Editar
                    </button>
                    <button 
                      className="btn btn-danger btn-sm" 
                      onClick={() => onEliminar(prod.id)}
                    >
                      Eliminar
                    </button>
                  </div>
                )}
              </div>
            </div>
          ))}
        </div>
      </section>
    );
  }