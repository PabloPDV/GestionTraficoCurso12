<template>
  <div class="container">
    <h1>Vías</h1>
    <div class="content">
      <div class="form-container">
        <form @submit.prevent="createVia" class="form">
          <div class="form-group">
            <label for="nombre">Nombre</label>
            <input id="nombre" v-model="newVia.nombre" required />
          </div>
          <div class="form-row">
            <div class="form-group">
              <label for="estado">Estado</label>
              <input id="estado" v-model="newVia.estado" required />
            </div>
            <div class="form-group">
              <label for="tipo">Tipo</label>
              <select id="tipo" v-model="newVia.tipo.id" required>
                <option value="" disabled selected>Seleccione el Tipo</option>
                <option v-for="tipo in tipos" :key="tipo.id" :value="tipo.id">{{ tipo.codigo }}</option>
              </select>
            </div>
            <div class="form-group">
              <label for="congestion">Nivel de Congestión</label>
              <select id="congestion" v-model="newVia.congestion" required>
                <option value="" disabled>Selecciona el nivel</option>
                <option value="NINGUNO">Ninguno</option>
                <option value="BAJO">Bajo</option>
                <option value="MEDIO">Medio</option>
                <option value="ALTO">Alto</option>
              </select>
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label for="longitud">Longitud (m)</label>
              <input id="longitud" v-model="newVia.longitud" type="number" step="0.01" required />
            </div>
            <div class="form-group">
              <label for="ancho">Ancho (m)</label>
              <input id="ancho" v-model="newVia.ancho" type="number" step="0.01" required />
            </div>
            <div class="form-group">
              <label for="capacidad">Capacidad (veh/h)</label>
              <input id="capacidad" v-model="newVia.capacidad" type="number" required />
            </div>
          </div>
          <div class="form-actions">
            <button type="submit" class="btn-crear">Crear</button>
            <button type="button"class="btn-limpiar" @click="clearForm">Limpiar</button>
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
                <th>Tipo</th>
                <th>Longitud</th>
                <th>Ancho</th>
                <th>Capacidad</th>
                <th class="actions-column">Acciones</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="via in vias" :key="via.id">
                <td>{{ via.nombre }}</td>
                <td>{{ via.estado }}</td>
                <td>{{ via.congestion }}</td>
                <td class="number-cell">{{ getTipoCodigo(via.tipo.id) }}</td>
                <td class="number-cell">{{ via.longitud }}</td>
                <td class="number-cell">{{ via.ancho }}</td>
                <td class="number-cell">{{ via.capacidad }}</td>
                <td class="actions-column">
                  <button class="btn-edit" @click="editVia(via)">Editar</button>
                  <button class="btn-delete" @click="confirmDelete(via)">Eliminar</button>
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
    <div v-if="editingVia" class="modal-edit">
      <div class="modal-content-edit modal-left">
        <h2>Editar Vía</h2>
        <form @submit.prevent="updateVia" class="form">
          <div class="form-group">
            <label for="edit-nombre">Nombre</label>
            <input id="edit-nombre" v-model="editingVia.nombre" required />
          </div>
          <div class="form-row">
            <div class="form-group">
              <label for="edit-estado">Estado</label>
              <input id="edit-estado" v-model="editingVia.estado" required />
            </div>
            <div class="form-group">
              <label for="edit-tipo">Tipo</label>
              <select id="edit-tipo" v-model="editingVia.tipo.id" required>
                <option value="" disabled>Seleccione el Tipo</option>
                <option v-for="tipo in tipos" :key="tipo.id" :value="tipo.id">{{ tipo.codigo }}</option>
              </select>
            </div>
            <div class="form-group">
              <label for="edit-congestion">Nivel de Congestión</label>
              <select id="edit-congestion" v-model="editingVia.congestion" required>
                <option value="" disabled>Selecciona el nivel</option>
                <option value="NINGUNO">Ninguno</option>
                <option value="BAJO">Bajo</option>
                <option value="MEDIO">Medio</option>
                <option value="ALTO">Alto</option>
              </select>
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label for="edit-longitud">Longitud (m)</label>
              <input id="edit-longitud" v-model="editingVia.longitud" type="number" step="0.01" required />
            </div>
            <div class="form-group">
              <label for="edit-ancho">Ancho (m)</label>
              <input id="edit-ancho" v-model="editingVia.ancho" type="number" step="0.01" required />
            </div>
            <div class="form-group">
              <label for="edit-capacidad">Capacidad (veh/h)</label>
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
import { format } from 'date-fns';

const vias = ref([]);
const tipos = ref([]);
const newVia = ref({
  nombre: '',
  tipo: { id: '' },
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
const messageItemName = ref('');

const fetchVias = async () => {
  try {
    const response = await axios.get('/api/vias');
    vias.value = response.data;
  } catch (error) {
    console.error('Error fetching vias:', error);
    showMessage('Error al obtener las vías', 'error');
  }
};

const fetchTipos = async () => {
  try {
    const response = await axios.get('/api/tipovia');
    tipos.value = response.data;
  } catch (error) {
    console.error('Error fetching tipos:', error);
    showMessage('Error al obtener los tipos de vía', 'error');
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
    showMessage('Vía creada exitosamente', 'success', newVia.value.nombre);
    clearForm();
  } catch (error) {
    console.error('Error creating via:', error);
    showMessage('Error al crear la vía', 'error', newVia.value.nombre);
  }
};

const clearForm = () => {
  newVia.value = {
    nombre: '',
    tipo: { id: '' },
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
    showMessage('Vía actualizada exitosamente', 'success', editingVia.value.nombre);
    editingVia.value = null;
  } catch (error) {
    console.error('Error updating via:', error);
    showMessage('Error al actualizar la vía', 'error', editingVia.value.nombre);
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
    showMessage('Vía eliminada exitosamente', 'success', deleteViaName.value);
    deleteViaId.value = null;
    deleteViaName.value = '';
  } catch (error) {
    console.error('Error deleting via:', error);
    showMessage('Error al eliminar la vía', 'error', deleteViaName.value);
  }
};

const cancelDelete = () => {
  showDeleteModal.value = false;
  deleteViaId.value = null;
  deleteViaName.value = '';
};

const cancelEdit = () => {
  editingVia.value = null;
  fetchVias();
};

const showMessage = (msg, type, itemName = '') => {
  message.value = msg;
  messageType.value = type;
  messageItemName.value = itemName;
  setTimeout(() => {
    message.value = '';
    messageType.value = '';
    messageItemName.value = '';
  }, 3000);
};

const validateVia = (via) => {
  return via.nombre && via.tipo.id !== null && via.estado && via.congestion && via.longitud !== null && via.ancho !== null && via.capacidad !== null;
};

const formatDate = (dateString) => {
  return format(new Date(dateString), 'dd/MM/yyyy HH:mm:ss');
};

const convertDecimal = (event, viaType, field) => {
  const value = event.target.value.replace(',', '.');
  if (viaType === 'newVia') {
    newVia.value[field] = parseFloat(value);
  } else if (viaType === 'editingVia') {
    editingVia.value[field] = parseFloat(value);
  }
};

const getTipoCodigo = (id) => {
  const tipo = tipos.value.find(t => t.id === id);
  return tipo ? tipo.codigo : '';
};

onMounted(() => {
  fetchVias();
  fetchTipos();
});
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
  font-size: 0.9rem;
  /* Reduce el tamaño de letra de las etiquetas */
}

.form input,
.form select,
.form textarea,
.form button {
  padding: 0.3rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  width: 100%;
  /* Ajustar el ancho al 100% */
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

.via-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 0.9rem;
  background-color: #fff;
  border-radius: 8px;
  overflow: hidden;
}

.via-table th,
.via-table td {
  padding: 0.75rem;
  border-bottom: 1px solid #ddd;
}

.via-table th {
  background-color: #f4f4f4;
  font-weight: bold;
  border-bottom: 2px solid #ddd;
  position: sticky;
  top: 0;
  z-index: 1;
}

.via-table tr:nth-child(even) {
  background-color: #f9f9f9;
}

.via-table tr:hover {
  background-color: #f1f1f1;
}

.via-table .actions-column {
  text-align: center;
  white-space: nowrap;
  /* Evitar salto de línea */
}

.via-table button {
  padding: 0.3rem 0.6rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  margin-right: 0.5rem;
  /* Añade separación entre los botones */
}

.via-table button:last-child {
  margin-right: 0;
  /* Elimina el margen derecho del último botón */
}

.via-table button:hover {
  background-color: #ddd;
}

.via-table .btn-edit {
  background-color: #FFC107;
  color: white;
}

.via-table .btn-edit:hover {
  background-color: #E0A800;
}

.via-table .btn-delete {
  background-color: #f44336;
  color: white;
}

.via-table .btn-delete:hover {
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
  margin-bottom: 15%;
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

.number-cell {
  text-align: right;
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

  .via-table {
    display: block;
    /* Hacer que la tabla sea un bloque */
    width: 100%;
    /* Asegurar que la tabla ocupe todo el ancho */
    overflow-x: auto;
    /* Habilitar el desplazamiento horizontal */
  }

  .via-table th,
  .via-table td {
    display: block;
    /* Hacer que las celdas sean bloques */
    width: 100%;
    /* Asegurar que las celdas ocupen todo el ancho */
    box-sizing: border-box;
    /* Incluir el padding y el borde en el ancho */
  }

  .via-table th {
    position: relative;
    /* Permitir que las celdas se posicionen correctamente */
    top: auto;
    /* Restablecer la posición superior */
    z-index: auto;
    /* Restablecer el índice z */
  }

  .via-table tr {
    display: block;
    /* Hacer que las filas sean bloques */
    margin-bottom: 1rem;
    /* Añadir espacio entre las filas */
  }
}
</style>