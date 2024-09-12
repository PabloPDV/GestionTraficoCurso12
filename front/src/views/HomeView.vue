<script setup>
</script>

<template>
  <main class="welcome-container">
    <div class="overlay">
      <h1>Bienvenido a la Aplicación de Vías</h1>
      <p>Esta es la página de inicio de nuestra aplicación de gestión de tráfico. Aquí podrás encontrar información sobre las vías, su estado, capacidad y mucho más.</p>
    </div>
  </main>
</template>

<style scoped>
.welcome-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh; /* Ajustar la altura */
  text-align: center;
  padding: 1rem; /* Reducir el padding */
  background-image: url('/src/assets/background.jpg'); /* Añadir imagen de fondo */
  background-size: cover; /* Asegura que la imagen cubra todo el contenedor */
  background-position: center; /* Centra la imagen */
  background-repeat: no-repeat; /* Evita que la imagen se repita */
  position: relative; /* Para el overlay */
  color: white; /* Cambiar el color del texto a blanco */
  margin-top: 0; /* Eliminar el margen superior */
}

.overlay {
  background-color: rgba(0, 0, 0, 0.5); /* Fondo semi-transparente */
  padding: 2rem;
  border-radius: 8px;
}

.welcome-container h1 {
  font-size: 3rem;
  color: #ffffff;
  margin-bottom: 1rem;
}

.welcome-container p {
  font-size: 1.5rem;
  color: #ffffff;
  max-width: 800px;
}
</style>