<template>
    <div class="container">
      <h1>Tipos de Vía</h1>
      <div class="content">
        <div class="form-container">
          <form @submit.prevent="createTipoVia" class="form">
            <input v-model="newTipoVia.tipo" placeholder="Tipo" required />
            <input v-model="newTipoVia.codigo" placeholder="Código" required />
            <input v-model="newTipoVia.abreviatura" placeholder="Abreviatura" />
            <input v-model="newTipoVia.descripcion" placeholder="Descripción" />
            <button type="submit">Crear Tipo de Vía</button>
          </form>
        </div>
        <div class="list-container">
          <table class="tipo-via-table">
            <thead>
              <tr>
                <th>Tipo</th>
                <th>Código</th>
                <th>Abreviatura</th>
                <th>Descripción</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="tipoVia in tipoVias" :key="tipoVia.id">
                <td>{{ tipoVia.tipo }}</td>
                <td>{{ tipoVia.codigo }}</td>
                <td>{{ tipoVia.abreviatura }}</td>
                <td>{{ tipoVia.descripcion }}</td>
                <td>
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
        <div class="modal-content">
          <h2>Editar Tipo de Vía</h2>
          <form @submit.prevent="updateTipoVia" class="form">
            <input v-model="editingTipoVia.tipo" placeholder="Tipo" required />
            <input v-model="editingTipoVia.codigo" placeholder="Código" required />
            <input v-model="editingTipoVia.abreviatura" placeholder="Abreviatura" />
            <input v-model="editingTipoVia.descripcion" placeholder="Descripción" />
            <div class="form-actions">
              <button type="submit">Actualizar Tipo de Vía</button>
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
      newTipoVia.value = {
        tipo: '',
        codigo: '',
        abreviatura: '',
        descripcion: ''
      };
      showMessage('Tipo de vía creado exitosamente', 'success');
    } catch (error) {
      console.error('Error creating tipoVia:', error);
      showMessage('Error al crear el tipo de vía', 'error');
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
    display: flex;
    flex-direction: column;
    height: 100%;
    padding: 2rem;
    background-color: #f9f9f9;
  }
  
  h1 {
    text-align: center;
    color: #333;
  }
  
  .content {
    display: flex;
    flex: 1;
    gap: 2rem;
  }
  
  .form-container {
    flex: 1;
  }
  
  .list-container {
    flex: 2;
  }
  
  .form {
    display: flex;
    flex-direction: column;
    gap: 1rem;
  }
  
  .form input,
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
  
  .tipo-via-table {
    width: 100%;
    border-collapse: collapse;
  }
  
  .tipo-via-table th, .tipo-via-table td {
    padding: 0.5rem;
    border: 1px solid #ccc;
    text-align: left;
  }
  
  .tipo-via-table th {
    background-color: #f2f2f2;
  }
  
  .tipo-via-table td button {
    margin-right: 0.5rem;
    padding: 0.3rem 0.6rem;
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