<template>
  <div>
    <h2>Ajouter un patient</h2>
    <form @submit.prevent="addPatient">
      <input v-model="newPatient.firstName" placeholder="Prénom" required />
      <input v-model="newPatient.lastName" placeholder="Nom" required />
      <select v-model="newPatient.gender" required>
        <option disabled value="">Genre</option>
        <option value="M">M</option>
        <option value="F">F</option>
      </select>
      <input v-model="newPatient.dateOfBirth" type="date" required />
      <input v-model="newPatient.address" placeholder="Adresse" required />
      <input v-model="newPatient.phoneNumber" placeholder="Téléphone" required />
      <button type="submit">Ajouter</button>
    </form>
    <p v-if="patientMessage" style="color: green; font-weight: bold;">{{ patientMessage }}</p>

    <h2>Ajouter une note</h2>
    <form @submit.prevent="addNote">
      <select v-model="note.patientId" required>
        <option disabled value="">Choisir un patient</option>
        <option v-for="p in patients" :key="p.id" :value="p.id">{{ p.firstName }} {{ p.lastName }}</option>
      </select>
      <textarea v-model="note.content" placeholder="Contenu de la note" required></textarea>
      <button type="submit">Ajouter Note</button>
    </form>
    <p v-if="noteMessage" style="color: green; font-weight: bold;">{{ noteMessage }}</p>

    <h2>Liste des patients</h2>
    <button @click="fetchPatients">🔄 Recharger les patients</button>
    <ul v-if="patients.length">
      <li v-for="p in patients" :key="p.id" style="margin: 1em 0;">
        👤 {{ p.firstName }} {{ p.lastName }} ({{ p.gender }}, {{ p.dateOfBirth }})
        <button @click="evaluateRisk(p.id)">Évaluer le risque</button>
        <span v-if="results[p.id]">➡️ {{ results[p.id] }}</span>
        <button @click="showPatient(p.id)">👁️ Voir patient</button>
        <button @click="editPatient(p)">✏️ Modifier patient</button>
        <button @click="deletePatient(p.id)">🗑️ Supprimer patient</button>
      </li>
    </ul>

    <div v-if="selectedPatient">
      <h3>Détails du patient</h3>
      <pre>{{ selectedPatient }}</pre>
    </div>

    <div v-if="editingPatient">
      <h3>Modifier le patient</h3>
      <form @submit.prevent="updatePatient">
        <input v-model="editingPatient.firstName" placeholder="Prénom" required />
        <input v-model="editingPatient.lastName" placeholder="Nom" required />
        <select v-model="editingPatient.gender" required>
          <option value="M">M</option>
          <option value="F">F</option>
        </select>
        <input v-model="editingPatient.dateOfBirth" type="date" required />
        <input v-model="editingPatient.address" placeholder="Adresse" required />
        <input v-model="editingPatient.phoneNumber" placeholder="Téléphone" required />
        <button type="submit">💾 Enregistrer</button>
        <button type="button" @click="editingPatient = null">❌ Annuler</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

const patients = ref([])
const results = ref({})
const selectedPatient = ref(null)
const editingPatient = ref(null)

const newPatient = ref({
  firstName: '',
  lastName: '',
  gender: '',
  dateOfBirth: '',
  address: '',
  phoneNumber: '',
})

const note = ref({
  patientId: '',
  content: '',
})

const patientMessage = ref('')
const noteMessage = ref('')

const fetchPatients = async () => {
  console.log('📥 fetchPatients called')
  try {
    const res = await axios.get('http://localhost:8080/patient')
    patients.value = res.data
    console.log('✅ Patients récupérés:', res.data)
  } catch (e) {
    console.error('❌ Erreur fetchPatients:', e)
    patientMessage.value = '❌ Impossible de charger les patients.'
  }
}

const addPatient = async () => {
  console.log('➕ Tentative d\'ajout patient:', newPatient.value)
  try {
    const res = await axios.post('http://localhost:8080/patient', newPatient.value)
    console.log('✅ Patient ajouté:', res.data)
    patientMessage.value = '✅ Patient ajouté avec succès.'
    Object.keys(newPatient.value).forEach(k => newPatient.value[k] = '')
    await fetchPatients()
  } catch (e) {
    console.error('❌ Erreur ajout patient:', e)
    patientMessage.value = '❌ Échec de l\'ajout du patient.'
  }
}

const addNote = async () => {
  console.log('📝 Tentative d\'ajout note:', note.value)
  try {
    const res = await axios.post('http://localhost:8080/notes', note.value)
    console.log('✅ Note ajoutée:', res.data)
    noteMessage.value = '✅ Note ajoutée avec succès.'
    note.value.patientId = ''
    note.value.content = ''
  } catch (e) {
    console.error('❌ Erreur ajout note:', e)
    noteMessage.value = '❌ Échec de l\'ajout de la note.'
  }
}

const evaluateRisk = async (patientId) => {
  console.log('🔎 Évaluation du risque pour patient:', patientId)
  try {
    const res = await axios.get(`http://localhost:8080/assess/${patientId}`)
    results.value[patientId] = res.data.riskLevel
    console.log(`📊 Résultat du risque [${patientId}]:`, res.data.riskLevel)
  } catch (e) {
    console.error(`⚠️ Erreur évaluation risque [${patientId}]:`, e)
    results.value[patientId] = '⚠️ Erreur évaluation'
  }
}

const showPatient = async (id) => {
  try {
    const res = await axios.get(`http://localhost:8080/patient/${id}`)
    selectedPatient.value = res.data
    console.log('👁️ Patient sélectionné:', res.data)
  } catch (e) {
    console.error('❌ Erreur getById:', e)
  }
}

const editPatient = (patient) => {
  editingPatient.value = { ...patient }
}

const updatePatient = async () => {
  try {
    const res = await axios.put(`http://localhost:8080/patient/${editingPatient.value.id}`, editingPatient.value)
    console.log('✏️ Patient modifié:', res.data)
    patientMessage.value = '✅ Patient mis à jour.'
    editingPatient.value = null
    await fetchPatients()
  } catch (e) {
    console.error('❌ Erreur update:', e)
    patientMessage.value = '❌ Échec mise à jour patient.'
  }
}

const deletePatient = async (id) => {
  if (!confirm('❗ Voulez-vous vraiment supprimer ce patient ?')) return
  try {
    await axios.delete(`http://localhost:8080/patient/${id}`)
    console.log('🗑️ Patient supprimé:', id)
    patientMessage.value = '✅ Patient supprimé.'
    await fetchPatients()
  } catch (e) {
    console.error('❌ Erreur delete:', e)
    patientMessage.value = '❌ Échec suppression patient.'
  }
}

// Auto-load au montage
fetchPatients()
</script>

<style scoped>
form {
  margin-bottom: 1.5rem;
  padding: 1rem;
  background: #f7f7f7;
  border-radius: 8px;
}
input, select, textarea {
  margin: 0.5rem 0;
  display: block;
  padding: 0.4rem;
  width: 100%;
  max-width: 400px;
}
button {
  margin: 0.2rem;
  padding: 0.5rem 1rem;
  cursor: pointer;
}
</style>
