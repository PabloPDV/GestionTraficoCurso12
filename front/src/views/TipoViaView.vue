<template>
  <div class="container">
    <h1>Tipos de Vía</h1>
    <div class="content">
      <div class="form-container">
        <form @submit.prevent="createTipoVia" class="form">
          <div class="form-row">
            <div class="form-group">
              <label for="tipo">Tipo</label>
              <input id="tipo" v-model="newTipoVia.tipo" required />
            </div>
            <div class="form-group">
              <label for="codigo">Código</label>
              <input id="codigo" v-model="newTipoVia.codigo" required />
            </div>
            <div class="form-group">
              <label for="abreviatura">Abreviatura</label>
              <input id="abreviatura" v-model="newTipoVia.abreviatura" required />
            </div>
          </div>
          <div class="form-group">
            <label for="descripcion">Descripción</label>
            <textarea id="descripcion" v-model="newTipoVia.descripcion" ref="descripcionTextarea" required></textarea>
          </div>
          <div class="form-actions">
            <button type="submit" class="btn-crear">Crear</button>
            <button type="button" class="btn-limpiar" @click="clearForm">Limpiar</button>
          </div>
        </form>
      </div>
      <div class="list-container">
        <div class="table-container">
          <table class="tipo-via-table">
            <thead>
              <tr>
                <th>Tipo</th>
                <th>Código</th>
                <th class="abreviatura-column">Abreviatura</th>
                <th class="descripcion-column">Descripción</th>
                <th class="actions-column">Acciones</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="tipoVia in tipoVias" :key="tipoVia.id">
                <td data-label="Tipo">{{ tipoVia.tipo }}</td>
                <td data-label="Código">{{ tipoVia.codigo }}</td>
                <td data-label="Abreviatura" class="abreviatura-column">{{ tipoVia.abreviatura }}</td>
                <td data-label="Descripción" class="descripcion-column">{{ tipoVia.descripcion }}</td>
                <td data-label="Acciones" class="actions-column">
                  <button class="btn-edit" @click="editTipoVia(tipoVia)">Editar</button>
                  <button class="btn-delete" @click="confirmDelete(tipoVia)">Eliminar</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <!-- Mensajes de éxito y error -->
        <div v-if="message" :class="['message', messageType]">
          {{ message }}: {{ messageItemName }}
        </div>
      </div>
    </div>

    <!-- Modal de edición -->
    <div v-if="editingTipoVia" class="modal-edit">
      <div class="modal-content-edit modal-left">
        <h2>Editar Tipo de Vía</h2>
        <form @submit.prevent="updateTipoVia" class="form">
          <div class="form-row">
            <div class="form-group">
              <label for="edit-tipo">Tipo</label>
              <input id="edit-tipo" v-model="editingTipoVia.tipo" required />
            </div>
            <div class="form-group">
              <label for="edit-codigo">Código</label>
              <input id="edit-codigo" v-model="editingTipoVia.codigo" required />
            </div>
            <div class="form-group">
              <label for="edit-abreviatura">Abreviatura</label>
              <input id="edit-abreviatura" v-model="editingTipoVia.abreviatura" required />
            </div>
          </div>
          <div class="form-group">
            <label for="edit-descripcion">Descripción</label>
            <textarea id="edit-descripcion" v-model="editingTipoVia.descripcion" required></textarea>
          </div>
          <div class="form-actions">
            <button type="submit" class="btn-crear">Actualizar</button>
            <button type="button" class="btn-limpiar" @click="cancelEdit">Cancelar</button>
          </div>
        </form>
      </div>
    </div>

    <!-- Modal de confirmación -->
    <div v-if="showDeleteModal" class="modal">
      <div class="modal-content">
        <h3>Confirmar Eliminación</h3>
        <p>¿Estás seguro de que deseas eliminar el tipo de vía "{{ deleteTipoViaName }}"?</p>
        <div class="modal-actions">
          <button @click="deleteTipoVia(deleteTipoViaId)">Sí, eliminar</button>
          <button @click="cancelDelete">Cancelar</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const tipoVias = ref([]);
const newTipoVia = ref({
  tipo: '',
  codigo: '',
  abreviatura: '',
  descripcion: ''
});
const editingTipoVia = ref(null);
const showDeleteModal = ref(false);
const deleteTipoViaId = ref(null);
const deleteTipoViaName = ref('');
const message = ref('');
const messageType = ref('');
const messageItemName = ref('');
const descripcionTextarea = ref(null);

const fetchTipoVias = async () => {
  try {
    const response = await axios.get('/api/tipovia');
    tipoVias.value = response.data;
  } catch (error) {
    console.error('Error fetching tipoVias:', error);
    showMessage('Error al obtener los tipos de vía', 'error');
  }
};

const createTipoVia = async () => {
  if (!validateTipoVia(newTipoVia.value)) {
    showMessage('Por favor, completa todos los campos requeridos', 'error');
    return;
  }

  try {
    await axios.post('/api/tipovia', newTipoVia.value);
    fetchTipoVias();
    showMessage('Tipo de vía creado exitosamente', 'success', newTipoVia.value.tipo);
    clearForm();
  } catch (error) {
    console.error('Error creating tipoVia:', error);
    showMessage('Error al crear el tipo de vía', 'error', newTipoVia.value.tipo);
  }
};

const clearForm = () => {
  newTipoVia.value = {
    tipo: '',
    codigo: '',
    abreviatura: '',
    descripcion: ''
  };
  if (descripcionTextarea.value) {
    descripcionTextarea.value.style.height = 'auto'; // Restablecer el tamaño del textarea
  }
  fetchTipoVias();
};

const editTipoVia = (tipoVia) => {
  editingTipoVia.value = { ...tipoVia };
};

const updateTipoVia = async () => {
  if (!validateTipoVia(editingTipoVia.value)) {
    showMessage('Por favor, completa todos los campos requeridos', 'error');
    return;
  }

  try {
    await axios.put(`/api/tipovia/${editingTipoVia.value.id}`, editingTipoVia.value);
    fetchTipoVias();
    showMessage('Tipo de vía actualizado exitosamente', 'success', editingTipoVia.value.tipo);
    editingTipoVia.value = null;
  } catch (error) {
    console.error('Error updating tipoVia:', error);
    showMessage('Error al actualizar el tipo de vía', 'error', editingTipoVia.value.tipo);
  }
};

const confirmDelete = (tipoVia) => {
  deleteTipoViaId.value = tipoVia.id;
  deleteTipoViaName.value = tipoVia.tipo;
  showDeleteModal.value = true;
};

const deleteTipoVia = async (id) => {
  try {
    await axios.delete(`/api/tipovia/${id}`);
    fetchTipoVias();
    showMessage('Tipo de vía eliminado exitosamente', 'success', deleteTipoViaName.value);
  } catch (error) {
    console.error('Error deleting tipoVia:', error);
    if (error.response && error.response.status === 500 && error.response.data.message.includes('Referential integrity constraint violation')) {
      showMessage(`No se puede eliminar el tipo de vía "${deleteTipoViaName.value}" porque está asignado a una vía`, 'error', 'deberás eliminar las vías asociadas primero', 4000);
    } else {
      showMessage('Error al eliminar el tipo de vía', 'error', deleteTipoViaName.value);
    }
  } finally {
    showDeleteModal.value = false;
    deleteTipoViaId.value = null;
    deleteTipoViaName.value = '';
  }
};

const cancelDelete = () => {
  showDeleteModal.value = false;
  deleteTipoViaId.value = null;
  deleteTipoViaName.value = '';
  fetchTipoVias();
};

const cancelEdit = () => {
  editingTipoVia.value = null;
  fetchTipoVias();
};

const showMessage = (msg, type, itemName = '', duration = 3000) => {
  message.value = msg;
  messageType.value = type;
  messageItemName.value = itemName;
  setTimeout(() => {
    message.value = '';
    messageType.value = '';
    messageItemName.value = '';
  }, duration);
};

const validateTipoVia = (tipoVia) => {
  return tipoVia.tipo && tipoVia.codigo && tipoVia.abreviatura && tipoVia.descripcion;
};

onMounted(fetchTipoVias);
</script>

<style scoped>
html,
body,
#app {
  height: 100%;
  margin: 0;
}

.container {
  max-width: 1800px;
  margin: 0 auto;
  padding: 2rem;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-top: 2rem;
}

h1 {
  text-align: center;
  color: #333;
}

.content {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.form-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  position: relative;
}

.list-container {
  flex: 2;
  margin-left: 1rem;
}

.form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.form-row {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.form-group label {
  margin-bottom: 0.5rem;
  font-weight: bold;
}

.form input,
.form select,
.form textarea,
.form button {
  padding: 0.3rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  width: 100%;
}

.form input[type="number"] {
  text-align: right;
}

.form textarea {
  resize: vertical;
  max-height: 150px;
  /* Limita el crecimiento del textarea */
}

.form button {
  color: white;
  cursor: pointer;
}

.form-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 1rem;
  /* Asegura que los botones estén debajo de los campos */
}

.btn-crear {
  background-color: #4caf50;
}


.btn-limpiar {
  background-color: #007bff;
}

.btn-crear:hover {
  background-color: #218838;
}

.btn-limpiar:hover {
  background-color: #0157b3;
}

.table-container {
  max-height: 400px;
  overflow-y: auto;
  position: relative;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.tipo-via-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 0.9rem;
  background-color: #fff;
  border-radius: 8px;
  overflow: hidden;
}

.tipo-via-table th,
.tipo-via-table td {
  padding: 0.75rem;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.tipo-via-table th {
  background-color: #f4f4f4;
  font-weight: bold;
  border-bottom: 2px solid #ddd;
  position: sticky;
  top: 0;
  z-index: 1;
}

.tipo-via-table tr:nth-child(even) {
  background-color: #f9f9f9;
}

.tipo-via-table tr:hover {
  background-color: #f1f1f1;
}

.tipo-via-table .actions-column {
  text-align: center;
  white-space: nowrap;
  /* Evitar salto de línea */
}

.tipo-via-table button {
  padding: 0.3rem 0.6rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  margin-right: 0.5rem;
  /* Añade separación entre los botones */
}

.tipo-via-table button:last-child {
  margin-right: 0;
  /* Elimina el margen derecho del último botón */
}

.tipo-via-table button:hover {
  background-color: #ddd;
}

.tipo-via-table .btn-edit {
  background-color: #FFC107;
  color: white;
}

.tipo-via-table .btn-edit:hover {
  background-color: #E0A800;
}

.tipo-via-table .btn-delete {
  background-color: #f44336;
  color: white;
}

.tipo-via-table .btn-delete:hover {
  background-color: #e53935;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 10;
  /* Asegura que los modales tengan prioridad sobre la cabecera de la tabla */
}

.modal-edit {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: left;
  align-items: center;
  z-index: 10;
  /* Asegura que los modales tengan prioridad sobre la cabecera de la tabla */
}

.modal-content {
  background-color: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.modal-content-edit {
  background-color: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  text-align: center;
  margin-left: 2%;
  margin-bottom: 21%;
}

.modal-content-confirmacion {
  background-color: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.modal-actions {
  display: flex;
  justify-content: space-around;
  margin-top: 1rem;
}

.modal-actions button {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.modal-actions button:first-child {
  background-color: #dc3545;
  color: white;
}

.modal-actions button:first-child:hover {
  background-color: #c82333;
}

.modal-actions button:last-child {
  background-color: #6c757d;
  color: white;
}

.modal-actions button:last-child:hover {
  background-color: #5a6268;
}

.message {
  padding: 1rem;
  margin-top: 1rem;
  border-radius: 4px;
  text-align: center;
}

.message.success {
  background-color: #d4edda;
  color: #155724;
}

.message.error {
  background-color: #f8d7da;
  color: #721c24;
}

@media (min-width: 768px) {
  .content {
    flex-direction: row;
  }

  .form-container {
    margin-right: 1rem;
  }

  .list-container {
    margin-left: 1rem;
  }

  .form-row {
    flex-direction: row;
  }

  .form input,
  .form select,
  .form textarea,
  .form button {
    width: 95%;
    /* Acorta levemente los campos del formulario */
  }
}

@media (max-width: 767px) {
  .container {
    padding: 1rem;
  }

  .form input,
  .form select,
  .form textarea,
  .form button {
    width: 100%;
  }

  .form-container,
  .list-container {
    margin: 0;
  }

  .content {
    gap: 0.5rem;
  }

  .table-container {
    max-height: none;
    /* Permitir que la tabla crezca en altura */
    overflow-x: auto;
    /* Habilitar el desplazamiento horizontal */
  }

  .tipo-via-table {
    display: block;
    /* Hacer que la tabla sea un bloque */
    width: 100%;
    /* Asegurar que la tabla ocupe todo el ancho */
    overflow-x: auto;
    /* Habilitar el desplazamiento horizontal */
  }

  .tipo-via-table th,
  .tipo-via-table td {
    display: block;
    /* Hacer que las celdas sean bloques */
    width: 100%;
    /* Asegurar que las celdas ocupen todo el ancho */
    box-sizing: border-box;
    /* Incluir el padding y el borde en el ancho */
  }

  .tipo-via-table th {
    position: relative;
    /* Permitir que las celdas se posicionen correctamente */
    top: auto;
    /* Restablecer la posición superior */
    z-index: auto;
    /* Restablecer el índice z */
  }

  .tipo-via-table td::before {
    content: attr(data-label);
    /* Usar el atributo data-label para mostrar la cabecera */
    font-weight: bold;
    display: block;
    margin-bottom: 0.5rem;
  }

  .tipo-via-table tr {
    display: block;
    /* Hacer que las filas sean bloques */
    margin-bottom: 1rem;
    /* Añadir espacio entre las filas */
  }
}
</style>