// static/js/clientes.js

document.addEventListener("DOMContentLoaded", () => {
  const tablaClientes = document.getElementById("tabla-clientes");
  const form = document.getElementById("cliente-form");
  const btnGuardar = document.getElementById("btn-guardar");
  const btnLimpiar = document.getElementById("btn-limpiar");

  const configDiv = document.getElementById("app-config");
  const apiClientesUrl = configDiv ? configDiv.dataset.apiClientesUrl : null;

  // --- CSRF (si Spring Security CSRF está activo) ---
  const csrfTokenMeta = document.querySelector("meta[name='_csrf']");
  const csrfHeaderMeta = document.querySelector("meta[name='_csrf_header']");
  const csrfToken = csrfTokenMeta ? csrfTokenMeta.getAttribute("content") : null;
  const csrfHeaderName = csrfHeaderMeta ? csrfHeaderMeta.getAttribute("content") : null;

  function buildHeaders() {
    const headers = {
      "Content-Type": "application/json"
    };
    if (csrfToken && csrfHeaderName) {
      headers[csrfHeaderName] = csrfToken;
    }
    return headers;
  }

  // --- Helpers para el formulario ---

  function limpiarFormulario() {
    if (!form) return;
    form.reset();
    const idInput = document.getElementById("id");
    if (idInput) {
      idInput.value = "";
    }
  }

  function llenarFormularioDesdeDto(dto) {
    if (!form || !dto) return;

    const idInput = document.getElementById("id");
    const nombreInput = document.getElementById("nombre");
    const apellidoInput = document.getElementById("apellido");
    const dniInput = document.getElementById("dni");

    if (idInput) idInput.value = dto.id ?? "";
    if (nombreInput) nombreInput.value = dto.nombre ?? "";
    if (apellidoInput) apellidoInput.value = dto.apellido ?? "";
    // toleramos que el backend lo devuelva como `dni` o como `numeroDocumento`
    if (dniInput) dniInput.value = dto.dni ?? dto.numeroDocumento ?? "";
  }

  function construirPayloadCliente() {
    if (!form) return null;
    const formData = new FormData(form);
    const raw = Object.fromEntries(formData.entries());

    // Payload EXACTO de ClienteRequestDto:
    // nombre, apellido, dni
    const payload = {
      nombre: raw.nombre || null,
      apellido: raw.apellido || null,
      dni: raw.dni || null
    };

    return { payload, id: raw.id || "" };
  }

  // --- Guardar (crear o actualizar) ---
  async function guardarCliente() {
    if (!apiClientesUrl) {
      console.error("API clientes URL no configurada");
      return;
    }

    const data = construirPayloadCliente();
    if (!data) return;

    const { payload, id } = data;
    const esEdicion = id != null && id !== "";

    const url = esEdicion ? `${apiClientesUrl}/${id}` : apiClientesUrl;
    const method = esEdicion ? "PUT" : "POST";

    try {
      const response = await fetch(url, {
        method,
        headers: buildHeaders(),
        body: JSON.stringify(payload)
      });

      if (!response.ok) {
        const errorText = await response.text();
        console.error("Error HTTP:", response.status, errorText);
        alert("Error al guardar el cliente. Revisa la consola para más detalles.");
        return;
      }

      const dataResp = await response.json();
      console.log("Cliente guardado:", dataResp);

      // Para simplificar, recargamos la página y vemos la tabla actualizada
      window.location.reload();

    } catch (err) {
      console.error("Error en la petición de guardado:", err);
      alert("Error de red al guardar el cliente.");
    }
  }

  // --- Editar: GET /api/clientes/{id} y llenar el form ---
  async function editarCliente(id) {
    if (!apiClientesUrl) return;

    const url = `${apiClientesUrl}/${id}`;

    try {
      const response = await fetch(url, {
        method: "GET",
        headers: buildHeaders()
      });

      if (!response.ok) {
        console.error("Error obteniendo cliente:", response.status);
        alert("No se pudo obtener el cliente para editar.");
        return;
      }

      const dto = await response.json();
      console.log("Cliente para edición:", dto);

      // dto debería tener al menos: id, nombre, apellido, dni o numeroDocumento
      llenarFormularioDesdeDto(dto);
    } catch (err) {
      console.error("Error en la petición de edición:", err);
      alert("Error de red al obtener el cliente.");
    }
  }

  // --- Eliminar: DELETE /api/clientes/{id} ---
  async function eliminarCliente(id, fila) {
    if (!apiClientesUrl) return;

    const confirmar = window.confirm(`¿Seguro que deseas eliminar el cliente con id ${id}?`);
    if (!confirmar) return;

    const url = `${apiClientesUrl}/${id}`;

    try {
      const response = await fetch(url, {
        method: "DELETE",
        headers: buildHeaders()
      });

      if (!response.ok) {
        console.error("Error eliminando cliente:", response.status);
        alert("No se pudo eliminar el cliente.");
        return;
      }

      // Removemos la fila sin recargar
      if (fila && fila.parentNode) {
        fila.parentNode.removeChild(fila);
      }

      // Si preferís, podrías hacer: window.location.reload();
    } catch (err) {
      console.error("Error en la petición de eliminación:", err);
      alert("Error de red al eliminar el cliente.");
    }
  }

  // --- Eventos ---

  if (btnGuardar && form) {
    btnGuardar.addEventListener("click", (e) => {
      e.preventDefault();
      guardarCliente();
    });
  }

  if (btnLimpiar && form) {
    btnLimpiar.addEventListener("click", (e) => {
      e.preventDefault();
      limpiarFormulario();
    });
  }

  if (tablaClientes) {
    tablaClientes.addEventListener("click", (event) => {
      const target = event.target;

      // EDITAR
      if (target.classList.contains("btn-editar")) {
        const fila = target.closest("tr");
        const id = fila.dataset.clienteId;
        editarCliente(id);
      }

      // ELIMINAR
      if (target.classList.contains("btn-eliminar")) {
        const fila = target.closest("tr");
        const id = fila.dataset.clienteId;
        eliminarCliente(id, fila);
      }
    });
  }
});
