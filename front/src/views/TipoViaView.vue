<template>
  <div class="container">
    <h1>Tipos de Vía</h1>
    <div class="content">
      <div class="form-container">
        <form @submit.prevent="createTipoVia" class="form">
          <input v-model="newTipoVia.tipo" placeholder="Tipo" required />
          <input v-model="newTipoVia.codigo" placeholder="Código" required />
          <input v-model="newTipoVia.abreviatura" placeholder="Abreviatura" />
          <textarea ref="descripcionTextarea" v-model="newTipoVia.descripcion" placeholder="Descripción"></textarea>
          <div class="form-actions">
            <button type="submit" class="btn-crear">Crear</button>
            <button type="button" @click="clearForm">Limpiar</button>
          </div>
        </form>
      </div>
      <div class="list-container">
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
                <button @click="confirmDelete(tipoVia.id)">Eliminar</button>
              </td>
            </tr>
          </tbody>
        </table>
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
          <input v-model="editingTipoVia.tipo" placeholder="Tipo" required />
          <input v-model="editingTipoVia.codigo" placeholder="Código" required />
          <input v-model="editingTipoVia.abreviatura" placeholder="Abreviatura" />
          <textarea v-model="editingTipoVia.descripcion" placeholder="Descripción"></textarea>
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
        <p>¿Estás seguro de que deseas eliminar este tipo de vía?</p>
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

const confirmDelete = (id) => {
  deleteTipoViaId.value = id;
  showDeleteModal.value = true;
};

const deleteTipoVia = async (id) => {
  try {
    await axios.delete(`/api/tipovia/${id}`);
    fetchTipoVias();
    showDeleteModal.value = false;
    deleteTipoViaId.value = null;
    showMessage('Tipo de vía eliminado exitosamente', 'success');
  } catch (error) {
    console.error('Error deleting tipoVia:', error);
    showMessage('Error al eliminar el tipo de vía', 'error');
  }
};

const cancelDelete = () => {
  showDeleteModal.value = false;
  deleteTipoViaId.value = null;
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
  return tipoVia.tipo && tipoVia.codigo;
};

onMounted(fetchTipoVias);
</script>

<style scoped>
html, body, #app {
  height: 100%;
  margin: 0;
}

.container {
  max-width: 1200px;
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
  gap: 1rem; /* Reducir el gap para acercar las secciones */
}

.form-container {
  flex: 1;
  margin-right: 1rem; /* Reducir el margen derecho */
}

.list-container {
  flex: 2;
  margin-left: 1rem; /* Reducir el margen izquierdo */
}

.form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.form input,
.form textarea,
.form button {
  padding: 0.3rem; /* Reducir el padding para hacer los campos más estrechos */
  border: 1px solid #ccc;
  border-radius: 4px;
  width: 80%; /* Ajustar el ancho de los campos */
  margin: 0 auto; /* Centrar los campos */
}

.form textarea {
  resize: vertical; /* Permitir el redimensionamiento vertical del textarea */
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

.btn-crear {
  background-color: #28a745; /* Color verde */
}

.btn-crear:hover {
  background-color: #218838; /* Color verde oscuro */
}

.tipo-via-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 0.9rem; /* Reducir el tamaño de fuente para hacer la tabla más pequeña */
}

.tipo-via-table th, .tipo-via-table td {
  padding: 0.2rem; /* Reducir el padding para hacer la tabla más compacta */
  border: 1px solid #ccc;
  text-align: left;
}

.tipo-via-table th {
  background-color: #f2f2f2;
}

.abreviatura-column {
  width: 80px; /* Reducir el ancho de la columna Abreviatura */
}

.descripcion-column {
  width: 300px; /* Aumentar el ancho de la columna Descripción */
  word-wrap: break-word; /* Permitir que el texto se muestre en varias líneas */
}

.actions-column {
  white-space: nowrap;
  width: 1%;
}

.tipo-via-table td button {
  margin-right: 0.2rem; /* Reducir el margen entre botones */
  padding: 0.2rem 0.4rem; /* Reducir el padding de los botones */
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

/* Estilos para el modal */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: flex-start; /* Alinear el modal a la izquierda */
  align-items: center;
}

.modal-content {
  background-color: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  text-align: center;
  margin-left: 25rem; 
  margin-bottom: 15rem;
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

/* Media queries para hacer la vista responsive */
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