<template>
  <div class="container">
    <h1>Vías</h1>
    <div class="content">
      <div class="form-container">
        <form @submit.prevent="createVia" class="form">
          <input v-model="newVia.nombre" placeholder="Nombre" required />
          <input v-model="newVia.longitud" placeholder="Longitud" type="number" required />
          <input v-model="newVia.ancho" placeholder="Ancho" type="number" required />
          <input v-model="newVia.capacidad" placeholder="Capacidad" type="number" required />
          <input v-model="newVia.estado" placeholder="Estado" required />
          <select v-model="newVia.congestion" required>
            <option value="" disabled>Selecciona el nivel de congestión</option>
            <option value="BAJO">Bajo</option>
            <option value="MEDIO">Medio</option>
            <option value="ALTO">Alto</option>
          </select>
          <input v-model="newVia.tipo.id" placeholder="Tipo ID" type="number" required />
          <button type="submit">Crear Vía</button>
        </form>
      </div>
      <div class="list-container">
        <ul class="via-list">
          <li v-for="via in vias" :key="via.id" class="via-item">
            <span>{{ via.nombre }} - {{ via.estado }}</span>
            <div class="actions">
              <button @click="editVia(via)">Editar</button>
              <button @click="confirmDelete(via.id)">Eliminar</button>
            </div>
          </li>
        </ul>
      </div>
    </div>

    <!-- Modal de edición -->
    <div v-if="editingVia" class="modal">
      <div class="modal-content">
        <h2>Editar Vía</h2>
        <form @submit.prevent="updateVia" class="form">
          <input v-model="editingVia.nombre" placeholder="Nombre" required />
          <input v-model="editingVia.longitud" placeholder="Longitud" type="number" required />
          <input v-model="editingVia.ancho" placeholder="Ancho" type="number" required />
          <input v-model="editingVia.capacidad" placeholder="Capacidad" type="number" required />
          <input v-model="editingVia.estado" placeholder="Estado" required />
          <select v-model="editingVia.congestion" required>
            <option value="" disabled>Selecciona el nivel de congestión</option>
            <option value="BAJO">Bajo</option>
            <option value="MEDIO">Medio</option>
            <option value="ALTO">Alto</option>
          </select>
          <input v-model="editingVia.tipo.id" placeholder="Tipo ID" type="number" required />
          <div class="form-actions">
            <button type="submit">Actualizar Vía</button>
            <button type="button" @click="cancelEdit">Cancelar</button>
          </div>
        </form>
      </div>
    </div>

    <!-- Modal de confirmación -->
    <div v-if="showDeleteModal" class="modal">
      <div class="modal-content">
        <h3>Confirmar Eliminación</h3>
        <p>¿Estás seguro de que deseas eliminar esta vía?</p>
        <div class="modal-actions">
          <button @click="deleteVia(deleteViaId)">Sí, eliminar</button>
          <button @click="cancelDelete">Cancelar</button>
        </div>
      </div>
    </div>

    <!-- Mensajes de éxito y error -->
    <div v-if="message" :class="['message', messageType]">
      {{ message }}
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const vias = ref([]);
const newVia = ref({
  nombre: '',
  longitud: '',
  ancho: '',
  capacidad: '',
  estado: '',
  congestion: '',
  tipo: {
    id: ''
  }
});
const editingVia = ref(null);
const showDeleteModal = ref(false);
const deleteViaId = ref(null);
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
    newVia.value = {
      nombre: '',
      longitud: '',
      ancho: '',
      capacidad: '',
      estado: '',
      congestion: '',
      tipo: {
        id: ''
      }
    };
    showMessage('Vía creada exitosamente', 'success');
  } catch (error) {
    console.error('Error creating via:', error);
    showMessage('Error al crear la vía', 'error');
  }
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

const confirmDelete = (id) => {
  deleteViaId.value = id;
  showDeleteModal.value = true;
};

const deleteVia = async (id) => {
  try {
    await axios.delete(`/api/vias/${id}`);
    fetchVias();
    showDeleteModal.value = false;
    deleteViaId.value = null;
    showMessage('Vía eliminada exitosamente', 'success');
  } catch (error) {
    console.error('Error deleting via:', error);
    showMessage('Error al eliminar la vía', 'error');
  }
};

const cancelDelete = () => {
  showDeleteModal.value = false;
  deleteViaId.value = null;
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
  return via.nombre && via.longitud && via.ancho && via.capacidad && via.estado && via.congestion && via.tipo.id;
};

onMounted(fetchVias);
</script>

<style scoped>
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-top: 2rem; /* Añadir margen superior */
}

h1 {
  text-align: center;
  color: #333;
}

.content {
  display: flex;
  justify-content: space-between;
}

.form-container,
.list-container {
  width: 48%;
}

.form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-bottom: 2rem;
}

.form input,
.form select,
.form button {
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
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
}

.via-list {
  list-style: none;
  padding: 0;
  max-height: 400px; /* Altura máxima para la lista */
  overflow-y: auto; /* Hacer la lista desplazable */
}

.via-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.5rem;
  border-bottom: 1px solid #ccc;
}

.via-item .actions button {
  margin-left: 0.5rem;
  padding: 0.3rem 0.6rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.via-item .actions button:first-child {
  background-color: #ffc107;
  color: white;
}

.via-item .actions button:first-child:hover {
  background-color: #e0a800;
}

.via-item .actions button:last-child {
  background-color: #dc3545;
  color: white;
}

.via-item .actions button:last-child:hover {
  background-color: #c82333;
}

/* Estilos para el modal */
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

/* Estilos para los mensajes */
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
</style>