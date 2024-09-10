<template>
    <div class="container">
      <h1>Gestión de Tipos de Vía</h1>
      <form @submit.prevent="createTipoVia" class="form">
        <input v-model="newTipoVia.tipo" placeholder="Tipo" required />
        <input v-model="newTipoVia.codigo" placeholder="Código" required />
        <input v-model="newTipoVia.abreviatura" placeholder="Abreviatura" />
        <input v-model="newTipoVia.descripcion" placeholder="Descripción" />
        <button type="submit">Crear Tipo de Vía</button>
      </form>
  
      <ul class="tipo-via-list">
        <li v-for="tipoVia in tipoVias" :key="tipoVia.id" class="tipo-via-item">
          <span>{{ tipoVia.tipo }} - {{ tipoVia.codigo }}</span>
          <div class="actions">
            <button @click="editTipoVia(tipoVia)">Editar</button>
            <button @click="confirmDelete(tipoVia.id)">Eliminar</button>
          </div>
        </li>
      </ul>
  
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
  
      <!-- Mensajes de éxito y error -->
      <div v-if="message" :class="['message', messageType]">
        {{ message }}
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
  .container {
    max-width: 800px;
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
  
  .form {
    display: flex;
    flex-direction: column;
    gap: 1rem;
    margin-bottom: 2rem;
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
  
  .tipo-via-list {
    list-style: none;
    padding: 0;
    max-height: 400px; /* Altura máxima para la lista */
    overflow-y: auto; /* Hacer la lista desplazable */
  }
  
  .tipo-via-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0.5rem;
    border-bottom: 1px solid #ccc;
  }
  
  .tipo-via-item .actions button {
    margin-left: 0.5rem;
    padding: 0.3rem 0.6rem;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
  
  .tipo-via-item .actions button:first-child {
    background-color: #ffc107;
    color: white;
  }
  
  .tipo-via-item .actions button:first-child:hover {
    background-color: #e0a800;
  }
  
  .tipo-via-item .actions button:last-child {
    background-color: #dc3545;
    color: white;
  }
  
  .tipo-via-item .actions button:last-child:hover {
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