<template>
  <div class="container">
    <h1>Évaluation du Risque - Patients</h1>
    <PatientList />
  </div>
</template>

<script setup>
import PatientList from './components/PatientList.vue'
</script>

<style>
.container {
  max-width: 800px;
  margin: auto;
  padding: 2rem;
  font-family: sans-serif;
}
</style>
