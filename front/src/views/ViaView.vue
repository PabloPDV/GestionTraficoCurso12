<template>
  <div class="container">
    <h1>Vías</h1>
    <div class="content">
      <div class="form-container">
        <form @submit.prevent="createVia" class="form">
          <div class="form-row">
            <div class="form-group">
              <label for="nombre">Nombre</label>
              <input id="nombre" v-model="newVia.nombre" required />
            </div>
            <div class="form-group">
              <label for="tipo-id">Tipo ID</label>
              <input id="tipo-id" v-model="newVia.tipo.id" type="number" required />
            </div>
          </div>
          <div class="form-group">
            <label for="estado">Estado</label>
            <input id="estado" v-model="newVia.estado" required />
          </div>
          <div class="form-group">
            <label for="congestion">Nivel de Congestión</label>
            <select id="congestion" v-model="newVia.congestion" required>
              <option value="" disabled>Selecciona el nivel de congestión</option>
              <option value="NINGUNO">Ninguno</option>
              <option value="BAJO">Bajo</option>
              <option value="MEDIO">Medio</option>
              <option value="ALTO">Alto</option>
            </select>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label for="longitud">Longitud</label>
              <input id="longitud" v-model="newVia.longitud" type="number" required />
            </div>
            <div class="form-group">
              <label for="ancho">Ancho</label>
              <input id="ancho" v-model="newVia.ancho" type="number" required />
            </div>
            <div class="form-group">
              <label for="capacidad">Capacidad</label>
              <input id="capacidad" v-model="newVia.capacidad" type="number" required />
            </div>
          </div>
          <div class="form-actions">
            <button type="submit" class="btn-crear">Crear</button>
            <button type="button" @click="clearForm">Limpiar</button>
          </div>
        </form>
      </div>
      <div class="list-container">
        <div class="table-container">
          <table class="via-table">
            <thead>
              <tr>
                <th>Nombre</th>
                <th>Estado</th>
                <th>Congestión</th>
                <th>Tipo ID</th>
                <th>Longitud</th>
                <th>Ancho</th>
                <th>Capacidad</th>
                <th>Creación</th>
                <th>Modificación</th>
                <th class="actions-column">Acciones</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="via in vias" :key="via.id">
                <td>{{ via.nombre }}</td>
                <td>{{ via.estado }}</td>
                <td>{{ via.congestion }}</td>
                <td class="number-cell">{{ via.tipo.id }}</td>
                <td class="number-cell">{{ via.longitud }}</td>
                <td class="number-cell">{{ via.ancho }}</td>
                <td class="number-cell">{{ via.capacidad }}</td>
                <td>{{ via.creacion }}</td>
                <td>{{ via.modificacion }}</td>
                <td class="actions-column">
                  <button @click="editVia(via)">Editar</button>
                  <button @click="confirmDelete(via)">Eliminar</button>
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
    <div v-if="editingVia" class="modal">
      <div class="modal-content modal-left">
        <h2>Editar Vía</h2>
        <form @submit.prevent="updateVia" class="form">
          <div class="form-row">
            <div class="form-group">
              <label for="edit-nombre">Nombre</label>
              <input id="edit-nombre" v-model="editingVia.nombre" required />
            </div>
            <div class="form-group">
              <label for="edit-tipo-id">Tipo ID</label>
              <input id="edit-tipo-id" v-model="editingVia.tipo.id" type="number" required />
            </div>
          </div>
          <div class="form-group">
            <label for="edit-estado">Estado</label>
            <input id="edit-estado" v-model="editingVia.estado" required />
          </div>
          <div class="form-group">
            <label for="edit-congestion">Nivel de Congestión</label>
            <select id="edit-congestion" v-model="editingVia.congestion" required>
              <option value="" disabled>Selecciona el nivel de congestión</option>
              <option value="NINGUNO">Ninguno</option>
              <option value="BAJO">Bajo</option>
              <option value="MEDIO">Medio</option>
              <option value="ALTO">Alto</option>
            </select>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label for="edit-longitud">Longitud</label>
              <input id="edit-longitud" v-model="editingVia.longitud" type="number" required />
            </div>
            <div class="form-group">
              <label for="edit-ancho">Ancho</label>
              <input id="edit-ancho" v-model="editingVia.ancho" type="number" required />
            </div>
            <div class="form-group">
              <label for="edit-capacidad">Capacidad</label>
              <input id="edit-capacidad" v-model="editingVia.capacidad" type="number" required />
            </div>
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
        <p>¿Estás seguro de que deseas eliminar la vía "{{ deleteViaName }}"?</p>
        <div class="modal-actions">
          <button @click="deleteVia(deleteViaId)">Sí, eliminar</button>
          <button @click="cancelDelete">Cancelar</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const vias = ref([]);
const newVia = ref({
  nombre: '',
  tipo: { id: null },
  estado: '',
  congestion: '',
  longitud: null,
  ancho: null,
  capacidad: null
});
const editingVia = ref(null);
const showDeleteModal = ref(false);
const deleteViaId = ref(null);
const deleteViaName = ref('');
const message = ref('');
const messageType = ref('');

const fetchVias = async () => {
  try {
    const response = await axios.get('/api/vias');
    vias.value = response.data;
  } catch (error) {
    console.error('Error fetching vias:', error);
    showMessage('Error al obtener las vías', 'error');
  }
};

const createVia = async () => {
  if (!validateVia(newVia.value)) {
    showMessage('Por favor, completa todos los campos requeridos', 'error');
    return;
  }

  try {
    await axios.post('/api/vias', newVia.value);
    fetchVias();
    clearForm();
    showMessage('Vía creada exitosamente', 'success');
  } catch (error) {
    console.error('Error creating via:', error);
    showMessage('Error al crear la vía', 'error');
  }
};

const clearForm = () => {
  newVia.value = {
    nombre: '',
    tipo: { id: null },
    estado: '',
    congestion: '',
    longitud: null,
    ancho: null,
    capacidad: null
  };
};

const editVia = (via) => {
  editingVia.value = { ...via };
};

const updateVia = async () => {
  if (!validateVia(editingVia.value)) {
    showMessage('Por favor, completa todos los campos requeridos', 'error');
    return;
  }

  try {
    await axios.put(`/api/vias/${editingVia.value.id}`, editingVia.value);
    fetchVias();
    editingVia.value = null;
    showMessage('Vía actualizada exitosamente', 'success');
  } catch (error) {
    console.error('Error updating via:', error);
    showMessage('Error al actualizar la vía', 'error');
  }
};

const confirmDelete = (via) => {
  deleteViaId.value = via.id;
  deleteViaName.value = via.nombre;
  showDeleteModal.value = true;
};

const deleteVia = async (id) => {
  try {
    await axios.delete(`/api/vias/${id}`);
    fetchVias();
    showDeleteModal.value = false;
    deleteViaId.value = null;
    deleteViaName.value = '';
    showMessage('Vía eliminada exitosamente', 'success');
  } catch (error) {
    console.error('Error deleting via:', error);
    showMessage('Error al eliminar la vía', 'error');
  }
};

const cancelDelete = () => {
  showDeleteModal.value = false;
  deleteViaId.value = null;
  deleteViaName.value = '';
};

const cancelEdit = () => {
  editingVia.value = null;
};

const showMessage = (msg, type) => {
  message.value = msg;
  messageType.value = type;
  setTimeout(() => {
    message.value = '';
    messageType.value = '';
  }, 3000);
};

const validateVia = (via) => {
  return via.nombre && via.tipo.id !== null && via.estado && via.congestion && via.longitud !== null && via.ancho !== null && via.capacidad !== null;
};

onMounted(fetchVias);
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
  max-height: 150px; /* Limita el crecimiento del textarea */
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

.via-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 0.9rem;
}

.via-table th, .via-table td {
  padding: 0.2rem;
  border: 1px solid #ccc;
  text-align: left;
}

.via-table th {
  background-color: #b6b5b5 !important;
  position: sticky;
  top: 0;
  z-index: 1;
  border-bottom: 2px solid #ccc !important;
}

.tipo-id-column {
  width: 80px;
}

.actions-column {
  white-space: nowrap;
  width: 1%;
}

.via-table td button {
  margin-right: 0.2rem;
  padding: 0.2rem 0.4rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.via-table td button:first-child {
  background-color: #ffc107;
  color: white;
}

.via-table td button:first-child:hover {
  background-color: #e0a800;
}

.via-table td button:last-child {
  background-color: #dc3545;
  color: white;
}

.via-table td button:last-child:hover {
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
}

.modal-content {
  background-color: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  text-align: center;
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