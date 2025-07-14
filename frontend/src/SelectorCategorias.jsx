export default function SelectorCategorias({ categorias, onSelect }) {
    return (
      <div className="mb-4" style={{ maxWidth: 400, margin: "0 auto" }}>
        <label htmlFor="select-categorias" style={{ fontWeight: 600, color: "#b497bd" }}>
          Categoría:
        </label>
        <select id="select-categorias" className="form-select" onChange={e => onSelect(e.target.value)}>
          <option value="">Selecciona una categoría</option>
          {categorias.map(cat => (
            <option key={cat.id} value={cat.id}>{cat.nombreCategoria}</option>
          ))}
        </select>
      </div>
    );
  }