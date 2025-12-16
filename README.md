# MDD - Monde de Dév

## Guide rapide de démarrage

### 1. Clonage du projet
```powershell
git clone https://github.com/Akima-zed/MDD-option-B.git
cd MDD-option-B
```

### 2. Lancement du back-end (Spring Boot)
Dans le dossier `back/` :
```powershell
# Définir les variables d’environnement (PowerShell)
$env:DB_URL="jdbc:mysql://localhost:3306/mdd"
$env:DB_USER="root"
$env:DB_PASSWORD="votre_mot_de_passe"

# Lancer le serveur
./mvnw spring-boot:run
```

### 3. Lancement du front-end (Angular)
Dans le dossier `front/` :
```powershell
npm install
ng serve
```

### 4. Accès à l’application
- API : http://localhost:8080/api
- Front : http://localhost:4200

---

Pour la documentation complète et le rapport détaillé, voir le fichier `# Documentation et rapport du projet MDD.md` à la racine du projet.
