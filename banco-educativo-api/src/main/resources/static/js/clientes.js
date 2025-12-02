// static/js/clientes.js

document.addEventListener("DOMContentLoaded", () => {
  const tablaClientes = document.getElementById("tabla-clientes");
  const form = document.getElementById("cliente-form");
  const btnGuardar = document.getElementById("btn-guardar");
  const btnLimpiar = document.getElementById("btn-limpiar");
  const alertContainer = document.getElementById("alert-container");

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

  // --- Helpers para mensajes de alerta ---
  function mostrarAlerta(mensaje, tipo = "error") {
    if (!alertContainer) return;

    const alertClass = tipo === "success" ? "alert-success" : "alert-error";
    alertContainer.innerHTML = `
      <div class="alert ${alertClass}">
        ${mensaje}
      </div>
    `;

    setTimeout(() => {
      alertContainer.innerHTML = "";
    }, 5000);
  }

  function limpiarErrores() {
    const errorSpans = document.querySelectorAll(".error-message");
    errorSpans.forEach(span => span.textContent = "");

    const invalidInputs = document.querySelectorAll(".form-control.is-invalid");
    invalidInputs.forEach(input => input.classList.remove("is-invalid"));
  }

  function mostrarErrorCampo(campo, mensaje) {
    const input = document.getElementById(campo);
    const errorSpan = document.getElementById(`error-${campo}`);

    if (input) {
      input.classList.add("is-invalid");
    }
    if (errorSpan) {
      errorSpan.textContent = mensaje;
    }
  }

  // --- Helpers para el formulario ---

  function limpiarFormulario() {
    if (!form) return;
    form.reset();
    const idInput = document.getElementById("id");
    if (idInput) {
      idInput.value = "";
    }
    limpiarErrores();
    if (alertContainer) {
      alertContainer.innerHTML = "";
    }
  }

  function llenarFormularioDesdeDto(dto) {
    if (!form || !dto) return;

    const idInput = document.getElementById("id");
    const tipoPersonaInput = document.getElementById("tipoPersona");
    const nombreInput = document.getElementById("nombre");
    const apellidoInput = document.getElementById("apellido");
    const tipoDocumentoInput = document.getElementById("tipoDocumento");
    const dniInput = document.getElementById("dni");
    const emailInput = document.getElementById("email");
    const telefonoInput = document.getElementById("telefono");
    const direccionInput = document.getElementById("direccion");
    const estadoInput = document.getElementById("estado");

    if (idInput) idInput.value = dto.id ?? "";
    if (tipoPersonaInput) tipoPersonaInput.value = dto.tipoPersona ?? "FISICA";
    if (nombreInput) nombreInput.value = dto.nombre ?? "";
    if (apellidoInput) apellidoInput.value = dto.apellido ?? "";
    if (tipoDocumentoInput) tipoDocumentoInput.value = dto.tipoDocumento ?? "DNI";
    if (dniInput) dniInput.value = dto.numeroDocumento ?? "";
    if (emailInput) emailInput.value = dto.email ?? "";
    if (telefonoInput) telefonoInput.value = dto.telefono ?? "";
    if (direccionInput) direccionInput.value = dto.direccion ?? "";
    if (estadoInput) estadoInput.value = dto.estado ?? "ACTIVO";

    limpiarErrores();
  }

  function construirPayloadCliente() {
    if (!form) return null;
    const formData = new FormData(form);
    const raw = Object.fromEntries(formData.entries());
    const id = raw.id || "";

    // Si es creación (sin id), usamos ClienteRequestDto simplificado
    if (!id) {
      return {
        payload: {
          nombre: raw.nombre || null,
          apellido: raw.apellido || null,
          dni: raw.dni || null
        },
        id: ""
      };
    }

    // Si es actualización (con id), usamos ClienteUpdateRequestDto completo
    return {
      payload: {
        tipoPersona: raw.tipoPersona || "FISICA",
        nombre: raw.nombre || null,
        apellido: raw.apellido || null,
        tipoDocumento: raw.tipoDocumento || "DNI",
        numeroDocumento: raw.dni || null,
        email: raw.email || null,
        telefono: raw.telefono || null,
        direccion: raw.direccion || null,
        estado: raw.estado || "ACTIVO"
      },
      id: id
    };
  }

  // --- Guardar (crear o actualizar) ---
  async function guardarCliente() {
    if (!apiClientesUrl) {
      console.error("API clientes URL no configurada");
      return;
    }

    limpiarErrores();

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

        // Intentar parsear errores de validación
        try {
          const errorJson = JSON.parse(errorText);
          if (errorJson.errors) {
            // Errores de validación de Spring
            Object.keys(errorJson.errors).forEach(campo => {
              mostrarErrorCampo(campo, errorJson.errors[campo]);
            });
          } else {
            mostrarAlerta(errorJson.message || "Error al guardar el cliente");
          }
        } catch (e) {
          mostrarAlerta("Error al guardar el cliente. Revisa los datos ingresados.");
        }
        return;
      }

      const dataResp = await response.json();
      console.log("Cliente guardado:", dataResp);

      mostrarAlerta(
        esEdicion ? "Cliente actualizado correctamente" : "Cliente creado correctamente",
        "success"
      );

      // Recargar después de un breve delay para que se vea el mensaje
      setTimeout(() => {
        window.location.reload();
      }, 1500);

    } catch (err) {
      console.error("Error en la petición de guardado:", err);
      mostrarAlerta("Error de red al guardar el cliente.");
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
        mostrarAlerta("No se pudo obtener el cliente para editar.");
        return;
      }

      const dto = await response.json();
      console.log("Cliente para edición:", dto);

      llenarFormularioDesdeDto(dto);

      // Scroll al formulario
      form.scrollIntoView({ behavior: "smooth", block: "start" });
    } catch (err) {
      console.error("Error en la petición de edición:", err);
      mostrarAlerta("Error de red al obtener el cliente.");
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
        mostrarAlerta("No se pudo eliminar el cliente.");
        return;
      }

      // Removemos la fila sin recargar
      if (fila && fila.parentNode) {
        fila.parentNode.removeChild(fila);
      }

      mostrarAlerta("Cliente eliminado correctamente", "success");
    } catch (err) {
      console.error("Error en la petición de eliminación:", err);
      mostrarAlerta("Error de red al eliminar el cliente.");
    }
  }

  // --- Bloquear: POST /api/clientes/{id}/bloquear ---
  async function bloquearCliente(id) {
    if (!apiClientesUrl) return;

    const confirmar = window.confirm(`¿Seguro que deseas bloquear el cliente con id ${id}?`);
    if (!confirmar) return;

    const url = `${apiClientesUrl}/${id}/bloquear`;

    try {
      const response = await fetch(url, {
        method: "POST",
        headers: buildHeaders()
      });

      if (!response.ok) {
        console.error("Error bloqueando cliente:", response.status);
        mostrarAlerta("No se pudo bloquear el cliente.");
        return;
      }

      const data = await response.json();
      console.log("Cliente bloqueado:", data);

      mostrarAlerta("Cliente bloqueado correctamente", "success");

      setTimeout(() => {
        window.location.reload();
      }, 1500);
    } catch (err) {
      console.error("Error en la petición de bloqueo:", err);
      mostrarAlerta("Error de red al bloquear el cliente.");
    }
  }

  // --- Activar: POST /api/clientes/{id}/activar ---
  async function activarCliente(id) {
    if (!apiClientesUrl) return;

    const confirmar = window.confirm(`¿Seguro que deseas activar el cliente con id ${id}?`);
    if (!confirmar) return;

    const url = `${apiClientesUrl}/${id}/activar`;

    try {
      const response = await fetch(url, {
        method: "POST",
        headers: buildHeaders()
      });

      if (!response.ok) {
        console.error("Error activando cliente:", response.status);
        mostrarAlerta("No se pudo activar el cliente.");
        return;
      }

      const data = await response.json();
      console.log("Cliente activado:", data);

      mostrarAlerta("Cliente activado correctamente", "success");

      setTimeout(() => {
        window.location.reload();
      }, 1500);
    } catch (err) {
      console.error("Error en la petición de activación:", err);
      mostrarAlerta("Error de red al activar el cliente.");
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

      // BLOQUEAR
      if (target.classList.contains("btn-bloquear")) {
        const fila = target.closest("tr");
        const id = fila.dataset.clienteId;
        bloquearCliente(id);
      }

      // ACTIVAR
      if (target.classList.contains("btn-activar")) {
        const fila = target.closest("tr");
        const id = fila.dataset.clienteId;
        activarCliente(id);
      }
    });
  }
});
