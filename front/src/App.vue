<script setup>
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <div class="container">
    <header>
      <div class="wrapper">
        <nav>
          <RouterLink to="/" exact>Home</RouterLink>
          <RouterLink to="/vias">Vías</RouterLink>
          <RouterLink to="/tipo-vias">Tipo Vías</RouterLink>
        </nav>
      </div>
    </header>
    <RouterView class="router-view" />
  </div>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.router-view {
  flex: none;
  height: 600px;
  width: 100%;
  overflow: auto;
  padding: 1rem;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-top: 1rem;
}

header {
  background-color: #007bff;
  color: white;
  padding: 1rem;
}

header .wrapper {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: center;
  align-items: center;
}

nav {
  display: flex;
  gap: 1rem;
}

nav a,
.router-link-active {
  color: white;
  text-decoration: none;
  font-weight: bold;
  text-align: center;
  font-size: 1.2rem;
}

nav a:hover {
  text-decoration: underline;
}

.router-link-active {
  font-weight: bolder;
  font-size: 1.4rem;
  text-align: center;
  text-decoration: underline;
}
</style>