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
          <div class="form-actions fixed-actions">
            <button type="submit" class="btn-crear">Crear</button>
            <button type="button" @click="clearForm">Limpiar</button>
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
                <td>{{ tipoVia.tipo }}</td>
                <td>{{ tipoVia.codigo }}</td>
                <td class="abreviatura-column">{{ tipoVia.abreviatura }}</td>
                <td class="descripcion-column">{{ tipoVia.descripcion }}</td>
                <td class="actions-column">
                  <button @click="editTipoVia(tipoVia)">Editar</button>
                  <button @click="confirmDelete(tipoVia)">Eliminar</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <!-- Mensajes de éxito y error -->
        <div v-if="message" :class="['message', messageType]">
          {{ message }}
        </div>
      </div>
    </div>

    <!-- Modal de edición -->
    <div v-if="editingTipoVia" class="modal">
      <div class="modal-content modal-left">
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
            <button type="submit">Actualizar</button>
            <button type="button" @click="cancelEdit">Cancelar</button>
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
    clearForm();
    showMessage('Tipo de vía creado exitosamente', 'success');
  } catch (error) {
    console.error('Error creating tipoVia:', error);
    showMessage('Error al crear el tipo de vía', 'error');
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
    editingTipoVia.value = null;
    showMessage('Tipo de vía actualizado exitosamente', 'success');
  } catch (error) {
    console.error('Error updating tipoVia:', error);
    showMessage('Error al actualizar el tipo de vía', 'error');
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
    showDeleteModal.value = false;
    deleteTipoViaId.value = null;
    deleteTipoViaName.value = '';
    showMessage('Tipo de vía eliminado exitosamente', 'success');
  } catch (error) {
    console.error('Error deleting tipoVia:', error);
    showMessage('Error al eliminar el tipo de vía', 'error');
  }
};

const cancelDelete = () => {
  showDeleteModal.value = false;
  deleteTipoViaId.value = null;
  deleteTipoViaName.value = '';
};

const cancelEdit = () => {
  editingTipoVia.value = null;
};

const showMessage = (msg, type) => {
  message.value = msg;
  messageType.value = type;
  setTimeout(() => {
    message.value = '';
    messageType.value = '';
  }, 3000);
};

const validateTipoVia = (tipoVia) => {
  return tipoVia.tipo && tipoVia.codigo && tipoVia.abreviatura && tipoVia.descripcion;
};

onMounted(fetchTipoVias);
</script>

<style scoped>
html, body, #app {
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
  flex-direction: row;
  gap: 1rem;
}

.form-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  position: relative;
  height: 100%;
  padding-bottom: 4rem; /* Añade espacio para los botones fijos */
}

.list-container {
  flex: 2;
  margin-left: 1rem;
}

.form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  height: 100%;
}

.form-row {
  display: flex;
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
  max-height: 97px;
}

.form button {
  background-color: #007bff;
  color: white;
  cursor: pointer;
}

.form button:hover {
  background-color: #0056b3;
}

.form-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 1rem; /* Asegura que los botones estén debajo de los campos */
}

.fixed-actions {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: transparent; /* Cambia el fondo a transparente */
  padding: 1rem;
  box-shadow: none; /* Elimina la sombra */
  margin-top: 1rem; /* Añade margen superior */
}

.btn-crear {
  background-color: #28a745;
}

.btn-crear:hover {
  background-color: #218838;
}

.table-container {
  max-height: 400px;
  overflow-y: auto;
  position: relative;
}

.tipo-via-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 0.9rem;
}

.tipo-via-table th, .tipo-via-table td {
  padding: 0.2rem;
  border: 1px solid #ccc;
  text-align: left;
}

.tipo-via-table th {
  background-color: #b6b5b5 !important;
  position: sticky;
  top: 0;
  z-index: 1;
  border-bottom: 2px solid #ccc !important;
}

.actions-column {
  white-space: nowrap;
  width: 1%;
}

.tipo-via-table td button {
  margin-right: 0.2rem;
  padding: 0.2rem 0.4rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.tipo-via-table td button:first-child {
  background-color: #ffc107;
  color: white;
}

.tipo-via-table td button:first-child:hover {
  background-color: #e0a800;
}

.tipo-via-table td button:last-child {
  background-color: #dc3545;
  color: white;
}

.tipo-via-table td button:last-child:hover {
  background-color: #c82333;
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
  z-index: 1000; /* Asegura que el modal esté por encima del contenido */
}

.modal-content {
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

  .form input,
  .form select,
  .form textarea,
  .form button {
    width: 100%;
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
}
</style>